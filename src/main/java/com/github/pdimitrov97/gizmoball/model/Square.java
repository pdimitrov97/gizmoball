package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

public class Square extends Gizmo implements Serializable
{
	public Square(int xL, int yL)
	{
		super("some name", xL, yL, (int) (xL + ONE_L_IN_PX), (int) (yL + ONE_L_IN_PX), new Color(254, 169, 104), new Color(204, 252, 212));
		super.connectGizmo(this);
		move(xL, yL);
	}

	public Square(String name, int xL, int yL, Color restingColor, Color actingColor)
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
		newLineSegments.add(new LineSegment((x + ONE_L_IN_PX), y, (x + ONE_L_IN_PX), (y + ONE_L_IN_PX)));
		newLineSegments.add(new LineSegment((x + ONE_L_IN_PX), (y + ONE_L_IN_PX), x, (y + ONE_L_IN_PX)));
		newLineSegments.add(new LineSegment(x, (y + ONE_L_IN_PX), x, y));
		super.setLineSegments(newLineSegments);

		List<Circle> newCircles = new ArrayList<>();
		newCircles.add(new Circle(x, y, 0));
		newCircles.add(new Circle((x + ONE_L_IN_PX), y, 0));
		newCircles.add(new Circle((x + ONE_L_IN_PX), (y + ONE_L_IN_PX), 0));
		newCircles.add(new Circle(x, (y + ONE_L_IN_PX), 0));
		super.setCircles(newCircles);
	}

	public void rotate()
	{
		return;
	}
}