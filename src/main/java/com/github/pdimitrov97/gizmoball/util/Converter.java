package com.github.pdimitrov97.gizmoball.util;

public class Converter
{
	public static int toPx(int l)
	{
		return l * 30;
	}

	public static double toPx(double l)
	{
		return l * 30;
	}

	public static int toL(double px)
	{
		return (int) (px / 30);
	}
}