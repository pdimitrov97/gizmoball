package com.github.pdimitrov97.gizmoball.controller;

import static com.github.pdimitrov97.gizmoball.util.Constants.ONE_L_IN_PX;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.event.MouseInputListener;

import com.github.pdimitrov97.gizmoball.model.Model;
import com.github.pdimitrov97.gizmoball.view.BuildBoard;

public class BoardMouseListener implements MouseListener
{
	private BuildBoard board;
	private Model model;
	private MouseInputListener mouseListener;

	public BoardMouseListener(BuildBoard board, Model model)
	{
		this.board = board;
		this.model = model;
	}

	public void setMouseListener(MouseInputListener mouseListener)
	{
		this.mouseListener = mouseListener;
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
		int x = (int) (e.getX() / ONE_L_IN_PX);
		int y = (int) (e.getY() / ONE_L_IN_PX);

		model.setSelectedBlock(x, y);

		if (mouseListener != null)
			mouseListener.mouseReleased(e);
	}

	public void mouseEntered(MouseEvent e)
	{

	}

	public void mouseExited(MouseEvent e)
	{

	}
}