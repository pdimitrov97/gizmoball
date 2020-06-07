package com.github.pdimitrov97.gizmoball.controller;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import com.github.pdimitrov97.gizmoball.model.Model;

public class BoardKeyListener implements KeyListener
{
	private Model model;

	public BoardKeyListener(Model model)
	{
		this.model = model;
	}

	public void keyTyped(KeyEvent e)
	{

	}

	public void keyPressed(KeyEvent e)
	{

	}

	public void keyReleased(KeyEvent e)
	{
		model.setKeySelected(e.getKeyCode());
	}
}