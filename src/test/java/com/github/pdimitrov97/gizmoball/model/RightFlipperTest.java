package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_AND_A_HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.QUARTER_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.SEVEN_QUARTERS_L_IN_PX;
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

class RightFlipperTest
{
	private RightFlipper rf;

	@BeforeEach
	void setUp()
	{
		rf = new RightFlipper(0, 0);
	}

	@Test
	void getX()
	{
		assertEquals(0, rf.getX());
	}

	@Test
	void getY()
	{
		assertEquals(0, rf.getY());
	}

	@Test
	void getColour()
	{
		assertEquals(new Color(119, 119, 221), rf.getColour());
	}

	@Test
	void actRaised()
	{
		// Is raised
		rf.moveByAngle(Angle.DEG_90);
		assertTrue(rf.isRaised());
		assertTrue(!rf.isRaising());
		assertTrue(!rf.isLowered());
		assertEquals(0, rf.getVelocity());
	}

	@Test
	void actLowered()
	{
		// Raise it all the way to be able to lower it
		rf.setRaising(true);
		rf.moveByAngle(Angle.DEG_90);

		// Lower the flipper
		rf.setRaising(false);
		rf.moveByAngle(new Angle(-Angle.DEG_90.radians()));
		assertTrue(rf.isLowered());
		assertTrue(!rf.isRaised());
		assertTrue(!rf.isRaising());
		assertEquals(0, rf.getVelocity());
	}

	@Test
	void actRaising()
	{
		// Raising
		rf.setRaising(true);
		rf.moveByAngle(Angle.DEG_45);
		assertTrue(rf.isRaising());
		assertTrue(!rf.isLowered());
		assertTrue(!rf.isRaised());
		assertTrue(rf.getVelocity() > 0);
	}

	@Test
	void actLowering()
	{
		// Raise the flipper all the way
		rf.setRaising(true);
		rf.moveByAngle(Angle.DEG_90);
		// Lowering
		rf.setRaising(false);
		rf.moveByAngle(new Angle(-Angle.DEG_45.radians()));
		assertTrue(!rf.isRaising());
		assertTrue(!rf.isLowered());
		assertTrue(!rf.isRaised());
		assertTrue(rf.getVelocity() < 0);
	}

	@Test
	@DisplayName("Lower flipper when already lowered")
	void lowerLoweredFlipper()
	{
		rf.setRaising(false);
		rf.moveByAngle(new Angle(-Angle.DEG_90.radians()));
		assertTrue(!rf.isRaising());
		assertTrue(rf.isLowered());
		assertTrue(!rf.isRaised());
		assertEquals(0, rf.getVelocity());
	}

	@Test
	@DisplayName("Raise flipper when already raised")
	void raiseRaisedFlipper()
	{
		rf.setRaising(true);
		rf.moveByAngle(Angle.DEG_90);
		assertTrue(rf.isRaised());

		rf.setRaising(true);
		rf.moveByAngle(Angle.DEG_45);
		assertTrue(rf.isRaised());
		assertTrue(!rf.isRaising());
		assertTrue(!rf.isLowered());
		assertEquals(0, rf.getVelocity());
	}

	@Test
	void getLineSegs()
	{
		List<LineSegment> ls = new ArrayList<>();

		// Horizontal Top Line
		ls.add(new LineSegment(rf.getX() + ONE_AND_A_HALF_L_IN_PX, rf.getY() + QUARTER_L_IN_PX, rf.getX() + TWO_L_IN_PX, rf.getY() + QUARTER_L_IN_PX));
		// Horizontal Bottom Line
		ls.add(new LineSegment(rf.getX() + ONE_AND_A_HALF_L_IN_PX, rf.getY() + TWO_L_MINUS_QUARTER_IN_PX, rf.getX() + TWO_L_IN_PX, rf.getY() + TWO_L_MINUS_QUARTER_IN_PX));

		// Line in the middle from top to bottom
		ls.add(new LineSegment(rf.getX() + SEVEN_QUARTERS_L_IN_PX, rf.getY(), rf.getX() + SEVEN_QUARTERS_L_IN_PX, rf.getY() + TWO_L_IN_PX));

		double x = rf.getX() + ONE_AND_A_HALF_L_IN_PX;
		while (x <= rf.getX() + TWO_L_IN_PX)
		{
			ls.add(new LineSegment(x, rf.getY() + QUARTER_L_IN_PX, x, rf.getY() + TWO_L_MINUS_QUARTER_IN_PX));
			x++;
		}
//        //Left Line
//        ls.add(new LineSegment(ONE_AND_A_HALF_L_IN_PX, QUARTER_L_IN_PX, ONE_AND_A_HALF_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX));
//        //Right Line
//        ls.add(new LineSegment(TWO_L_IN_PX, QUARTER_L_IN_PX, TWO_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX));
		assertEquals(ls, rf.getLineSegments());
	}

