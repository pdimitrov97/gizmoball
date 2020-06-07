package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.github.pdimitrov97.gizmoball.physics.Angle;
import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.Geometry;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;
import com.github.pdimitrov97.gizmoball.physics.Vect;

public abstract class Flipper extends Gizmo implements Serializable
{
	Angle currentAngle;
	protected double velocity;
	boolean raising;

	public Flipper(String name, int xL, int yL, int x2L, int y2L, Color restingColor, Color actingColor)
	{
		super(name, xL, yL, x2L, y2L, restingColor, actingColor);
		move(xL, yL);

		velocity = 0;
		currentAngle = Angle.DEG_90;
		raising = false;
	}

	public abstract boolean isRaised();

	public abstract void move(int xL, int yL);

	public abstract boolean isLowered();

	public abstract void moveByAngle(Angle angle);

	public abstract void setRaising(boolean raising);

	void raiseOrLower(Angle angle)
	{
		List<LineSegment> copyLs = new ArrayList<>();
		List<Circle> copyCircles = new ArrayList<>();

		for (LineSegment l : super.getLineSegments())
		{
			copyLs.add(Geometry.rotateAround(l, super.getCircles().get(0).getCenter(), angle));
		}

		for (Circle c : super.getCircles())
		{
			copyCircles.add(Geometry.rotateAround(c, super.getCircles().get(0).getCenter(), angle));
		}

		super.setLineSegments(copyLs);
		super.setCircles(copyCircles);
		currentAngle = currentAngle.plus(angle);
	}

	public double getVelocity()
	{
		return velocity;
	}

	public void setVelocity(double vel)
	{
		this.velocity = vel;
	}

	boolean isRaising()
	{
		return raising;
	}

	public void rotate()
	{
		List<LineSegment> oldLS = super.getLineSegments();
		List<Circle> oldC = super.getCircles();
		oldLS = oldLS.stream().map(line -> Geometry.rotateAround(line, new Vect(this.getX() + ONE_L_IN_PX, this.getY() + ONE_L_IN_PX), Angle.DEG_90)).collect(Collectors.toList());
		oldC = oldC.stream().map(c -> Geometry.rotateAround(c, new Vect(this.getX() + ONE_L_IN_PX, this.getY() + ONE_L_IN_PX), Angle.DEG_90)).collect(Collectors.toList());
		super.setLineSegments(oldLS);
		super.setCircles(oldC);
		super.setNumberOfRotations((super.getNumberOfRotations() + 1) % 4);
	}

	@Override
	public void act()
	{
		setRaising(!raising);
	}
}