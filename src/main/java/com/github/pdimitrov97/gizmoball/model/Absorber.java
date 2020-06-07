package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;
import com.github.pdimitrov97.gizmoball.physics.Vect;

public class Absorber extends Gizmo implements Serializable
{
	private List<Ball> absorbedBalls;
	private List<Ball> shotBalls;

	public Absorber(int xL, int yL, int x2L, int y2L)
	{
		super("some name", xL, yL, x2L, y2L, new Color(195, 141, 193), new Color(195, 141, 193));
		move(xL, yL);

		absorbedBalls = new LinkedList<>();
		shotBalls = new LinkedList<>();
	}

	void absorbBall(Ball ball)
	{
		ball.setVelo(new Vect(0, 0));
		ball.stop();
		absorbedBalls.add(ball);
		alignBalls();
	}

	public void removeShotBall(Ball ball)
	{
		absorbedBalls.remove(ball);
		shotBalls.remove(ball);
	}

	public boolean containsAbsorbedBall(Ball ball)
	{
		return absorbedBalls.contains(ball);
	}

	public boolean isShootingBall(Ball ball)
	{
		return shotBalls.contains(ball);
	}

	public List<Ball> getAbsorbedBalls()
	{
		return absorbedBalls;
	}

	public List<Ball> getShotBalls()
	{
		return shotBalls;
	}

	private void alignBalls()
	{
		int step = 0;

		for (Ball ball : absorbedBalls)
		{
			ball.setExactX(toPx(super.getX2L()) - ball.getRadius() - step);
			ball.setExactY(toPx(super.getY2L()) - ball.getRadius());

			if ((toPx(super.getX2L()) - ball.getRadius() - step) >= toPx(super.getXL()) + ball.getRadius())
				step += 3 * ball.getRadius();
		}
	}

	public boolean isBallInsideAbsrober(Ball ball)
	{
		return ball.getCircle().getCenter().x() + ball.getRadius() > toPx(super.getXL()) && ball.getCircle().getCenter().x() - ball.getRadius() <= toPx(super.getX2L()) && ball.getCircle().getCenter().y() + ball.getRadius() > toPx(super.getYL()) && ball.getCircle().getCenter().y() - ball.getRadius() <= toPx(super.getY2L());
	}

	public void act()
	{
		Ball shootBall = null;

		if (absorbedBalls.size() > 0)
			shootBall = absorbedBalls.get(0);

		if (shootBall != null)
		{
			shotBalls.add(shootBall);
			shootBall.setVelo(new Vect(toPx(0), toPx(-50))); // CHECK THIS LINE!!!
			shootBall.start();

			Timer timer = new Timer();
			TimerTask timerTask = new TimerTask()
			{
				@Override
				public void run()
				{
					alignBalls();
				}
			};
			timer.schedule(timerTask, 200);
		}
	}

	public void move(int newXL, int newYL)
	{
		int width = super.getX2L() - super.getXL();
		int height = super.getY2L() - super.getYL();

		super.setXL(newXL);
		super.setYL(newYL);
		super.setX2L(newXL + width);
		super.setY2L(newYL + height);

		int x = toPx(newXL);
		int y = toPx(newYL);
		int x2 = toPx(newXL + width);
		int y2 = toPx(newYL + height);

		List<LineSegment> newLineSegments = new ArrayList<>();
		newLineSegments.add(new LineSegment(x, y, x2, y));
		newLineSegments.add(new LineSegment(x2, y, x2, y2));
		newLineSegments.add(new LineSegment(x2, y2, x, y2));
		newLineSegments.add(new LineSegment(x, y2, x, y));
		super.setLineSegments(newLineSegments);

		List<Circle> newCircles = new ArrayList<>();
		newCircles.add(new Circle(x, y, 0));
		newCircles.add(new Circle(x2, y, 0));
		newCircles.add(new Circle(x2, y2, 0));
		newCircles.add(new Circle(x, y2, 0));
		super.setCircles(newCircles);
	}

	public void rotate()
	{
		return;
	}

	public boolean equals(Object o)
	{
		if (o == null)
			return false;

		if (getClass() != o.getClass())
			return false;

		if (this == o)
			return true;

		Absorber absorber = (Absorber) o;

		return super.getXL() == absorber.getXL() && super.getYL() == absorber.getYL() && super.getX2L() == absorber.getX2L() && super.getY2L() == absorber.getY2L();
	}
}