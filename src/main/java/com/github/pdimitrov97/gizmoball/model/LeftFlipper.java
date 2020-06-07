package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.QUARTER_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.TWO_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.TWO_L_MINUS_QUARTER_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.github.pdimitrov97.gizmoball.physics.Angle;
import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

public class LeftFlipper extends Flipper implements Serializable
{
	public LeftFlipper(int xL, int yL)
	{
		super("some name", xL, yL, (int) (xL + TWO_L_IN_PX), (int) (yL + TWO_L_IN_PX), new Color(119, 119, 221), new Color(119, 119, 221));
	}

	public LeftFlipper(String name, int xL, int yL, Color restingColor, Color actingColor)
	{
		super(name, xL, yL, (int) (xL + TWO_L_IN_PX), (int) (yL + TWO_L_IN_PX), restingColor, actingColor);
	}

	@Override
	public void moveByAngle(Angle angle)
	{
		if (currentAngle.plus(angle).compareTo(Angle.DEG_90) < 0)
		{
			raiseOrLower(angle);
			
			if (raising)
				velocity = -Angle.DEG_90.radians() * 12;
			else
				velocity = Angle.DEG_90.radians() * 12;
		}
		else
		{
			if (raising)
			{
				raiseOrLower(Angle.ZERO.minus(currentAngle));
				velocity = 0;
			}
			else
			{
				raiseOrLower(Angle.DEG_90.minus(currentAngle));
				velocity = 0;
			}
		}
	}

	@Override
	public boolean isRaised()
	{
		if (currentAngle.equals(Angle.ZERO))
		{
			raising = false;
			velocity = 0;
			return true;
		}
		
		return false;
	}

	@Override
	public boolean isLowered()
	{
		if (currentAngle.equals(Angle.DEG_90))
		{
			raising = false;
			velocity = 0;
			return true;
		}
		
		return false;
	}

	@Override
	public void setRaising(boolean isRaising)
	{
		raising = isRaising;
	}

	@SuppressWarnings("Duplicates")
	@Override
	public void move(int newXL, int newYL)
	{
		super.setXL(newXL);
		super.setYL(newYL);
		int xPx = toPx(newXL);
		int yPx = toPx(newYL);

		List<Circle> circles = new ArrayList<>();
		List<LineSegment> ls = new ArrayList<>();
		// Top And Bottom Circles
		double j = QUARTER_L_IN_PX;
		
		while (j >= 0)
		{
			circles.add(new Circle(xPx + QUARTER_L_IN_PX, yPx + QUARTER_L_IN_PX, j));
			circles.add(new Circle(xPx + QUARTER_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, j));
			j--;
		}

//        //Top Circle
//        circles.add(new Circle(xPx + QUARTER_L_IN_PX, yPx + QUARTER_L_IN_PX, QUARTER_L_IN_PX));
//        //Bottom Circle
//        circles.add(new Circle(xPx + QUARTER_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, QUARTER_L_IN_PX));

		// Horizontal Top Line
		ls.add(new LineSegment(xPx, yPx + QUARTER_L_IN_PX, xPx + HALF_L_IN_PX, yPx + QUARTER_L_IN_PX));
		// Horizontal Bottom Line
		ls.add(new LineSegment(xPx, yPx + TWO_L_MINUS_QUARTER_IN_PX, xPx + HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX));

		// Line in the middle from top to bottom
		ls.add(new LineSegment(xPx + QUARTER_L_IN_PX, yPx, xPx + QUARTER_L_IN_PX, yPx + TWO_L_IN_PX));

		// 0 radius circles for horizontal lines
		circles.add(new Circle(xPx, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx + HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(xPx + HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));

		// 0 radius circle for vertical line through whole flipper
		circles.add(new Circle(xPx + QUARTER_L_IN_PX, yPx, 0));
		circles.add(new Circle(xPx + QUARTER_L_IN_PX, yPx + TWO_L_IN_PX, 0));

		// All lines from left to right of the flipper
		int x = xPx;
		
		while (x <= xPx + HALF_L_IN_PX)
		{
			ls.add(new LineSegment(x, yPx + QUARTER_L_IN_PX, x, yPx + TWO_L_MINUS_QUARTER_IN_PX));
			x++;
		}
//        //Left Line
//        ls.add(new LineSegment(xPx, yPx + QUARTER_L_IN_PX, xPx, yPx + TWO_L_MINUS_QUARTER_IN_PX));
//        //Right Line
//        ls.add(new LineSegment(xPx + HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, xPx + HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX));

		// 0 radius circles for every line
		circles.add(new Circle(xPx, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(xPx + HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx + HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));

		super.setLineSegments(ls);
		super.setCircles(circles);

		int tempRotations = super.getNumberOfRotations();

		for (int i = 0; i < tempRotations; i++)
			rotate();

		super.setNumberOfRotations(tempRotations);
	}
}