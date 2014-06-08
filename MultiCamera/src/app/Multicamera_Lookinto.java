package app;

import gab.opencv.OpenCV;

import java.awt.Rectangle;
import java.io.IOException;

import processing.core.*;
import processing.data.*;
import processing.event.*;
import processing.opengl.*;
import processing.video.Capture;

import java.util.HashMap;
import java.util.ArrayList;
import java.io.File;
import java.io.BufferedReader;
import java.io.PrintWriter;
import java.io.InputStream;
import java.io.OutputStream;

public class Multicamera_Lookinto extends PApplet {

	/**
	 * Load and Display
	 * 
	 * Images can be loaded and displayed to the screen at their actual size or
	 * any other size.
	 */
	
	OpenCV opencv = null;
	Capture cam = null;
	int camWidth = 320, camHeight = 240;
	

	// The next line is needed if running in JavaScript Mode with Processing.js
	/* @pjs preload="moonwalk.jpg"; */

	PImage img; // Declare variable "a" of type PImage

	int num_y = 8;
	int num_x = 10;
	int size_x = 1440, size_y = 900;
//	int size_x = 1024, size_y = 680;
//	int size_x = displayWidth, size_y = displayHeight;
	
	float grid_unit_x = (float) size_x / num_x;
	float grid_unit_y = (float) size_y / num_y;
	int grid_x;
	int grid_y;
	PImage[][] sequence = new PImage[num_y][num_x];

	public static void main(String args[]) {
		PApplet.main(new String[] { "app.Multicamera_Lookinto" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}
	
	public boolean sketchFullScreen() {
		  return true;
//		  return false;
	}

	public void setup() {
		size(size_x, size_y);
		// The image file must be in the data folder of the current sketch
		// to load successfully

		PImage curr;

		String start = "focus_object/Nxt1_001_0";
		String middle = "_X1_00";
		String end = ".jpg";
		for (int i = 0; i < num_y; i++) {
			String filename = start + (i + 2) + middle;
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
		
		cam = new Capture(this, this.camWidth, this.camHeight);
		opencv = new OpenCV(this, this.camWidth, this.camHeight);
		opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
		
		cam.start();
	}

	public void draw() {

		opencv.loadImage(cam);
		Rectangle[] faces = opencv.detect();
		
		grid_x = (int) (mouseX / grid_unit_x);
		grid_y = (int) (mouseY / grid_unit_y);
		
//		if(faces.length > 0){
//			grid_x = (int) (faces[0].x / grid_unit_x);
//			grid_y = (int) (faces[0].y / grid_unit_y);
//		}

		grid_x = num_x - grid_x - 1;
		// grid_y = num_y-grid_y-1;

		// println(grid_x + ", " + grid_y);

		img = sequence[grid_y][grid_x];
		// Displays the image at its actual size at point (0,0)
		// image(img, 0, 0, width, height);
		image(img, 0, 0, width, height);
		
		pushStyle();
		tint(0, 153, 204);
//		tint(100);
		image(cam, 0, 0);
		popStyle();
		
		noFill();
		stroke(255, 255, 255);
		strokeWeight(1);
		
		for (int i = 0; i < faces.length; i++) {
			System.out.println(faces[i].x + ", " + faces[i].y);
			rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
		}
	}
	
	public void captureEvent(Capture c) {
		c.read();
	}
}
