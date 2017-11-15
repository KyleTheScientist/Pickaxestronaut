package frame.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import javax.swing.JPanel;

import entity.Player;
import frame.Game;
import frame.InputListener;
import fx.GameObject;
import fx.Sensor;
import level.Cell;
import level.Map;
import loaders.ImageLoader;
import resources.KMath;

public class Display extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1125736937121589792L;
	private static boolean showColliders = false;
	private static boolean showGrid = false;

	public static Display display;

	private InputListener input = new InputListener(this);
	private Camera camera;
	public static double scale = 1;
	public static double renderDistance = 30 * GameObject.tileSize;

	private Image backgroundImage = ImageLoader.load("res/icons/background.png");
	private Color backgroundColor = new Color(120, 220, 255);

	public Display(Dimension dimension) {
		display = this;
		setPreferredSize(dimension);
		setMinimumSize(dimension);
		setMaximumSize(dimension);
		requestFocusInWindow();
		camera = new Camera(new Rectangle(dimension));
		setBackground(backgroundColor);
		setIgnoreRepaint(true);
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		BufferedImage bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics2D g2 = (Graphics2D) bufferedImage.getGraphics();

		g2.setColor(darkenColor(backgroundColor, Game.getMap().lightCycle.brightness * 5 / 2));
		g2.fillRect(0, 0, getWidth(), getHeight());

		if (camera != null) {
			camera.adjust();
			g2.translate(-camera.x, -camera.y);
			g2.scale(scale, scale);
		}

		ArrayList<GameObject> objects;
		objects = GameObject.objects;

		for (int i = 0; i < objects.size(); i++) {
			GameObject o = objects.get(i);
			if (o.shouldRender) {
				if (o.image == null) {
					g2.setColor(darkenColor(o.getColor(), Game.getMap().lightCycle.brightness * 2 / 3));
					g2.fill(o);
				} else {
					if (o.flipRender) {
						g2.drawImage(o.image, o.x + o.width, o.y, -o.width, o.height, null);
					}else{
						g2.drawImage(o.image, o.x, o.y, o.width, o.height, null);
					}
				}
			}
		}

		if (showColliders)
			showColliders(g2, objects);

		g.drawImage(bufferedImage, 0, 0, getWidth(), getHeight(), this);

		g2 = (Graphics2D) g.create();
		if (showGrid)
			showGrid(g2);
	}

//	private void showBackground(Graphics2D g2) {
//		g2.drawImage(backgroundImage, 0, 0, this.getWidth(), this.getHeight(), null);
//	}

	private Color darkenColor(Color color, int d) {
		int r = color.getRed();
		int g = color.getGreen();
		int b = color.getBlue();

		r = KMath.clamp(r - d, 0, 255);
		g = KMath.clamp(g - d, 0, 255);
		b = KMath.clamp(b - d, 0, 255);

		return new Color(r, g, b, color.getAlpha());
	}

	private void showGrid(Graphics2D g2) {
		g2.translate(-camera.x, -camera.y);
		g2.setColor(Color.black);
		if (Game.getMap() != null) {
			Cell[][] grid = Game.getMap().getGrid().getCells();
			for (Cell[] ca : grid) {
				for (Cell c : ca) {
					if (c.contains(display.input.mouse.location)) {
						g2.drawImage(ImageLoader.load("res/cursor.png"), c.x, c.y, c.width, c.height, this);
					}
					g2.draw(c);
				}
			}
		}
	}

	private void showColliders(Graphics2D g2, ArrayList<GameObject> objects) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject o = objects.get(i);
			if (o.isActive()) {
				for (Sensor s : o.getCollider().getSensors()) {
					g2.setColor(Color.red.darker());
					g2.fill(s);
				}
			}
		}
	}

	public InputListener getInput() {
		return input;
	}

	public void setInput(InputListener input) {
		this.input = input;
	}

	public Camera getCamera() {
		return camera;
	}

	public void setCamera(Camera camera) {
		this.camera = camera;
	}

	public class Camera extends Rectangle {

		/**
		 * 
		 */
		private static final long serialVersionUID = 4580889347786866156L;

		public Camera(Rectangle bounds) {
			this.setBounds(bounds);
		}

		public void adjust() {
			Player player = Player.getPlayer();
			Map map = Game.getMap();

			if (map != null) {
				double plyCenterX = player.getCenterX() * scale, plyCenterY = player.getCenterY() * scale; // scale
																											// is
																											// good
				double camCenterX = camera.getCenterX(), camCenterY = camera.getCenterY();
				int camW = (int) (camera.width), camH = (int) (camera.height);
				double camMaxX = camera.getMaxX(), camMaxY = camera.getMaxY();
				int absMapW = (int) (map.getAbsMapWidth() * scale), absMapH = (int) (map.getAbsMapHeight() * scale);

				if (plyCenterX > camCenterX && camMaxX < absMapW) {
					camera.x = (int) (plyCenterX - camW / 2);
				}
				if (plyCenterX < camCenterX && camMaxX > 0) {
					camera.x = (int) (plyCenterX - camW / 2);
				}

				if (camera.x < 0) {
					camera.x = 0;
				}
				if (plyCenterX + camW / 2 > absMapW) {
					camera.x = absMapW - camW;
				}

				if (plyCenterY > camCenterY && camMaxY < absMapH) {
					camera.y = (int) (plyCenterY - camH / 2);
				}
				if (plyCenterY < camCenterY) {
					camera.y = (int) (plyCenterY - camH / 2);
				}

				if (camMaxY > absMapH) {
					camera.y = absMapH - camH;
				}
			}
		}
	}
}
