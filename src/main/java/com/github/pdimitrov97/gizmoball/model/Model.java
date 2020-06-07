package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_X_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_Y_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU2_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRAVITY_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import com.github.pdimitrov97.gizmoball.model.loading.LoadingMain;
import com.github.pdimitrov97.gizmoball.model.saving.SavingMain;
import com.github.pdimitrov97.gizmoball.physics.Angle;
import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.Geometry;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;
import com.github.pdimitrov97.gizmoball.physics.Vect;

public class Model extends Observable implements Serializable
{
	private File loadFile = null;

	private int ballVX = BALL_VELOCITY_X_INIT;
	private int ballVY = BALL_VELOCITY_Y_INIT;
	private int gravity = GRAVITY_INIT;
	private double frictionMU = (double) FRICTION_MU_INIT / 1000;
	private double frictionMU2 = (double) FRICTION_MU2_INIT / 1000;

	private Walls gws = new Walls("OuterWalls", 0, 0, 20, 20);
	private List<CircleGizmo> circles = new ArrayList<>();
	private List<Square> squares = new ArrayList<>();
	private List<Triangle> triangles = new ArrayList<>();
	private List<Flipper> leftFlippers = new ArrayList<>();
	private List<Flipper> rightFlippers = new ArrayList<>();
	private List<Absorber> absorbers = new ArrayList<>();
	private List<Ball> balls = new LinkedList<>();

	private int selectedXL = -1;
	private int selectedYL = -1;
	private Gizmo firstSelected;
	private boolean isFirstSelected = false;
	private KeyStroke selectedKey = null;
	private boolean keySelectEnabled = false;

	private Map<KeyStroke, List<Gizmo>> keyConnections = new HashMap<>();

	public Model()
	{

	}

	public Model(File fileName) throws IOException
	{
		LoadingMain loading = new LoadingMain(this);
		loading.loadFile(fileName);

		if (!loading.wasCompilationSuccessful())
		{
			System.err.println("There were some incorrect lines within the file:");
			loading.getMessages().forEach(System.err::println);
		}

		loadFile = fileName;

		balls = loading.getBalls();
		parseGizmosList(loading.getGizmos());

		balls.forEach(b -> setBallVXVY((int) b.getVelo().x(), (int) b.getVelo().y()));
	}

	public File getLoadedFile()
	{
		return loadFile;
	}

	private void parseGizmosList(Collection<Gizmo> gizmos)
	{
		for (Gizmo g : gizmos)
		{
			if (g instanceof Square)
				squares.add((Square) g);
			else if (g instanceof CircleGizmo)
				circles.add((CircleGizmo) g);
			else if (g instanceof Triangle)
				triangles.add((Triangle) g);
			else if (g instanceof LeftFlipper)
				leftFlippers.add((LeftFlipper) g);
			else if (g instanceof RightFlipper)
				rightFlippers.add((RightFlipper) g);
			else if (isAbsorber(g))
			{
				absorbers.add((Absorber) g);
			}
			else
				throw new AssertionError("Unknown gizmo: " + g.getClass().getCanonicalName());
		}
	}

	public void tick()
	{
		addBallsToAbsorber(); // Add balls to absorbers if necessary.
		removeBallsFromAbsorbers(); // Remove balls that have been shot from absorbers.

		// Initialize variables.
		boolean collision = false;
		double moveTime = 1.0 / 120;
		CollisionDetails collisionDetails = getShortestCollisionDetails();

		// There might not be a collision that is going to happen at all.
		if (collisionDetails == null)
			return;

		if (collisionDetails.getTimeUntilCollision() <= moveTime) // Is a collision going to occur in this tick?
		{
			collision = true; // Yes, collision is going to occur.
			moveTime = collisionDetails.getTimeUntilCollision(); // Change the move time to the Collision Details' time in order to get a nice
																	// touching collision.
		}

		// Move all Balls for the time required.
		for (Ball b : balls)
			moveBallForTime(b, moveTime);

		// Move all Left Flippers for the time required.
		for (Flipper lf : leftFlippers)
			moveLeftFlipperForTime(lf, moveTime);

		// Move all Right Flippers for the time required.
		for (Flipper rf : rightFlippers)
			moveRightFlipperForTime(rf, moveTime);

		// If there is a collision in this tick
		if (collision)
		{
			// Check if the collision is between two Balls.
			if (collisionDetails.getSecondBall() != null)
			{
				// Set the two Balls' new velocities.
				collisionDetails.getFirstBall().setVelo(collisionDetails.getFirstBallNewVelocity());
				collisionDetails.getSecondBall().setVelo(collisionDetails.getSecondBallNewVelocity());
			}
			else // Collision is between a ball and a Gizmo.
			{
				// Set the colliding Ball's new velocity.
				collisionDetails.getFirstBall().setVelo(collisionDetails.getFirstBallNewVelocity());

				// If the ball is colliding with an Absorber.
				if (collisionDetails.getGizmo() instanceof Absorber)
				{
					// Add the ball to the Absorber.
					Absorber hitAbsorber = (Absorber) collisionDetails.getGizmo();
					hitAbsorber.absorbBall(collisionDetails.getFirstBall());
				}

				// Make all connected Gizmos to the hit one do their action.
				for (Gizmo g : collisionDetails.getGizmo().getConnectedGizmos())
					g.act();
			}
		}

		// Notify observers, redraw updated model.
		this.setChanged();
		this.notifyObservers();
	}

	/**
	 * If balls are inside an absorber and not in the list of containing balls of
	 * that absorber, it will add them to the list and move them to the correct
	 * position.
	 */
	private void addBallsToAbsorber()
	{
		for (Absorber absorber : absorbers)
		{
			for (Ball ball : balls)
			{
				if (absorber.isBallInsideAbsrober(ball) && !absorber.containsAbsorbedBall(ball))
					absorber.absorbBall(ball);
			}
		}
	}

