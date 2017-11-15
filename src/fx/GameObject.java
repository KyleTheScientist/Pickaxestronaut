package fx;

import java.awt.Color;
import java.awt.Image;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Random;

import entity.Player;
import frame.Game;
import level.Cell;
import level.Map;
import objects.Attributes;
import objects.BEDROCK;
import objects.GROUND;
import objects.LEAVES;
import objects.LOG;
import objects.ORE;
import objects.STONE;
import resources.KMath;

public class GameObject extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2982636933130864051L;
	public static ArrayList<GameObject> objects = new ArrayList<GameObject>(0);
	public static ArrayList<GameObject> removedObjects = new ArrayList<GameObject>(0);
	private double vx, vy;
	protected String id = "";
	protected Color color = Color.black, baseColor = Color.black;
	protected boolean isActive, hasGravity;
	public boolean shouldRender;
	protected boolean isSolid = true;
	protected boolean isNatural = true;
	protected int shadeLevel = 0;
	public static final int tileSize = 100;
	private static Rectangle bounds = new Rectangle(0, 0, tileSize, tileSize);
	protected Collider collider;
	public Image image;
	public BlockType type;
	public GameObject sourceBlock;
	public Cell cell;
	public double damage, durability = 100;
	public boolean flipRender = false;

	public static ArrayList<GameObject> renderedObjects = new ArrayList<GameObject>();
	public static int startItems = 0;

	public GameObject() {
		this.setBounds(bounds);
		addObject(this);
	}

	public GameObject(int x, int y) {
		this.setBounds(bounds);
		this.x = x;
		this.y = y;
		addObject(this);
	}

	public GameObject(int x, int y, BlockType type) {
		this.setBounds(bounds);
		this.x = x;
		this.y = y;
		this.isSolid = type.isSolid();

		Game.getMap();
		this.color = shadeColor(randomizeColorBrightness(type.color), y / tileSize - Map.getMapHeight() / 2);
		this.baseColor = color;
		this.type = type;
		this.durability = type.durabilty;

		addObject(this);
	}

	private void addObject(GameObject o) {
		objects.add(o);
		updateGrid();
	}

	public static void removeObject(GameObject o) {
		removedObjects.add(o);
		objects.remove(o);
		updateGrid();
	}

	private static void updateGrid() {
		Map g2 = Game.getMap();
		if (g2 != null) {
			g2.grid.update();
		}
	}

	public static void updateLighting() {
		for (int i = 0; i < objects.size(); i++) {
			GameObject o = objects.get(i);
			if (o == null || o.isActive)
				continue;
			// o.color = shadeColor(o.baseColor, o.y / tileSize -
			// Game.getMap().getMapHeight() / 2);
		}
	}

	// public static void updateLighting() {
	// for (int i = 0; i < objects.size(); i++) {
	// GameObject o = objects.get(i);
	// if (o.isActive) {
	// continue;
	// }
	// Grid grid = Game.getMap().getGrid();
	//
	// int maxShade = 1000;
	// for (int xa = -1; xa < 1; xa++) {
	// for (int ya = -1; ya < 1; ya++) {
	// int x = o.x - xa * tileSize;
	// int y = o.y - ya * tileSize;
	//
	// Cell c = grid.getCell(x, y);
	// if (c != null && c.isFull()) {
	// o.shadeLevel = c.getObject().shadeLevel + 1;
	// } else {
	// o.shadeLevel = 0;
	// }
	// }
	// }
	//
	// o.color = shadeColor(o.baseColor, o.shadeLevel);
	// }
	// }

	private static Color shadeColor(Color c, int shadeLevel) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		int clampedShadeLevel = KMath.clamp(shadeLevel * 3, 0, 75);

		r = KMath.clamp(r - clampedShadeLevel, 0, 255);
		g = KMath.clamp(g - clampedShadeLevel, 0, 255);
		b = KMath.clamp(b - clampedShadeLevel, 0, 255);

		return new Color(r, g, b);
	}

	private Color randomizeColorBrightness(Color c) {
		int r = c.getRed();
		int g = c.getGreen();
		int b = c.getBlue();

		Random rand = new Random();
		int offset = rand.nextInt(20) - 10;

		int newR = KMath.clamp(r + offset, 0, 255);
		int newG = KMath.clamp(g + offset, 0, 255);
		int newB = KMath.clamp(b + offset, 0, 255);

		return new Color(newR, newG, newB);

	}

	public static BlockType[] getTypeValues() {
		BlockType[] types = BlockType.values();
		BlockType[] validTypes = new BlockType[types.length - 1];

		int count = 0;
		for (BlockType t : types) {
			if (t != BlockType.Player) {
				validTypes[count] = t;
				count++;
			}
		}
		return validTypes;
	}

	public enum BlockType {
		Ground(GROUND.attributes), Stone(STONE.attributes), Ore(ORE.attributes), Log(LOG.attributes), Leaves(
				LEAVES.attributes), Bedrock(BEDROCK.attributes), Player(255, 0, 0);

		private Color color;
		private boolean isSolid;
		private int collected = startItems;
		private int durabilty = 100;

		BlockType(Attributes a) {
			this.color = a.color;
			this.isSolid = a.isSolid;
			this.durabilty = a.durability;
		}

		BlockType(int r, int g, int b) {
			this(r, g, b, true);
		}

		BlockType(int r, int g, int b, boolean isSolid) {
			this.color = new Color(r, g, b);
			this.isSolid = isSolid;
		}

		public Color getColor() {
			return color;
		}

		public void setColor(Color color) {
			this.color = color;
		}

		public int getCollected() {
			return collected;
		}

		public void setCollected(int collected) {
			this.collected = collected;
		}

		public boolean isSolid() {
			return isSolid;
		}

		public void setSolid(boolean isSolid) {
			this.isSolid = isSolid;
		}

		public int getDurabilty() {
			return durabilty;
		}

		public void setDurabilty(int durabilty) {
			this.durabilty = durabilty;
		}

		public static int getIndex(BlockType t) {
			for (int i = 0; i < BlockType.values().length; i++) {
				if (t == GameObject.BlockType.values()[i]) {
					return i;
				}
			}
			return -1;
		}

	}

	public static GameObject spawnObject(int x, int y, BlockType t, boolean isNatural) {
		Point p = new Point(x, y);
		return spawnObject(p, t, isNatural);
	}

	public static GameObject spawnObject(int x, int y, BlockType t, GameObject source) {
		Point p = new Point(x, y);

		GameObject o = spawnObject(p, t, true);
		if (o != null) {
			o.sourceBlock = source;
		}
		return o;
	}

	public static GameObject spawnObject(int x, int y, BlockType t) {
		Point p = new Point(x, y);
		return spawnObject(p, t, true);
	}

	public static GameObject spawnObject(Point p, BlockType t, boolean isNatural) {
		if (p == null || t == null) {
			return null;
		}

		boolean contains = false;
		Cell[][] grid = Game.getMap().getGrid().getCells();
		for (Cell[] ca : grid) {
			for (Cell c : ca) {
				if (c.contains(p)) {
					contains = true;
					if (!isNatural) {
						if (!c.isFull() && Player.player.removeBlock(1, t)) {
							GameObject o = spawnObject(t, c.getLocation());
							o.isNatural = isNatural;
							return o;
						}
					} else {
						if (!c.isFull()) {
							GameObject o = spawnObject(t, c.getLocation());
							o.isNatural = isNatural;
							return o;
						}
					}
				}
			}
		}
		return null;
	}

	// Only to be utilized by the other spawnObject method
	private static GameObject spawnObject(BlockType t, Point p) {
		if (p == null || t == null) {
			return null;
		}

		return new GameObject(p.x, p.y, t);
	}

	public static void damageObject(Point p) {
		damageObject(getObject(p), Player.damage);
	}

	public static void damageObject(GameObject o) {
		damageObject(o, Player.damage);
	}

	public static void damageObject(GameObject o, int damage) {
		if (o != null) {
			if (o.damage + damage < o.durability) {
				o.damage += damage;
			} else {
				Player.player.addBlock(1, o.type);
				removeObject(o);
			}
		}
	}

	public static void removeObject(Point p) {
		for (int i = 0; i < objects.size(); i++) {
			if (objects.get(i).contains(p) && !objects.get(i).isActive) {
				removeObject(objects.get(i));
			}
		}
	}

	public static GameObject getObject(Point p) {
		for (int i = 0; i < objects.size(); i++) {
			GameObject o = objects.get(i);
			if (o != null && o.contains(p) && !o.isActive) {
				return o;
			}
		}
		return null;
	}

	public static void makeActive(GameObject o) {
		o.isActive = true;
		o.hasGravity = true;
		o.collider = new Collider(o);
	}

	public static void makeActive(GameObject o, boolean hasGravity) {
		makeActive(o);
		o.hasGravity = hasGravity;
	}

	public Rectangle nextLocation() {
		Rectangle r = new Rectangle(getNextX(), getNextY(), width, height);
		return r;
	}

	public int getNextX() {
		return (int) (x + vx);
	}

	public int getNextY() {
		return (int) (y - vy);
	}

	public void addForce(double[] force) {
		vx += force[0];
		vy += force[1];
	}

	public void addForce(double xa, double ya) {
		vx += xa;
		vy += ya;
	}

	public void vMove() {
		this.x += vx;
		this.y -= vy;

		if (isActive) {
			collider.adjustSensors();
		}
	}

	public double[] getVelocity() {
		double[] velocity = new double[2];
		velocity[0] = vx;
		velocity[1] = vy;
		return velocity;
	}

	public static ArrayList<GameObject> getObjects() {
		return objects;
	}

	public static void setObjects(ArrayList<GameObject> objects) {
		GameObject.objects = objects;
	}

	public void setX(double x) {
		this.x = (int) x;
	}

	public void setY(double y) {
		this.y = (int) y;
	}

	public double getVx() {
		return vx;
	}

	public void setVx(double vx) {
		this.vx = vx;
	}

	public double getVy() {
		return vy;
	}

	public void setVy(double vy) {
		this.vy = vy;
	}

	public boolean isActive() {
		return isActive;
	}

	public void setActive(boolean isActive) {
		this.isActive = isActive;
	}

	public boolean hasGravity() {
		return hasGravity;
	}

	public void setHasGravity(boolean hasGravity) {
		this.hasGravity = hasGravity;
	}

	public Collider getCollider() {
		return collider;
	}

	public void setCollider(Collider collider) {
		this.collider = collider;
	}

	public static int getTilesize() {
		return tileSize;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	public BlockType getType() {
		return type;
	}

	public void setType(BlockType type) {
		this.type = type;
	}

	public static int numObjects() {
		return objects.size();
	}

	public Color getBaseColor() {
		return baseColor;
	}

	public void setBaseColor(Color baseColor) {
		this.baseColor = baseColor;
	}

	public boolean isSolid() {
		return isSolid;
	}

	public void setSolid(boolean isSolid) {
		this.isSolid = isSolid;
	}

}
