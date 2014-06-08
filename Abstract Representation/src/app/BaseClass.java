package app;

import processing.core.PApplet;

public class BaseClass extends PApplet {

	public static void main(String[] args) {
		PApplet.main(new String[] { "app.Mirror" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}
	
	public boolean sketchFullScreen() {
		  return true;
	}
	
	public void setup() {
		
	}
	
	public void draw() {
		
	}

}
