package network;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class UDPReceiver extends Thread{

	public int port = 9090;
	DatagramSocket rSocket = null;
	byte[] buffer = new byte[65536];
	IncomingUDPListener UDPListener = null;
	
	
	public UDPReceiver(IncomingUDPListener UDPListener){
		this.UDPListener = UDPListener;
		try {
			rSocket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public UDPReceiver(IncomingUDPListener UDPListener, int port){
		this.port = port;
		this.UDPListener = UDPListener;
		try {
			rSocket = new DatagramSocket(this.port);
		} catch (SocketException e) {
			e.printStackTrace();
		}
	}
	
	public void run(){
		while(true){
			DatagramPacket p = new DatagramPacket(buffer, buffer.length);
			try {
				//System.out.println("before rSocket.receive");
				rSocket.receive(p);
				//System.out.println("after rSocket.receive");
			} catch (IOException e) {
				e.printStackTrace();
			}
			byte[] data = p.getData();
			this.UDPListener.getUDPincomingByteArray(data);
			System.out.println("[Received] datagram with " + data.length + " bytes.");
		}
	}
}
