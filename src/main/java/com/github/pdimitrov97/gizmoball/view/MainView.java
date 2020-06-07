package com.github.pdimitrov97.gizmoball.view;

import static com.github.pdimitrov97.gizmoball.util.Constants.TITLE;
import static com.github.pdimitrov97.gizmoball.util.Constants.WINDOW_BUILD_VIEW_HEIGHT;
import static com.github.pdimitrov97.gizmoball.util.Constants.WINDOW_BUILD_VIEW_WIDTH;
import static com.github.pdimitrov97.gizmoball.util.Constants.WINDOW_RUN_VIEW_HEIGHT;
import static com.github.pdimitrov97.gizmoball.util.Constants.WINDOW_RUN_VIEW_WIDTH;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.InputMap;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;
import javax.swing.border.BevelBorder;

import com.github.pdimitrov97.gizmoball.controller.BoardKeyListener;
import com.github.pdimitrov97.gizmoball.controller.BoardMouseListener;
import com.github.pdimitrov97.gizmoball.controller.BuildController;
import com.github.pdimitrov97.gizmoball.controller.GizmoActionListener;
import com.github.pdimitrov97.gizmoball.controller.RunController;
import com.github.pdimitrov97.gizmoball.model.Gizmo;
import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.util.Constants.MODE;

public class MainView
{
	private JFrame frame;
	private MODE modeSelected = MODE.RUN_MODE;
	private JMenuBar menubar = new JMenuBar();
	private JPanel board = new JPanel();
	private JPanel buttons = new JPanel();
	private JPanel statusbar = new JPanel();
	private JLabel statusbarLabel;
	private IView view;
	private Model model;
	private Model modelCopy;

	public MainView()
	{
		// Set up the main frame
		frame = new JFrame(TITLE);
		frame.setSize(0, 0);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setResizable(false);
		frame.setVisible(true);

		model = new Model();
		modelCopy = model.copy();
	}

	/**
	 * This method creates a Run GUI with the current Model.
	 */
	private void createRunGUI()
	{
		clearWindow();

		// Create the controller and view
		RunController runController = new RunController(this, this.model);
		view = new RunView(runController);

		// Get and add the MenuBar to the window
		menubar = view.createMenuBar();
		frame.setJMenuBar(menubar);

		// Create and add the playing board to the window
		board = new RunBoard(this.model);
		board.setLocation(10, 10);
		frame.getContentPane().add(board);

		// Get and add the control buttons to the window
		buttons = view.createButtons();
		buttons.setBounds(0, 610, 625, 40);
		frame.getContentPane().add(buttons);

		// Create and add the status bar to the window
		createStatusBar(WINDOW_RUN_VIEW_WIDTH, WINDOW_RUN_VIEW_HEIGHT);
		setStatusbarMessage("Welcome to Gizmoball! Press Start to play the game.");
		frame.getContentPane().add(statusbar);

		updateWindow(WINDOW_RUN_VIEW_WIDTH, WINDOW_RUN_VIEW_HEIGHT);
	}

	/**
	 * This method creates a Build GUI with the current Model.
	 */
	private void createBuildGUI()
	{
		clearWindow();

		// Create and add the playing board to the window
		board = new BuildBoard(this.model);
		board.setLocation(10, 10);
		frame.getContentPane().add(board);

		// Create and set listeners of the board
		BoardMouseListener boardListener = new BoardMouseListener((BuildBoard) this.board, this.model);
		BoardKeyListener boardKeyListener = new BoardKeyListener(this.model);
		board.addMouseListener(boardListener);
		board.addKeyListener(boardKeyListener);

		// Allow focus to the board and frame
		board.setFocusable(true);
		frame.setFocusable(true);
		frame.addFocusListener(new FocusAdapter()
		{
			public void focusGained(FocusEvent aE)
			{
				board.requestFocusInWindow();
			}
		});

		// Create the controller and view
		BuildController buildController = new BuildController(this, this.model, boardListener);
		view = new BuildView(this.model, buildController);

		// Get and add the MenuBar to the window
		menubar = view.createMenuBar();
		frame.setJMenuBar(menubar);

		// Get and add the control buttons to the window
		buttons = view.createButtons();
		buttons.setBounds(615, 0, 300, 610);
		frame.getContentPane().add(buttons);

		// Create and add the status bar to the window
		createStatusBar(WINDOW_BUILD_VIEW_WIDTH, WINDOW_BUILD_VIEW_HEIGHT);
		setStatusbarMessage("Welcome to Gizmoball Build Mode. Please select an action...");
		frame.getContentPane().add(statusbar);

		updateWindow(WINDOW_BUILD_VIEW_WIDTH, WINDOW_BUILD_VIEW_HEIGHT);
	}

	/**
	 * This method adds a label for providing instructions to the user at the bottom
	 * of the screen.
	 *
	 * @param viewWidth  the width of the view
	 * @param viewHeight the height of the view
	 */
	private void createStatusBar(int viewWidth, int viewHeight)
	{
		statusbar = new JPanel();
		statusbar.setLayout(null);
		statusbar.setBounds(0, (viewHeight - 80), (viewWidth - 5), 30);
		statusbar.setBorder(new BevelBorder(BevelBorder.LOWERED));
		statusbarLabel = new JLabel("", SwingConstants.CENTER);
		statusbarLabel.setFont(new Font("Serif", Font.BOLD, 18));
		statusbarLabel.setBounds(0, 0, (viewWidth - 5), 25);
		statusbar.add(statusbarLabel);
	}