	/**
	 * If balls have been shot from an absorbers and have left that absorber bounds,
	 * then they are removed from the containing list of that absorber.
	 */
	private void removeBallsFromAbsorbers()
	{
		for (Absorber absorber : absorbers)
		{
			List<Ball> shotBalls = absorber.getShotBalls();
			List<Ball> removeBalls = new LinkedList<>();

			for (Ball ball : shotBalls)
			{
				if (!absorber.isBallInsideAbsrober(ball))
					removeBalls.add(ball);
			}

			for (Ball ball : removeBalls)
				absorber.removeShotBall(ball);
		}
	}

	private void moveLeftFlipperForTime(Flipper flipper, double time)
	{
		if (flipper.isRaising())
			flipper.moveByAngle(new Angle(-Angle.DEG_90.radians() * time * 12));
		else if (!flipper.isLowered())
			flipper.moveByAngle(new Angle(Angle.DEG_90.radians() * time * 12));
	}

	private void moveRightFlipperForTime(Flipper flipper, double time)
	{
		if (flipper.isRaising())
			flipper.moveByAngle(new Angle(Angle.DEG_90.radians() * time * 12));
		else if (!flipper.isLowered())
			flipper.moveByAngle(new Angle(-Angle.DEG_90.radians() * time * 12));
	}

	// Edited by Pavel
	private void moveBallForTime(Ball ball, double time)
	{
		if (!ball.isStopped())
		{
			double newX = ball.getExactX() + (ball.getVelo().x() * time); // Move ball on the X axis
			double newY = ball.getExactY() + (ball.getVelo().y() * time); // Move ball on the Y axis

			Vect forces = new Vect(0, (gravity * ONE_L_IN_PX * time)); // Calculate gravity
			forces = ball.getVelo().plus(forces); // Add gravity
			forces = forces.times((1 - (frictionMU * time) - (frictionMU2 * Math.abs(forces.length()) / ONE_L_IN_PX * time))); // Calculate and add friction

			ball.setExactX(newX);
			ball.setExactY(newY);
			ball.setVelo(new Vect(forces.x(), forces.y()));
		}
	}

	private boolean isAbsorber(Gizmo g)
	{
		return g instanceof Absorber;
	}

	private ArrayList getAllGizmos()
	{
		return new ArrayList()
		{
			{
				addAll(squares);
				addAll(circles);
				addAll(triangles);
			}
		};
	}

	private ArrayList getAllFlippers()
	{
		return new ArrayList()
		{
			{
				addAll(leftFlippers);
				addAll(rightFlippers);
			}
		};
	}

	// Edited
	private CollisionDetails getShortestCollisionDetails()
	{
		CollisionDetails min;
		CollisionDetails current;

		if (balls.isEmpty())
			return null;

		min = timeUntilCollision(balls.get(0));

		for (int i = 1; i < balls.size(); i++)
		{
			current = timeUntilCollision(balls.get(i));

			if (current == null || min == null)
				continue;

			if (current.getTimeUntilCollision() < min.getTimeUntilCollision())
				min = current;
		}

		return min;
	}

	private CollisionDetails getCollisionDetailsWithFlipper(Ball ball, Flipper flipper)
	{
		double cdTime = 0;
		double cdShortestTime = Double.MAX_VALUE;
		Vect cdNewVelocity = new Vect(0, 0);

		for (LineSegment ls : flipper.getLineSegments())
		{
			cdTime = Geometry.timeUntilRotatingWallCollision(ls, flipper.getCircles().get(0).getCenter(), flipper.getVelocity(), ball.getCircle(), ball.getVelo());

			if (cdTime < cdShortestTime)
			{
				cdShortestTime = cdTime;
				cdNewVelocity = Geometry.reflectRotatingWall(ls, flipper.getCircles().get(0).getCenter(), flipper.getVelocity(), ball.getCircle(), ball.getVelo(), 0.95);
			}
		}

		for (Circle cir : flipper.getCircles())
		{
			cdTime = Geometry.timeUntilRotatingCircleCollision(cir, flipper.getCircles().get(0).getCenter(), flipper.getVelocity(), ball.getCircle(), ball.getVelo());

			if (cdTime < cdShortestTime)
			{
				cdShortestTime = cdTime;
				cdNewVelocity = Geometry.reflectRotatingCircle(cir, flipper.getCircles().get(0).getCenter(), flipper.getVelocity(), ball.getCircle(), ball.getVelo(), 0.95);
			}
		}

		return new CollisionDetails(ball, cdNewVelocity, null, null, flipper, cdShortestTime);
	}

	private CollisionDetails getCollisionDetails(Ball ball, Gizmo gizmo)
	{
		if (gizmo instanceof Absorber)
			if (((Absorber) gizmo).containsAbsorbedBall(ball) && ((Absorber) gizmo).isShootingBall(ball))
				return null;

		double cdTime = 0;
		double cdShortestTime = Double.MAX_VALUE;
		Vect cdNewVelocity = new Vect(0, 0);

		for (LineSegment lineSegment : gizmo.getLineSegments())
		{
			cdTime = Geometry.timeUntilWallCollision(lineSegment, ball.getCircle(), ball.getVelo());

			if (cdTime < cdShortestTime)
			{
				cdShortestTime = cdTime;
				cdNewVelocity = Geometry.reflectWall(lineSegment, ball.getVelo(), 1.0);
			}
		}

		for (Circle circle : gizmo.getCircles())
		{
			cdTime = Geometry.timeUntilCircleCollision(circle, ball.getCircle(), ball.getVelo());

			if (cdTime < cdShortestTime)
			{
				cdShortestTime = cdTime;
				cdNewVelocity = Geometry.reflectCircle(circle.getCenter(), ball.getCircle().getCenter(), ball.getVelo());
			}
		}

		return new CollisionDetails(ball, cdNewVelocity, null, null, gizmo, cdShortestTime);
	}

