package comport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import comport.ComportListener;
import comport.SerialPort_Comm_Generic;

public class ReadSerial extends SerialPort_Comm_Generic {

	ListenSerial listener = null;

	public ReadSerial(ListenSerial listener, String port, int baudrate) {
		super(port, baudrate);
		this.listener = listener;
	}

	public void processIncomingData(DataInputStream dis) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);

		try {
			int check = -1;
			check = (int) dis.readByte();
			dout.writeByte(check);

			byte index = dis.readByte();
			dout.writeByte(index);
			short heading = dis.readShort();
			dout.writeShort(heading);

			System.out.print("Index=" + index);
			System.out.println("\t\t\t Heading =" + (float) heading / 10.0);
			listener.incomingdata(bout.toByteArray());
		} catch (IOException e) {

		}
	}

	public void processIncomingData(DataInputStream dis, boolean bool) {
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);

		byte b[] = new byte[1];
		try {
			while ((dis.read(b)) != -1) {
				if (b[0] == 10)
					break;
				dout.write(b);
			}
			listener.incomingdata(bout.toByteArray());
		} catch (IOException e) {

		}
	}
}
