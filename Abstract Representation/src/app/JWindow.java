package app;

import processing.core.*;
import processing.video.*;

import javax.imageio.*;

import network.*;

import java.awt.image.*;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.*;

public class JWindow extends PApplet implements IncomingUDPListener{
	
	static int port = 9090;
	static String destAddress = "localhost";
//	static String destAddress = "192.168.1.124";
	
	DatagramSocket sSocket = null;
	//DatagramSocket rSocket = null;
	
	byte[] buffer = new byte[65536];
	
	Capture camera = null;
	PImage incomingVideoStream = null;
	int cameraWidth = 864, cameraHeight = 480;
	boolean showLocalImage = true;
	
	public static void main(String[] args) {
		PApplet.main(new String[] { "app.JWindow" });
		System.out.println(java.lang.Runtime.getRuntime().maxMemory());

		if(args.length > 0){
			for(int i = 0 ; i < args.length ; i++){
				if(args[i].startsWith("IP")){
					destAddress = args[i].split(":")[1];
				}else if(args[i].startsWith("port")){
					port = Integer.parseInt(args[i].split(":")[1]);
				}
			}
		}		
	}
	
	public boolean sketchFullScreen() {
		  return false;
	}
	
	public void setup(){
		checkCamera();
		size(this.cameraWidth, this.cameraHeight);
		
		try{
			sSocket = new DatagramSocket();
			//rSocket = new DatagramSocket(port);
		}catch (SocketException e){
			e.printStackTrace();
		}
		
		camera = new Capture(this, this.cameraWidth, this.cameraHeight);
		camera.start();
		
		incomingVideoStream = createImage(this.cameraWidth, this.cameraHeight, RGB);
		
		(new UDPReceiver(this, port)).start();
	}
	
	public void draw() {
		
		if(camera.available() == true){
			camera.read();
			sendUDP(camera);
		}
		image(this.incomingVideoStream, 0, 0, this.cameraWidth, this.cameraHeight);
		drawLocalImage(showLocalImage);
	}
	
	public void drawLocalImage(boolean showImage){
		if(showImage){
			PImage img = camera;
			pushMatrix();
			scale(-1, 1);
			scale(0.25f);
			image(img, -img.width, 0);
			popMatrix();
		}
	}
	
	private void checkCamera(){
		String[] cameras = Capture.list();
		if (cameras.length == 0) {
			System.out.println("There are no cameras available for capture.");
		} else {
			System.out.println("Available cameras:");
			for (int i = 0; i < cameras.length; i++) {
				System.out.println(""+i+":"+cameras[i]);
			}
		}
	}
	
//	public void run(){
//		while(true){
//			checkForImage();
//		}
//	}
	
//	private void checkForImage(){
//		DatagramPacket p = new DatagramPacket(buffer, buffer.length);
//		try {
//			System.out.println("before rSocket.receive");
//			rSocket.receive(p);
//			System.out.println("after rSocket.receive");
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		byte[] data = p.getData();
//
//		println("[Received] datagram with " + data.length + " bytes.");
//
//		ByteArrayInputStream bais = new ByteArrayInputStream(data);
//
//		incomingVideoStream.loadPixels();
//		try {
//			BufferedImage img = ImageIO.read(bais);
//			img.getRGB(0, 0, incomingVideoStream.width, incomingVideoStream.height, incomingVideoStream.pixels, 0, incomingVideoStream.width);
//			
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		incomingVideoStream.updatePixels();
//	}
	
	@Override
	public void getUDPincomingByteArray(byte[] data) {
		
		//System.out.println("**************");
		ByteArrayInputStream bais = new ByteArrayInputStream(data);

		incomingVideoStream.loadPixels();
		try {
			BufferedImage img = ImageIO.read(bais);
			img.getRGB(0, 0, incomingVideoStream.width, incomingVideoStream.height, incomingVideoStream.pixels, 0, incomingVideoStream.width);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		incomingVideoStream.updatePixels();
	}
	
	private void captureEvent(Capture image) {
		image.read();
		sendUDP(image);
	}
	
	public void sendUDP(PImage img) {

		BufferedImage bimg = new BufferedImage(img.width, img.height, BufferedImage.TYPE_INT_RGB);
		
		img.loadPixels();
		bimg.setRGB(0, 0, img.width, img.height, img.pixels, 0, img.width);

		ByteArrayOutputStream baStream = new ByteArrayOutputStream();
		BufferedOutputStream bos = new BufferedOutputStream(baStream);

		try {
			ImageIO.write(bimg, "jpg", bos);
		} catch (IOException e) {
			//e.printStackTrace();
		}

		byte[] packet = baStream.toByteArray();
		System.out.println("[Sending] datagram with " + packet.length + " bytes to " + this.destAddress);
		try {
			sSocket.send(new DatagramPacket(packet, packet.length, InetAddress.getByName(destAddress), port));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}


	
	
	
}
