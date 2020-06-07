package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pdimitrov97.gizmoball.physics.Circle;

public class CircleGizmo extends Gizmo implements Serializable
{
	public CircleGizmo(int xL, int yL)
	{
		super("some name", xL, yL, (int) (xL + ONE_L_IN_PX), (int) (yL + ONE_L_IN_PX), new Color(0, 198, 197), new Color(204, 252, 212));
		super.connectGizmo(this);
		move(xL, yL);
	}

	public CircleGizmo(String name, int xL, int yL, Color restingColor, Color actingColor)
	{
		super(name, xL, yL, (int) (xL + ONE_L_IN_PX), (int) (yL + ONE_L_IN_PX), restingColor, actingColor);
		super.connectGizmo(this);
		move(xL, yL);
	}

	public void move(int newXL, int newYL)
	{
		super.setXL(newXL);
		super.setYL(newYL);
		super.setLineSegments(new ArrayList<>());

		List<Circle> newCircles = new ArrayList<>();
		newCircles.add(new Circle((toPx(newXL) + HALF_L_IN_PX), (toPx(newYL) + HALF_L_IN_PX), HALF_L_IN_PX));
		super.setCircles(newCircles);
	}

	public void rotate()
	{
		return;
	}
}