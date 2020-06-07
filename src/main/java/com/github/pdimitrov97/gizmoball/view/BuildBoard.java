package com.github.pdimitrov97.gizmoball.view;

import static com.github.pdimitrov97.gizmoball.util.Constants.BOARD_HEIGHT;
import static com.github.pdimitrov97.gizmoball.util.Constants.BOARD_WIDTH;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRID_CELLS_PER_LINE;
import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.Line2D;
import java.awt.geom.Rectangle2D;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

import com.github.pdimitrov97.gizmoball.model.Absorber;
import com.github.pdimitrov97.gizmoball.model.CircleGizmo;
import com.github.pdimitrov97.gizmoball.model.Flipper;
import com.github.pdimitrov97.gizmoball.model.Gizmo;
import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.model.Square;
import com.github.pdimitrov97.gizmoball.model.Triangle;

public class BuildBoard extends Board implements Observer
{
	private Model model;
	private int cellSizeX;
	private int cellSizeY;

	public BuildBoard(Model model)
	{
		super(model);

		setLayout(null);
		setSize(BOARD_WIDTH, BOARD_HEIGHT);
		setBackground(Color.BLACK);

		this.model = model;
		this.model.addObserver(this);
		this.cellSizeX = BOARD_WIDTH / GRID_CELLS_PER_LINE;
		this.cellSizeY = BOARD_HEIGHT / GRID_CELLS_PER_LINE;
	}

	private void drawGrid(Graphics2D g2)
	{
		// Draw vertical lines
		g2.setPaint(Color.GRAY);

		int gridX;

		for (int i = 0; i <= GRID_CELLS_PER_LINE; i++)
		{
			gridX = i * this.cellSizeX;
			g2.draw(new Line2D.Double(gridX, 0, gridX, BOARD_HEIGHT));
		}

		// Draw horizontal lines
		int gridY;

		for (int i = 0; i <= GRID_CELLS_PER_LINE; i++)
		{
			gridY = i * this.cellSizeY;
			g2.draw(new Line2D.Double(0, gridY, BOARD_WIDTH, gridY));
		}
	}

	private void drawSelected(Graphics2D g2, Gizmo gizmo, boolean connected)
	{
		if (gizmo != null)
		{
			if (connected)
				g2.setColor(new Color(255, 0, 0, 125));
			else
				g2.setColor(new Color(255, 255, 255, 125));

			if (gizmo instanceof CircleGizmo || gizmo instanceof Square || gizmo instanceof Triangle)
				g2.fill(new Rectangle2D.Double(gizmo.getX(), gizmo.getY(), ONE_L_IN_PX, ONE_L_IN_PX));
			else if (gizmo instanceof Flipper)
				g2.fill(new Rectangle2D.Double(gizmo.getX(), gizmo.getY(), (2 * ONE_L_IN_PX), (2 * ONE_L_IN_PX)));
			else if (gizmo instanceof Absorber)
				g2.fill(new Rectangle2D.Double(gizmo.getX(), gizmo.getY(), (((Absorber) gizmo).getX2() - gizmo.getX()), (((Absorber) gizmo).getY2() - gizmo.getY())));
		}
	}

	private void drawSelectedConnections(Graphics2D g2)
	{
		if (model.isAcceptingKeySelect())
		{
			List<Gizmo> gizmos = model.getKeyConnectedGizmos(model.getSelectedKey());

			if (gizmos != null)
			{
				for (Gizmo g : gizmos)
				{
					drawSelected(g2, g, true);
				}
			}
		}
		else
		{
			Gizmo gizmo = model.getSelectedGizmo();

			if (gizmo != null)
			{
				for (Gizmo g : gizmo.getConnectedGizmos())
				{
					if (!g.equals(gizmo) || g instanceof Absorber)
						drawSelected(g2, g, true);
				}
			}
		}
	}

	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;

		super.drawGizmos(g2);
		drawSelected(g2, model.getSelectedGizmo(), false);
		drawSelectedConnections(g2);
		drawGrid(g2);
	}

	public void update(Observable o, Object arg)
	{
		repaint();
	}
}