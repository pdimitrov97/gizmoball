package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

public class Walls extends Gizmo implements Serializable
{
	public Walls(String name, int xL, int yL, int x2L, int y2L)
	{
		super(name, xL, yL, x2L, y2L, new Color(0, 0, 0), new Color(0, 0, 0));

		int x = toPx(xL);
		int y = toPx(yL);
		int x2 = toPx(x2L);
		int y2 = toPx(y2L);

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

	public void move(int newXL, int newYL)
	{
		return;
	}

	public void rotate()
	{
		return;
	}
}