	/**
	 * This method sets the status message at the bottom to the message in the
	 * parameter.
	 *
	 * @param message the message to be displayed
	 */
	public void setStatusbarMessage(String message)
	{
		if (statusbarLabel != null)
			statusbarLabel.setText(message);
	}

	/**
	 * This method clears the window so that it is prepared for new components.
	 */
	private void clearWindow()
	{
		// Clear frame
		frame.setJMenuBar(null);
		frame.remove(board);
		frame.remove(buttons);
		frame.remove(statusbar);
	}

	/**
	 * This methods refreshes the window so that it can be redrawn after
	 * modifications to the View.
	 *
	 * @param viewWidth  the width of the view
	 * @param viewHeight the height of the view
	 */
	private void updateWindow(int viewWidth, int viewHeight)
	{
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		frame.setLocation(((dimension.width / 2) - (viewWidth / 2)), ((dimension.height / 2) - (viewHeight / 2)));
		frame.setSize(viewWidth, viewHeight);
		frame.revalidate();
		frame.setVisible(true);
		frame.repaint();
	}

	/**
	 * This method sets the modeSelected variable of the View.
	 *
	 * @param mode the mode to be set
	 */
	public void setMode(MODE mode)
	{
		if (mode == MODE.RUN_MODE)
		{
			modeSelected = MODE.RUN_MODE;
			this.modelCopy = model.copy();
			switchMode();
		}
		else
		{
			modeSelected = MODE.BUILD_MODE;
			switchMode();
		}
	}

	/**
	 * This method switches the MODE of the view to the one set in the modeSelected
	 * variable.
	 */
	public void switchMode()
	{
		if (modeSelected == MODE.RUN_MODE)
		{
			createRunGUI();
			setKeyBindings(false);
		}
		else
		{
			createBuildGUI();
			setKeyBindings(true);

			// Force focus to board
			board.requestFocusInWindow();

			// Reset model variables for building
			model.setSelectedBlock(-1, -1);
			model.resetKeySelected();
			model.acceptKeySelect(false);
		}
	}

	/**
	 * This method sets the Model of the view to be displayed and also makes a copy
	 * of it in case we need to restart the case later.
	 *
	 * @param model the Model to be set
	 */
	public void setModel(Model model)
	{
		this.model = model;
		this.modelCopy = model.copy();
		switchMode();
	}

	/**
	 * This method restarts the game by setting the Model to the one that was
	 * previously copied.
	 */
	public void restartGame()
	{
		this.model = modelCopy.copy();
		switchMode();
	}

	/**
	 * Gets all KeyStrokes from the Model, adds them to the InputMap of the frame
	 * and adds an GizmoActionListener in the ActionMap to the gizmos.
	 *
	 * @param reset When true, InputMap and ActionMap are reset and nothing is
	 *              added. When false, they are reset and then set to Model's
	 *              KeyStrokes and GizmoActionListeners.
	 */
	private void setKeyBindings(boolean reset)
	{
		JPanel content = (JPanel) frame.getContentPane();
		InputMap inputMap = content.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
		inputMap.clear();
		content.getActionMap().clear();

		// If resetting KeyBindings, we don't add anything
		if (!reset)
		{
			Map<KeyStroke, List<Gizmo>> keyConnections = model.getKeyConnections();
			List<KeyStroke> pressedKeys = new ArrayList<>();
			int counter = 0;

			// Add all pressed key strokes first, by creating opposite released key strokes
			// that are used just for resetting
			for (Map.Entry<KeyStroke, List<Gizmo>> entry : keyConnections.entrySet())
			{
				if (!entry.getKey().isOnKeyRelease())
				{
					KeyStroke releaseKey = KeyStroke.getKeyStroke(entry.getKey().getKeyCode(), 0, true);

					inputMap.put(releaseKey, ("ActionMapKey" + counter));
					content.getActionMap().put(("ActionMapKey" + counter), new GizmoActionListener(null, releaseKey, pressedKeys));
					counter++;

					inputMap.put(entry.getKey(), ("ActionMapKey" + counter));
					content.getActionMap().put(("ActionMapKey" + counter), new GizmoActionListener(entry.getValue(), entry.getKey(), pressedKeys));
					counter++;
				}
			}

			// Add all released key strokes. Some may override previously created released
			// key strokes for resetting
			for (Map.Entry<KeyStroke, List<Gizmo>> entry : keyConnections.entrySet())
			{
				if (entry.getKey().isOnKeyRelease())
				{
					inputMap.put(entry.getKey(), ("ActionMapKey" + counter));
					content.getActionMap().put(("ActionMapKey" + counter), new GizmoActionListener(entry.getValue(), entry.getKey(), pressedKeys));
					counter++;
				}
			}
		}
	}

	public JFrame getFrame()
	{
		return frame;
	}
}
