package visaulEffect;

import processing.core.*;

public class Effect01 extends EffectAbstractClass{

	public void visualEffect(PApplet applet, PImage image, int cellSize, int feature, boolean fullVideo) {

		if(fullVideo){
			applet.image(image, 0, 0, 1920, 1080);
			return;
		}

		PGraphics imageBuff = applet.createGraphics(1920, 1080);
		
		imageBuff.pushMatrix();
		imageBuff.scale(-1, 1);
		imageBuff.image(image, -image.width - 1060 , 0, 1920, 1080);
		imageBuff.popMatrix();
		
		image = imageBuff.get();
		
		image.loadPixels();
		int cols = applet.width / cellSize;
		int rows = applet.height / cellSize;

		for (int i = 0; i < cols; i++) {
			// Begin loop for rows
			for (int j = 0; j < rows; j++) {

				// Where are we, pixel-wise?
				int x = i * cellSize;
				int y = j * cellSize;
				int loc = (image.width - x - 1) + y * image.width; 

				float r = applet.red(image.pixels[loc]);
				float g = applet.green(image.pixels[loc]);
				float b = applet.blue(image.pixels[loc]);
				
				// Make a new color with an alpha component
				int c = applet.color(r, g, b, 75);

				// Code for drawing a single rect
				// Using translate in order for rotation to work properly
				applet.pushMatrix();
				applet.translate(x + cellSize / 2, y + cellSize / 2);
				
				// Rotation formula based on brightness
				applet.rotate((float) (2 * applet.PI * applet.brightness(c) / 255.0 * feature));

				applet.rectMode(applet.CENTER);
				applet.fill(c);
				applet.noStroke();
				// Rects are larger than the cell for some overlap
				applet.rect(0, 0, cellSize + 6, cellSize + 6);
				applet.popMatrix();
			}
		}
	}
}
