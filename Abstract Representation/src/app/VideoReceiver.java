package app;

import java.awt.image.*;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

import javax.imageio.*;

import processing.core.*;

public class VideoReceiver extends PApplet {

	// Port we are receiving.
	int port = 9100;
	DatagramSocket ds;
	// A byte array to read into (max size of 65536, could be smaller)
	byte[] buffer = new byte[65536];

	PImage video;
	
	private int screenWidth = 1920, screenHight = 1080;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "app.VideoReceiver" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}
	
	public boolean sketchFullScreen() {
		  return false;
	}

	public void setup() {
//		size(800, 600);
		size(864, 480);
		try {
			ds = new DatagramSocket(port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
//		video = createImage(800, 600, RGB);
		video = createImage(864, 480, RGB);
	}

	public void draw() {
		// checkForImage() is blocking, stay tuned for threaded example!
		checkForImage();

//		// Draw the image
//		background(0);
		imageMode(CENTER);
		image(video, width / 2, height / 2, 864, 480);
//		abstractRepresentation(20, 1);//, screenWidth, screenHight);
		
	}

	void checkForImage() {
		DatagramPacket p = new DatagramPacket(buffer, buffer.length);
		try {
			ds.receive(p);
		} catch (IOException e) {
			e.printStackTrace();
		}
		byte[] data = p.getData();

		println("Received datagram with " + data.length + " bytes.");

		// Read incoming data into a ByteArrayInputStream
		ByteArrayInputStream bais = new ByteArrayInputStream(data);

		// We need to unpack JPG and put it in the PImage video
		video.loadPixels();
		try {
			// Make a BufferedImage out of the incoming bytes
			BufferedImage img = ImageIO.read(bais);
			// Put the pixels into the video PImage
			img.getRGB(0, 0, video.width, video.height, video.pixels, 0,
					video.width);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		// Update the PImage pixels
		video.updatePixels();
	}
	
	public void abstractRepresentation(int cellSize, int feature) {
		video.loadPixels();
		int cols = width / cellSize;
		int rows = height / cellSize;
//		background(0);
//		imageMode(CENTER);
		// Begin loop for columns
		for (int i = 0; i < cols; i++) {
			// Begin loop for rows
			for (int j = 0; j < rows; j++) {

				// Where are we, pixel-wise?
				int x = i * cellSize;
				int y = j * cellSize;
				int loc = (video.width - x - 1) + y * video.width; 

				float r = red(video.pixels[loc]);
				float g = green(video.pixels[loc]);
				float b = blue(video.pixels[loc]);
				// Make a new color with an alpha component
				// color c = color(r, g, b, 75);

				int c = color(r, g, b, 75);

				// Code for drawing a single rect
				// Using translate in order for rotation to work properly
				pushMatrix();
				translate(x + cellSize / 2, y + cellSize / 2);
				// Rotation formula based on brightness

				rotate((float) (2 * PI * brightness(c) / 255.0 * feature));

				rectMode(CENTER);
				fill(c);
				noStroke();
				// Rects are larger than the cell for some overlap
				rect(0, 0, cellSize + 6, cellSize + 6);
				popMatrix();
			}
		}
	}
	
}
