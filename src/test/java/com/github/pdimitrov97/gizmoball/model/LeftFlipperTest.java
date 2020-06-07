package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.QUARTER_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.TWO_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.TWO_L_MINUS_QUARTER_IN_PX;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.github.pdimitrov97.gizmoball.physics.Angle;
import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

class LeftFlipperTest
{
	private Flipper lf;

	@BeforeEach
	void setUp()
	{
		lf = new LeftFlipper(0, 0);
	}

	@Test
	void getX()
	{
		assertEquals(0, lf.getX());
	}

	@Test
	void getY()
	{
		assertEquals(0, lf.getY());
	}

	@Test
	void getXL()
	{
		assertEquals(0, lf.getXL());
	}

	@Test
	void getYL()
	{
		assertEquals(0, lf.getYL());
	}

	@Test
	void getColour()
	{
		assertEquals(new Color(119, 119, 221), lf.getColour());
	}

	@Test
	void actInterfaceMethod()
	{
		lf.act();
	}

	@Test
	void actRaised()
	{
		// Is raised
		lf.moveByAngle(new Angle(-Angle.DEG_90.radians()));
		assertTrue(lf.isRaised());
		assertTrue(!lf.isRaising());
		assertTrue(!lf.isLowered());
		assertEquals(0, lf.getVelocity());
	}

	@Test
	void actLowered()
	{
		// Raise it all the way to be able to lower it
		lf.setRaising(true);
		lf.moveByAngle(new Angle(-Angle.DEG_90.radians()));

		// Lower the flipper
		lf.setRaising(false);
		lf.moveByAngle(Angle.DEG_90);
		assertTrue(lf.isLowered());
		assertTrue(!lf.isRaised());
		assertTrue(!lf.isRaising());
		assertEquals(0, lf.getVelocity());
	}

	@Test
	void actRaising()
	{
		// Raising
		lf.setRaising(true);
		lf.moveByAngle(new Angle(-Angle.DEG_45.radians()));
		assertTrue(lf.isRaising());
		assertTrue(!lf.isLowered());
		assertTrue(!lf.isRaised());
		assertTrue(lf.getVelocity() < 0);
	}

	@Test
	void actLowering()
	{
		// Raise the flipper all the way
		lf.setRaising(true);
		lf.moveByAngle(new Angle(-Angle.DEG_90.radians()));
		// Lowering
		lf.setRaising(false);
		lf.moveByAngle(Angle.DEG_45);
		assertTrue(!lf.isRaising());
		assertTrue(!lf.isLowered());
		assertTrue(!lf.isRaised());
		assertTrue(lf.getVelocity() > 0);
	}

	@Test
	@DisplayName("Lower flipper when already lowered")
	void lowerLoweredFlipper()
	{
		lf.setRaising(false);
		lf.moveByAngle(Angle.DEG_90);
		assertTrue(!lf.isRaising());
		assertTrue(lf.isLowered());
		assertTrue(!lf.isRaised());
		assertEquals(0, lf.getVelocity());
	}

	@Test
	@DisplayName("Raise flipper when already raised")
	void raiseRaisedFlipper()
	{
		lf.setRaising(true);
		lf.moveByAngle(new Angle(-Angle.DEG_90.radians()));
		assertTrue(lf.isRaised());

		lf.setRaising(true);
		lf.moveByAngle(new Angle(-Angle.DEG_45.radians()));
		assertTrue(lf.isRaised());
		assertTrue(!lf.isRaising());
		assertTrue(!lf.isLowered());
		assertEquals(0, lf.getVelocity());
	}