	private CollisionDetails getBallCollisionDetails(Ball firstBall, Ball secondBall)
	{
		double cdTime = Geometry.timeUntilBallBallCollision(firstBall.getCircle(), firstBall.getVelo(), secondBall.getCircle(), secondBall.getVelo());
		Geometry.VectPair cdNewVelocities = Geometry.reflectBalls(firstBall.getCircle().getCenter(), 1.0, firstBall.getVelo(), secondBall.getCircle().getCenter(), 1.0, secondBall.getVelo());

		return new CollisionDetails(firstBall, cdNewVelocities.v1, secondBall, cdNewVelocities.v2, null, cdTime);
	}

	private CollisionDetails timeUntilCollision(Ball ball)
	{
		ArrayList<CollisionDetails> allCollisions = new ArrayList<>();

		for (Ball tempBall : balls)
		{
			if (!tempBall.equals(ball) && !tempBall.isOverlaping(ball))
				allCollisions.add(getBallCollisionDetails(ball, tempBall));
		}

		for (Square square : squares)
			allCollisions.add(getCollisionDetails(ball, square));

		for (CircleGizmo circle : circles)
			allCollisions.add(getCollisionDetails(ball, circle));

		for (Triangle triangle : triangles)
			allCollisions.add(getCollisionDetails(ball, triangle));

		for (Flipper leftFlipper : leftFlippers)
			allCollisions.add(getCollisionDetailsWithFlipper(ball, leftFlipper));

		for (Flipper rightFlipper : rightFlippers)
			allCollisions.add(getCollisionDetailsWithFlipper(ball, rightFlipper));

		for (Absorber absorber : absorbers)
			allCollisions.add(getCollisionDetails(ball, absorber));

		allCollisions.add(getCollisionDetails(ball, gws));

		double min;
		CollisionDetails cdMin;
		double current;
		CollisionDetails cdCurrent;

		if (allCollisions.isEmpty())
			return null;

		cdMin = allCollisions.get(0);
		min = cdMin.getTimeUntilCollision();

		for (int i = 1; i < allCollisions.size(); i++)
		{
			cdCurrent = allCollisions.get(i);

			if (cdCurrent == null)
				continue;

			current = cdCurrent.getTimeUntilCollision();

			if (current < min)
			{
				min = current;
				cdMin = cdCurrent;
			}
		}

		return cdMin;
	}

	public void saveModel(File fileName) throws IOException
	{
		Set<Gizmo> gizmos = new HashSet<Gizmo>()
		{
			{
				addAll(absorbers);
				addAll(leftFlippers);
				addAll(rightFlippers);
				addAll(squares);
				addAll(triangles);
				addAll(circles);
			}
		};
		Set<Ball> allBalls = new HashSet<Ball>()
		{
			{
				addAll(balls);
			}
		};

		SavingMain saving = new SavingMain();
		List<String> commands = saving.convertGizmosToCommands(gizmos, balls, getKeyConnections(), getGravity(), getFrictionMU(), getFrictionMU2());
		BufferedWriter writer = new BufferedWriter(new FileWriter(fileName));
		
		for (String c : commands)
			writer.write(c + '\n');
		
		writer.close();
	}

	public void addCircle(int x, int y)
	{
		x = (int) (x / ONE_L_IN_PX);
		y = (int) (y / ONE_L_IN_PX);

		circles.add(new CircleGizmo(x, y));
		this.setChanged();
		this.notifyObservers();
	}

	public void addSquare(int x, int y)
	{
		x = (int) (x / ONE_L_IN_PX);
		y = (int) (y / ONE_L_IN_PX);

		squares.add(new Square(x, y));
		this.setChanged();
		this.notifyObservers();
	}

	public void addTriangle(int x, int y)
	{
		x = (int) (x / ONE_L_IN_PX);
		y = (int) (y / ONE_L_IN_PX);

		triangles.add(new Triangle(x, y));
		this.setChanged();
		this.notifyObservers();
	}

	public void addLeftFlipper(int x, int y)
	{
		x = (int) (x / ONE_L_IN_PX);
		y = (int) (y / ONE_L_IN_PX);

		leftFlippers.add(new LeftFlipper(x, y));
		this.setChanged();
		this.notifyObservers();
	}

	public void addRightFlipper(int x, int y)
	{
		x = (int) (x / ONE_L_IN_PX);
		y = (int) (y / ONE_L_IN_PX);

		rightFlippers.add(new RightFlipper((x - 1), y));
		this.setChanged();
		this.notifyObservers();
	}

	public void addAbsorber(int x, int y, int x2, int y2)
	{
		Absorber newAbsorber = new Absorber(x, y, x2, y2);
		absorbers.add(newAbsorber);
		this.setChanged();
		this.notifyObservers();
	}

	public boolean addBall(int x, int y)
	{
		Ball tempBall = new Ball(x, y, ballVX, ballVY);

		int xLeft = (int) (tempBall.getExactX() - tempBall.getRadius());
		int xRight = (int) (tempBall.getExactX() + tempBall.getRadius());
		int yTop = (int) (tempBall.getExactY() - tempBall.getRadius());
		int yBottom = (int) (tempBall.getExactY() + tempBall.getRadius());

		if (isOccupied(xLeft, yTop, tempBall, null))
			return false;
		else if (isOccupied(xLeft, yBottom, tempBall, null))
			return false;
		else if (isOccupied(xRight, yTop, tempBall, null))
			return false;
		else if (isOccupied(xRight, yBottom, tempBall, null))
			return false;

		balls.add(tempBall);
		this.setChanged();
		this.notifyObservers();
		return true;
	}

	public List<Square> getSquares()
	{
		return squares;
	}

	public List<Triangle> getTriangles()
	{
		return triangles;
	}

	public List<CircleGizmo> getCircles()
	{
		return circles;
	}

	public List<Ball> getBalls()
	{
		return balls;
	}

	public List<Flipper> getLeftFlippers()
	{
		return leftFlippers;
	}

	public List<Flipper> getRightFlippers()
	{
		return rightFlippers;
	}

	public List<Absorber> getAbsorbers()
	{
		return absorbers;
	}

	private void setBallSpeed(int x, int y)
	{
		balls.forEach(ball -> ball.setVelo(new Vect(x, y)));
	}

