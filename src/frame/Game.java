package frame;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JLayeredPane;

import entity.Player;
import frame.components.Background;
import frame.components.Display;
import frame.components.HUD;
import frame.components.LoadingScreen;
import fx.Physics;
import level.Map;

public class Game extends JFrame implements Runnable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 8480883185833071213L;

	public static int width = 1000, height = 900;

	private boolean resizable = false;
	private long desiredFps = 1000 / 100;
	private long desiredTps = 1000 / 80;
	public static long lastTickCheck = 0, lastFrameCheck = 0;
	public static long tps = 0, fps = 0;
	private static int ticks, frames;

	private static JFrame frame;
	private Dimension dimension;
	private Display display;
	private Background background;
	public static HUD hud;
	private JLayeredPane layeredPane;
	public static LoadingScreen loadingScreen;

	public static final int LEFT = 0;
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;

	private static Map map;

	public Game() {
		map = new Map();
		frame = this;

		dimension = new Dimension(width, height);
		setSize(dimension);
		setMinimumSize(dimension);
		setResizable(resizable);
		setVisible(true);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);

		display = new Display(dimension);
		background = new Background(dimension);
		hud = new HUD(dimension);
		hud.repaint();

		layeredPane = new JLayeredPane();

		layeredPane.add(hud, new Integer(1000));
		layeredPane.add(display, new Integer(50));
		layeredPane.add(background, new Integer(0));

		display.setBounds(0, 0, width, height);
		hud.setBounds(0, 0, width, height);

		// shader.setBounds(0, 0, width, height);
		// background.setBounds(0, 0, width, height);

		background.setIgnoreRepaint(true);

		// add(layeredPane);

		loadingScreen = new LoadingScreen(dimension, layeredPane);
		add(loadingScreen);
		pack();
	}

	public void run() {

		map.generateWorld();
		while (!map.loaded) {
			loadingScreen.repaint();
		}
		// GameObject.updateLighting();

		long lastTime = System.currentTimeMillis();
		long lastUpdate = lastTime;

		while (true) {
			long startTime = System.currentTimeMillis();
			long startNano = System.nanoTime();
			if (startTime - lastTime > desiredFps) {
				lastTime = startTime;
				repaint();
				hud.update();
				frame();
			}
			if (startTime - lastUpdate > desiredTps) {
				lastUpdate = startTime;
				Physics.update();
				Player.update();
				tick();
			}
		}
	}

	private static void frame() {
		frames++;

		if (System.currentTimeMillis() - lastFrameCheck > 1000) {
			fps = frames;
			frames = 0;
			lastFrameCheck = System.currentTimeMillis();
		}
	}

	private static void tick() {
		ticks++;

		if (System.currentTimeMillis() - lastTickCheck > 1000) {
			tps = ticks;
			ticks = 0;
			lastTickCheck = System.currentTimeMillis();
		}
	}

	public static void main(String[] args) {
		new Thread(new Game()).start();
	}

	public static Map getMap() {
		return map;
	}

	public static JFrame getFrame() {
		return frame;
	}

}
