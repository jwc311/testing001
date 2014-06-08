package comport;

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
public abstract class SerialPort_Comm_Generic implements Runnable,
		SerialPortEventListener {
	public int baudrate = 115200;
	static CommPortIdentifier portId;
	static Enumeration portList;

	InputStream inputStream;
	OutputStream outputStream;
	SerialPort serialPort;
	Thread readThread;

	public static void main(String[] args) {
		// new Message_Threading();
		// new SimpleReadGeneric(new OneObject(), "COM15");

	}

	public SerialPort_Comm_Generic(String port, int baudrate) {
		boolean portFound = false;
		String defaultPort = port;
		this.baudrate = baudrate;
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
			outputStream = serialPort.getOutputStream();
		} catch (IOException e) {
		}

		try {
			serialPort.addEventListener(this);
		} catch (TooManyListenersException e) {
		}

		serialPort.notifyOnDataAvailable(true);

		try {
			serialPort.setSerialPortParams(baudrate, SerialPort.DATABITS_8,
					SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		} catch (UnsupportedCommOperationException e) {
		}

		readThread = new Thread(this);

		readThread.start();
	}

	public void sendData(byte[] data) {
		try {
			outputStream.write(data);
			// outputStream.flush();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void run() {
		try {
			Thread.sleep(20000);
		} catch (InterruptedException e) {
		}
	}

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
			DataInputStream dis = new DataInputStream(inputStream);
			processIncomingData(dis, true);
			break;
		}
	}

	public abstract void processIncomingData(DataInputStream dis, boolean bool);

	public abstract void processIncomingData(DataInputStream dis);

}