	public void setBallVX(int newBallVX)
	{
		ballVX = newBallVX;
		balls.forEach(ball -> setBallSpeed(ballVX, ballVY));
	}

	public void setBallVY(int newBallVY)
	{
		ballVY = newBallVY;
		balls.forEach(ball -> setBallSpeed(ballVX, ballVY));
	}

	private void setBallVXVY(int newBallVX, int newBallVY)
	{
		ballVX = newBallVX;
		ballVY = newBallVY;
		balls.forEach(ball -> setBallSpeed(ballVX, ballVY));
	}

	public void setGravity(int newGravity)
	{
		gravity = newGravity;
	}

	public void setFrictionMU(double newFrictionMU)
	{
		frictionMU = newFrictionMU;
	}

	public void setFrictionMU2(double newFrictionMU2)
	{
		frictionMU2 = newFrictionMU2;
	}

	public int getBallVX()
	{
		return ballVX;
	}

	public int getBallVY()
	{
		return ballVY;
	}

	public int getGravity()
	{
		return gravity;
	}

	public double getFrictionMU()
	{
		return frictionMU;
	}

	public double getFrictionMU2()
	{
		return frictionMU2;
	}

	private double[] rotateAroundPoint(double x, double y, double cx, double cy)
	{
		double[] result = new double[2];
		double x1 = x - cx;
		double y1 = y - cy;

		double x2 = x1 * Math.cos(Math.PI / 4) - y1 * Math.sin(Math.PI / 4);
		double y2 = x1 * Math.sin(Math.PI / 4) + y1 * Math.cos(Math.PI / 4);

		result[0] = x2 + cx;
		result[1] = y2 + cy;

		return result;
	}

	private boolean checkTriangle(Triangle t, Ball b)
	{
		int noOfRotations = t.getNumberOfRotations();
		List<LineSegment> lineSegments = t.getLineSegments();
		int[] x = new int[3];
		int[] y = new int[3];
		x[0] = (int) lineSegments.get(0).p1().x();
		x[1] = (int) lineSegments.get(1).p1().x();
		x[2] = (int) lineSegments.get(2).p1().x();
		y[0] = (int) lineSegments.get(0).p1().y();
		y[1] = (int) lineSegments.get(1).p1().y();
		y[2] = (int) lineSegments.get(2).p1().y();

		double[] resultT;
		double[] resultB;

		resultT = rotateAroundPoint(x[1], y[1], x[2], y[2]);
		resultB = rotateAroundPoint(b.getExactX(), b.getExactY(), x[2], y[2]);

		if (noOfRotations == 0)
		{
			return resultT[1] >= (resultB[1] - b.getRadius());
		}
		else if (noOfRotations == 1)
		{
			return resultT[0] <= (resultB[0] + b.getRadius());
		}
		else if (noOfRotations == 2)
		{
			return resultT[1] <= (resultB[1] + b.getRadius());
		}
		else
		{
			return resultT[0] >= (resultB[0] - b.getRadius());
		}

	}

	public boolean isOccupied(int exactX, int exactY, Ball tempBall, Gizmo ignore)
	{
		int xl = (int) (exactX / ONE_L_IN_PX);
		int yl = (int) (exactY / ONE_L_IN_PX);

		for (Square s : squares)
		{
			if (s.getXL() == xl && s.getYL() == yl)
				return true;
		}

		for (CircleGizmo c : circles)
		{
			if (c.getXL() == xl && c.getYL() == yl)
				return true;
		}

		for (Triangle t : triangles)
		{
			if (t.getXL() == xl && t.getYL() == yl)
			{
				if (tempBall == null)
					return true;
				else
					return checkTriangle(t, tempBall);
			}
		}

		for (Flipper lf : leftFlippers)
		{
			if (lf.equals(ignore))
				continue;

			if (tempBall == null)
			{
				if (tryGetFlipperAt(lf, xl, yl))
					return true;
			}
			else
			{
				int lfXL = lf.getXL();
				int lfYL = lf.getYL();
				int noOfRotations = lf.getNumberOfRotations();

				if (noOfRotations == 0)
				{
					if (checkRotations0And1(exactX, xl, yl, lfXL, lfYL, lfXL, lfYL + 1))
						return true;
				}
				else if (noOfRotations == 1)
				{
					if (checkRotations0And1(exactY, xl, yl, lfXL, lfYL, lfXL + 1, lfYL))
						return true;
				}
				else if (noOfRotations == 2)
				{
					if (checkRotations2And3(exactX, xl, yl, lfXL, lfYL, lfXL + 1, lfYL + 1))
						return true;
				}
				else
				{
					if (checkRotations2And3(exactY, xl, yl, lfXL, lfYL + 1, lfXL, lfYL + 1))
						return true;
				}
			}
		}

		for (Flipper rf : rightFlippers)
		{
			if (rf.equals(ignore))
				continue;

			if (tempBall == null)
			{
				if (tryGetFlipperAt(rf, xl, yl))
					return true;
			}
			else
			{
				int rfXL = rf.getXL();
				int rfYL = rf.getYL();
				int noOfRotations = rf.getNumberOfRotations();

				if (noOfRotations == 0)
				{
					if ((rfXL + 1) == xl && rfYL == yl)
					{
						if (exactX % ONE_L_IN_PX > HALF_L_IN_PX)
							return true;
					}
					else if ((rfXL + 1) == xl && (rfYL + 1) == yl)
					{
						if (exactX % ONE_L_IN_PX > HALF_L_IN_PX)
							return true;
					}
				}
				else if (noOfRotations == 1)
				{
					if (checkRotations2And3(exactY, xl, yl, rfXL, rfYL + 1, rfXL, rfYL + 1))
						return true;
				}
				else if (noOfRotations == 2)
				{
					if (rfXL == xl && rfYL == yl)
					{
						if (exactX % ONE_L_IN_PX < HALF_L_IN_PX)
							return true;
					}
					else if (rfXL == xl && (rfYL + 1) == yl)
					{
						if (exactX % ONE_L_IN_PX < HALF_L_IN_PX)
							return true;
					}
				}
				else
				{
					if (checkRotations0And1(exactY, xl, yl, rfXL, rfYL, rfXL + 1, rfYL))
						return true;
				}
			}
		}

		if (tempBall == null)
		{
			for (Absorber a : absorbers)
			{
				if (a.equals(ignore))
					continue;

				if (a.getXL() <= xl && a.getX2L() > xl && a.getYL() <= yl && a.getY2L() > yl)
					return true;
			}
		}

		if (!balls.isEmpty())
		{
			if (tempBall == null)
				return checkBallPosition(xl, yl);
			else
			{
				double deltaX;
				double deltaY;
				double deltaZ;

				for (Ball b : balls)
				{
					if (b.equals(tempBall))
						continue;

					deltaX = Math.abs((b.getExactX() - tempBall.getExactX()));
					deltaY = Math.abs((b.getExactY() - tempBall.getExactY()));
					deltaZ = Math.sqrt((deltaX * deltaX) + (deltaY * deltaY));

					if (deltaZ <= (b.getRadius() + tempBall.getRadius()))
						return true;
				}
			}
		}

		return false;
	}

