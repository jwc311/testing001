package app;

import gab.opencv.*;
import processing.core.PApplet;
import processing.video.*;
import java.awt.*;

public class FaceTracking extends PApplet {

	OpenCV opencv = null;
	Capture cam = null;
	int widthP = 864, heightP = 480;
//	int cameraWidth = 864, cameraHeight = 480;

	public static void main(String[] args) {
		PApplet.main(new String[] { "app.FaceTracking" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}

	public boolean sketchFullScreen() {
		return false;
	} 

	public void setup() {

		size(widthP, heightP);
		cam = new Capture(this, widthP / 2, heightP / 2);
		opencv = new OpenCV(this, widthP / 2, heightP / 2);
		opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);

		cam.start();

	}

	public void draw() {
		// if (cam.available() == true) {
		scale(2);
//		scale(-2, 2);
		opencv.loadImage(cam);
		
//		scale(-1, 1);
//		image(cam, -cam.width, 0);
		image(cam, 0, 0);

		noFill();
		stroke(255, 255, 255);
		strokeWeight(1);
		Rectangle[] faces = opencv.detect();

		for (int i = 0; i < faces.length; i++) {
			System.out.println(faces[i].x + ", " + faces[i].y);
			rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
		}
		// }
	}

	public void captureEvent(Capture c) {
		c.read();
	}
}
