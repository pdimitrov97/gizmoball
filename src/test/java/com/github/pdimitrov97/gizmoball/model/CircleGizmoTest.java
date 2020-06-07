package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.HALF_L_IN_PX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pdimitrov97.gizmoball.physics.Circle;

class CircleGizmoTest
{
	private CircleGizmo c;
	private Color mint = new Color(204, 252, 212);
	private Color blue = new Color(0, 198, 197);

	@BeforeEach
	void setUp()
	{
		c = new CircleGizmo(0, 0);
	}

	@Test
	void getCircles()
	{
		List<Circle> cs = new ArrayList<>();
		Circle circle = new Circle((0 + HALF_L_IN_PX), (0 + HALF_L_IN_PX), 15);
		cs.add(circle);
		assertEquals(c.getCircles(), cs);
	}

	@Test
	void rotate()
	{
		c.rotate();
	}

	@Test
	void getColour()
	{
		assertEquals(c.getColour(), blue);
	}

	@Test
	void act()
	{
		c.act();
		assertEquals(mint, c.getColour());
		Timer t = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				assertEquals(blue, c.getColour());
			}
		};
		t.schedule(task, 1000);
	}

	@Test
	void getLineSegs()
	{
		assertTrue(c.getLineSegments().isEmpty());
	}

	@Test
	void getX()
	{
		assertEquals(c.getX(), 0);
	}

	@Test
	void getY()
	{
		assertEquals(c.getY(), 0);
	}

	@Test
	void move()
	{
		c.move(2, 2);
		assertEquals(c.getX(), 60);
		assertEquals(c.getY(), 60);
	}

	@Test
	void getGizmos()
	{
		assertEquals(c.getConnectedGizmos().size(), 1);
	}

	@Test
	void connectToGizmo()
	{
		List<Gizmo> gizmos = new ArrayList<>();
		gizmos.add(c);
		Triangle triangle = new Triangle(2, 2);
		gizmos.add(triangle);
		c.connectGizmo(triangle);
		assertEquals(c.getConnectedGizmos(), gizmos);
	}
}