	private boolean checkRotations2And3(int exactX, int xl, int yl, int lfXL, int lfYL, int i, int i2)
	{
		if ((i) == xl && lfYL == yl)
			return exactX % ONE_L_IN_PX > HALF_L_IN_PX;
		else if ((lfXL + 1) == xl && (i2) == yl)
			return exactX % ONE_L_IN_PX > HALF_L_IN_PX;
		
		return false;
	}

	private boolean checkRotations0And1(int exactX, int xl, int yl, int lfXL, int lfYL, int lfXL2, int i)
	{
		if (lfXL == xl && lfYL == yl)
			return exactX % ONE_L_IN_PX < HALF_L_IN_PX;
		else if (lfXL2 == xl && (i) == yl)
			return exactX % ONE_L_IN_PX < HALF_L_IN_PX;

		return false;
	}

	private boolean removeGizmo(Gizmo g, List<?> gizmoList)
	{
		gizmoList.remove(g);

		List<Gizmo> gizmos = getAllGizmos();
		gizmos.stream().forEach(gizmo -> gizmo.disconnectGizmo(g));
		List<Flipper> flippers = getAllFlippers();
		flippers.stream().forEach(flipper -> flipper.disconnectGizmo(g));

		for (Absorber a : absorbers)
			a.disconnectGizmo(g);

		for (Map.Entry<KeyStroke, List<Gizmo>> entry : getKeyConnections().entrySet())
			removeKeyConnection(entry.getKey(), g);

		this.setChanged();
		this.notifyObservers();
		return true;
	}

	private boolean tryDeleteFlipper(Flipper f, List<Flipper> flippers, int xl, int yl)
	{
		int flipperXL = f.getXL();
		int flipperYL = f.getYL();

		if (flipperXL == xl && flipperYL == yl)
			return removeGizmo(f, flippers);
		else if ((flipperXL + 1) == xl && flipperYL == yl)
			return removeGizmo(f, flippers);
		else if (flipperXL == xl && (flipperYL + 1) == yl)
			return removeGizmo(f, flippers);
		else if ((flipperXL + 1) == xl && (flipperYL + 1) == yl)
			return removeGizmo(f, flippers);

		return false;
	}

	public boolean deleteGizmo(int xl, int yl)
	{
		/*
		 * if (checkBallPosition(xl, yl)) { balls.remove(ballToBeRemoved); return true;
		 * }
		 */

		for (Ball b : balls)
		{
			if ((b.getExactX() - b.getRadius()) < xl && (b.getExactX() + b.getRadius()) > xl && (b.getExactY() - b.getRadius()) < yl && (b.getExactY() + b.getRadius()) > yl)
			{
				balls.remove(b);
				return true;
			}
		}

		xl = (int) (xl / ONE_L_IN_PX);
		yl = (int) (yl / ONE_L_IN_PX);

		for (CircleGizmo c : circles)
		{
			if (c.getXL() == xl && c.getYL() == yl)
				return removeGizmo(c, circles);
		}

		for (Square s : squares)
		{
			if (s.getXL() == xl && s.getYL() == yl)
				return removeGizmo(s, squares);
		}

		for (Triangle t : triangles)
		{
			if (t.getXL() == xl && t.getYL() == yl)
				return removeGizmo(t, triangles);
		}

		for (Flipper lf : leftFlippers)
		{
			if (tryDeleteFlipper(lf, leftFlippers, xl, yl))
				return true;
		}

		for (Flipper rf : rightFlippers)
		{
			if (tryDeleteFlipper(rf, rightFlippers, xl, yl))
				return true;
		}

		for (Absorber a : absorbers)
		{
			if (a.getXL() <= xl && a.getX2L() > xl && a.getYL() <= yl && a.getY2L() > yl)
				return removeGizmo(a, absorbers);
		}

		return false;
	}

	private boolean checkBallPosition(int xl, int yl)
	{
		int xLeft = 0;
		int xRight = 0;
		int yTop = 0;
		int yBottom = 0;
		for (Ball ball : balls)
		{
			xLeft = (int) ((ball.getExactX() - ball.getRadius()) / ONE_L_IN_PX);
			xRight = (int) ((ball.getExactX() + ball.getRadius()) / ONE_L_IN_PX);
			yTop = (int) ((ball.getExactY() - ball.getRadius()) / ONE_L_IN_PX);
			yBottom = (int) ((ball.getExactY() + ball.getRadius()) / ONE_L_IN_PX);
			if (xLeft == xl && yTop == yl)
			{
				// ballToBeRemoved = ball;
				return true;
			}
			else if (xLeft == xl && yBottom == yl)
			{
				// ballToBeRemoved = ball;
				return true;
			}
			else if (xRight == xl && yTop == yl)
			{
				// ballToBeRemoved = ball;
				return true;
			}
			else if (xRight == xl && yBottom == yl)
			{
				// ballToBeRemoved = ball;
				return true;
			}
		}

		return false;
	}

