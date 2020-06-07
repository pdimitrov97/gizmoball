package com.github.pdimitrov97.gizmoball.model;

import java.awt.Color;
import java.io.Serializable;
import java.util.Objects;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.Geometry;
import com.github.pdimitrov97.gizmoball.physics.Vect;

public class Ball implements Serializable
{
	private Vect velocity;
	private double radius;
	private double xpos;
	private double ypos;
	private Color colour;

	private boolean stopped;

	// x, y coordinates and x,y velocity
	public Ball(double x, double y, double xv, double yv)
	{
		xpos = x; // Centre coordinates
		ypos = y;
		colour = new Color(0, 156, 255);
		velocity = new Vect(xv, yv);
		radius = 7.5;
		stopped = false;
	}

	public Vect getVelo()
	{
		return velocity;
	}

	void setVelo(Vect v)
	{
		velocity = v;
	}

	public double getRadius()
	{
		return radius;
	}

	public Circle getCircle()
	{
		return new Circle(xpos, ypos, radius);
	}

	// Ball specific methods that deal with double precision.
	public double getExactX()
	{
		return xpos;
	}

	public double getExactY()
	{
		return ypos;
	}

	public void setExactX(double x)
	{
		xpos = x;
	}

	public void setExactY(double y)
	{
		ypos = y;
	}

	void stop()
	{
		stopped = true;
	}

	void start()
	{
		stopped = false;
	}

	boolean isStopped()
	{
		return stopped;
	}

	public Color getColour()
	{
		return colour;
	}

	boolean isOverlaping(Ball ball)
	{
		return Geometry.distanceSquared(this.getCircle().getCenter(), ball.getCircle().getCenter()) <= Math.pow(this.radius + ball.getRadius(), 2);
	}

	public void move(double x, double y, double xv, double yv)
	{
		xpos = x; // Centre coordinates
		ypos = y;
		colour = new Color(0, 156, 255);
		velocity = new Vect(xv, yv);
		radius = 7.5;
		stopped = false;
	}

	@Override
	public boolean equals(Object o)
	{
		if (this == o)
			return true;
		
		if (o == null || getClass() != o.getClass())
			return false;
		
		Ball ball = (Ball) o;
		return Double.compare(ball.radius, radius) == 0 && Double.compare(ball.xpos, xpos) == 0 && Double.compare(ball.ypos, ypos) == 0 && stopped == ball.stopped && Objects.equals(velocity, ball.velocity);
	}

	@Override
	public int hashCode()
	{
		return Objects.hash(velocity, radius, xpos, ypos, stopped);
	}
}