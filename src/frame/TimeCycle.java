package frame;

public class TimeCycle extends Thread {

	public int brightness = 0;
	public int direction = 1, delay = 100;

	public TimeCycle(int delay) {
		this.delay = delay;
	}
	
	public void run() {
		while (true) {
			if (brightness < 100 && direction > 0 || brightness > 0 && direction < 0) {
				brightness += direction;
			} else if (brightness == 100 || brightness == 0) {
				direction = -direction;
			}

			try {
				Thread.sleep(delay);
			} catch (InterruptedException e) {
			}
		}
	}
}
