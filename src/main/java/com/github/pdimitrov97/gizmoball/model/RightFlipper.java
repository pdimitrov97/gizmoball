package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_AND_A_HALF_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.QUARTER_L_IN_PX;
import static com.github.pdimitrov97.gizmoball.util.Constants.SEVEN_QUARTERS_L_IN_PX;
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

public class RightFlipper extends Flipper implements Serializable
{
	public RightFlipper(int xL, int yL)
	{
		super("some name", xL, yL, (int) (xL + TWO_L_IN_PX), (int) (yL + TWO_L_IN_PX), new Color(119, 119, 221), new Color(119, 119, 221));
	}

	public RightFlipper(String name, int xL, int yL, Color restingColor, Color actingColor)
	{
		super(name, xL, yL, (int) (xL + TWO_L_IN_PX), (int) (yL + TWO_L_IN_PX), restingColor, actingColor);
	}

	public void moveByAngle(Angle angle)
	{
		if (currentAngle.plus(angle).compareTo(Angle.DEG_180) <= 0 && currentAngle.plus(angle).compareTo(Angle.DEG_90) >= 0)
		{
			raiseOrLower(angle);
			
			if (raising)
				velocity = Angle.DEG_90.radians() * 12;
			else
				velocity = -Angle.DEG_90.radians() * 12;
		}
		else
		{
			if (raising)
				raiseOrLower(Angle.DEG_180.minus(currentAngle));
			else
				raiseOrLower(Angle.DEG_90.minus(currentAngle));
			
			velocity = 0;
		}
	}

	public boolean isRaised()
	{
		if (currentAngle.equals(Angle.DEG_180))
		{
			raising = false;
			velocity = 0;
			return true;
		}

		return false;
	}

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
		if (isRaising)
			velocity = Angle.DEG_90.radians();
		else
			velocity = -Angle.DEG_90.radians();
		
		raising = isRaising;
	}

	public void move(int newXL, int newYL)
	{
		super.setXL(newXL);
		super.setYL(newYL);
		int xPx = toPx(newXL);
		int yPx = toPx(newYL);

		List<LineSegment> ls = new ArrayList<>();
//

		List<Circle> circles = new ArrayList<>();
//

		// Top And Bottom Circles
		double j = QUARTER_L_IN_PX;
		
		while (j >= 0)
		{
			circles.add(new Circle(xPx + SEVEN_QUARTERS_L_IN_PX, yPx + QUARTER_L_IN_PX, j));
			circles.add(new Circle(xPx + SEVEN_QUARTERS_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, j));
			j--;
		}

//        //Top Circle
//        circles.add(new Circle(xPx + SEVEN_QUARTERS_L_IN_PX, yPx + QUARTER_L_IN_PX, QUARTER_L_IN_PX));
//        //Bottom Circle
//        circles.add(new Circle(xPx + SEVEN_QUARTERS_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, QUARTER_L_IN_PX));

		// Horizontal Top Line
		ls.add(new LineSegment(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, xPx + TWO_L_IN_PX, yPx + QUARTER_L_IN_PX));
		// Horizontal Bottom Line
		ls.add(new LineSegment(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, xPx + TWO_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX));

		// Line in the middle from top to bottom
		ls.add(new LineSegment(xPx + SEVEN_QUARTERS_L_IN_PX, yPx, xPx + SEVEN_QUARTERS_L_IN_PX, yPx + TWO_L_IN_PX));

		// 0 radius circles for left/right line
		circles.add(new Circle(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(xPx + TWO_L_IN_PX, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx + TWO_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));

		// 0 radius circles for horizontal lines
		circles.add(new Circle(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx + TWO_L_IN_PX, yPx + QUARTER_L_IN_PX, 0));
		circles.add(new Circle(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));
		circles.add(new Circle(xPx + TWO_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX, 0));

		// 0 radius circle for vertical line through whole flipper
		circles.add(new Circle(xPx + SEVEN_QUARTERS_L_IN_PX, yPx, 0));
		circles.add(new Circle(xPx + SEVEN_QUARTERS_L_IN_PX, yPx + TWO_L_IN_PX, 0));

		// All lines from left to right of the flipper
		double x = xPx + ONE_AND_A_HALF_L_IN_PX;
		while (x <= xPx + TWO_L_IN_PX)
		{
			ls.add(new LineSegment(x, yPx + QUARTER_L_IN_PX, x, yPx + TWO_L_MINUS_QUARTER_IN_PX));
			x++;
		}

		super.setLineSegments(ls);
		super.setCircles(circles);

//        //Left Line
//        ls.add(new LineSegment(xPx + ONE_AND_A_HALF_L_IN_PX, yPx + QUARTER_L_IN_PX, xPx + ONE_AND_A_HALF_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX));
//        //Right Line
//        ls.add(new LineSegment(xPx + TWO_L_IN_PX, yPx + QUARTER_L_IN_PX, xPx + TWO_L_IN_PX, yPx + TWO_L_MINUS_QUARTER_IN_PX));

		int tempRotations = super.getNumberOfRotations();

		for (int i = 0; i < tempRotations; i++)
			rotate();

		super.setNumberOfRotations(tempRotations);
	}
}