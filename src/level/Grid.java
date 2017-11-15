package level;

import java.awt.Point;
import java.util.ArrayList;

import fx.GameObject;

public class Grid {

	private final int size = GameObject.tileSize;
	private Cell[][] cells;

	public Grid(int width, int height) {

		cells = new Cell[width][height];
		for (int i = 0; i < cells.length; i++) {
			for (int j = 0; j < cells[i].length; j++) {
				cells[i][j] = new Cell(i * size, j * size, size);
				cells[i][j].setIndex(i, j);
			}
		}

	}

	public void update() {
		// ArrayList<GameObject> objects = GameObject.objects;
		//
		// for (int g = 0; g < objects.size(); g++) {
		// GameObject o = objects.get(g);
		// if (o.isActive()) {
		// continue;
		// }
		// for (int i = 0; i < cells.length; i++) {
		// for (int j = 0; j < cells[i].length; j++) {
		// cells[i][j].setObject(null);
		// if (cells[i][j].intersects(o)) {
		// cells[i][j].setObject(o);
		// }
		// }
		// }
		// }
	}

	public ArrayList<Cell> getNeighbors(Cell c) {
		int x = c.getIndex()[0], y = c.getIndex()[1];
		ArrayList<Cell> neighbors = new ArrayList<Cell>(0);

		if (x > 0)
			neighbors.add(getLeft(c));
		if (x < cells.length - 1)
			neighbors.add(getRight(c));
		if (y > 0)
			neighbors.add(getUp(c));
		if (y < cells[x].length - 1)
			neighbors.add(getDown(c));

		return neighbors;
	}

	public Cell getLeft(Cell c) {
		int x = c.getIndex()[0], y = c.getIndex()[1];
		return cells[x - 1][y];
	}

	public Cell getRight(Cell c) {
		int x = c.getIndex()[0], y = c.getIndex()[1];
		return cells[x + 1][y];
	}

	public Cell getUp(Cell c) {
		int x = c.getIndex()[0], y = c.getIndex()[1];
		return cells[x][y - 1];
	}

	public Cell getDown(Cell c) {
		int x = c.getIndex()[0], y = c.getIndex()[1];
		return cells[x][y + 1];
	}

	public Cell[][] getCells() {
		return cells;
	}

	public void setCells(Cell[][] cells) {
		this.cells = cells;
	}

	public Cell getCell(int x, int y) {
		for (Cell[] ca : cells) {
			for (Cell c : ca) {
				if (c.contains(new Point(x, y))) {
					return c;
				}
			}
		}
		return null;
	}

}
