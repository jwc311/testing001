package comport;

/*
 * @(#)SimpleRead.java	1.12 98/06/25 SMI
 * 
 * Copyright 2003 Sun Microsystems, Inc. All rights reserved.
 * SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 * 
 * Sun grants you ("Licensee") a non-exclusive, royalty free, license
 * to use, modify and redistribute this software in source and binary
 * code form, provided that i) this copyright notice and license appear
 * on all copies of the software; and ii) Licensee does not utilize the
 * software in a manner which is disparaging to Sun.
 * 
 * This software is provided "AS IS," without a warranty of any kind.
 * ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND WARRANTIES,
 * INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY EXCLUDED. SUN AND
 * ITS LICENSORS SHALL NOT BE LIABLE FOR ANY DAMAGES SUFFERED BY
 * LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING THE
 * SOFTWARE OR ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS
 * BE LIABLE FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT,
 * INDIRECT, SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES,
 * HOWEVER CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING
 * OUT OF THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
 * ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
 * 
 * This software is not designed or intended for use in on-line control
 * of aircraft, air traffic, aircraft navigation or aircraft
 * communications; or in the design, construction, operation or
 * maintenance of any nuclear facility. Licensee represents and
 * warrants that it will not use or redistribute the Software for such
 * purposes.
 */
import java.io.*;
import java.util.*; //import javax.comm.*;
import gnu.io.*;

/**
 * Class declaration
 * 
 * 
 * @author
 * @version 1.8, 08/03/00
 */
public class SimpleRead2 implements Runnable, SerialPortEventListener {
	static CommPortIdentifier portId;
	static Enumeration portList;
	InputStream inputStream;
	SerialPort serialPort;
	Thread readThread;
	ComportListener listener = null;

	/**
	 * Method declaration
	 * 
	 * 
	 * @param args
	 * 
	 * @see
	 */
	public static void main(String[] args) {
		// new Message_Threading();
		new SimpleRead2(new OneObject2(), "COM15");

	}

	/**
	 * Constructor declaration
	 * 
	 * 
	 * @see
	 */

	public SimpleRead2(ComportListener listener, String port) {
		this.listener = listener;
		boolean portFound = false;
		String defaultPort = port;

		portList = CommPortIdentifier.getPortIdentifiers();

		while (portList.hasMoreElements()) {
			portId = (CommPortIdentifier) portList.nextElement();
			if (portId.getPortType() == CommPortIdentifier.PORT_SERIAL) {
				if (portId.getName().equals(defaultPort)) {
					System.out.println("Found port: " + defaultPort);
					portFound = true;
					SimpleRead_start();
				}
			}
		}
		if (!portFound) {
			System.out.println("port " + defaultPort + " not found.");
		}
	}

	public void SimpleRead_start() {
		try {
			serialPort = (SerialPort) portId.open("SimpleReadApp", 2000);
		} catch (PortInUseException e) {
		}

		try {
			inputStream = serialPort.getInputStream();
		} catch (IOException e) {
		}

		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
		}

		serialPort.notifyOnDataAvailable(true);

		try {
			serialPort.setSerialPortParams(9600/* 115200 */,
					SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
					SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}

		readThread = new Thread(this);

		readThread.start();
	}

	/**
	 * Method declaration
	 * 
	 * 
	 * @see
	 */
	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
	}

	/**
	 * Method declaration
	 * 
	 * 
	 * @param event
	 * 
	 * @see
	 */
	public void serialEvent(SerialPortEvent event) {
		switch (event.getEventType()) {

		case SerialPortEvent.BI:

		case SerialPortEvent.OE:

		case SerialPortEvent.FE:

		case SerialPortEvent.PE:

		case SerialPortEvent.CD:

		case SerialPortEvent.CTS:

		case SerialPortEvent.DSR:

		case SerialPortEvent.RI:

		case SerialPortEvent.OUTPUT_BUFFER_EMPTY:
			break;

		case SerialPortEvent.DATA_AVAILABLE:
			byte[] readBuffer = new byte[3];
			byte[] header = new byte[1];
			byte[] angle = new byte[1];
			byte[] speed = new byte[1];

			byte[] checking = new byte[2];

			byte[] heading = new byte[2];
			byte[] pitch = new byte[2];
			byte[] roll = new byte[2];

			int check = 0;
			int current1 = 0,
			current2 = 0,
			current3 = 0;
			int numBytes = 0;

			DataInputStream dis = new DataInputStream(inputStream);

			try {
				while (dis.available() > 0) {

					// numBytes = inputStream.read(checking);
					check = (int) dis.readShort();

					if (check == 0) {

						// numBytes = inputStream.read(heading);
						current1 = (int) dis.readShort();
						System.out.print("Heading=" + current1);

						// numBytes = inputStream.read(pitch);
						current2 = (int) dis.readShort();
						System.out.print("\t\t\tRoll=" + current2);

						// numBytes = inputStream.read(roll);
						current3 = (int) dis.readShort();
						System.out.println("\t\t\tPitch=" + current3);
					}

					// if(listener != null){
					// listener.comportListener(readBuffer);
					// listener.comportListener((new
					// String(readBuffer)).trim());
					// }
				}

				// System.out.print((new String(readBuffer)).trim());
			} catch (IOException e) {
			}

			break;
		}
	}
}

class OneObject2 implements ComportListener {

	@Override
	public void comportListener(byte[] data) {
		// TODO Auto-generated method stub
		System.out.println("===========================");
		System.out.print("Header=" + (int) data[0]);
		System.out.print(", Angle=" + (int) data[1]);
		System.out.println(", Speed=" + (int) data[2]);
	}

	@Override
	public void comportListener(String data) {

	}
}
