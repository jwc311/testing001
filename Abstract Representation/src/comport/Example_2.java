package comport;

public class Example_2 implements ComportListener {

	PressbuttonSpeedController2 controller = new PressbuttonSpeedController2();

	public static void main(String arg[]) {
		new Example_2();
	}

	Example_2() {
		new SimpleRead(this, "COM17");
		System.out.println("Port Opened");
		controller.start();
	}

	@Override
	public void comportListener(byte[] data) {
		// TODO Auto-generated method stub
		System.out.println("===========================");
		System.out.print("Header=" + data[0]);
		System.out.print(", Angle=" + data[1]);
		System.out.println(", Speed=" + data[2]);
		// controller.setDelay_Angle((int)data[1]);
	}

	@Override
	public void comportListener(String data) {
		// TODO Auto-generated method stub

	}

}

class PressbuttonSpeedController2 extends Thread {

	int delay = 200;
	int angle = 0;
	String direction = "Center";

	public void setDelay_Angle(int angle) {
		this.angle = angle;

		if (angle <= 15 && angle > 17) {
			// 2 degree
			direction = "Move_Left";
			delay = 50;
		} else if (angle <= 17 && angle < 23) {
			// 5 degree
			direction = "Move_Left";
			delay = 100;
		} else if (angle >= 23 && angle < 28) {
			// 5 degree
			direction = "Move_Left";
			delay = 200;
		} else if (angle >= 28 && angle < 32) {
			// 4 degree
			direction = "Center";
			delay = 300;
		} else if (angle >= 32 && angle < 37) {
			// 5 degree
			direction = "Move_Right";
			delay = 200;
		} else if (angle >= 37 && angle < 42) {
			// 5 degree
			direction = "Move_Right";
			delay = 100;
		} else if (angle >= 42 && angle < 46) {
			// 4 degree
			direction = "Move_Right";
			delay = 50;
		}
	}

	public void run() {
		while (true) {
			try {
				System.out.println(System.currentTimeMillis() + ":" + direction
						+ ":" + angle + ":" + delay + " Action");
				Thread.sleep(50);
				System.out.println(System.currentTimeMillis() + ":" + direction
						+ ":" + angle + ":" + delay + " Stop");
				Thread.sleep(delay);
			} catch (Exception e) {
			}
		}
	}
}
