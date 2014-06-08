package app;

import java.io.IOException;
import java.util.Date;
import comport.ListenSerial;
import comport.ReadSerial;
import processing.core.*;
import processing.video.*;

public class WindowFilter extends PApplet implements ListenSerial{

	// Size of each cell in the grid
	int cellSize = 20;
	
	// Number of columns and rows in our system
	int cols, rows;
	// Variable for capture device
	Capture video;
	ReadSerial serial = new ReadSerial(this, "COM3", 9600);

	public static void main(String[] args) {
		PApplet.main(new String[] { "app.WindowFilter" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}
	
	public boolean sketchFullScreen() {
		  return true;
	}

	public void setup() {
		//1280 x 720
		//59:name=Logitech HD Webcam C615,size=1920x1080,fps=30
		this.size(1920, 1080);
		
		frameRate(30);
		cols = width / cellSize;
		rows = height / cellSize;
		//colorMode(RGB, 255, 255, 255, 100);
		//rectMode(CENTER);
		
		
		String[] cameras = Capture.list();

		if (cameras.length == 0) {
			System.out.println("There are no cameras available for capture.");
		} else {
			System.out.println("Available cameras:");
			for (int i = 0; i < cameras.length; i++) {
				System.out.println(""+i+":"+cameras[i]);
			}
		}

		// This the default video input, see the GettingStartedCapture
		// example if it creates an error
		video = new Capture(this, width, height, 30);

		// Start capturing the images from the camera
		video.start();

		background(0);
	}

	public void draw() {
		
		if (nobUpdated == true) {
			cellSize = cellSizeChanged;
			nobUpdated = false;
		}
		
		channel = channel%2;
		if(channel == 0){
			if(cellSize < 10){
				if (video.available()) {
					video.read();
					video.loadPixels();
					if(cellSize < 10){
						PImage img = video;
						pushMatrix();
						scale(-1, 1);
						image(img, -img.width, 0);
						popMatrix();
						return;
					}
				}
			}else{
				rotateRect();
			}
		}else if (channel == 1){
			if(cellSize < 5){
				if (video.available()) {
					video.read();
					video.loadPixels();
					if(cellSize < 10){
						PImage img = video;
						pushMatrix();
						scale(-1, 1);
						image(img, -img.width, 0);
						popMatrix();
						return;
					}
				}
			}else{
				monoRect();
			}
		}
	}
	
	public void monoRect() {
		synchronized (this) {
			if (video.available()) {
				video.read();
				video.loadPixels();

				cols = width / cellSize;
				rows = height / cellSize;

				background(0, 0, 255);

				// Begin loop for columns
				for (int i = 0; i < cols; i++) {
					// Begin loop for rows
					for (int j = 0; j < rows; j++) {

						// Where are we, pixel-wise?
						int x = i * cellSize;
						int y = j * cellSize;
						int loc = (video.width - x - 1) + y * video.width;

						// Each rect is colored white with a size determined by
						// brightness
						int c = video.pixels[loc];
						float sz = (float) ((brightness(c) / 255.0) * cellSize);
						fill(255);
						noStroke();
						rect(x + cellSize / 2, y + cellSize / 2, sz, sz);
					}
				}
			}
		}
	}

	public void rotateRect() {
		synchronized (this) {
			if (video.available()) {
				video.read();
				video.loadPixels();

				cols = width / cellSize;
				rows = height / cellSize;

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
						// Using translate in order for rotation to work
						// properly
						pushMatrix();
						translate(x + cellSize / 2, y + cellSize / 2);
						// Rotation formula based on brightness

						rotate((float) (2 * PI * brightness(c) / 255.0));

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
	}
	
	public  void abstractRepresentation(PApplet applet, PImage video, int cellSize, int feature) {

		video.loadPixels();
		int cols = applet.width / cellSize;
		int rows = applet.height / cellSize;
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

				float r = applet.red(video.pixels[loc]);
				float g = applet.green(video.pixels[loc]);
				float b = applet.blue(video.pixels[loc]);
				// Make a new color with an alpha component
				// color c = color(r, g, b, 75);

				int c = applet.color(r, g, b, 75);

				// Code for drawing a single rect
				// Using translate in order for rotation to work properly
				applet.pushMatrix();
				applet.translate(x + cellSize / 2, y + cellSize / 2);
				// Rotation formula based on brightness

				applet.rotate((float) (2 * PI * applet.brightness(c) / 255.0 * feature));

				applet.rectMode(CENTER);
				applet.fill(c);
				applet.noStroke();
				// Rects are larger than the cell for some overlap
				applet.rect(0, 0, cellSize + 6, cellSize + 6);
				applet.popMatrix();
			}
		}
		
	}
	
	public void keyPressed(){
		
		incomingdata((""+this.key).getBytes());
		
	}
	
	boolean nobUpdated = false;
	long keyPressindex = 0;
	long prevTime = 0;
	int cellSizeChanged = 0;
	int channel = 0;

	@Override
	public void incomingdata(byte[] data) {

		long timestemp = System.currentTimeMillis() - prevTime;
		System.out.println(keyPressindex +" : "+new String(data) + ":"+timestemp);
		String command = (new String(data)).trim();
		
		if(command.startsWith("Vol.UP") || command.startsWith("=")){
			if(cellSize >= 10){
				cellSizeChanged = cellSize + 10;
			}else if(cellSize < 10){
				cellSizeChanged = cellSize + 2;
			}
			nobUpdated = true;
		}else if(command.startsWith("Vol.DOWN") || command.startsWith("-")){
			if(cellSize >= 20){
				cellSizeChanged = cellSize - 10;
			}else if(cellSize <= 10 && cellSize > 2 ){
				cellSizeChanged = cellSize - 2;
			}
			nobUpdated = true;
		}else if(command.startsWith("Chn.UP") || command.startsWith("[")){
			channel++;
			if(channel%2 == 0){
				//cellSizeChanged = 20;
				nobUpdated = true;
			}else{
				//cellSizeChanged = 50;
				nobUpdated = true;				
			}
			background(0);
		}else if(command.startsWith("Chn.DOWN") || command.startsWith("]")){
			if(channel > 0 ){
				channel--;
				if(channel%2 == 0){
					//cellSizeChanged = 20;
					nobUpdated = true;
				}else{
					//cellSizeChanged = 50;
					nobUpdated = true;				
				}
			}
			background(0);
		}else if(command.startsWith("0")){
			if(channel%2 == 0){
				cellSizeChanged = 20;
			}else if(channel%2 == 1){
				cellSizeChanged = 50;
			}
			nobUpdated = true;
		}
			
		System.out.println("[Block size]="+cellSize);
	}
}
