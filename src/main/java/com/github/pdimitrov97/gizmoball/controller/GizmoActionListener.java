package com.github.pdimitrov97.gizmoball.controller;

import java.awt.event.ActionEvent;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.KeyStroke;

import com.github.pdimitrov97.gizmoball.model.Gizmo;

public class GizmoActionListener extends AbstractAction
{
	private List<Gizmo> gizmos;
	private static List<KeyStroke> pressedKeys;
	private KeyStroke keyStroke;

	public GizmoActionListener(List<Gizmo> gizmos, KeyStroke keyStroke, List<KeyStroke> pressedKeys)
	{
		this.gizmos = gizmos;
		this.keyStroke = keyStroke;
		this.pressedKeys = pressedKeys;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (keyStroke.isOnKeyRelease())
		{
			KeyStroke removeKey = KeyStroke.getKeyStroke(keyStroke.getKeyCode(), 0, false);
			pressedKeys.remove(removeKey);
		}

		if (!pressedKeys.contains(keyStroke))
		{
			if (gizmos != null)
			{
				for (Gizmo g : gizmos)
					g.act();
			}

			if (!keyStroke.isOnKeyRelease())
				pressedKeys.add(keyStroke);
		}
	}
}