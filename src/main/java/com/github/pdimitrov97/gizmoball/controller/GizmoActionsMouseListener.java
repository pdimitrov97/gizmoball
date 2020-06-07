package com.github.pdimitrov97.gizmoball.controller;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;

import java.awt.event.MouseEvent;

import javax.swing.event.MouseInputListener;

import com.github.pdimitrov97.gizmoball.model.Model;

public class GizmoActionsMouseListener implements MouseInputListener
{
	private Model model;
	private String type;
	private int startX;
	private int startY;
	private int endX;
	private int endY;

	public GizmoActionsMouseListener(Model model, String type)
	{
		this.model = model;
		this.type = type;

		model.setSelectedBlock(-1, -1);
		model.resetKeySelected();

		model.acceptKeySelect(false);

		if (type.equals("Show Key Connections") || type.equals("Connect Key") || type.equals("Disconnect Key"))
			model.acceptKeySelect(true);
	}

	public void mouseClicked(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		startX = e.getX();
		startY = e.getY();
	}

	public void mouseReleased(MouseEvent e)
	{
		int x = e.getX();
		int y = e.getY();

		switch (type)
		{
			case "Circle":
			{
				if (!model.isOccupied(x, y, null, null))
					model.addCircle(x, y);

				break;
			}
			case "Square":
			{
				if (!model.isOccupied(x, y, null, null))
					model.addSquare(x, y);

				break;
			}
			case "Triangle":
			{
				if (!model.isOccupied(x, y, null, null))
					model.addTriangle(x, y);

				break;
			}
			case "Left Flipper":
			{
				if (x < 0 || x >= 19 * ONE_L_IN_PX || y < 0 || y >= 19 * ONE_L_IN_PX)
					return;

				if (!model.isOccupied(x, y, null, null) && !model.isOccupied(x, (y + (int) ONE_L_IN_PX), null, null) && !model.isOccupied((x + (int) ONE_L_IN_PX), y, null, null) && !model.isOccupied((x + (int) ONE_L_IN_PX), (y + (int) ONE_L_IN_PX), null, null))
					model.addLeftFlipper(x, y);

				break;
			}
			case "Right Flipper":
			{
				if (x <= ONE_L_IN_PX || x > 20 * ONE_L_IN_PX || y < 0 || y >= 19 * ONE_L_IN_PX)
					return;

				if (!model.isOccupied(x, y, null, null) && !model.isOccupied(x, (y + (int) ONE_L_IN_PX), null, null) && !model.isOccupied((x - (int) ONE_L_IN_PX), y, null, null) && !model.isOccupied((x - (int) ONE_L_IN_PX), (y + (int) ONE_L_IN_PX), null, null))
					model.addRightFlipper(x, y);

				break;
			}
			case "Delete":
			{
				// x = (int) (x / ONE_L_IN_PX);
				// y = (int) (y / ONE_L_IN_PX);

				model.deleteGizmo(x, y);
				break;
			}
			case "Move":
			{
				// endX = (int) (x / ONE_L_IN_PX);
				// endY = (int) (y / ONE_L_IN_PX);
				// startX = (int) (startX / ONE_L_IN_PX);
				// startY = (int) (startY / ONE_L_IN_PX);

				// model.moveGizmo(startX, startY, endX, endY);
				model.moveGizmo(startX, startY, x, y);

				break;
			}
			case "Rotate":
			{
				x = (int) (x / ONE_L_IN_PX);
				y = (int) (y / ONE_L_IN_PX);

				model.rotateGizmo(x, y);
				break;
			}
			case "Absorber":
			{
				endX = (int) (x / ONE_L_IN_PX);
				endY = (int) (y / ONE_L_IN_PX);
				startX = (int) (startX / ONE_L_IN_PX);
				startY = (int) (startY / ONE_L_IN_PX);

				if (startX > endX)
				{
					startX = startX + endX;
					endX = startX - endX;
					startX = startX - endX;
				}

				if (startY > endY)
				{
					startY = startY + endY;
					endY = startY - endY;
					startY = startY - endY;
				}

				endX += 1;
				endY += 1;

				for (int r = startX; r < endX; r++)
				{
					for (int c = startY; c < endY; c++)
					{
						if (model.isOccupied((int) (r * ONE_L_IN_PX), (int) (c * ONE_L_IN_PX), null, null))
							return;
					}
				}

				model.addAbsorber(startX, startY, endX, endY);
				break;
			}
			case "Ball":
			{
				model.addBall(x, y);
				break;
			}
			case "Show Gizmo Connections":
			{

				break;
			}
			case "Connect Gizmo":
			{
				if (!model.isFirstSelected())
					model.setFirstSelectedGizmo(model.getSelectedGizmo());
				else
					model.setSecondSelectedGizmo(model.getSelectedGizmo(), true);

				break;
			}
			case "Disconnect Gizmo":
			{
				if (!model.isFirstSelected())
					model.setFirstSelectedGizmo(model.getSelectedGizmo());
				else
					model.setSecondSelectedGizmo(model.getSelectedGizmo(), false);

				break;
			}
			case "Show Key Connections":
			{

				break;
			}
			case "Connect Key":
			{
				if (model.isKeySelected())
					model.setSelectedKeyGizmo(model.getSelectedGizmo(), true);

				break;
			}
			case "Disconnect Key":
			{
				if (model.isKeySelected())
					model.setSelectedKeyGizmo(model.getSelectedGizmo(), false);

				break;
			}
		}
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void mouseDragged(MouseEvent e)
	{

	}

	public void mouseMoved(MouseEvent e)
	{

	}
}