package app;

import comport.ListenSerial;
import comport.ReadSerial;

public class Proximity implements ListenSerial{
	
	
	ReadSerial serial = new ReadSerial(this, "COM5", 9600);

	
	public static void main(String[] arg){
		new Proximity();
	}
	
	public Proximity(){
		
	}
	
	@Override
	public void incomingdata(byte[] data) {
		System.out.print(new String(data));
	}

}
