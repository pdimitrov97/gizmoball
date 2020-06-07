package com.github.pdimitrov97.gizmoball.controller;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.Timer;

import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.util.Constants.MODE;
import com.github.pdimitrov97.gizmoball.view.MainView;

public class RunController implements ActionListener
{
	private Model model;
	private Timer timer;
	private MainView view;

	public RunController(MainView view, Model model)
	{
		this.model = model;
		this.view = view;
		timer = new Timer(1000 / 120, this);
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == timer)
		{
			model.tick();
			return;
		}

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
			case "build mode":
			{
				// view.restartGame();
				timer.stop();
				view.setMode(MODE.BUILD_MODE);
				break;
			}
			case "exit":
			{
				System.exit(0);
				break;
			}
			case "start game":
			{
				timer.start();
				break;
			}
			case "stop game":
			{
				timer.stop();
				break;
			}
			case "tick":
			{
				model.tick();
				break;
			}
			case "restart game":
			{
				timer.stop();
				view.restartGame();
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
						createWarningMessage("Cannot save gizmoball!", "Invalid file");
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

	private void createWarningMessage(String message, String title)
	{
		JOptionPane.showMessageDialog(null, "Error: " + message, title, JOptionPane.WARNING_MESSAGE);
	}
}