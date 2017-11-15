package level;

import java.awt.Rectangle;
import java.util.ArrayList;

import fx.GameObject;

public class Cell extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1991544766683800007L;
	private boolean isFull;
	private int[] index;
	private GameObject object;

	public Cell(int x, int y, int size) {
		this.x = x;
		this.y = y;
		this.width = size;
		this.height = size;
	}

	public int[] getIndex() {
		return index;
	}

	public void setIndex(int x, int y) {
		int[] index = { x, y };
		this.setIndex(index);
	}

	public void setIndex(int[] index) {
		this.index = index;
	}

	public GameObject getObject() {
		ArrayList<GameObject> objects;
		objects = GameObject.objects;

		for (int i = 0; i < objects.size(); i++) {
			if (this.intersects(objects.get(i))) {
				return objects.get(i);
			}
		}
		return null;
	}

	public boolean isFull() {
		ArrayList<GameObject> objects;
		objects = GameObject.objects;

		for (int i = 0; i < objects.size(); i++) {
			if (this.intersects(objects.get(i))) {
				return true;
			}
		}
		return false;
	}

	public void setFull(boolean isFull) {
		this.isFull = isFull;
	}

	public void setObject(GameObject object) {
		this.object = object;
	}
	
}
