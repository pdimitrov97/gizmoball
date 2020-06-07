package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;
import com.github.pdimitrov97.gizmoball.physics.Vect;

class AbsorberTest
{
	private Absorber a;

	@BeforeEach
	void setUp()
	{
		a = new Absorber(0, 0, 1, 1);
	}

	@Test
	void act()
	{
		Ball ball = new Ball(10, 10, 0, 0);
		a.absorbBall(ball);
		List<Ball> balls = new ArrayList<>();
		balls.add(ball);
		assertTrue(a.containsAbsorbedBall(ball));
		assertEquals(balls, a.getAbsorbedBalls());
		a.act();
		assertEquals(new Vect(0, -toPx(50)), balls.get(balls.size() - 1).getVelo());
	}

	@Test
	void rotate()
	{

	}

	@Test
	void alignBalls()
	{

	}

	@Test
	void triggerAbsorber()
	{

	}

	@Test
	void getxL()
	{
		assertEquals(0, a.getXL());
	}

	@Test
	void getyL()
	{
		assertEquals(0, a.getYL());
	}

	@Test
	void getX2L()
	{
		assertEquals(1, a.getX2L());
	}

	@Test
	void getY2L()
	{
		assertEquals(1, a.getY2L());
	}

	@Test
	void getLineSegs()
	{
		List<LineSegment> ls = new ArrayList<>();
		ls.add(new LineSegment(0, 0, toPx(1), 0));
		ls.add(new LineSegment(toPx(1), 0, toPx(1), toPx(1)));
		ls.add(new LineSegment(toPx(1), toPx(1), 0, toPx(1)));
		ls.add(new LineSegment(0, toPx(1), 0, 0));
		
		assertEquals(ls, a.getLineSegments());
	}

	@Test
	void getCircles()
	{
		List<Circle> cs = new ArrayList<>();
		cs.add(new Circle(0, 0, 0));
		cs.add(new Circle(30, 0, 0));
		cs.add(new Circle(30, 30, 0));
		cs.add(new Circle(0, 30, 0));
		assertEquals(cs, a.getCircles());
	}

	@Test
	void getX()
	{
		assertEquals(0, a.getX());
	}

	@Test
	void getY()
	{
		assertEquals(0, a.getY());
	}

	@Test
	void getX2()
	{
		assertEquals(toPx(1), a.getX2());
	}

	@Test
	void getY2()
	{
		assertEquals(toPx(1), a.getY2());
	}

	@Test
	void getColour()
	{
		assertEquals(new Color(195, 141, 193), a.getColour());
	}

	@Test
	void move()
	{
		Absorber absorber = new Absorber(0, 0, 1, 1);
		a.move(0, 0);
		assertEquals(absorber, a);
	}

	/*
	 * @Test void containsBallOutOfPosition() { Ball ball = new Ball(0.1, 0.1, 0,
	 * 0); assertTrue(a.containsBallOutOfPosition(ball)); }
	 */
	@Test
	void getGizmos()
	{
		List<Gizmo> connected = new ArrayList<>();
		CircleGizmo c = new CircleGizmo(5, 6);
		connected.add(c);
		a.connectGizmo(c);
		assertEquals(connected, a.getConnectedGizmos());
	}

	@Test
	void connectToGizmo()
	{
		assertTrue(a.connectGizmo(new CircleGizmo(5, 6)));
	}

	@Test
	void disconnectGizmo()
	{
		Square square = new Square(4, 4);
		a.connectGizmo(square);
		List<Gizmo> gizmos = new ArrayList<>();
		gizmos.add(square);
		assertEquals(gizmos, a.getConnectedGizmos());
		a.disconnectGizmo(square);
		assertEquals(new ArrayList<>(), a.getConnectedGizmos());
	}

	@Test
	void connectToAlreadyConnected()
	{
		CircleGizmo c = new CircleGizmo(100, 100);
		a.connectGizmo(c);
		assertFalse(a.connectGizmo(c));
	}
}