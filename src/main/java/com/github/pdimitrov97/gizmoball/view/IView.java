package com.github.pdimitrov97.gizmoball.view;

import javax.swing.JMenuBar;
import javax.swing.JPanel;

public interface IView
{
	JMenuBar createMenuBar();

	JPanel createButtons();
}