package com.github.pdimitrov97.gizmoball.view;

import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_X_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_X_MAJOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_X_MAX;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_X_MIN;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_X_MINOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_Y_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_Y_MAJOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_Y_MAX;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_Y_MIN;
import static com.github.pdimitrov97.gizmoball.util.Constants.BALL_VELOCITY_Y_MINOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU2_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU2_MAJOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU2_MAX;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU2_MIN;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU2_MINOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU_MAJOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU_MAX;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU_MIN;
import static com.github.pdimitrov97.gizmoball.util.Constants.FRICTION_MU_MINOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRAVITY_INIT;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRAVITY_MAJOR_SPACING;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRAVITY_MAX;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRAVITY_MIN;
import static com.github.pdimitrov97.gizmoball.util.Constants.GRAVITY_MINOR_SPACING;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Dictionary;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JSeparator;
import javax.swing.JSlider;

import com.github.pdimitrov97.gizmoball.controller.BuildController;
import com.github.pdimitrov97.gizmoball.model.Model;

public class BuildView implements IView
{
	private Model model;
	private BuildController controller;

	public BuildView(Model model, BuildController controller)
	{
		this.model = model;
		this.controller = controller;
	}

	public JMenuBar createMenuBar()
	{
		// Initialize
		JMenuBar menuBar = new JMenuBar();
		menuBar.setFocusable(false);

		// Add File menu to the menu bar
		JMenu menuFile = new JMenu("File");
		menuFile.setFocusable(false);
		menuBar.add(menuFile);

		// Add Load button to the File menu
		JMenuItem menuItemOpen = new JMenuItem("Open");
		menuItemOpen.addActionListener(controller);
		menuItemOpen.setActionCommand("open game");
		menuItemOpen.setFocusable(false);
		menuFile.add(menuItemOpen);

		// Add Save button to the File menu
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuItemSave.addActionListener(controller);
		menuItemSave.setActionCommand("save game");
		menuItemSave.setFocusable(false);
		menuFile.add(menuItemSave);

		// Add Save As button to the File menu
		JMenuItem menuItemSaveAs = new JMenuItem("Save As");
		menuItemSaveAs.addActionListener(controller);
		menuItemSaveAs.setActionCommand("save as game");
		menuItemSaveAs.setFocusable(false);
		menuFile.add(menuItemSaveAs);

		// Add a separator to the File menu
		menuFile.add(new JSeparator());

		// Add Build Mode button to the File menu
		JMenuItem menuItemRunMode = new JMenuItem("Run Mode");
		menuItemRunMode.addActionListener(controller);
		menuItemRunMode.setActionCommand("run mode");
		menuItemRunMode.setFocusable(false);
		menuFile.add(menuItemRunMode);

		// Add a separator to the File menu
		menuFile.add(new JSeparator());

		// Add Exit button to the File menu
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuItemExit.addActionListener(controller);
		menuItemExit.setActionCommand("exit");
		menuItemExit.setFocusable(false);
		menuFile.add(menuItemExit);

		// Add Connections menu to the menu bar
		JMenu menuConnections = new JMenu("Connections");
		menuConnections.setFocusable(false);
		menuBar.add(menuConnections);

		// Add Show Gizmo Connections button to the Connections menu
		JMenuItem menuItemShowGizmoConnections = new JMenuItem("Show Gizmo Connections");
		menuItemShowGizmoConnections.addActionListener(controller);
		menuItemShowGizmoConnections.setActionCommand("show gizmo connections");
		menuItemShowGizmoConnections.setFocusable(false);
		menuConnections.add(menuItemShowGizmoConnections);

		// Add Connect Gizmo button to the Connections menu
		JMenuItem menuItemConnectGizmo = new JMenuItem("Connect Gizmo");
		menuItemConnectGizmo.addActionListener(controller);
		menuItemConnectGizmo.setActionCommand("connect gizmo");
		menuItemConnectGizmo.setFocusable(false);
		menuConnections.add(menuItemConnectGizmo);

		// Add Disconnect Gizmo button to the Connections menu
		JMenuItem menuItemDisconnectGizmo = new JMenuItem("Disconnect Gizmo");
		menuItemDisconnectGizmo.addActionListener(controller);
		menuItemDisconnectGizmo.setActionCommand("disconnect gizmo");
		menuItemDisconnectGizmo.setFocusable(false);
		menuConnections.add(menuItemDisconnectGizmo);

		// Add Show Key Connections button to the Connections menu
		JMenuItem menuItemShowKeyConnections = new JMenuItem("Show Key Connections");
		menuItemShowKeyConnections.addActionListener(controller);
		menuItemShowKeyConnections.setActionCommand("show key connections");
		menuItemShowKeyConnections.setFocusable(false);
		menuConnections.add(menuItemShowKeyConnections);

		// Add Connect Key button to the Connections menu
		JMenuItem menuItemConnectKey = new JMenuItem("Connect Key");
		menuItemConnectKey.addActionListener(controller);
		menuItemConnectKey.setActionCommand("connect key");
		menuItemConnectKey.setFocusable(false);
		menuConnections.add(menuItemConnectKey);

		// Add Disconnect Key button to the Connections menu
		JMenuItem menuItemDisconnectKey = new JMenuItem("Disconnect Key");
		menuItemDisconnectKey.addActionListener(controller);
		menuItemDisconnectKey.setActionCommand("disconnect key");
		menuItemDisconnectKey.setFocusable(false);
		menuConnections.add(menuItemDisconnectKey);

		return menuBar;
	}

