package com.github.pdimitrov97.gizmoball.model;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

class TriangleTest
{
	private Triangle t;
	private Color mint = new Color(204, 252, 212);
	private Color yellow = new Color(239, 244, 140);

	@BeforeEach
	void setUp()
	{
		t = new Triangle(0, 0);
	}

	/*
	 * @Test void getXpoints() { int[] xs = new int[3]; xs[0] = 0; xs[1] = 30; xs[2]
	 * = 0;
	 * 
	 * assertEquals(t.getXpoints()[0], xs[0]); assertEquals(t.getXpoints()[1],
	 * xs[1]); assertEquals(t.getXpoints()[2], xs[2]); }
	 * 
	 * @Test void getYpoints() { int[] ys = new int[3];
	 * 
	 * ys[0] = 0; ys[1] = 0; ys[2] = 30;
	 * 
	 * assertEquals(t.getYpoints()[0], ys[0]); assertEquals(t.getYpoints()[1],
	 * ys[1]); assertEquals(t.getYpoints()[2], ys[2]); }
	 */
	@Test
	void rotate()
	{
		t.rotate();
		assertEquals(t.getNumberOfRotations(), (0 + 1) % 4);
	}

	@Test
	void act()
	{
		t.act();
		assertEquals(mint, t.getColour());
		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				assertEquals(yellow, t.getColour());
			}
		};
		timer.schedule(task, 1000);
	}

	@Test
	void getLineSegs()
	{
		List<LineSegment> ls = new ArrayList<>();

		ls.add(new LineSegment(0, 0, 30, 0)); // a
		ls.add(new LineSegment(30, 0, 0, 30)); // b
		ls.add(new LineSegment(0, 30, 0, 0)); // c

		assertEquals(t.getLineSegments(), ls);

	}

	@Test
	void getCircles()
	{
		List<Circle> cs = new ArrayList<>();

		cs.add(new Circle(0, 0, 0));
		cs.add(new Circle(30, 0, 0));
		cs.add(new Circle(0, 30, 0));

		assertEquals(t.getCircles(), cs);
	}

	@Test
	void getX()
	{
		assertEquals(t.getX(), 0);
	}

	@Test
	void getY()
	{
		assertEquals(t.getY(), 0);
	}

	@Test
	void getColour()
	{
		assertEquals(t.getColour(), yellow);
	}

	@Test
	void getRotationNo()
	{
		assertEquals(t.getNumberOfRotations(), 0);
	}

	@Test
	void move()
	{
		t.rotate();
		t.move(2, 2);
		assertEquals(t.getX(), 60);
		assertEquals(t.getY(), 60);
	}

	@Test
	void getGizmos()
	{
		assertEquals(t.getConnectedGizmos().size(), 1);
	}

	@Test
	void connectToGizmo()
	{
		Square square = new Square(1, 1);
		t.connectGizmo(square);
		List<Gizmo> gizmos = new ArrayList<>();
		gizmos.add(t);
		gizmos.add(square);
		assertEquals(t.getConnectedGizmos(), gizmos);
	}

	@Test
	void disconnectGizmo()
	{
		Square square = new Square(4, 4);
		t.connectGizmo(square);
		List<Gizmo> gizmos = new ArrayList<>();
		gizmos.add(t);
		gizmos.add(square);
		assertEquals(gizmos, t.getConnectedGizmos());
		t.disconnectGizmo(square);
		List<Gizmo> gizmoAfter = new ArrayList<>();
		gizmoAfter.add(t);
		assertEquals(gizmoAfter, t.getConnectedGizmos());
	}
}