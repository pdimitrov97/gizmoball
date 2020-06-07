package com.github.pdimitrov97.gizmoball.model;

import java.io.Serializable;

import com.github.pdimitrov97.gizmoball.physics.Vect;

public class CollisionDetails implements Serializable
{
	private Ball firstBall;
	private Vect firstBallNewVelocity;
	private Ball secondBall;
	private Vect secondBallNewVelocity;
	private Gizmo gizmo;
	private double timeUntilCollision;

	public CollisionDetails(Ball firstBall, Vect firstBallNewVelocity, Ball secondBall, Vect secondBallNewVelocity, Gizmo gizmo, double timeUntilCollision)
	{
		this.firstBall = firstBall;
		this.firstBallNewVelocity = firstBallNewVelocity;
		this.secondBall = secondBall;
		this.secondBallNewVelocity = secondBallNewVelocity;
		this.gizmo = gizmo;
		this.timeUntilCollision = timeUntilCollision;
	}

	public Ball getFirstBall()
	{
		return firstBall;
	}

	public Vect getFirstBallNewVelocity()
	{
		return firstBallNewVelocity;
	}

	public Ball getSecondBall()
	{
		return secondBall;
	}

	public Vect getSecondBallNewVelocity()
	{
		return secondBallNewVelocity;
	}

	public Gizmo getGizmo()
	{
		return gizmo;
	}

	public double getTimeUntilCollision()
	{
		return timeUntilCollision;
	}
}