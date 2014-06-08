package app;

import processing.core.*;
import processing.video.*;

import javax.imageio.*;

import java.awt.image.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class VideoSender extends PApplet {

	// This is the port we are sending to
	int clientPort = 9100;
	String address = "localhost";
	
	// This is our object that sends UDP out
	DatagramSocket ds;
	// Capture object
	Capture cam;

	public static void main(String[] args) {
		PApplet.main(new String[] { "app.VideoSender" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());
	}
	
	public void setup() {
		
		String[] cameras = Capture.list();

		if (cameras.length == 0) {
			System.out.println("There are no cameras available for capture.");
		} else {
			System.out.println("Available cameras:");
			for (int i = 0; i < cameras.length; i++) {
				System.out.println(""+i+":"+cameras[i]);
			}
		}
		
//		size(640, 480);
		size(864, 480);
//		size(800, 600);
		// Setting up the DatagramSocket, requires try/catch
		try {
			ds = new DatagramSocket();
		} catch (SocketException e) {
			e.printStackTrace();
		}
		// Initialize Camera
		cam = new Capture(this, width, height, 30);
		cam.start();
	}

	private void captureEvent(Capture c) {
		c.read();
		// Whenever we get a new image, send it!
		broadcast(c);
	}

	public void draw() {
		if(cam.available() == true){
			cam.read();
		}
		PImage img = cam;
		pushMatrix();
		scale(-1, 1);
		image(img, -img.width, 0);
		popMatrix();
		broadcast(cam);
	}

	// Function to broadcast a PImage over UDP
	// Special thanks to: http://ubaa.net/shared/processing/udp/
	// (This example doesn't use the library, but you can!)
	public void broadcast(PImage img) {

		// We need a buffered image to do the JPG encoding
		BufferedImage bimg = new BufferedImage(img.width, img.height,
				BufferedImage.TYPE_INT_RGB);

//		int size = bimg.get
		
		// Transfer pixels from localFrame to the BufferedImage
		img.loadPixels();
		bimg.setRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);

		// Need these output streams to get image as bytes for UDP communication
		ByteArrayOutputStream baStream = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baStream);

		// Turn the BufferedImage into a JPG and put it in the
		// BufferedOutputStream
		// Requires try/catch
		try {
			ImageIO.write(bimg, "jpg", bos);
		} catch (IOException e) {
			e.printStackTrace();
		}

		// Get the byte array, which we will send out via UDP!
		byte[] packet = baStream.toByteArray();
		
		// Send JPEG data as a datagram
		System.out.println("Sending datagram with " + packet.length + " bytes");
		try {
			ds.send(new DatagramPacket(packet, packet.length, InetAddress
					.getByName(address), clientPort));
//			ds.send(new DatagramPacket(packet, packet.length, InetAddress
//					.getByName("localhost"), clientPort));
//			System.out.println("data sent");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
