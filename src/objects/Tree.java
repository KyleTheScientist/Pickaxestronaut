package objects;

import java.awt.Point;
import java.util.Random;

import frame.Game;
import fx.GameObject;
import level.Cell;
import level.Grid;

public class Tree {

	protected GameObject[] logs;
	protected LEAVES[] leaves;

	public Tree(int height) {
		logs = new LOG[height];
	}

	public static boolean spawnTree(int x, int y) {
		Random r = new Random();
		int height = r.nextInt(7) + 3;
		int size = GameObject.tileSize;
		Grid grid = Game.getMap().getGrid();

		Tree tree = new Tree(height);
		// check for room
		for (int i = 0; i < height; i++) {
			if (Game.getMap().getGrid().getCell(x, y - i * size) == null
					|| Game.getMap().getGrid().getCell(x, y - i * size).isFull()) {
				return false;
			}
		}
		// check for ground
		Cell gCell = grid.getCell(x, y + size);
		if (!gCell.isFull()) {
			return false;
		}

		if (gCell.getObject().isActive() || !gCell.getObject().isSolid()) {
			return false;
		}

		// create logs
		for (int i = 0; i < height - 1; i++) {
			GameObject log = GameObject.spawnObject(x, y - i * size, GameObject.BlockType.Log, true);
			log.sourceBlock = grid.getCell(x, y - i * size + size).getObject();
		}
		GameObject sourceBlock = GameObject.spawnObject(x, y - (height - 1) * size, GameObject.BlockType.Log, true);
		sourceBlock.sourceBlock = grid.getCell(x, y - (height - 1) * size + size).getObject();

		// create leaves
		for (int i = -1; i <= 1; i++) {
			for (int j = -1; j <= 1; j++) {
				GameObject.spawnObject(x + i * size, (y - size * (height)) + j * size, GameObject.BlockType.Leaves,
						sourceBlock);
			}
		}
		GameObject.spawnObject(x + (2 * size), y - (size * (height)), GameObject.BlockType.Leaves, sourceBlock);
		GameObject.spawnObject(x - (2 * size), y - (size * (height)), GameObject.BlockType.Leaves, sourceBlock);
		GameObject.spawnObject(x, y - (size * (height + 2)), GameObject.BlockType.Leaves, sourceBlock);
		return true;
	}

	public static void spawnTree(Point p) {
		spawnTree(p.x, p.y);
	}
}