	@Test
	void getCircles()
	{
		List<Circle> circles = new ArrayList<>();

		// Top And Bottom Circles
		double j = QUARTER_L_IN_PX;
		while (j >= 0)
		{
			circles.add(new Circle(rf.getX() + SEVEN_QUARTERS_L_IN_PX, rf.getY() + QUARTER_L_IN_PX, j));
			circles.add(new Circle(rf.getX() + SEVEN_QUARTERS_L_IN_PX, rf.getY() + TWO_L_MINUS_QUARTER_IN_PX, j));
			j--;
		}
//        //Top Circle
//        circles.add(new Circle(SEVEN_QUARTERS_L_IN_PX, QUARTER_L_IN_PX, QUARTER_L_IN_PX));
//        //Bottom Circle
//        circles.add(new Circle(SEVEN_QUARTERS_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX, QUARTER_L_IN_PX));

		// 0 radius circles for every line
		circles.add(new Circle(ONE_AND_A_HALF_L_IN_PX, QUARTER_L_IN_PX, 0));
		circles.add(new Circle(ONE_AND_A_HALF_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(TWO_L_IN_PX, QUARTER_L_IN_PX, 0));
		circles.add(new Circle(TWO_L_IN_PX, TWO_L_MINUS_QUARTER_IN_PX, 0));

		// 0 radius circles for horizontal lines
		circles.add(new Circle(rf.getX() + ONE_AND_A_HALF_L_IN_PX, rf.getY() + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(rf.getX() + TWO_L_IN_PX, rf.getY() + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(rf.getX() + ONE_AND_A_HALF_L_IN_PX, rf.getY() + TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(rf.getX() + TWO_L_IN_PX, rf.getY() + TWO_L_MINUS_QUARTER_IN_PX, 0));

		// 0 radius circle for vertical line through whole flipper
		circles.add(new Circle(rf.getX() + SEVEN_QUARTERS_L_IN_PX, rf.getY(), 0));
		circles.add(new Circle(rf.getX() + SEVEN_QUARTERS_L_IN_PX, rf.getY() + TWO_L_IN_PX, 0));

		assertEquals(circles, rf.getCircles());
	}

	@Test
	void isRaised()
	{
		assertFalse(rf.isRaised());
	}

	@Test
	void isLowered()
	{
		assertTrue(rf.isLowered());
	}

	@Test
	void setRaising()
	{
		rf.setRaising(true);
		assertTrue(rf.isRaising());
	}

	@Test
	void isRaising()
	{
		assertFalse(rf.isRaising());
	}

	@Test
	void getVelocity()
	{
		assertEquals(0, rf.getVelocity());
	}

	@Test
	void setVelocity()
	{
		rf.setVelocity(1);
		assertEquals(1, rf.getVelocity());
	}

	@Test
	void rotate()
	{
		rf.rotate();
		assertEquals(0, rf.getX());
		assertEquals(0, rf.getY());
	}

	@Test
	void getRotationNo()
	{
		assertEquals(0, rf.getNumberOfRotations());
		rf.rotate();
		assertEquals(1, rf.getNumberOfRotations());
	}

	@Test
	void move()
	{
		rf.move(5, 6);
		assertEquals(5, rf.getXL());
		assertEquals(6, rf.getYL());
	}
}