package frame;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import javax.swing.AbstractAction;
import javax.swing.JComponent;
import javax.swing.KeyStroke;

import entity.Player;
import frame.components.Display;
import fx.GameObject;

public class InputListener {

	// this Display variable can be swapped for any JComponent, so this file
	// isn't completely unusable
	Display display;
	private final int FOCUS = JComponent.WHEN_IN_FOCUSED_WINDOW;
	public static Key left, up, right, down;
	public Mouse mouse = new Mouse();

	public InputListener(Display display) {
		this.display = display;
		createBinds();
		createKeys();
		createMouse();
	}

	private void createMouse() {
		display.addMouseMotionListener(new MouseAdapter() {
			public void mouseMoved(MouseEvent e) {
				mouse.location = adjustPoint(e.getPoint());
			}

			public void mouseDragged(MouseEvent e) {
				mouse.location = adjustPoint(e.getPoint());
				if (e.getModifiersEx() == MouseEvent.BUTTON1_DOWN_MASK) {
					mouse.leftIsPressed = true;
					GameObject.damageObject(adjustPoint(e.getPoint()));
				}
				if (e.getModifiersEx() == MouseEvent.BUTTON3_DOWN_MASK) {
					mouse.rightPressed = true;
					// Tree.spawnTree(e.getPoint());
					GameObject.spawnObject(adjustPoint(e.getPoint()), Player.player.getItemType(), false);
				}
			}
		});
		display.addMouseListener(new MouseAdapter() {
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mouse.leftIsPressed = true;
					GameObject.damageObject(adjustPoint(e.getPoint()));
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					mouse.rightPressed = true;
					// Tree.spawnTree(adjustPoint(e.getPoint()));
					GameObject.spawnObject(adjustPoint(e.getPoint()), Player.player.getItemType(), false);
				}
			}

			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					mouse.leftIsPressed = false;
				}
				if (e.getButton() == MouseEvent.BUTTON3) {
					mouse.rightPressed = false;
				}
			}
		});

		display.addMouseWheelListener(new MouseAdapter() {
			public void mouseWheelMoved(MouseWheelEvent e) {
				if (e.getWheelRotation() > 0) {
					Player.player.cycleItem(1);
				}
				if (e.getWheelRotation() < 0) {
					Player.player.cycleItem(-1);
				}
			}
		});

	}

	private Point adjustPoint(Point p) {
		double s = Display.scale;
		int x = (int) (p.getX() / s);
		int y = (int) (p.getY() / s);

		x += display.getCamera().x / s;
		y += display.getCamera().y / s;

		return new Point(x, y);
	}

	private void createKeys() {
		left = new Key("left", Game.LEFT);
		right = new Key("right", Game.RIGHT);
		up = new Key("up", Game.UP);
		down = new Key("down", Game.DOWN);
	}

	private void createBinds() {
		KeyStroke pLeft = KeyStroke.getKeyStroke("pressed LEFT");
		KeyStroke pUp = KeyStroke.getKeyStroke("pressed UP");
		KeyStroke pSpace = KeyStroke.getKeyStroke("pressed SPACE");
		KeyStroke pRight = KeyStroke.getKeyStroke("pressed RIGHT");
		KeyStroke pDown = KeyStroke.getKeyStroke("pressed DOWN");
		KeyStroke pA = KeyStroke.getKeyStroke("pressed A");
		KeyStroke pW = KeyStroke.getKeyStroke("pressed W");
		KeyStroke pD = KeyStroke.getKeyStroke("pressed D");
		KeyStroke pS = KeyStroke.getKeyStroke("pressed S");

		KeyStroke rLeft = KeyStroke.getKeyStroke("released LEFT");
		KeyStroke rUp = KeyStroke.getKeyStroke("released UP");
		KeyStroke rSpace = KeyStroke.getKeyStroke("released SPACE");
		KeyStroke rRight = KeyStroke.getKeyStroke("released RIGHT");
		KeyStroke rDown = KeyStroke.getKeyStroke("released DOWN");
		KeyStroke rA = KeyStroke.getKeyStroke("released A");
		KeyStroke rW = KeyStroke.getKeyStroke("released W");
		KeyStroke rD = KeyStroke.getKeyStroke("released D");
		KeyStroke rS = KeyStroke.getKeyStroke("released S");

		display.getInputMap(FOCUS).put(pLeft, "press_left");
		display.getInputMap(FOCUS).put(pUp, "press_up");
		display.getInputMap(FOCUS).put(pSpace, "press_up");
		display.getInputMap(FOCUS).put(pRight, "press_right");
		display.getInputMap(FOCUS).put(pDown, "press_down");
		display.getInputMap(FOCUS).put(pA, "press_left");
		display.getInputMap(FOCUS).put(pW, "press_up");
		display.getInputMap(FOCUS).put(pD, "press_right");
		display.getInputMap(FOCUS).put(pS, "press_down");

		display.getInputMap(FOCUS).put(rLeft, "release_left");
		display.getInputMap(FOCUS).put(rUp, "release_up");
		display.getInputMap(FOCUS).put(rSpace, "release_up");
		display.getInputMap(FOCUS).put(rRight, "release_right");
		display.getInputMap(FOCUS).put(rDown, "release_down");
		display.getInputMap(FOCUS).put(rA, "release_left");
		display.getInputMap(FOCUS).put(rW, "release_up");
		display.getInputMap(FOCUS).put(rD, "release_right");
		display.getInputMap(FOCUS).put(rS, "release_down");

		display.getActionMap().put("press_left", new BasicInput(Game.LEFT, true));
		display.getActionMap().put("press_up", new BasicInput(Game.UP, true));
		display.getActionMap().put("press_right", new BasicInput(Game.RIGHT, true));
		display.getActionMap().put("press_down", new BasicInput(Game.UP, true));

		display.getActionMap().put("release_left", new BasicInput(Game.LEFT, false));
		display.getActionMap().put("release_up", new BasicInput(Game.UP, false));
		display.getActionMap().put("release_right", new BasicInput(Game.RIGHT, false));
		display.getActionMap().put("release_down", new BasicInput(Game.UP, false));

		display.getInputMap(FOCUS).put(KeyStroke.getKeyStroke("pressed Z"), "zoom");
		display.getActionMap().put("zoom", new SpecialInput("z"));

		display.getInputMap(FOCUS).put(KeyStroke.getKeyStroke("pressed E"), "toggle_inventory");
		display.getActionMap().put("toggle_inventory", new SpecialInput("e"));

		display.getInputMap(FOCUS).put(KeyStroke.getKeyStroke("pressed Q"), "toggle_store");
		display.getActionMap().put("toggle_store", new SpecialInput("q"));

		display.getInputMap(FOCUS).put(KeyStroke.getKeyStroke("pressed ESCAPE"), "quit");
		display.getActionMap().put("quit", new SpecialInput("esc"));

	}

	private void zoom() {
		int s = (int) (Display.scale * 10);
		if (s == 5)
			Display.scale = 1;
		else if (s == 10)
			Display.scale = 1.5;
		else if (s == 15)
			Display.scale = .5;
		else
			Display.scale = 1;
	}

	private class SpecialInput extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -8481385486213301713L;
		String key;

		public SpecialInput(String key) {
			this.key = key;
		}

		public void actionPerformed(ActionEvent e) {
			if (key.equalsIgnoreCase("z")) {
				zoom();
			}
			if (key.equalsIgnoreCase("e")) {
				Game.hud.inventory.toggle();
			}
			if (key.equalsIgnoreCase("q")) {
				Game.hud.store.toggle();
			}
			if (key.equalsIgnoreCase("esc")) {
				System.exit(0);
			}
		}
	}

	private class BasicInput extends AbstractAction {

		/**
		 * 
		 */
		private static final long serialVersionUID = -2298656193497784683L;
		int direction;
		boolean pressed;

		public BasicInput(int direction, boolean pressed) {
			this.direction = direction;
			this.pressed = pressed;

		}

		public void actionPerformed(ActionEvent e) {
			switch (direction) {
			case (Game.LEFT):
				left.setPressed(pressed);
				break;
			case (Game.UP):
				up.setPressed(pressed);
				break;
			case (Game.RIGHT):
				right.setPressed(pressed);
				break;
			case (Game.DOWN):
				down.setPressed(pressed);
				break;
			}
		}
	}

	public class Mouse {
		public boolean leftIsPressed, rightPressed;
		public Point location = new Point(0, 0);
	}

	public class Key {
		public boolean isPressed;
		public String id;
		public int keyValue;

		public Key(String id) {
			this(id, -1);
		}

		public Key(String id, int keyVal) {
			this.id = id;
			this.keyValue = keyVal;
		}

		public void toggle() {

		}

		public boolean isPressed() {
			return isPressed;
		}

		public void setPressed(boolean isPressed) {
			this.isPressed = isPressed;
		}

	}

}
