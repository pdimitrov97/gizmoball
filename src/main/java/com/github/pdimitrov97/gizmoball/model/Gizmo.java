package com.github.pdimitrov97.gizmoball.model;

import static com.github.pdimitrov97.gizmoball.util.Converter.toPx;

import java.awt.Color;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

public abstract class Gizmo implements Serializable
{
	private String name;
	private int xL;
	private int yL;
	private int x2L;
	private int y2L;
	private int x;
	private int y;
	private int x2;
	private int y2;
	private Color currentColour;
	private Color restingColor;
	private Color actingColor;
	private int numberOfRotations = 0;
	private List<LineSegment> lineSegments;
	private List<Circle> circles;
	private List<Gizmo> connectedGizmos;

	public Gizmo(String name, int xL, int yL, int x2L, int y2L, Color restingColor, Color actingColor)
	{
		this.name = name;
		this.xL = xL;
		this.yL = yL;
		this.x2L = x2L;
		this.y2L = y2L;
		this.x = toPx(this.xL);
		this.y = toPx(this.yL);
		this.x2 = toPx(this.x2L);
		this.y2 = toPx(this.y2L);
		this.restingColor = restingColor;
		this.actingColor = actingColor;
		this.currentColour = this.restingColor;

		lineSegments = new ArrayList<>();
		circles = new ArrayList<>();
		connectedGizmos = new ArrayList<>();
	}

	public int getXL()
	{
		return this.xL;
	}

	public void setXL(int newXL)
	{
		this.xL = newXL;
		this.x = toPx(this.xL);
	}

	public int getYL()
	{
		return this.yL;
	}

	public void setYL(int newYL)
	{
		this.yL = newYL;
		this.y = toPx(this.yL);
	}

	public int getX2L()
	{
		return this.x2L;
	}

	public void setX2L(int newX2L)
	{
		this.x2L = newX2L;
		this.x2 = toPx(this.x2L);
	}

	public int getY2L()
	{
		return this.y2L;
	}

	public void setY2L(int newY2L)
	{
		this.y2L = newY2L;
		this.y2 = toPx(this.y2L);
	}

	public int getX()
	{
		return this.x;
	}

	public int getY()
	{
		return this.y;
	}

	public int getX2()
	{
		return this.x2;
	}

	public int getY2()
	{
		return this.y2;
	}

	public List<LineSegment> getLineSegments()
	{
		return lineSegments;
	}

	public void setLineSegments(List<LineSegment> newLineSegments)
	{
		this.lineSegments = newLineSegments;
	}

	public List<Circle> getCircles()
	{
		return circles;
	}

	public void setCircles(List<Circle> newCircles)
	{
		this.circles = newCircles;
	}

	public List<Gizmo> getConnectedGizmos()
	{
		return connectedGizmos;
	}

	public Color getColour()
	{
		return currentColour;
	}

	public int getNumberOfRotations()
	{
		return numberOfRotations;
	}

	public void setNumberOfRotations(int newNumberOfRotations)
	{
		this.numberOfRotations = newNumberOfRotations;
	}

	abstract public void move(int newXL, int newYL);

	abstract public void rotate();

	public void act()
	{
		currentColour = actingColor;

		Timer timer = new Timer();
		TimerTask task = new TimerTask()
		{
			@Override
			public void run()
			{
				currentColour = restingColor;
			}
		};

		timer.schedule(task, 1000);
	}

	public boolean connectGizmo(Gizmo gizmo)
	{
		if (!connectedGizmos.contains(gizmo))
			return connectedGizmos.add(gizmo);

		return false;
	}

	public boolean disconnectGizmo(Gizmo gizmo)
	{
		return connectedGizmos.remove(gizmo);
	}
}