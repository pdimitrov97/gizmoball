package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pdimitrov97.gizmoball.physics.Angle;
import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.Geometry;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;
import com.github.pdimitrov97.gizmoball.physics.Vect;

public class Triangle extends Gizmo implements Serializable
{
	public Triangle(int xL, int yL)
	{
		super("some name", xL, yL, (int) (xL + ONE_L_IN_PX), (int) (yL + ONE_L_IN_PX), new Color(239, 244, 140), new Color(204, 252, 212));
		super.connectGizmo(this);
		move(xL, yL);
	}

	public Triangle(String name, int xL, int yL, Color restingColor, Color actingColor)
	{
		super(name, xL, yL, (int) (xL + ONE_L_IN_PX), (int) (yL + ONE_L_IN_PX), restingColor, actingColor);
		super.connectGizmo(this);
		move(xL, yL);
	}

	public void move(int newXL, int newYL)
	{
		super.setXL(newXL);
		super.setYL(newYL);
		int x = toPx(newXL);
		int y = toPx(newYL);

		List<LineSegment> newLineSegments = new ArrayList<>();
		newLineSegments.add(new LineSegment(x, y, (x + ONE_L_IN_PX), y));
		newLineSegments.add(new LineSegment((x + ONE_L_IN_PX), y, x, (y + ONE_L_IN_PX)));
		newLineSegments.add(new LineSegment(x, (y + ONE_L_IN_PX), x, y));
		super.setLineSegments(newLineSegments);

		List<Circle> newCircles = new ArrayList<>();
		newCircles.add(new Circle(x, y, 0));
		newCircles.add(new Circle((x + ONE_L_IN_PX), y, 0));
		newCircles.add(new Circle(x, (y + ONE_L_IN_PX), 0));
		super.setCircles(newCircles);

		int tempRotations = super.getNumberOfRotations();

		for (int i = 0; i < tempRotations; i++)
			rotate();

		super.setNumberOfRotations(tempRotations);
	}

	public void rotate()
	{
		List<LineSegment> oldLineSegments = super.getLineSegments();
		LineSegment l1 = Geometry.rotateAround(oldLineSegments.get(0), new Vect((super.getX() + HALF_L_IN_PX), (super.getY() + HALF_L_IN_PX)), new Angle(Math.toRadians(90)));
		LineSegment l2 = Geometry.rotateAround(oldLineSegments.get(1), new Vect((super.getX() + HALF_L_IN_PX), (super.getY() + HALF_L_IN_PX)), new Angle(Math.toRadians(90)));
		LineSegment l3 = Geometry.rotateAround(oldLineSegments.get(2), new Vect((super.getX() + HALF_L_IN_PX), (super.getY() + HALF_L_IN_PX)), new Angle(Math.toRadians(90)));

		List<LineSegment> newLineSegments = new ArrayList<>();
		newLineSegments.add(l1);
		newLineSegments.add(l2);
		newLineSegments.add(l3);
		super.setLineSegments(newLineSegments);

		List<Circle> newCircles = new ArrayList<>();
		newCircles.add(new Circle(l1.p1().x(), l1.p1().y(), 0));
		newCircles.add(new Circle(l2.p1().x(), l2.p1().y(), 0));
		newCircles.add(new Circle(l3.p1().x(), l3.p1().y(), 0));
		super.setCircles(newCircles);

		super.setNumberOfRotations((super.getNumberOfRotations() + 1) % 4);
	}
}