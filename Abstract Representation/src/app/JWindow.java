package app;

import visaulEffect.*;
import gab.opencv.OpenCV;

import javax.imageio.*;

import comport.*;
import network.*;
import processing.core.*;
import processing.video.*;

import java.awt.Rectangle;
import java.awt.image.*;
import java.io.*;
import java.net.*;
import java.util.Vector;

public class JWindow extends PApplet implements IncomingUDPListener, ListenSerial{
	
	static int port = 9090;
	static String destAddress = "localhost";
//	static String destAddress = "192.168.1.124";
	
	DatagramSocket sSocket = null;
	//DatagramSocket rSocket = null;
	
	byte[] buffer = new byte[65536];
	
	Capture camera = null;
	OpenCV opencv = null;
	PImage incomingVideoStream = null;
	int cameraWidth = 864, cameraHeight = 480;
	int screenWidth = 1920, screenHeight = 1080;
	boolean showLocalImage = true, showFaceBoundary = true;
	
	Vector<EffectAbstractClass> effects = new Vector<EffectAbstractClass>();
		
	ReadSerial serial = new ReadSerial(this, "COM3", 9600);
	
	boolean nobUpdated = false;
	long keyPressindex = 0;
	long prevTime = 0;
	int cellSizeChanged = 20;
	int channel = 0;
	int cellSize = 20;
	boolean fullVideo = false;
	long timerFullVideo = 0;
	long timeOpenSec = 60;
	
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
		  return true;
	}
	
	public void setup(){
		checkCamera();
		size(this.screenWidth, this.screenHeight);
		
		try{
			sSocket = new DatagramSocket();
			//rSocket = new DatagramSocket(port);
		}catch (SocketException e){
			e.printStackTrace();
		}
		
		camera = new Capture(this, this.cameraWidth, this.cameraHeight);
		opencv = new OpenCV(this, this.cameraWidth/2, this.cameraHeight/2);
		opencv.loadCascade(OpenCV.CASCADE_FRONTALFACE);
		camera.start();
		
		incomingVideoStream = createImage(this.cameraWidth, this.cameraHeight, RGB);
		
		effects.add(new Effect01());
		effects.add(new Effect02());
		
		(new UDPReceiver(this, port)).start();
	}
	
	public void draw() {
		
		if(camera.available() == true){
			camera.read();
			sendUDP(camera);
		}
		drawMainWindow(this, incomingVideoStream, 1);
		drawPinPImage(showLocalImage, showFaceBoundary, camera);
		
		if(this.fullVideo && this.timerFullVideo < System.currentTimeMillis()){
			this.fullVideo = false;
		}
	}
	
	public void drawMainWindow(PApplet applet, PImage incomingVideoStream, int index){
		if (nobUpdated == true) {
			cellSize = cellSizeChanged;
			nobUpdated = false;
		}
		channel = channel%2;
		(effects.get(channel)).visualEffect(this, incomingVideoStream, cellSize, 1, this.fullVideo);
	}
	
	public void drawPinPImage(boolean showImage, boolean showFaceBoundary, PImage img){
		if(showImage){
			PGraphics imageBuff = createGraphics(this.cameraWidth/2, this.cameraHeight/2);
			imageBuff.image(img, 0, 0, this.cameraWidth/2, this.cameraHeight/2);
			
			if(showFaceBoundary){
				opencv.loadImage(imageBuff);
				imageBuff.beginDraw();
				imageBuff.noFill();
				imageBuff.stroke(255, 255, 255);
				imageBuff.strokeWeight(2);
				Rectangle[] faces = opencv.detect();
				for (int i = 0; i < faces.length; i++) {
					imageBuff.rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
					double overlap = this.overlappingRatio(this.cameraWidth/4 - 50, this.cameraHeight/4 - 50, 100, faces[i].x, faces[i].y, faces[i].width);
					System.out.println("Overlap=" + overlap + ":"+ faces[i].x + ", " + faces[i].y + "!!! "+ faces[i].width + ", "+ faces[i].height);
					if(overlap > 0.7){
						System.out.println("[OVERLAP]");
						this.fullVideo = true;
						this.timerFullVideo = timeOpenSec * 1000 + System.currentTimeMillis();
					}
				}
				
				imageBuff.stroke(100, 100, 100);
				imageBuff.strokeWeight(5);
				//imageBuff.rectMode(CENTER);
				imageBuff.rect(this.cameraWidth/4 - 50, this.cameraHeight/4 - 50, 100, 100);
				imageBuff.endDraw();
			}

			pushMatrix();
//			tint(0, 153, 204, 90);
			tint(255, 190);
			scale(-1, 1);
			scale(0.5f);
			if(imageBuff != null){
				img = imageBuff.get();
				this.image(img, (float) (-img.width*8.8), 35);
			}
			popMatrix();
		}
	}
	
	public void drawFaceBoundary(){
		
		opencv.loadImage(camera);
		
		noFill();
		stroke(255, 255, 255);
		strokeWeight(1);
		Rectangle[] faces = opencv.detect();

		for (int i = 0; i < faces.length; i++) {
			System.out.println(faces[i].x + ", " + faces[i].y);
			rect(faces[i].x, faces[i].y, faces[i].width, faces[i].height);
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
	
	@Override
	public void getUDPincomingByteArray(byte[] data) {
		
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
	

	
	public void keyPressed(){
		incomingdata((""+key).getBytes());
		if(key == 'f'){
			//frame.setSize(this.displayWidth, this.displayWidth);
			frame.setBounds(0, 0, 1920, 1080);
		}
	}

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
		}else if(command.startsWith("Chn.DOWN")){
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
	}
	
	private double overlappingRatio(int x1, int y1, int width1, int x2, int y2, int width2){
		
		double newX = Math.max(x1, x2);
		double newY = Math.max(y1, y2);
		double newWidth = Math.min(x1 + width1, x2 + width2) - newX;
		double newHeight = Math.min(y1 + width1, y2 + width2) - newY;
		
		if (newWidth <= 0d || newHeight <= 0d) return 0;
		
		return (double) (newWidth*newHeight)/(width1*width1);
		
	}
	
}