	private void removeBallAfterChange()
	{
		int xLeft;
		int xRight;
		int yTop;
		int yBottom;
		
		for (Ball ball : balls)
		{
			xLeft = (int) (ball.getExactX() - ball.getRadius());
			xRight = (int) (ball.getExactX() + ball.getRadius());
			yTop = (int) (ball.getExactY() - ball.getRadius());
			yBottom = (int) (ball.getExactY() + ball.getRadius());

			if (isOccupied(xLeft, yTop, ball, null))
				balls.remove(ball);
			else if (isOccupied(xLeft, yBottom, ball, null))
				balls.remove(ball);
			else if (isOccupied(xRight, yTop, ball, null))
				balls.remove(ball);
			else if (isOccupied(xRight, yBottom, ball, null))
				balls.remove(ball);
		}
	}

	private boolean rotateGizmo(Gizmo g)
	{
		g.rotate();
		removeBallAfterChange();
		this.setChanged();
		this.notifyObservers();
		return true;
	}

	private boolean tryRotateFlipper(Flipper f, int xl, int yl)
	{
		int flipperXL = f.getXL();
		int flipperYL = f.getYL();

		if (flipperXL == xl && flipperYL == yl)
			return rotateGizmo(f);
		else if ((flipperXL + 1) == xl && flipperYL == yl)
			return rotateGizmo(f);
		else if (flipperXL == xl && (flipperYL + 1) == yl)
			return rotateGizmo(f);
		else if ((flipperXL + 1) == xl && (flipperYL + 1) == yl)
			return rotateGizmo(f);

		return false;
	}

	public boolean rotateGizmo(int xl, int yl)
	{
		for (Triangle t : triangles)
		{
			if (t.getXL() == xl && t.getYL() == yl)
				rotateGizmo(t);
		}

		for (Flipper f : leftFlippers)
		{
			if (tryRotateFlipper(f, xl, yl))
				return true;
		}

		for (Flipper f : rightFlippers)
		{
			if (tryRotateFlipper(f, xl, yl))
				return true;
		}

		return false;
	}

	private boolean moveGizmo(Gizmo g, int xlNew, int ylNew)
	{
		g.move(xlNew, ylNew);
		removeBallAfterChange();
		this.setChanged();
		this.notifyObservers();
		return true;
	}

