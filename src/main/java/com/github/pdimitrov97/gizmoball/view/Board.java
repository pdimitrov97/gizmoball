package com.github.pdimitrov97.gizmoball.view;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;

import java.awt.Graphics2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
import java.util.List;

import javax.swing.JPanel;

import com.github.pdimitrov97.gizmoball.model.Absorber;
import com.github.pdimitrov97.gizmoball.model.Ball;
import com.github.pdimitrov97.gizmoball.model.Flipper;
import com.github.pdimitrov97.gizmoball.model.Gizmo;
import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.model.Triangle;
import com.github.pdimitrov97.gizmoball.physics.Circle;
import com.github.pdimitrov97.gizmoball.physics.LineSegment;

public abstract class Board extends JPanel
{
	private Model model;

	public Board(Model model)
	{
		this.model = model;
	}

	public void drawSquare(Graphics2D g2, Gizmo square)
	{
		Rectangle2D rect = new Rectangle2D.Double(square.getX(), square.getY(), ONE_L_IN_PX, ONE_L_IN_PX);
		g2.setPaint(square.getColour());
		// g2.draw(rect);
		g2.fill(rect);
	}

	public void drawCircle(Graphics2D g2, Gizmo circle)
	{
		Ellipse2D ellipse = new Ellipse2D.Double(circle.getX(), circle.getY(), ONE_L_IN_PX, ONE_L_IN_PX);
		g2.setPaint(circle.getColour());
		g2.draw(ellipse);
		g2.fill(ellipse);
	}

	public void drawTriangle(Graphics2D g2, Gizmo triangle)
	{
		Path2D path = new Path2D.Double();
		List<LineSegment> lineSegments = triangle.getLineSegments();
		path.moveTo(((int) lineSegments.get(0).p1().x()), (lineSegments.get(0).p1().y()));
		path.lineTo(((int) lineSegments.get(1).p1().x()), (lineSegments.get(1).p1().y()));
		path.lineTo(((int) lineSegments.get(2).p1().x()), (lineSegments.get(2).p1().y()));
		path.closePath();
		g2.setPaint(triangle.getColour());
		// g2.draw(path);
		g2.fill(path);
	}

	public void drawFlipper(Graphics2D g2, Flipper flipper)
	{
		g2.setColor(flipper.getColour());

		double x;
		double y;
		double width;

		for (Circle circle : flipper.getCircles())
		{
			x = circle.getCenter().x() - circle.getRadius();
			y = circle.getCenter().y() - circle.getRadius();
			width = circle.getRadius() * 2;

			Ellipse2D ellipse = new Ellipse2D.Double(x, y, width, width);
			g2.fill(ellipse);
			g2.draw(ellipse);
		}

		Path2D path = new Path2D.Double();
		LineSegment line1 = flipper.getLineSegments().get(0);
		LineSegment line2 = flipper.getLineSegments().get(1);
		path.moveTo(line1.p1().x(), line1.p1().y());
		path.lineTo(line1.p2().x(), line1.p2().y());
		path.lineTo(line2.p2().x(), line2.p2().y());
		path.lineTo(line2.p1().x(), line2.p1().y());
		path.closePath();
		g2.draw(path);
		g2.fill(path);
	}

	public void drawAbsorber(Graphics2D g2, Gizmo absorber)
	{
		g2.setPaint(absorber.getColour());
		g2.fill(new Rectangle2D.Double(absorber.getX(), absorber.getY(), (absorber.getX2() - absorber.getX()), (absorber.getY2() - absorber.getY())));
		g2.draw(new Rectangle2D.Double(absorber.getX(), absorber.getY(), (absorber.getX2() - absorber.getX()), (absorber.getY2() - absorber.getY())));
	}

	public void drawBall(Graphics2D g2, Ball ball)
	{
		g2.setPaint(ball.getColour());
		g2.fill(new Ellipse2D.Double((ball.getExactX() - ball.getRadius()), (ball.getExactY() - ball.getRadius()), (ball.getRadius() * 2), (ball.getRadius() * 2)));
		// g2.draw(new Ellipse2D.Double((ball.getExactX() - ball.getRadius()),
		// (ball.getExactY() - ball.getRadius()), (ball.getRadius() * 2),
		// (ball.getRadius() * 2)));
	}

	public void drawGizmos(Graphics2D g2)
	{
		// Draw Squares
		for (Gizmo square : this.model.getSquares())
			drawSquare(g2, square);

		// Draw Circles
		for (Gizmo circle : this.model.getCircles())
			drawCircle(g2, circle);

		// Draw Triangles
		for (Triangle triangle : this.model.getTriangles())
			drawTriangle(g2, triangle);

		// Draw Left Flippers
		for (Flipper leftFlipper : this.model.getLeftFlippers())
			drawFlipper(g2, leftFlipper);

		// Draw Right Flippers
		for (Flipper rightFlipper : this.model.getRightFlippers())
			drawFlipper(g2, rightFlipper);

		// Draw Absorbers
		for (Absorber absorber : this.model.getAbsorbers())
			drawAbsorber(g2, absorber);

		// Draw Balls
		for (Ball ball : this.model.getBalls())
			drawBall(g2, ball);
	}
}