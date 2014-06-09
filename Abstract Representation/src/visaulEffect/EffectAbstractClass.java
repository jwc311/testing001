package visaulEffect;

import processing.core.PApplet;
import processing.core.PImage;

public abstract class EffectAbstractClass {
	String effectName = "";
	public abstract void visualEffect(PApplet applet, PImage image, int cellSize, int feature, boolean fullVideo);
}