	private boolean tryMoveFlipper(Flipper f, int xl, int yl, int xlNew, int ylNew)
	{
		int flipperXL = f.getXL();
		int flipperYL = f.getYL();

		if (flipperXL == xl && flipperYL == yl)
		{
			if (!isOccupied((int) (xlNew * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f) && !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) ((ylNew + 1) * ONE_L_IN_PX), null, f) && !isOccupied((int) ((xlNew + 1) * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f) && !isOccupied((int) ((xlNew + 1) * ONE_L_IN_PX), (int) ((ylNew + 1) * ONE_L_IN_PX), null, f))
			{
				if (xlNew < 0 || xlNew >= 19 || ylNew < 0 || ylNew >= 19)
					return false;

				return moveGizmo(f, xlNew, ylNew);
			}
		}
		else if ((flipperXL + 1) == xl && flipperYL == yl)
		{
			if (!isOccupied((int) ((xlNew - 1) * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f) && !isOccupied((int) ((xlNew - 1) * ONE_L_IN_PX), (int) ((ylNew + 1) * ONE_L_IN_PX), null, f) && !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f) && !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) ((ylNew + 1) * ONE_L_IN_PX), null, f))
			{
				if ((xlNew - 1) < 0 || (xlNew - 1) >= 19 || ylNew < 0 || ylNew >= 19)
					return false;

				return moveGizmo(f, (xlNew - 1), ylNew);
			}
		}
		else if (flipperXL == xl && (flipperYL + 1) == yl)
		{
			if (!isOccupied((int) (xlNew * ONE_L_IN_PX), (int) ((ylNew - 1) * ONE_L_IN_PX), null, f) && !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f) && !isOccupied((int) ((xlNew + 1) * ONE_L_IN_PX), (int) ((ylNew - 1) * ONE_L_IN_PX), null, f) && !isOccupied((int) ((xlNew + 1) * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f))
			{
				if (xlNew < 0 || xlNew >= 19 || (ylNew - 1) < 0 || (ylNew - 1) >= 19)
					return false;

				return moveGizmo(f, xlNew, (ylNew - 1));
			}
		}
		else if ((flipperXL + 1) == xl && (flipperYL + 1) == yl)
		{
			if (!isOccupied((int) ((xlNew - 1) * ONE_L_IN_PX), (int) ((ylNew - 1) * ONE_L_IN_PX), null, f) && !isOccupied((int) ((xlNew - 1) * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f) && !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) ((ylNew - 1) * ONE_L_IN_PX), null, f) && !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, f))
			{
				if ((xlNew - 1) < 0 || (xlNew - 1) >= 19 || (ylNew - 1) < 0 || (ylNew - 1) >= 19)
					return false;

				return moveGizmo(f, (xlNew - 1), (ylNew - 1));
			}
		}

		return false;
	}

	public boolean moveGizmo(int xl, int yl, int xlNew, int ylNew)
	{
		for (Ball b : balls)
		{
			if ((b.getExactX() - b.getRadius()) < xl && (b.getExactX() + b.getRadius()) > xl && (b.getExactY() - b.getRadius()) < yl && (b.getExactY() + b.getRadius()) > yl)
			{
				if (!isOccupied(xlNew - (int) Math.ceil(b.getRadius()), ylNew - (int) Math.ceil(b.getRadius()), b, null) && !isOccupied(xlNew - (int) Math.ceil(b.getRadius()), ylNew + (int) Math.ceil(b.getRadius()), b, null) && !isOccupied(xlNew + (int) Math.ceil(b.getRadius()), ylNew - (int) Math.ceil(b.getRadius()), b, null) && !isOccupied(xlNew + (int) Math.ceil(b.getRadius()), ylNew + (int) Math.ceil(b.getRadius()), b, null))
				{

					b.setExactX((double) xlNew);
					b.setExactY((double) ylNew);
					return true;
				}
			}
		}

		xl = (int) (xl / ONE_L_IN_PX);
		yl = (int) (yl / ONE_L_IN_PX);
		xlNew = (int) (xlNew / ONE_L_IN_PX);
		ylNew = (int) (ylNew / ONE_L_IN_PX);

		for (CircleGizmo c : circles)
		{
			if (checkIfPossibleToMove(xl, yl, xlNew, ylNew, c.getXL(), c.getYL(), c))
				return moveGizmo(c, xlNew, ylNew);
		}

		for (Square s : squares)
		{
			if (checkIfPossibleToMove(xl, yl, xlNew, ylNew, s.getXL(), s.getYL(), s))
				return moveGizmo(s, xlNew, ylNew);
		}

		for (Triangle t : triangles)
		{
			if (checkIfPossibleToMove(xl, yl, xlNew, ylNew, t.getXL(), t.getYL(), t))
				return moveGizmo(t, xlNew, ylNew);
		}

		for (Flipper lf : leftFlippers)
		{
			if (tryMoveFlipper(lf, xl, yl, xlNew, ylNew))
				return true;
		}

		for (Flipper rf : rightFlippers)
		{
			if (tryMoveFlipper(rf, xl, yl, xlNew, ylNew))
				return true;
		}

		for (Absorber a : absorbers)
		{
			if (a.getXL() <= xl && a.getX2L() > xl && a.getYL() <= yl && a.getY2L() > yl)
			{
				int left = xl - a.getXL();
				int right = a.getX2L() - xl - 1;
				int top = yl - a.getYL();
				int bottom = a.getY2L() - yl - 1;
				int checkLeft = xlNew - left;
				int checkRight = xlNew + right;
				int checkTop = ylNew - top;
				int checkBottom = ylNew + bottom;

				if (checkLeft < 0 || checkRight > 19 || checkTop < 0 || checkBottom > 19)
					return false;

				for (int r = checkLeft; r <= checkLeft + left; r++)
				{
					if (checkIfPossibleToMoveAbsorber(ylNew, a, top, checkTop, checkBottom, r))
						return false;
				}

				for (int r = xlNew + 1; r <= checkRight; r++)
				{
					if (checkIfPossibleToMoveAbsorber(ylNew, a, top, checkTop, checkBottom, r))
						return false;
				}

				return moveGizmo(a, checkLeft, checkTop);
			}
		}

		return false;
	}

	private boolean checkIfPossibleToMoveAbsorber(int ylNew, Absorber a, int top, int checkTop, int checkBottom, int r)
	{
		for (int c = checkTop; c <= checkTop + top; c++)
		{
			if (isOccupied((int) (r * ONE_L_IN_PX), (int) (c * ONE_L_IN_PX), null, a))
				return true;
		}

		for (int c = ylNew + 1; c <= checkBottom; c++)
		{
			if (isOccupied((int) (r * ONE_L_IN_PX), (int) (c * ONE_L_IN_PX), null, a))
				return true;
		}
		
		return false;
	}

	private boolean checkIfPossibleToMove(int xl, int yl, int xlNew, int ylNew, int xl2, int yl2, Gizmo g)
	{
		if (xl2 == xl && yl2 == yl)
			return !isOccupied((int) (xlNew * ONE_L_IN_PX), (int) (ylNew * ONE_L_IN_PX), null, null);
		
		return false;
	}

	private boolean tryGetFlipperAt(Flipper f, int xl, int yl)
	{
		int flipperXL = f.getXL();
		int flipperYL = f.getYL();

		if (flipperXL == xl && flipperYL == yl)
			return true;
		else if ((flipperXL + 1) == xl && flipperYL == yl)
			return true;
		else if (flipperXL == xl && (flipperYL + 1) == yl)
			return true;
		else
			return (flipperXL + 1) == xl && (flipperYL + 1) == yl;
	}

	public Gizmo getGizmoAt(int xl, int yl)
	{
		for (Square s : squares)
		{
			if (s.getXL() == xl && s.getYL() == yl)
				return s;
		}

		for (CircleGizmo c : circles)
		{
			if (c.getXL() == xl && c.getYL() == yl)
				return c;
		}

		for (Triangle t : triangles)
		{
			if (t.getXL() == xl && t.getYL() == yl)
				return t;
		}

		for (Flipper lf : leftFlippers)
		{
			if (tryGetFlipperAt(lf, xl, yl))
				return lf;
		}

		for (Flipper rf : rightFlippers)
		{
			if (tryGetFlipperAt(rf, xl, yl))
				return rf;
		}

		for (Absorber a : absorbers)
		{
			if (a.getXL() <= xl && a.getX2L() > xl && a.getYL() <= yl && a.getY2L() > yl)
				return a;
		}

		return null;
	}

	public Gizmo getSelectedGizmo()
	{
		if (selectedXL == -1 || selectedYL == -1)
			return null;

		return getGizmoAt(selectedXL, selectedYL);
	}

	public void setSelectedBlock(int xL, int yL)
	{
		if (xL == -1 && yL == -1)
		{
			firstSelected = null;
			isFirstSelected = false;
		}

		Gizmo pointing = getGizmoAt(xL, yL);

		if (pointing != null)
		{
			if (getGizmoAt(xL, yL).equals(getSelectedGizmo()))
			{
				if (firstSelected != null)
				{
					if (getGizmoAt(xL, yL).equals(firstSelected))
					{
						if (isAbsorber(firstSelected))
							return;

						firstSelected = null;
						isFirstSelected = false;
					}
				}

				selectedXL = -1;
				selectedYL = -1;
				this.setChanged();
				this.notifyObservers();
				return;
			}
		}

		if (isAcceptingKeySelect() && getSelectedKey() == null)
			return;

		selectedXL = xL;
		selectedYL = yL;
		this.setChanged();
		this.notifyObservers();
	}

	public boolean isFirstSelected()
	{
		return isFirstSelected;
	}

	public void setFirstSelectedGizmo(Gizmo g)
	{
		if (g == null)
			return;

		firstSelected = g;
		isFirstSelected = true;
	}

	public void setSecondSelectedGizmo(Gizmo g, boolean connect)
	{
		selectedXL = (int) (firstSelected.getX() / ONE_L_IN_PX);
		selectedYL = (int) (firstSelected.getY() / ONE_L_IN_PX);

		if (g == null)
			return;

		if (g.equals(firstSelected))
		{
			if (isAbsorber(g))
			{
				int answer = -1;

				if (connect)
				{
					String[] options = { "Connect", "Leave", "Cancel" };
					answer = JOptionPane.showOptionDialog(null, "Do you want to connect this Absrober to itself or leave the connection mode for this Absrober?", "Connect Absorber", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
				}
				else
				{
					String[] options = { "Disconnect", "Leave", "Cancel" };
					answer = JOptionPane.showOptionDialog(null, "Do you want to disconnect this Absrober from itself or leave the disconnection mode for this Absrober?", "Disconnect Absorber", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
				}

				if (answer == 0)
				{
					if (connect)
					{
						addGizmoConnection(firstSelected, g);
						this.setChanged();
						this.notifyObservers();
						return;
					}
					else
					{
						removeGizmoConnection(firstSelected, g);
						this.setChanged();
						this.notifyObservers();
						return;
					}
				}
				else if (answer == -1 || answer == 2)
					return;
			}

			firstSelected = null;
			isFirstSelected = false;
			selectedXL = -1;
			selectedYL = -1;
			this.setChanged();
			this.notifyObservers();
			return;
		}

		if (connect)
			addGizmoConnection(firstSelected, g);
		else
			removeGizmoConnection(firstSelected, g);

		this.setChanged();
		this.notifyObservers();
	}

	public boolean isKeySelected()
	{
		return selectedKey != null;
	}

	public void resetKeySelected()
	{
		selectedKey = null;
	}

	public void setKeySelected(int keyCode)
	{
		if (!isAcceptingKeySelect())
			return;

		int answer = -1;

		String[] options = { "Pressed", "Released", "Cancel" };
		answer = JOptionPane.showOptionDialog(null, "Which key motion do you want to use?", "Key motion select", JOptionPane.DEFAULT_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);

		if (answer == -1 || answer == 2)
			return;
		else if (answer == 0)
			selectedKey = KeyStroke.getKeyStroke(keyCode, 0, false);
		else if (answer == 1)
			selectedKey = KeyStroke.getKeyStroke(keyCode, 0, true);

		this.setChanged();
		this.notifyObservers();
	}

	public void setSelectedKeyGizmo(Gizmo g, boolean connect)
	{
		if (g == null)
			return;

		if (connect)
			addKeyConnection(selectedKey, g);
		else
			removeKeyConnection(selectedKey, g);

		selectedXL = -1;
		selectedYL = -1;
		this.setChanged();
		this.notifyObservers();
	}

	public List<Gizmo> getKeyConnectedGizmos(KeyStroke key)
	{
		if (key == null)
			return null;

		return getKeyConnections().get(key);
	}

	public KeyStroke getSelectedKey()
	{
		return selectedKey;
	}

	public void acceptKeySelect(boolean accept)
	{
		keySelectEnabled = accept;
		this.setChanged();
		this.notifyObservers();
	}

	public boolean isAcceptingKeySelect()
	{
		return keySelectEnabled;
	}

	public void clearModel()
	{
		circles = new ArrayList<>();
		squares = new ArrayList<>();
		triangles = new ArrayList<>();
		leftFlippers = new ArrayList<>();
		rightFlippers = new ArrayList<>();
		absorbers = new ArrayList<>();
		balls = new ArrayList<>();

		setBallVX(BALL_VELOCITY_X_INIT);
		setBallVY(BALL_VELOCITY_Y_INIT);
		setGravity(GRAVITY_INIT);
		setFrictionMU((double) FRICTION_MU_INIT / 1000);
		setFrictionMU2((double) FRICTION_MU2_INIT / 1000);

		this.setChanged();
		this.notifyObservers();
	}

	public boolean addGizmoConnection(Gizmo g, Gizmo c)
	{
		g.connectGizmo(c);
		return true;
	}

	public boolean removeGizmoConnection(Gizmo g, Gizmo c)
	{
		g.disconnectGizmo(c);
		return true;
	}

	public boolean addKeyConnection(final KeyStroke key, final Gizmo gizmo)
	{
		if (!keyConnections.containsKey(key))
			keyConnections.put(key, new ArrayList<>());

		final List<Gizmo> toAddTo = keyConnections.get(key);
		
		if (toAddTo.contains(gizmo))
		{
			// System.err.println(gizmo + " gizmo is already connected to key " + key);
			return true;
		}
		
		return toAddTo.add(gizmo);
	}

	public boolean removeKeyConnection(final KeyStroke key, final Gizmo gizmo)
	{
		if (!keyConnections.containsKey(key))
			return false;
		
		final List<Gizmo> toRemoveFrom = keyConnections.get(key);
		
		if (!toRemoveFrom.contains(gizmo))
			return false;
		
		toRemoveFrom.remove(gizmo);
		
		if (toRemoveFrom.isEmpty())
			keyConnections.remove(key);
		
		return true;
	}

	public Map<KeyStroke, List<Gizmo>> getKeyConnections()
	{
		return new HashMap<>(keyConnections);
	}

	/**
	 * This method provides a safely created copy of this Model
	 *
	 * @return a copy of this Model
	 */
	public Model copy()
	{
		try
		{
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
			objectOutputStream.writeObject(this);

			ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());
			ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);

			return (Model) objectInputStream.readObject();
		}
		catch (Exception e)
		{
			e.printStackTrace();
			return null;
		}
	}
}