	public JPanel createButtons()
	{
		// Initialize
		JPanel panelButtons = new JPanel();
		panelButtons.setLayout(null);

		// Add popup menu for Adding a Gizmo
		JPopupMenu popupMenuAddGizmo = new JPopupMenu("Add Gizmo");

		// Add Circle radio button to the Add Gizmo popup menu
		JMenuItem menuItemCircle = new JMenuItem("Circle");
		menuItemCircle.addActionListener(controller);
		menuItemCircle.setActionCommand("circle selected");
		menuItemCircle.setFocusable(false);
		popupMenuAddGizmo.add(menuItemCircle);

		// Add Square radio button to the Add Gizmo popup menu
		JMenuItem menuItemSquare = new JMenuItem("Square");
		menuItemSquare.addActionListener(controller);
		menuItemSquare.setActionCommand("square selected");
		menuItemSquare.setFocusable(false);
		popupMenuAddGizmo.add(menuItemSquare);

		// Add Triangle radio button to the Add Gizmo popup menu
		JMenuItem menuItemTriangle = new JMenuItem("Triangle");
		menuItemTriangle.addActionListener(controller);
		menuItemTriangle.setActionCommand("triangle selected");
		menuItemTriangle.setFocusable(false);
		popupMenuAddGizmo.add(menuItemTriangle);

		// Add Left Flipper radio button to the Add Gizmo popup menu
		JMenuItem menuItemLeftFlipper = new JMenuItem("Left Flipper");
		menuItemLeftFlipper.addActionListener(controller);
		menuItemLeftFlipper.setActionCommand("left flipper selected");
		menuItemLeftFlipper.setFocusable(false);
		popupMenuAddGizmo.add(menuItemLeftFlipper);

		// Add Right Flipper radio button to the Add Gizmo popup menu
		JMenuItem menuItemRightFlipper = new JMenuItem("Right Flipper");
		menuItemRightFlipper.addActionListener(controller);
		menuItemRightFlipper.setActionCommand("right flipper selected");
		menuItemRightFlipper.setFocusable(false);
		popupMenuAddGizmo.add(menuItemRightFlipper);

		// Add a button for Adding a Gizmo
		JButton buttonAddGizmo = new JButton("Add Gizmo");
		buttonAddGizmo.setBounds(10, 10, 135, 20);
		buttonAddGizmo.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				popupMenuAddGizmo.show(buttonAddGizmo, buttonAddGizmo.getWidth(), 0);
			}
		});
		buttonAddGizmo.setActionCommand("add gizmo");
		buttonAddGizmo.setFocusable(false);
		panelButtons.add(buttonAddGizmo);

		// Add a button for Deleting a Gizmo
		JButton buttonDeleteGizmo = new JButton("Delete");
		buttonDeleteGizmo.setBounds(155, 10, 135, 20);
		buttonDeleteGizmo.addActionListener(controller);
		buttonDeleteGizmo.setActionCommand("delete gizmo");
		buttonDeleteGizmo.setFocusable(false);
		panelButtons.add(buttonDeleteGizmo);

		// Add a button for Moving a Gizmo
		JButton buttonMoveGizmo = new JButton("Move");
		buttonMoveGizmo.setBounds(10, 40, 135, 20);
		buttonMoveGizmo.addActionListener(controller);
		buttonMoveGizmo.setActionCommand("move gizmo");
		buttonMoveGizmo.setFocusable(false);
		panelButtons.add(buttonMoveGizmo);

		// Add a button for Rotating a Gizmo
		JButton buttonRotateGizmo = new JButton("Rotate Gizmo");
		buttonRotateGizmo.setBounds(155, 40, 135, 20);
		buttonRotateGizmo.addActionListener(controller);
		buttonRotateGizmo.setActionCommand("rotate gizmo");
		buttonRotateGizmo.setFocusable(false);
		panelButtons.add(buttonRotateGizmo);

		// Add a button for Adding an Absorber
		JButton buttonAddAbsorber = new JButton("Add Absorber");
		buttonAddAbsorber.setBounds(10, 70, 280, 20);
		buttonAddAbsorber.addActionListener(controller);
		buttonAddAbsorber.setActionCommand("add absorber");
		buttonAddAbsorber.setFocusable(false);
		panelButtons.add(buttonAddAbsorber);

		// Add a button for Adding a Ball
		JButton buttonAddBall = new JButton("Add Ball");
		buttonAddBall.setBounds(10, 100, 280, 20);
		buttonAddBall.addActionListener(controller);
		buttonAddBall.setActionCommand("add ball");
		buttonAddBall.setFocusable(false);
		panelButtons.add(buttonAddBall);

		// Add label for Ball Velocity X slider
		JLabel labelBallVelocityX = new JLabel("Ball Velocity X:");
		labelBallVelocityX.setBounds(10, 130, 100, 20);
		labelBallVelocityX.setFocusable(false);
		panelButtons.add(labelBallVelocityX);

		// Add a slider for adjusting the Ball X Velocity
		JSlider sliderBallVelocityX = new JSlider(JSlider.HORIZONTAL, BALL_VELOCITY_X_MIN, BALL_VELOCITY_X_MAX, BALL_VELOCITY_X_INIT);
		sliderBallVelocityX.setName("BallVelocityX");
		sliderBallVelocityX.setBounds(10, 150, 290, 45);
		sliderBallVelocityX.setMajorTickSpacing(BALL_VELOCITY_X_MAJOR_SPACING);
		sliderBallVelocityX.setMinorTickSpacing(BALL_VELOCITY_X_MINOR_SPACING);
		sliderBallVelocityX.setPaintTicks(true);
		sliderBallVelocityX.setPaintLabels(true);
		sliderBallVelocityX.addChangeListener(controller);
		sliderBallVelocityX.setValue(model.getBallVX());
		sliderBallVelocityX.setFocusable(false);
		panelButtons.add(sliderBallVelocityX);

		// Add label for Ball Velocity Y slider
		JLabel labelBallVelocityY = new JLabel("Ball Velocity Y:");
		labelBallVelocityY.setBounds(10, 220, 100, 20);
		labelBallVelocityY.setFocusable(false);
		panelButtons.add(labelBallVelocityY);

		// Add a slider for adjusting the Ball Y Velocity
		JSlider sliderBallVelocityY = new JSlider(JSlider.HORIZONTAL, BALL_VELOCITY_Y_MIN, BALL_VELOCITY_Y_MAX, BALL_VELOCITY_Y_INIT);
		sliderBallVelocityY.setName("BallVelocityY");
		sliderBallVelocityY.setBounds(10, 240, 290, 45);
		sliderBallVelocityY.setMajorTickSpacing(BALL_VELOCITY_Y_MAJOR_SPACING);
		sliderBallVelocityY.setMinorTickSpacing(BALL_VELOCITY_Y_MINOR_SPACING);
		sliderBallVelocityY.setPaintTicks(true);
		sliderBallVelocityY.setPaintLabels(true);
		sliderBallVelocityY.addChangeListener(controller);
		sliderBallVelocityY.setValue(model.getBallVY());
		sliderBallVelocityY.setFocusable(false);
		panelButtons.add(sliderBallVelocityY);

		// Add label for Gravity slider
		JLabel labelGravity = new JLabel("Gravity:");
		labelGravity.setBounds(10, 310, 100, 20);
		labelGravity.setFocusable(false);
		panelButtons.add(labelGravity);

		// Add a slider for adjusting the Gravity
		JSlider sliderGravity = new JSlider(JSlider.HORIZONTAL, GRAVITY_MIN, GRAVITY_MAX, GRAVITY_INIT);
		sliderGravity.setName("Gravity");
		sliderGravity.setBounds(10, 330, 290, 45);
		sliderGravity.setMajorTickSpacing(GRAVITY_MAJOR_SPACING);
		sliderGravity.setMinorTickSpacing(GRAVITY_MINOR_SPACING);
		sliderGravity.setPaintTicks(true);
		sliderGravity.setPaintLabels(true);
		sliderGravity.addChangeListener(controller);
		sliderGravity.setValue(model.getGravity());
		sliderGravity.setFocusable(false);
		panelButtons.add(sliderGravity);

		Dictionary<Integer, JLabel> labelTable = new Hashtable<>();
		labelTable.put(100, new JLabel("0.1"));
		labelTable.put(50, new JLabel("0.05"));
		labelTable.put(0, new JLabel("0.0"));
		labelTable.put(-50, new JLabel("-0.05"));
		labelTable.put(-100, new JLabel("-0.1"));

		// Add label for Friction MU slider
		JLabel labelFrictionMU = new JLabel("Friction MU:");
		labelFrictionMU.setBounds(10, 400, 100, 20);
		labelFrictionMU.setFocusable(false);
		panelButtons.add(labelFrictionMU);

		// Add a slider for adjusting the Friction MU
		JSlider sliderFrictionMU = new JSlider(JSlider.HORIZONTAL, FRICTION_MU_MIN, FRICTION_MU_MAX, FRICTION_MU_INIT);
		sliderFrictionMU.setName("FrictionMU");
		sliderFrictionMU.setBounds(10, 420, 290, 45);
		sliderFrictionMU.setMajorTickSpacing(FRICTION_MU_MAJOR_SPACING);
		sliderFrictionMU.setMinorTickSpacing(FRICTION_MU_MINOR_SPACING);
		sliderFrictionMU.setPaintTicks(true);
		sliderFrictionMU.setPaintLabels(true);
		sliderFrictionMU.setLabelTable(labelTable);
		sliderFrictionMU.addChangeListener(controller);
		sliderFrictionMU.setValue((int) (model.getFrictionMU() * 1000));
		sliderFrictionMU.setFocusable(false);
		panelButtons.add(sliderFrictionMU);

		// Add label for Friction MU2 slider
		JLabel labelFrictionMU2 = new JLabel("Friction MU2:");
		labelFrictionMU2.setBounds(10, 485, 100, 20);
		labelFrictionMU2.setFocusable(false);
		panelButtons.add(labelFrictionMU2);

		// Add a slider for adjusting the Friction MU2
		JSlider sliderFrictionMU2 = new JSlider(JSlider.HORIZONTAL, FRICTION_MU2_MIN, FRICTION_MU2_MAX, FRICTION_MU2_INIT);
		sliderFrictionMU2.setName("FrictionMU2");
		sliderFrictionMU2.setBounds(10, 505, 290, 45);
		sliderFrictionMU2.setMajorTickSpacing(FRICTION_MU2_MAJOR_SPACING);
		sliderFrictionMU2.setMinorTickSpacing(FRICTION_MU2_MINOR_SPACING);
		sliderFrictionMU2.setPaintTicks(true);
		sliderFrictionMU2.setPaintLabels(true);
		sliderFrictionMU2.setLabelTable(labelTable);
		sliderFrictionMU2.addChangeListener(controller);
		sliderFrictionMU2.setValue((int) (model.getFrictionMU2() * 1000));
		sliderFrictionMU2.setFocusable(false);
		panelButtons.add(sliderFrictionMU2);

		// Add a button for Resetting the Physics
		JButton buttonResetPhysics = new JButton("Reset Physics to Default");
		buttonResetPhysics.setBounds(10, 560, 280, 20);
		buttonResetPhysics.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent ae)
			{
				sliderBallVelocityX.setValue(BALL_VELOCITY_X_INIT);
				sliderBallVelocityY.setValue(BALL_VELOCITY_Y_INIT);
				sliderGravity.setValue(GRAVITY_INIT);
				sliderFrictionMU.setValue(FRICTION_MU_INIT);
				sliderFrictionMU2.setValue(FRICTION_MU2_INIT);
			}
		});
		buttonResetPhysics.setFocusable(false);
		panelButtons.add(buttonResetPhysics);

		// Add a button for Clearing the Board
		JButton buttonClearButton = new JButton("Clear Board");
		buttonClearButton.setBounds(10, 590, 280, 20);
		buttonClearButton.addActionListener(controller);
		buttonClearButton.setActionCommand("clear board");
		buttonClearButton.setFocusable(false);
		panelButtons.add(buttonClearButton);

		return panelButtons;
	}
}