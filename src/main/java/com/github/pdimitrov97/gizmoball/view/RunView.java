package com.github.pdimitrov97.gizmoball.view;

import javax.swing.JButton;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JSeparator;

import com.github.pdimitrov97.gizmoball.controller.RunController;

public class RunView implements IView
{
	private RunController controller;

	public RunView(RunController controller)
	{
		this.controller = controller;
	}

	public JMenuBar createMenuBar()
	{
		// Initialize
		JMenuBar menuBar = new JMenuBar();

		// Add File menu to the menu bar
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);

		// Add Load button to the File menu
		JMenuItem menuItemOpen = new JMenuItem("Open");
		menuItemOpen.addActionListener(controller);
		menuItemOpen.setActionCommand("open game");
		menuFile.add(menuItemOpen);

		// Add Save button to the File menu
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuItemSave.addActionListener(controller);
		menuItemSave.setActionCommand("save game");
		menuFile.add(menuItemSave);

		// Add Save As button to the File menu
		JMenuItem menuItemSaveAs = new JMenuItem("Save As");
		menuItemSaveAs.addActionListener(controller);
		menuItemSaveAs.setActionCommand("save as game");
		menuFile.add(menuItemSaveAs);

		// Add a separator to the File menu
		menuFile.add(new JSeparator());

		// Add Build Mode button to the File menu
		JMenuItem menuItemBuildMode = new JMenuItem("Build Mode");
		menuItemBuildMode.addActionListener(controller);
		menuItemBuildMode.setActionCommand("build mode");
		menuFile.add(menuItemBuildMode);

		// Add a separator to the File menu
		menuFile.add(new JSeparator());

		// Add Exit button to the File menu
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(controller);
		menuItemExit.setActionCommand("exit");
		menuFile.add(menuItemExit);

		return menuBar;
	}

	public JPanel createButtons()
	{
		// Initialize
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(null);

		// Add a button for starting the game
		JButton buttonStartGame = new JButton("Start Game");
		buttonStartGame.setBounds(10, 10, 140, 20);
		buttonStartGame.addActionListener(controller);
		buttonStartGame.setActionCommand("start game");
		buttonStartGame.setFocusable(false);
		panelButtons.add(buttonStartGame);

		// Add a button for stopping the game
		JButton buttonStopGame = new JButton("Stop Game");
		buttonStopGame.setBounds(163, 10, 140, 20);
		buttonStopGame.addActionListener(controller);
		buttonStopGame.setActionCommand("stop game");
		buttonStopGame.setFocusable(false);
		panelButtons.add(buttonStopGame);

		// Add a button for ticking the game
		JButton buttonTick = new JButton("Tick");
		buttonTick.setBounds(316, 10, 140, 20);
		buttonTick.addActionListener(controller);
		buttonTick.setActionCommand("tick");
		buttonTick.setFocusable(false);
		panelButtons.add(buttonTick);

		// Add a button for restarting the game
		JButton buttonRestartGame = new JButton("Restart Game");
		buttonRestartGame.setBounds(469, 10, 140, 20);
		buttonRestartGame.addActionListener(controller);
		buttonRestartGame.setActionCommand("restart game");
		buttonRestartGame.setFocusable(false);
		panelButtons.add(buttonRestartGame);

		return panelButtons;
	}
}