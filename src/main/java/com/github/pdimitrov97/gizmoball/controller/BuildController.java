package com.github.pdimitrov97.gizmoball.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.MouseInputListener;

import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.util.Constants.MODE;
import com.github.pdimitrov97.gizmoball.view.MainView;

public class BuildController implements ActionListener, ChangeListener, MouseListener
{
	private MainView view;
	private Model model;
	private BoardMouseListener boardListener;
	private MouseInputListener mouseListener;

	public BuildController(MainView view, Model model, BoardMouseListener boardListener)
	{
		this.view = view;
		this.model = model;
		this.boardListener = boardListener;
	}

	public void actionPerformed(ActionEvent e)
	{
		switch (e.getActionCommand())
		{
			case "open game":
			{
				loadGame();
				break;
			}
			case "save game":
			{
				saveGame();
				break;
			}
			case "save as game":
			{
				saveAsGame();
				break;
			}
			case "run mode":
			{
				view.setMode(MODE.RUN_MODE);
				break;
			}
			case "exit":
			{
				System.exit(0);
				break;
			}
			case "circle selected":
			{
				view.setStatusbarMessage("Select a square on the board to add a Circle.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Circle"));
				break;
			}
			case "square selected":
			{
				view.setStatusbarMessage("Select a square on the board to add a Square.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Square"));
				break;
			}
			case "triangle selected":
			{
				view.setStatusbarMessage("Select a square on the board to add a Triangle.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Triangle"));
				break;
			}
			case "left flipper selected":
			{
				view.setStatusbarMessage("Select a square on the board to add a Left Flipper.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Left Flipper"));
				break;
			}
			case "right flipper selected":
			{
				view.setStatusbarMessage("Select a square on the board to add a Right Flipper.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Right Flipper"));
				break;
			}
			case "delete gizmo":
			{
				view.setStatusbarMessage("Select a position on the board to delete the Object on that position.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Delete"));
				break;
			}
			case "move gizmo":
			{
				view.setStatusbarMessage("Press the left mouse button while pointing on Object and release it after pointing at your new desired position.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Move"));
				break;
			}
			case "rotate gizmo":
			{
				view.setStatusbarMessage("Select a position on the board to rotate the Object on that position.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Rotate"));
				break;
			}
			case "add absorber":
			{
				view.setStatusbarMessage("Press the left mouse button on your desired position and drag and drop it to draw an Absorber.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Absorber"));
				break;
			}
			case "add ball":
			{
				view.setStatusbarMessage("Select a position on the board to add a Ball.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Ball"));
				break;
			}
			case "clear board":
			{
				model.clearModel();
				break;
			}
			case "show gizmo connections":
			{
				view.setStatusbarMessage("Select a Gizmo on the board to view its Gizmo connections.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Show Gizmo Connections"));
				break;
			}
			case "connect gizmo":
			{
				view.setStatusbarMessage("Select first Gizmo and then second Gizmo to connect. Select first again to choose another first.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Connect Gizmo"));
				break;
			}
			case "disconnect gizmo":
			{
				view.setStatusbarMessage("Select first Gizmo and then second Gizmo to disconnect. Select first again to choose another first.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Disconnect Gizmo"));
				break;
			}
			case "show key connections":
			{
				view.setStatusbarMessage("Press a key to view its Gizmo connections.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Show Key Connections"));
				break;
			}
			case "connect key":
			{
				view.setStatusbarMessage("Press a key and then select a Gizmo to connect it to this key.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Connect Key"));
				break;
			}
			case "disconnect key":
			{
				view.setStatusbarMessage("Press a key and then select a Gizmo to disconnect it from this key.");
				boardListener.setMouseListener(new GizmoActionsMouseListener(model, "Disconnect Key"));
				break;
			}
		}
	}

	private void loadGame()
	{
		JFileChooser fileChooser = new JFileChooser();

		if (fileChooser.showOpenDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION)
		{
			try
			{
				Model newModel = new Model(fileChooser.getSelectedFile());
				view.setModel(newModel);
			}
			catch (IOException e)
			{
				createWarningMessage("Cannot load gizmoball.", "Invalid file");
				return;
			}
		}
	}

	private void saveGame()
	{
		File loadedFile = model.getLoadedFile();

		if (loadedFile == null)
			saveAsGame();
		else
		{
			try
			{
				model.saveModel(loadedFile);
			}
			catch (IOException e)
			{
				createWarningMessage("Cannot save gizmoball.", "Invalid file");
				return;
			}
		}
	}

	private void saveAsGame()
	{
		JFileChooser fileChooser = new JFileChooser()
		{
			// If file exists, ask the user whether they want to replace it
			@Override
			public void approveSelection()
			{
				File file = getSelectedFile();

				if (file.exists() && getDialogType() == SAVE_DIALOG)
				{
					int result = JOptionPane.showConfirmDialog(this, "This file already exists. Do you want to overwrite it?", "Existing file", JOptionPane.YES_NO_CANCEL_OPTION);

					if (result == JOptionPane.NO_OPTION || result == JOptionPane.CLOSED_OPTION)
						return;
					else if (result == JOptionPane.CANCEL_OPTION)
					{
						cancelSelection();
						return;
					}
					else if (result == JOptionPane.YES_OPTION)
					{
						super.approveSelection();
						return;
					}
					else
					{
						createWarningMessage("Cannot save gizmoball.", "Invalid file");
						return;
					}
				}

				super.approveSelection();
			}
		};

		if (fileChooser.showSaveDialog(view.getFrame()) == JFileChooser.APPROVE_OPTION)
		{
			File file = fileChooser.getSelectedFile();

			// Create or overwrite file
			try
			{
				if (file.exists())
					file.delete();

				file.createNewFile();
			}
			catch (IOException e1)
			{
				createWarningMessage("Cannot save gizmoball.", "Invalid file");
				return;
			}

			try
			{
				model.saveModel(file);
			}
			catch (IOException e)
			{
				createWarningMessage("Cannot save gizmoball.", "Invalid file");
				return;
			}
		}
	}

	public void mouseClicked(MouseEvent e)
	{

	}

	public void mousePressed(MouseEvent e)
	{
		if (mouseListener != null)
			mouseListener.mousePressed(e);
	}

	public void mouseReleased(MouseEvent e)
	{
		if (mouseListener != null)
			mouseListener.mouseReleased(e);
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}

	public void stateChanged(ChangeEvent e)
	{
		JSlider slider = (JSlider) e.getSource();

		switch (slider.getName())
		{
			case "BallVelocityX":
			{
				model.setBallVX(slider.getValue());
				break;
			}
			case "BallVelocityY":
			{
				model.setBallVY(slider.getValue());
				break;
			}
			case "Gravity":
			{
				model.setGravity(slider.getValue());
				break;
			}
			case "FrictionMU":
			{
				model.setFrictionMU((double) slider.getValue() / 1000);
				break;
			}
			case "FrictionMU2":
			{
				model.setFrictionMU2((double) slider.getValue() / 1000);
				break;
			}
		}
	}

	private void createWarningMessage(String message, String title)
	{
		JOptionPane.showMessageDialog(null, "Error: " + message, title, JOptionPane.WARNING_MESSAGE);
	}
}