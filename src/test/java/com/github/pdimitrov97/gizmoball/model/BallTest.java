package com.github.pdimitrov97.gizmoball.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.Vect;

class BallTest
{
	private Ball ball;

	@BeforeEach
	void setUp()
	{
		ball = new Ball(0, 0, 0, 0);
	}

	@Test
	void getVelo()
	{
		assertEquals(new Vect(0, 0), ball.getVelo());
	}

	@Test
	void setVelo()
	{
		Vect v = new Vect(10, 10);
		ball.setVelo(v);
		assertEquals(v, ball.getVelo());
	}

	@Test
	void getRadius()
	{
		assertEquals(7.5, ball.getRadius());
	}

	@Test
	void getCircle()
	{
		assertEquals(new Circle(0, 0, 7.5), ball.getCircle());
	}

	@Test
	void getExactX()
	{
		assertEquals(0, ball.getExactX());
	}

	@Test
	void getExactY()
	{
		assertEquals(0, ball.getExactY());
	}

	@Test
	void setExactX()
	{
		ball.setExactX(1);
		assertEquals(1, ball.getExactX());
	}

	@Test
	void setExactY()
	{
		ball.setExactY(1);
		assertEquals(1, ball.getExactY());
	}

	@Test
	void stop()
	{
		ball.stop();
		assertTrue(ball.isStopped());
	}

	@Test
	void start()
	{
		ball.start();
		assertFalse(ball.isStopped());
	}

	@Test
	void isStopped()
	{
		ball.stop();
		assertTrue(ball.isStopped());
	}

	@Test
	void getColour()
	{
		Color blue = new Color(0, 156, 255);
		assertEquals(blue, ball.getColour());

	}

	@Test
	void equals()
	{
		assertEquals(new Ball(0, 0, 0, 0), ball);
	}

	@Test
	void hashCodeTest()
	{
		assertNotEquals(new Ball(1, 1, 1, 1).hashCode(), ball.hashCode());
	}

	@Test
	void move()
	{
		Ball b = new Ball(90, 90, 0, 0);
		ball.move(90, 90, 0, 0);
		assertEquals(b, ball);
	}
}