	@Test
	void getLineSegs()
	{
		List<LineSegment> ls = new ArrayList<>();
		ls.add(new LineSegment(lf.getX(), lf.getY() + QUARTER_L_IN_PX, lf.getX() + HALF_L_IN_PX, lf.getY() + QUARTER_L_IN_PX));
		ls.add(new LineSegment(lf.getX(), lf.getY() + TWO_L_MINUS_QUARTER_IN_PX, lf.getX() + HALF_L_IN_PX, lf.getY() + TWO_L_MINUS_QUARTER_IN_PX));
		ls.add(new LineSegment(lf.getX() + QUARTER_L_IN_PX, lf.getY(), lf.getX() + QUARTER_L_IN_PX, lf.getY() + TWO_L_IN_PX));
//        ls.add(new LineSegment(0, QUARTER_L_IN_PX, 0, TWO_L_MINUS_QUARTER_IN_PX));
//        ls.add(new LineSegment(HALF_L_IN_PX, 0 + QUARTER_L_IN_PX, HALF_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX));
		int x = lf.getX();
		while (x <= lf.getX() + HALF_L_IN_PX)
		{
			ls.add(new LineSegment(x, lf.getY() + QUARTER_L_IN_PX, x, lf.getY() + TWO_L_MINUS_QUARTER_IN_PX));
			x++;
		}
		assertEquals(ls, lf.getLineSegments());
	}

	@Test
	void getCircles()
	{
		List<Circle> circles = new ArrayList<>();
		// Top And Bottom Circles
		double j = QUARTER_L_IN_PX;
		while (j >= 0)
		{
			circles.add(new Circle(lf.getX() + QUARTER_L_IN_PX, lf.getY() + QUARTER_L_IN_PX, j));
			circles.add(new Circle(lf.getX() + QUARTER_L_IN_PX, lf.getY() + TWO_L_MINUS_QUARTER_IN_PX, j));
			j--;
		}

		// 0 radius circles for horizontal lines
		circles.add(new Circle(lf.getX(), lf.getY() + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(lf.getX() + HALF_L_IN_PX, lf.getY() + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(lf.getX(), lf.getY() + TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(lf.getX() + HALF_L_IN_PX, lf.getY() + TWO_L_MINUS_QUARTER_IN_PX, 0));

		// 0 radius circle for vertical line through whole flipper
		circles.add(new Circle(lf.getX() + QUARTER_L_IN_PX, lf.getY(), 0));
		circles.add(new Circle(lf.getX() + QUARTER_L_IN_PX, lf.getY() + TWO_L_IN_PX, 0));

		// 0 radius circles for every line
		circles.add(new Circle(0, QUARTER_L_IN_PX, 0));
		circles.add(new Circle(0, TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(HALF_L_IN_PX, QUARTER_L_IN_PX, 0));
		circles.add(new Circle(HALF_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX, 0));

		assertEquals(circles, lf.getCircles());
	}

	@Test
	void isRaised()
	{
		assertFalse(lf.isRaised());
	}

	@Test
	void isLowered()
	{
		assertTrue(lf.isLowered());
	}

	@Test
	void setRaising()
	{
		lf.setRaising(true);
		assertTrue(lf.isRaising());
	}

	@Test
	void isRaising()
	{
		assertFalse(lf.isRaising());
	}

	@Test
	void getVelocity()
	{
		assertEquals(0, lf.getVelocity());
	}

	@Test
	void setVelocity()
	{
		lf.setVelocity(1);
		assertEquals(1, lf.getVelocity());
	}

	@Test
	void rotate()
	{
		lf.rotate();
		assertEquals(0, lf.getX());
		assertEquals(0, lf.getY());
	}

	@Test
	void getRotationNo()
	{
		assertEquals(0, lf.getNumberOfRotations());
		lf.rotate();
		assertEquals(1, lf.getNumberOfRotations());
	}

	@Test
	void getGizmos()
	{
		Gizmo circle = new CircleGizmo(60, 60);
		lf.connectGizmo(circle);
		assertEquals(1, lf.getConnectedGizmos().size());
	}

	@Test
	void connectToGizmo()
	{
		Gizmo circle = new CircleGizmo(5, 6);
		assertTrue(lf.connectGizmo(circle));
	}

	@Test
	void move()
	{
		lf.move(5, 6);
		assertEquals(5, lf.getXL());
		assertEquals(6, lf.getYL());
	}
}