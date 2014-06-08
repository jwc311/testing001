package comport;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import comport.ComportListener;
import comport.SerialPort_Comm_Generic;

public class ReadSerial_ecompass extends SerialPort_Comm_Generic {

	ListenSerial listener = null;

	public ReadSerial_ecompass(ListenSerial listener, String port, int baudrate) {
		super(port, baudrate);
		this.listener = listener;
	}

	public void processIncomingData(DataInputStream dis) {
		// System.out.println("Data available");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);

		try {
			int check = -1;
			check = (int) dis.readByte();
			// System.out.println("Check="+check);

			if (check == 0) {

				dout.writeByte(check);

				byte index = dis.readByte();
				dout.writeByte(index);
				short heading = dis.readShort();
				dout.writeShort(heading);

				System.out.print("Index=" + index);
				System.out.println("\t\t\t Heading =" + (float) heading / 10.0);
				listener.incomingdata(bout.toByteArray());
			}
		} catch (IOException e) {

		}
	}

	public void processIncomingData(DataInputStream dis, boolean bool) {
		// System.out.println("Data available");
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		DataOutputStream dout = new DataOutputStream(bout);

		try {
			int check = -1;
			check = (int) dis.readByte();
			// System.out.println("Check="+check);

			if (check == 0) {

				dout.writeByte(check);

				byte index = dis.readByte();
				dout.writeByte(index);
				// System.out.print("index="+index);

				byte size = dis.readByte();
				dout.write(size);
				// System.out.print(", size="+size);

				for (int i = 0; i < size; i++) {
					short sensor = dis.readShort();
					// System.out.print(", sensor["+i+"]="+sensor);
					dout.writeShort(sensor);
				}
				// System.out.println("");
				listener.incomingdata(bout.toByteArray());
			}
		} catch (IOException e) {

		}
	}
}
