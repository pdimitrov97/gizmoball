package com.github.pdimitrov97.gizmoball.view;

import static com.github.pdimitrov97.gizmoball.util.Constants.BOARD_HEIGHT;
import static com.github.pdimitrov97.gizmoball.util.Constants.BOARD_WIDTH;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.util.Observable;
import java.util.Observer;

import com.github.pdimitrov97.gizmoball.model.Model;

public class RunBoard extends Board implements Observer
{
    private Model model;

    public RunBoard(Model model)
    {
        super(model);

        setLayout(null);
        setSize(BOARD_WIDTH, BOARD_HEIGHT);
        setBackground(Color.black);

        this.model = model;
        this.model.addObserver(this);
    }

    public void paintComponent(Graphics g)
    {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        super.drawGizmos(g2);
    }

    public void update(Observable arg0, Object arg1)
    {
        repaint();
    }
}