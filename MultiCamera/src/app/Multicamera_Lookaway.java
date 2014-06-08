package app;

import java.io.IOException;

import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.IOException;

public class Multicamera_Lookaway extends PApplet {

	/**
	 * Load and Display
	 * 
	 * Images can be loaded and displayed to the screen at their actual size or
	 * any other size.
	 */

	// The next line is needed if running in JavaScript Mode with Processing.js
	/* @pjs preload="moonwalk.jpg"; */

	PImage img; // Declare variable "a" of type PImage

	int num_y = 8;
	int num_x = 10;
	int size_x = 1024;
	int size_y = 680;
	float grid_unit_x = (float) size_x / num_x;
	float grid_unit_y = (float) size_y / num_y;
	int grid_x;
	int grid_y;
	PImage[][] sequence = new PImage[num_y][num_x];

	public static void main(String args[]) {
		PApplet.main(new String[] { "app.Multicamera_Lookaway" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}
	
	public boolean sketchFullScreen() {
		  return true;
	}
	
	public void setup() {
		size(size_x, size_y);
		// The image file must be in the data folder of the current sketch
		// to load successfully

		PImage curr;

		String start = "focus_camera/Nxt2_001_0";
		String middle = "_X1_00";
		String end = ".jpg";
		for (int i = 0; i < num_y; i++) {
			String filename = start + (i + 1) + middle;
			for (int j = 0; j < num_x; j++) {
				// println(filename + (j+1) + end);
				String zero = "";
				if (j + 1 < 10)
					zero = "0";
				curr = loadImage(filename + zero + (j + 1) + end);
				// curr.resize(size_x,0);
				sequence[i][j] = curr;
			}
		}
	}

	public void draw() {

		grid_x = (int) (mouseX / grid_unit_x);
		grid_y = (int) (mouseY / grid_unit_y);

		grid_x = num_x - grid_x - 1;
		// grid_y = num_y-grid_y-1;

		// println(grid_x + ", " + grid_y);

		img = sequence[grid_y][grid_x];
		// Displays the image at its actual size at point (0,0)
		// image(img, 0, 0, width, height);
		image(img, 0, 0);
	}
}
