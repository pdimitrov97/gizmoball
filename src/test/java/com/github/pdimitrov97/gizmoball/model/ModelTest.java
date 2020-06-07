package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.IntStream;

import javax.swing.KeyStroke;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class ModelTest
{
	private Model m;
	private static final double DOUBLE_EQUALITY_TOLERANCE = 0.00001;

	private static final class Location
	{
		private final int xL;
		private final int yL;

		public Location(final int xL, final int yL)
		{
			this.xL = xL;
			this.yL = yL;
		}

		public int getXL()
		{
			return xL;
		}

		public int getYL()
		{
			return yL;
		}

		public int getXPx()
		{
			return toPx(xL);
		}

		public int getYPx()
		{
			return toPx(yL);
		}
	}

	@BeforeEach
	void setUp()
	{
		m = new Model();
	}

	@Test
	void physicsLoop()
	{

	}

	@Test
	void loadFile()
	{
		try
		{
			m = new Model(new File("testLoadFile.gizmo"));
			assertEquals(new File("testLoadFile.gizmo"), m.getLoadedFile());
			assertModelMatchesLoadTestFile(m);
		}
		catch (IOException e)
		{
			throw new AssertionError("expected test file to use with this test");
		}
	}

	@Test
	void saveModel()
	{
		try
		{
			m = new Model(new File("testLoadFile.gizmo"));

			final File savedFile = new File("testSaveFile.gizmo");
			m.saveModel(savedFile);
			m = new Model(savedFile);

			assertModelMatchesLoadTestFile(m);
		}
		catch (IOException e)
		{
			throw new AssertionError("expected test file to use with this test");
		}
	}

	@Test
	private void assertModelMatchesLoadTestFile(final Model model)
	{
		assertAll(() -> assertTrue(model.getGizmoAt(0, 0) instanceof Square), () -> assertTrue(model.getGizmoAt(1, 1) instanceof CircleGizmo), () -> assertTrue(model.getGizmoAt(2, 2) instanceof Triangle), () -> assertTrue(model.getGizmoAt(5, 5) instanceof LeftFlipper), () -> assertTrue(model.getGizmoAt(7, 7) instanceof RightFlipper), () -> assertTrue(model.getGizmoAt(0, 19) instanceof Absorber), () -> assertTrue(model.getGizmoAt(10, 10) instanceof Square),
				() -> assertEquals(toPx(12), model.getBalls().get(0).getExactX(), DOUBLE_EQUALITY_TOLERANCE), () -> assertEquals(toPx(12), model.getBalls().get(0).getExactY(), DOUBLE_EQUALITY_TOLERANCE),

				() -> assertEquals(25.0, model.getGravity(), DOUBLE_EQUALITY_TOLERANCE), () -> assertEquals(0.025, model.getFrictionMU(), DOUBLE_EQUALITY_TOLERANCE), () -> assertEquals(0.025, model.getFrictionMU2(), DOUBLE_EQUALITY_TOLERANCE));
	}

	@Test
	void bounceBallOffSquare()
	{
		m.addBall(toPx(2), toPx(1));
		m.addSquare(toPx(1), toPx(2));
		m.addSquare(toPx(2), toPx(2));
		IntStream.range(0, 23).forEach(i -> m.tick());

		// assert that the ball is touching the top one of the Squares
		final Gizmo square = m.getGizmoAt(1, 2);
		final Ball ball = m.getBalls().get(0);
		assertEquals(square.getY() - ball.getRadius(), ball.getExactY(), DOUBLE_EQUALITY_TOLERANCE);
	}

	@Test
	void moveBallTest()
	{
		m.addBall(800, 800);
		m.moveGizmo(800, 800, 900, 900);
		assertEquals(900, m.getBalls().get(m.getBalls().size() - 1).getExactX());
		assertEquals(900, m.getBalls().get(m.getBalls().size() - 1).getExactY());
	}

	@Test
	void bounceBallOffLeftFlipper()
	{
		m.addBall(toPx(60), toPx(59));
		m.addLeftFlipper(toPx(59), toPx(60));
		IntStream.range(0, 23).forEach(i -> m.tick());
		final Gizmo lf = m.getGizmoAt(59, 60);
		final Ball ball = m.getBalls().get(m.getBalls().size() - 1);
		assertEquals(Math.floor(lf.getY() - ball.getRadius()), Math.floor(ball.getExactY()), DOUBLE_EQUALITY_TOLERANCE);
	}

	@Test
	void bounceBallOffRightFlipper()
	{
		m.addBall(toPx(70), toPx(69));
		m.addRightFlipper(toPx(70), toPx(70));
		IntStream.range(0, 23).forEach(i -> m.tick());

		final Gizmo rf = m.getGizmoAt(70, 70);
		final Ball ball = m.getBalls().get(m.getBalls().size() - 1);
		assertEquals(Math.floor(rf.getY() - ball.getRadius()), Math.floor(ball.getExactY()), DOUBLE_EQUALITY_TOLERANCE);
	}

	@Test
	void moveLeftFlipper()
	{
		m.addLeftFlipper(toPx(1), toPx(1));
		m.moveGizmo(toPx(1), toPx(1), toPx(2), toPx(2));
		assertTrue(m.getGizmoAt(2, 2) instanceof LeftFlipper);
	}

	@Test
	void moveLeftFlipperToSpaceOccupied()
	{
		m.addLeftFlipper(toPx(1), toPx(1));
		m.addSquare(toPx(10), toPx(10));
		assertFalse(m.moveGizmo(toPx(1), toPx(1), toPx(10), toPx(10)));
	}

	@Test
	void tryPlaceBallOverSquare()
	{
		final Location occupied = new Location(5, 5);
		m.addSquare(occupied.getXPx(), occupied.getYPx());
		assertFalse(m.addBall(occupied.getXPx(), occupied.getYPx()));
	}

	@Test
	void ballIsRemovedAfterGizmoIsChanged()
	{
		m.addTriangle((int) (70 * ONE_L_IN_PX), (int) (70 * ONE_L_IN_PX));
		m.addBall(toPx(70) + 25, toPx(70) + 25);
		int size = m.getBalls().size();
		assertEquals(size, m.getBalls().size());
		m.rotateGizmo(70, 70);
		assertEquals(size - 1, m.getBalls().size());
		m.addBall(toPx(70), toPx(70));
		m.rotateGizmo(70, 70);
		assertEquals(size - 1, m.getBalls().size());
	}

	@Test
	void tryPlaceBallOverLeftFlipper()
	{
		final Location occupied = new Location(5, 5);
		m.addLeftFlipper(occupied.getXPx(), occupied.getYPx());
		assertFalse(m.addBall(occupied.getXPx(), occupied.getYPx()));
	}

	@Test
	void tryPlaceBallOverRightFlipper()
	{
		final int flipperXL = 5;
		final int flipperYL = 5;
		m.addRightFlipper(toPx(flipperXL), toPx(flipperYL));

		final Location occupied = new Location(flipperXL + 1, flipperYL);
		assertFalse(m.addBall(occupied.getXPx(), occupied.getYPx()));
	}

	@Test
	void addCircle()
	{
		m.addCircle(0, 0);
		assertEquals(0, m.getCircles().get(m.getCircles().size() - 1).getXL());
		assertEquals(0, m.getCircles().get(m.getCircles().size() - 1).getYL());
	}

	@Test
	void addSquare()
	{
		m.addSquare(0, 0);
		assertEquals(0, m.getSquares().get(m.getSquares().size() - 1).getXL());
		assertEquals(0, m.getSquares().get(m.getSquares().size() - 1).getYL());
	}

	@Test
	void addTriangle()
	{
		m.addTriangle(0, 0);
		assertEquals(0, m.getTriangles().get(m.getTriangles().size() - 1).getXL());
		assertEquals(0, m.getTriangles().get(m.getTriangles().size() - 1).getYL());

	}

	@Test
	void addLeftFlipper()
	{
		m.addLeftFlipper(0, 0);
		assertEquals(0, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getXL());
		assertEquals(0, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getYL());

	}

	@Test
	void addRightFlipper()
	{
		m.addRightFlipper(30, 30);
		// Reference is one L to the left
		assertEquals(0, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getX());
		assertEquals(30, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getY());
	}

	@Test
	void addAbsorber()
	{
		m.addAbsorber(0, 0, 2, 2);
		assertEquals(0, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getX());
		assertEquals(0, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getY());
		assertEquals(2 * ONE_L_IN_PX, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getX2());
		assertEquals(2 * ONE_L_IN_PX, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getY2());

	}

	@Test
	void addBall()
	{
		m.addBall(10, 10);
		assertEquals(10, m.getBalls().get(m.getBalls().size() - 1).getExactX());
		assertEquals(10, m.getBalls().get(m.getBalls().size() - 1).getExactY());
	}

	@Test
	void getBall()
	{
		m.addBall(10, 10);
		assertEquals(10, m.getBalls().get(m.getBalls().size() - 1).getExactX());
		assertEquals(10, m.getBalls().get(m.getBalls().size() - 1).getExactY());
	}
//
//    @Test
//    void setBallSpeed() {
//        m.setBallSpeed(10, 10);
//        assertEquals(10, m.getBall().getVelo().x());
//        assertEquals(10, m.getBall().getVelo().y());
//    }

//    @Test
//    void moveBall() {
//        m.moveBall(m.getBalls().get(0), 11, 11, );
//        assertEquals(11, m.getBalls().getExactX());
//        assertEquals(11, m.getBall().getExactY());
//    }
//
//    @Test
//    void shootBall() {
//        m.addAbsorber(50,50,60,60);
//        m.addBall(51,51);
//        m.getAbsorbers().forEach(a-> a.setBall(m.getBall()));
//        m.shootBall(m.getAbsorbers().get(m.getAbsorbers().size() - 1));
//        assertEquals(50, m.getBall().getVelo().y());
//    }

	@Test
	void shootBall1()
	{
	}

	@Test
	void setBallVX()
	{
		m.setBallVX(0);
		assertEquals(0, m.getBallVX());
	}

	@Test
	void setBallVY()
	{
		m.setBallVY(0);
		assertEquals(0, m.getBallVY());
	}

//    @Test
//    void setBallVXVY() {
//        m.setBallVXVY(0, 0);
//        assertEquals(0, m.getBallVX());
//        assertEquals(0, m.getBallVY());
//    }

	@Test
	void setGravity()
	{
		m.setGravity(10);
		assertEquals(10, m.getGravity());
	}

	@Test
	void setFrictionMU()
	{
		m.setFrictionMU(0.20);
		assertEquals(0.20, m.getFrictionMU());
	}

	@Test
	void setFrictionMU2()
	{
		m.setFrictionMU2(0.20);
		assertEquals(0.20, m.getFrictionMU2());
	}

	@Test
	void isOccupied()
	{
		m.addSquare(10, 10);
		assertTrue(m.isOccupied(0, 0, null, null));
	}

	@Test
	void isOccupiedTriangle()
	{
		m.addTriangle(10, 10);
		assertTrue(m.isOccupied(0, 0, new Ball(7.5, 7.5, 100, 100), null));
	}

	@Test
	void isNotOccupied()
	{
		assertFalse(m.isOccupied(30, 30, null, null));
	}

	@Test
	@DisplayName("Delete Square Gizmo")
	void deleteSquareGizmo()
	{
		m.addSquare(11, 11);
		m.deleteGizmo(11, 11);
		assertFalse(m.getSquares().contains(new Square(11, 11)));
	}

	@Test
	@DisplayName("Delete Circle Gizmo")
	void deleteCircleGizmo()
	{
		m.addCircle(11, 11);
		m.deleteGizmo(11, 11);
		assertFalse(m.getCircles().contains(new CircleGizmo(11, 11)));
	}

	@Test
	@DisplayName("Delete Triangle Gizmo")
	void deleteTriangleGizmo()
	{
		m.addTriangle(11, 11);
		m.deleteGizmo(11, 11);
		assertFalse(m.getTriangles().contains(new Triangle(11, 11)));
	}

	@Test
	@DisplayName("Delete LeftFlipper Gizmo")
	void deleteLeftFlipperGizmo()
	{
		m.addLeftFlipper(11, 11);
		m.deleteGizmo(11, 11);
		assertFalse(m.getLeftFlippers().contains(new LeftFlipper(11, 11)));
	}

	@Test
	@DisplayName("Delete RightFlipper Gizmo")
	void deleteRightFlipperGizmo()
	{
		m.addRightFlipper(11, 11);
		m.deleteGizmo(11, 11);
		assertFalse(m.getRightFlippers().contains(new RightFlipper(11, 11)));
	}

	@Test
	@DisplayName("Delete Absorber Gizmo")
	void deleteAbsorber()
	{
		m.addAbsorber(11, 11, 12, 12);
		m.deleteGizmo(toPx(11), toPx(11));
		assertFalse(m.getAbsorbers().contains(new Absorber(11, 11, 12, 12)));
	}

	@Test
	void checkTriangle()
	{
	}

	@Test
	@DisplayName("Rotate Triangle Gizmo")
	void rotateTriangleGizmo()
	{
		m.addTriangle(60, 60);
		m.rotateGizmo(2, 2);
		assertEquals(1, m.getTriangles().get(m.getTriangles().size() - 1).getNumberOfRotations());
		m.deleteGizmo(2, 2);
	}

	@Test
	@DisplayName("Rotate LeftFlipper Gizmo")
	void rotateLeftFlipperGizmo()
	{
		m.addLeftFlipper(60, 60);
		m.rotateGizmo(2, 2);
		assertEquals(1, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getNumberOfRotations());
		m.deleteGizmo(2, 2);
	}

	@Test
	@DisplayName("Rotate RightFlipper Gizmo")
	void rotateRightFlipperGizmo()
	{
		m.addRightFlipper(60, 60);
		m.rotateGizmo(2, 2);
		assertEquals(1, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getNumberOfRotations());
		m.deleteGizmo(2, 2);
	}

	@Test
	@DisplayName("Rotating Absorber not allowed")
	void rotateAbsorberGizmo()
	{
		m.addAbsorber(60, 60, 120, 120);
		m.rotateGizmo(2, 2);
		assertEquals(60, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getXL());
		assertEquals(60, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getYL());
		assertEquals(120, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getX2L());
		assertEquals(120, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getY2L());

		m.deleteGizmo(2, 2);
	}

	@Test
	void moveTriangleGizmo()
	{
		m.addTriangle(60, 60);
		assertEquals(2, m.getTriangles().get(m.getTriangles().size() - 1).getXL());
		assertEquals(2, m.getTriangles().get(m.getTriangles().size() - 1).getYL());
		m.moveGizmo(60, 60, 90, 90);
		assertEquals(3, m.getTriangles().get(m.getTriangles().size() - 1).getXL());
		assertEquals(3, m.getTriangles().get(m.getTriangles().size() - 1).getYL());
		m.deleteGizmo(2, 2);
	}

	@Test
	void moveSquareGizmo()
	{
		m.addSquare(60, 60);
		assertEquals(2, m.getSquares().get(m.getSquares().size() - 1).getXL());
		assertEquals(2, m.getSquares().get(m.getSquares().size() - 1).getYL());
		m.moveGizmo(60, 60, 90, 90);
		assertEquals(3, m.getSquares().get(m.getSquares().size() - 1).getXL());
		assertEquals(3, m.getSquares().get(m.getSquares().size() - 1).getYL());
		m.deleteGizmo(2, 2);

	}

	@Test
	void moveCircleGizmo()
	{
		m.addCircle(60, 60);
		assertEquals(2, m.getCircles().get(m.getCircles().size() - 1).getXL());
		assertEquals(2, m.getCircles().get(m.getCircles().size() - 1).getYL());
		m.moveGizmo(60, 60, 90, 90);
		assertEquals(3, m.getCircles().get(m.getCircles().size() - 1).getXL());
		assertEquals(3, m.getCircles().get(m.getCircles().size() - 1).getYL());
		m.deleteGizmo(2, 2);

	}

	@Test
	void moveLeftFlipperGizmo()
	{
		m.addLeftFlipper(30, 30);
		assertEquals(1, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getXL());
		assertEquals(1, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getYL());
		m.moveGizmo(30, 30, 60, 60);
		assertEquals(2, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getXL());
		assertEquals(2, m.getLeftFlippers().get(m.getLeftFlippers().size() - 1).getYL());
		m.deleteGizmo(2, 2);

	}

	@Test
	void moveRightFlipperGizmo()
	{
		m.addRightFlipper(60, 60);
		assertEquals(1, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getXL());
		assertEquals(2, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getYL());
		m.moveGizmo(1, 1, 2, 2);
		assertEquals(1, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getXL());
		assertEquals(2, m.getRightFlippers().get(m.getRightFlippers().size() - 1).getYL());
		m.deleteGizmo(2, 2);
	}

	@Test
	void moveAbsorberGizmo()
	{
		m.addAbsorber(2, 2, 3, 3);
		assertEquals(2, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getXL());
		assertEquals(2, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getYL());
		assertEquals(3, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getX2L());
		assertEquals(3, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getY2L());
		m.moveGizmo(60, 60, 90, 90);
		assertEquals(3, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getXL());
		assertEquals(3, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getYL());
		assertEquals(4, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getX2L());
		assertEquals(4, m.getAbsorbers().get(m.getAbsorbers().size() - 1).getY2L());
		m.deleteGizmo(2, 2);

	}

	@Test
	void clearModel()
	{
		m.clearModel();
		assertTrue(m.getTriangles().isEmpty());
		assertTrue(m.getAbsorbers().isEmpty());
		assertTrue(m.getRightFlippers().isEmpty());
		assertTrue(m.getLeftFlippers().isEmpty());
		assertTrue(m.getCircles().isEmpty());
		assertTrue(m.getSquares().isEmpty());

		assertTrue(m.getBalls().isEmpty());
	}

	@Test
	void cloneTest()
	{

	}

	@Test
	void addGizmoConnection()
	{
		CircleGizmo c = new CircleGizmo(90, 90);
		Square s = new Square(100, 100);
		m.addGizmoConnection(c, s);
		assertTrue(c.getConnectedGizmos().contains(s));
	}

	@Test
	void removeGizmoConnection()
	{
		CircleGizmo c = new CircleGizmo(90, 90);
		Square s = new Square(100, 100);
		m.addGizmoConnection(c, s);
		assertTrue(c.getConnectedGizmos().contains(s));
		m.removeGizmoConnection(c, s);
		assertFalse(c.getConnectedGizmos().contains(s));
	}

	@Test
	void addKeyConnection()
	{
		CircleGizmo c = new CircleGizmo(90, 90);
		m.addKeyConnection(KeyStroke.getKeyStroke('c'), c);
		List<Gizmo> connected = new ArrayList<>();
		connected.add(c);
		assertEquals(connected, m.getKeyConnections().get(KeyStroke.getKeyStroke('c')));
	}

	@Test
	void getKeyConnectedGizmos()
	{
		CircleGizmo c = new CircleGizmo(200, 200);
		m.addKeyConnection(KeyStroke.getKeyStroke('x'), c);
		List<Gizmo> connected = new ArrayList<>();
		connected.add(c);
//        assertEquals(connected, m.getKeyConnectedGizmos(KeyStroke.getKeyStroke('x')));

	}

	@Test
	void getNullKeyConnection()
	{
//        CircleGizmo c = new CircleGizmo(200,200);
//        m.addKeyConnection(KeyStroke.getKeyStroke('x'), c);
//        List<Gizmo> connected = new ArrayList<>();
//        connected.add(c);
//        assertNull(m.getKeyConnectedGizmos(null));

	}

	@Test
	void addSameKeyConnection()
	{
		CircleGizmo c = new CircleGizmo(90, 90);
		m.addKeyConnection(KeyStroke.getKeyStroke('c'), c);
		m.addKeyConnection(KeyStroke.getKeyStroke('c'), c);
		List<Gizmo> connected = new ArrayList<>();
		connected.add(c);
		assertEquals(connected, m.getKeyConnections().get(KeyStroke.getKeyStroke('c')));
	}

	@Test
	void removeKeyConnectionTest()
	{
		CircleGizmo c = new CircleGizmo(90, 90);
		m.addKeyConnection(KeyStroke.getKeyStroke('c'), c);
		List<Gizmo> connected = new ArrayList<>();
		connected.add(c);
		assertEquals(connected, m.getKeyConnections().get(KeyStroke.getKeyStroke('c')));
		m.removeKeyConnection(KeyStroke.getKeyStroke('c'), c);
		assertNull(m.getKeyConnections().get(KeyStroke.getKeyStroke('c')));
	}

	@Test
	void removeKeyFromNotConnectedGizmo()
	{
		CircleGizmo c = new CircleGizmo(90, 90);
		Square s = new Square(100, 100);
		m.addKeyConnection(KeyStroke.getKeyStroke('c'), c);
		List<Gizmo> connected = new ArrayList<>();
		connected.add(c);
		assertFalse(m.removeKeyConnection(KeyStroke.getKeyStroke('s'), s));
	}
}