package com.github.pdimitrov97.gizmoball.model;

import java.io.Serializable;

import com.github.pdimitrov97.gizmoball.physics.Vect;

class BallsCollisionDetails implements Serializable
{
	private double tuc;
	private Ball ball1;
	private Vect velo1;
	private Ball ball2;
	private Vect velo2;

	BallsCollisionDetails(double tuc, Ball ball1, Vect velo1, Ball ball2, Vect velo2)
	{
		this.tuc = tuc;
		this.ball1 = ball1;
		this.velo1 = velo1;
		this.ball2 = ball2;
		this.velo2 = velo2;
	}

	double getTuc()
	{
		return tuc;
	}

	Ball getBall1()
	{
		return ball1;
	}

	Vect getVelo1()
	{
		return velo1;
	}

	Ball getBall2()
	{
		return ball2;
	}

	Vect getVelo2()
	{
		return velo2;
	}
}