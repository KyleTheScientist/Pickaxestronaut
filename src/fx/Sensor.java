package fx;

import java.awt.Rectangle;

public class Sensor extends Rectangle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3131922931878492270L;
	private GameObject collision;
	private Collider collider;
	public static final int sheilding = 5 * 2;

	public Sensor(Collider collider, GameObject parent, int orientation) {
		if (orientation == 0) {
			// vert
			this.width = parent.width / 4;
			this.height = parent.height - sheilding;
		} else {
			// horiz
			this.width = parent.width - sheilding;
			this.height = parent.height / 4;
		}
		this.collider = collider;
	}

	public boolean hasCollided() {
		return (collision != null && collision.isSolid);
	}

	public GameObject getCollision() {
		return collision;
	}

	public void setCollision(GameObject collision) {
		this.collision = collision;
	}

	public Collider getCollider() {
		return collider;
	}

	public void setCollider(Collider collider) {
		this.collider = collider;
	}

}
