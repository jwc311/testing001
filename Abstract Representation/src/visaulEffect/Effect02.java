package visaulEffect;

import processing.core.*;

public class Effect02 extends EffectAbstractClass{

	public void visualEffect(PApplet applet, PImage image, int cellSize, int feature, boolean fullVideo) {

		if(fullVideo){
			applet.image(image, 0, 0, 1920, 1080);
			return;
		}
		
		PGraphics imageBuff = applet.createGraphics(1920, 1080);
		
		imageBuff.pushMatrix();
		imageBuff.scale(-1, 1);
		imageBuff.image(image, -image.width - 1080, 0, 1920, 1080);
		imageBuff.popMatrix();
		
		image = imageBuff;
		
		image.loadPixels();
		int cols = applet.width / cellSize;
		int rows = applet.height / cellSize;

		applet.background(0, 0, 255);

		// Begin loop for columns
		for (int i = 0; i < cols; i++) {
			// Begin loop for rows
			for (int j = 0; j < rows; j++) {

				// Where are we, pixel-wise?
				int x = i * cellSize;
				int y = j * cellSize;
				int loc = (image.width - x - 1) + y * image.width;

				// Each rect is colored white with a size determined by
				// brightness
				int c = image.pixels[loc];
				float sz = (float) ((applet.brightness(c) / 255.0) * cellSize);
				applet.fill(255);
				applet.noStroke();
				applet.rect(x + cellSize / 2, y + cellSize / 2, sz, sz);
			}
		}
		
	}
}
