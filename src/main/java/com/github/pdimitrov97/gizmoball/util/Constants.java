package com.github.pdimitrov97.gizmoball.util;

public class Constants
{
	// Modes enumeration for the view
	public enum MODE
	{
		RUN_MODE,
		BUILD_MODE
	}

	// Frame constants
	public static final String TITLE = "Gizmoball";

	public static final int WINDOW_RUN_VIEW_WIDTH = 625;
	public static final int WINDOW_RUN_VIEW_HEIGHT = 730;

	public static final int WINDOW_BUILD_VIEW_WIDTH = 925;
	public static final int WINDOW_BUILD_VIEW_HEIGHT = 700;

	// Board constants
	public static final int BOARD_WIDTH = 600;
	public static final int BOARD_HEIGHT = 600;

	// Grid constants
	public static final int GRID_CELLS_PER_LINE = 20;

	public static final double ONE_L_IN_PX = BOARD_WIDTH / GRID_CELLS_PER_LINE;
	public static final double TWO_L_IN_PX = 2 * ONE_L_IN_PX;
	public static final double HALF_L_IN_PX = ONE_L_IN_PX / 2;
	public static final double ONE_AND_A_HALF_L_IN_PX = ONE_L_IN_PX + HALF_L_IN_PX;
	public static final double QUARTER_L_IN_PX = HALF_L_IN_PX / 2;
	public static final double THREE_QUARTERS_L_IN_PX = ONE_L_IN_PX / 4 * 3;
	public static final double TWO_L_MINUS_QUARTER_IN_PX = 2 * ONE_L_IN_PX - QUARTER_L_IN_PX;
	public static final double SEVEN_QUARTERS_L_IN_PX = THREE_QUARTERS_L_IN_PX + 4 * QUARTER_L_IN_PX;

	// Ball Velocity X slider constants
	public static final int BALL_VELOCITY_X_MIN = -200;
	public static final int BALL_VELOCITY_X_MAX = 200;
	public static final int BALL_VELOCITY_X_INIT = 50;
	public static final int BALL_VELOCITY_X_MAJOR_SPACING = 50;
	public static final int BALL_VELOCITY_X_MINOR_SPACING = 25;

	// Ball Velocity y slider constants
	public static final int BALL_VELOCITY_Y_MIN = -200;
	public static final int BALL_VELOCITY_Y_MAX = 200;
	public static final int BALL_VELOCITY_Y_INIT = 50;
	public static final int BALL_VELOCITY_Y_MAJOR_SPACING = 50;
	public static final int BALL_VELOCITY_Y_MINOR_SPACING = 25;

	// Gravity slider constants
	public static final int GRAVITY_MIN = -50;
	public static final int GRAVITY_MAX = 50;
	public static final int GRAVITY_INIT = 25;
	public static final int GRAVITY_MAJOR_SPACING = 10;
	public static final int GRAVITY_MINOR_SPACING = 5;

	// Friction MU slider constants
	public static final int FRICTION_MU_MIN = -100;
	public static final int FRICTION_MU_MAX = 100;
	public static final int FRICTION_MU_INIT = 25;
	public static final int FRICTION_MU_MAJOR_SPACING = 10;
	public static final int FRICTION_MU_MINOR_SPACING = 5;

	// Friction MU2 slider constants
	public static final int FRICTION_MU2_MIN = -100;
	public static final int FRICTION_MU2_MAX = 100;
	public static final int FRICTION_MU2_INIT = 25;
	public static final int FRICTION_MU2_MAJOR_SPACING = 10;
	public static final int FRICTION_MU2_MINOR_SPACING = 5;
}