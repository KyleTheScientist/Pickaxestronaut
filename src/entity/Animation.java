package entity;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class Animation extends Thread {

	private ArrayList<Image> frames = new ArrayList<Image>();
	private Image frame;
	private boolean running;
	private long delay;

	public Animation(File folder, long delay) {
		File[] images = folder.listFiles();
		for (int i = 0; i < images.length; i++) {
			try {
				Image img = new ImageIcon(ImageIO.read(images[i])).getImage();
				frames.add(img);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		this.delay = delay;
		running = true;
	}

	public Animation(String path, long delay) {

		File folder = new File(this.getClass().getClassLoader().getResource("").getPath() + path);
		File[] files = folder.listFiles();
		for (int i = 0; i < files.length; i++) {
			try {
				frames.add(new ImageIcon(ImageIO.read(files[i])).getImage());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		this.delay = delay;
		running = true;
	}

	public void run() {
		while (running) {
			for (int i = 0; i < frames.size(); i++) {
				frame = frames.get(i);

				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
				}
			}
		}
	}

	public ArrayList<Image> getFrames() {
		return frames;
	}

	public void setFrames(ArrayList<Image> frames) {
		this.frames = frames;
	}

	public Image getFrame() {
		return frame;
	}

	public void setFrame(Image frame) {
		this.frame = frame;
	}

	public boolean isRunning() {
		return running;
	}

	public void setRunning(boolean running) {
		this.running = running;
	}

	public long getDelay() {
		return delay;
	}

	public void setDelay(long delay) {
		this.delay = delay;
	}
}
