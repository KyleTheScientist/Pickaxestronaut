package fx;

import java.awt.Color;
import java.util.ArrayList;

import entity.Player;
import frame.Game;
import frame.InputListener.Mouse;
import frame.components.Display;
import fx.GameObject.BlockType;
import level.Map;
import resources.KMath;

public class Physics {

	private static final double gravity = (-GameObject.tileSize / 100) * .9;
	private static int tileSize = GameObject.tileSize;

	public static void update() {
		ArrayList<GameObject> objects;
		objects = GameObject.objects;

		for (int i = 0; i < objects.size(); i++) {
			GameObject o = objects.get(i);
			handleRenderDistance(o);
			if (o.shouldRender) {
				handleMouseInput(o);
				handleDamage(o);
				handleTrees(o);
				if (o.isActive) {
					Collider c = o.getCollider();
					handleGravity(o, c);
					handleLevelBounds(o);
					o.getCollider().update();
					handleCollisions(o, c);
				}
			}
		}

		for (int i = 0; i < objects.size(); i++) {
			objects.get(i).vMove();
		}
	}

	private static void handleRenderDistance(GameObject o) {
		Player player = Player.player;
		double dx = o.x - player.x;
		double dy = o.y - player.y;
		double distance = Math.sqrt((dx * dx) + (dy * dy));

		if (distance < Display.renderDistance) {
			o.shouldRender = true;
		} else {
			o.shouldRender = false;
		}

	}

	public static void handleTrees(GameObject o) {
		if (o.getType() == BlockType.Log) {
			if (o.isNatural && GameObject.removedObjects.contains(o.sourceBlock)) {
				GameObject.damageObject(o, 10);
			}
		}
		if (o.getType() == BlockType.Leaves) {
			if (o.isNatural && GameObject.removedObjects.contains(o.sourceBlock)) {
				GameObject.damageObject(o, 10);
			}
		}
	}

	private static void handleDamage(GameObject o) {
		if (o.damage > 0 && !Display.display.getInput().mouse.leftIsPressed) {
			o.damage--;
		}
		alterColorBrightness(o);
	}

	private static void alterColorBrightness(GameObject o) {
		if (o.damage > 0) {
			int baseRed = o.getBaseColor().getRed();
			int newRed = KMath.clamp((int) (baseRed - (baseRed * o.damage / o.durability)), 0, 255);
			int baseGreen = o.getBaseColor().getGreen();
			int newGreen = KMath.clamp((int) (baseGreen - (baseGreen * o.damage / o.durability)), 0, 255);
			int baseBlue = o.getBaseColor().getBlue();
			int newBlue = KMath.clamp((int) (baseBlue - (baseBlue * o.damage / o.durability)), 0, 255);

			o.color = new Color(newRed, newGreen, newBlue);
		}

	}

	private static void handleMouseInput(GameObject o) {
		Mouse mouse = Display.display.getInput().mouse;
		if (mouse.leftIsPressed && o.contains(mouse.location)) {
			GameObject.damageObject(o.getLocation());
		}
	}

	private static void handleCollisions(GameObject o, Collider c) {
		if (c.collided(c.getDown())) {
			GameObject oi = c.getDown().getCollision();
			o.setVy(0);
			o.setY(oi.y - o.height);
		}
		if (c.collided(c.getUp())) {
			GameObject oi = c.getUp().getCollision();
			o.setVy(0);
			o.setY(oi.y + oi.height);
		}
		if (c.collided(c.getRight())) {
			GameObject oi = c.getRight().getCollision();
			o.setVx(0);
			o.setX(oi.x - o.width - 1);
		}
		if (c.collided(c.getLeft())) {
			GameObject oi = c.getLeft().getCollision();
			o.setVx(0);
			o.setX(oi.x + oi.width);
		}
	}

	private static void handleGravity(GameObject o, Collider c) {
		if (o.hasGravity) {
			o.addForce(0, gravity);
		}
	}

	private static void handleLevelBounds(GameObject o) {
		Map map = Game.getMap();

		if (o.getNextX() < 0) {
			o.setX(0);
			o.setVx(0);
		}
		if (o.getNextY() < 0) {
			o.setY(0);
			o.setVy(0);
		}
		if (o.getNextX() + o.getWidth() > map.getAbsMapWidth()) {
			o.setX(map.getAbsMapWidth() - o.getWidth());
			o.setVx(0);
		}
		if (o.getNextY() + o.getHeight() > map.getAbsMapHeight()) {
			o.setY(map.getAbsMapHeight() - o.getHeight());
			o.setVy(0);
		}

	}
}
