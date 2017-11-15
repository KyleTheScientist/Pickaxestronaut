package entity;

import frame.InputListener;
import fx.Collider;
import fx.GameObject;
import loaders.ImageLoader;

public class Player extends GameObject {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5434279776869058316L;
	public static Player player;
	private static final double ACCEL = 3.0;
	private static final double MOVE_SPEED = 20.0;
	private static final double JUMP_FORCE = 20.0;
	public static final int MINING_SPEED = 0;
	public static final int MAX_JUMPS = 1;
	public static int maxJumps = (2) - 1;
	public GameObject.BlockType itemType = GameObject.BlockType.Ground;
	public static int damage = 1;
	public int jumps = 1;
	long lastJump = 0;
	public boolean isOnGround;

	Animation run = new Animation("res/char/run", 80);
	Animation idle = new Animation("res/char/idle", 80);

	public Player(int x, int y) {
		this.type = BlockType.Player;
		this.color = type.getColor();
		this.x = x;
		this.y = y;
		this.width *= 2;
		this.height *= 2;
		GameObject.makeActive(this);
		player = this;
		id = "Player";
		hasGravity = true;
		idle.start();
		run.start();
	}

	public static void upgrade(int value) {
		switch (value) {
		case (MAX_JUMPS):
			maxJumps++;
			break;
		case (MINING_SPEED):
			damage++;
			break;
		}
	}

	public void cycleItem(int direction) {
		if (Math.abs(direction) != 1) {
			System.out.println("Item cycle direction error");
			return;
		}

		int arrayLength = BlockType.values().length;
		int originalValue = BlockType.getIndex(itemType);

		do {
			if (BlockType.getIndex(itemType) + direction < 0) {
				itemType = BlockType.values()[arrayLength - 1];
			} else if (BlockType.getIndex(itemType) + direction >= arrayLength) {
				itemType = BlockType.values()[0];
			} else {
				itemType = BlockType.values()[BlockType.getIndex(itemType) + direction];
			}

			if (BlockType.getIndex(itemType) == originalValue) {
				itemType = BlockType.values()[originalValue];
				return;
			}
		} while (itemType.getCollected() < 1);

		if (itemType.equals(BlockType.Player)) {
			cycleItem(direction);
		}
	}

	public void addBlock(int i, BlockType t) {
		t.setCollected(t.getCollected() + i);
	}

	public boolean removeBlock(int i, BlockType t) {
		if (t.getCollected() > 0) {
			t.setCollected(t.getCollected() - i);
			return true;
		}
		return false;
	}

	public static void update() {
		isOnGround();
		handleHorizontalMovement();
		handleAnimation();
		handleJumps();
	}

	private static void isOnGround() {
		Collider c = player.getCollider();
		if (c.getDown().hasCollided()) {
			player.isOnGround = true;
		} else {
			player.isOnGround = false;
		}
	}

	private static void handleAnimation() {
		if (player.getVx() > 0) {
			player.flipRender = false;
		} else if(player.getVx() < 0){
			player.flipRender = true;
		}
		
		if (player.isOnGround) {
			if (Math.abs(player.getVx()) > 0) {
				player.image = player.run.getFrame();
			} else {
				player.image = player.idle.getFrame();
			}
		}else{
			player.image = ImageLoader.load("res/char/run/run_003.png");
		}
	}

	private static void handleJumps() {
		Collider c = player.getCollider();
		if (c.getDown().hasCollided()) {
			player.jumps = maxJumps;
		}
		if (InputListener.up.isPressed && player.jumps > 0 && System.currentTimeMillis() - player.lastJump > 300) {
			player.jumps--;
			player.lastJump = System.currentTimeMillis();
			player.setVy(0);
			player.addForce(0, JUMP_FORCE);
		}
	}

	private static void handleHorizontalMovement() {
		if (InputListener.left.isPressed) {
			player.addForce(-ACCEL, 0);
		}
		if (InputListener.right.isPressed) {
			player.addForce(ACCEL, 0);
		}
		if (!InputListener.left.isPressed && !InputListener.right.isPressed) {
			if (player.getVx() > 0) {
				player.addForce(-ACCEL, 0);
			} else if (player.getVx() < 0) {
				player.addForce(ACCEL, 0);
			}
			if (Math.abs(player.getVx()) < ACCEL) {
				player.setVx(0);
			}
		}
		if (Math.abs(player.getVx()) > MOVE_SPEED) {
			if (player.getVx() < 0) {
				player.setVx(-MOVE_SPEED);
			} else {
				player.setVx(MOVE_SPEED);
			}
		}
	}

	public static Player getPlayer() {
		return player;
	}

	public static void setPlayer(Player player) {
		Player.player = player;
	}

	public GameObject.BlockType getItemType() {
		return itemType;
	}

}
