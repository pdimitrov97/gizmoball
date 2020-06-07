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

class SquareTest
{
	private Square s;
	private Color mint = new Color(204, 252, 212);
	private Color orange = new Color(254, 169, 104);

	@BeforeEach
	void setUp()
	{
		s = new Square(0, 0);
	}

	@Test
	void getColour()
	{
		assertEquals(orange, s.getColour());
	}

	@Test
	void act()
	{
		s.act();
		assertEquals(mint, s.getColour());
		Timer t = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				assertEquals(orange, s.getColour());
			}
		};
		t.schedule(task, 1000);
	}

	@Test
	void getLineSegs()
	{
		List<LineSegment> ls = new ArrayList<>();
		ls.add(new LineSegment(0, 0, 30, 0));
		ls.add(new LineSegment(30, 0, 30, 30));
		ls.add(new LineSegment(30, 30, 0, 30));
		ls.add(new LineSegment(0, 30, 0, 0));
		assertEquals(ls, s.getLineSegments());
	}

	@Test
	void getCircles()
	{
		List<Circle> cs = new ArrayList<>();
		cs.add(new Circle(0, 0, 0));
		cs.add(new Circle(30, 0, 0));
		cs.add(new Circle(30, 30, 0));
		cs.add(new Circle(0, 30, 0));
		assertEquals(cs, s.getCircles());
	}

	@Test
	void getX()
	{
		assertEquals(0, s.getX());
	}

	@Test
	void getY()
	{
		assertEquals(0, s.getY());
	}

	@Test
	void move()
	{
		s.move(2, 2);
		assertEquals(s.getX(), 60);
		assertEquals(s.getY(), 60);
	}

	@Test
	void getGizmos()
	{
		assertEquals(s.getConnectedGizmos().size(), 1);
	}

	@Test
	void connectToGizmo()
	{
		List<Gizmo> gizmos = new ArrayList<>();
		gizmos.add(s);
		CircleGizmo circleGizmo = new CircleGizmo(2, 2);
		gizmos.add(circleGizmo);
		s.connectGizmo(circleGizmo);
		assertEquals(s.getConnectedGizmos(), gizmos);
	}

	@Test
	void rotate()
	{
		s.rotate();
	}

	@Test
	void disconnectGizmo()
	{
		Square square = new Square(4, 4);
		s.connectGizmo(square);
		List<Gizmo> gizmos = new ArrayList<>();
		gizmos.add(s);
		gizmos.add(square);
		assertEquals(gizmos, s.getConnectedGizmos());
		s.disconnectGizmo(square);
		List<Gizmo> gizmoAfter = new ArrayList<>();
		gizmoAfter.add(s);
		assertEquals(gizmoAfter, s.getConnectedGizmos());
	}
}