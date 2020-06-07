package com.github.pdimitrov97.gizmoball.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

class WallsTest
{

	private Walls walls;

	@BeforeEach
	void setUp()
	{
		walls = new Walls("OuterWalls", 0, 0, 1, 1);
	}

	@Test
	void getLineSegs()
	{
		ArrayList<LineSegment> ls = new ArrayList<>();
		ls.add(new LineSegment(0, 0, 30, 0));
		ls.add(new LineSegment(30, 0, 30, 30));
		ls.add(new LineSegment(30, 30, 0, 30));
		ls.add(new LineSegment(0, 30, 0, 0));

		assertEquals(ls, walls.getLineSegments());
	}

	@Test
	void getCircles()
	{
		List<Circle> circles = new ArrayList<Circle>()
		{
			{
				add(new Circle(0, 0, 0));
				add(new Circle(30, 0, 0));
				add(new Circle(30, 30, 0));
				add(new Circle(0, 30, 0));
			}
		};
		assertEquals(circles, walls.getCircles());
	}

	@Test
	void getX()
	{
		assertEquals(0, walls.getX());
	}

	@Test
	void getY()
	{
		assertEquals(0, walls.getY());
	}

	@Test
	void getGizmos()
	{
		assertTrue(walls.getConnectedGizmos().isEmpty());
	}

	@Test
	void connectToGizmo()
	{
		CircleGizmo c = new CircleGizmo(5, 6);
		List<Gizmo> connected = new ArrayList<>();
		connected.add(c);
		walls.connectGizmo(c);
		assertEquals(connected, walls.getConnectedGizmos());
	}
}