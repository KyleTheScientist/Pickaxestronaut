package fx;

public class Collider {

	Sensor[] sensors;
	private Sensor left, up, right, down;
	private GameObject parent;

	public Collider(GameObject parent) {
		this.parent = parent;
		sensors = new Sensor[4];
		generateSensors();
		adjustSensors();
	}

	public void update() {
		for (Sensor s : sensors) {
			GameObject collision = null;
			for (int i = 0; i < GameObject.numObjects(); i++) {
				GameObject o = GameObject.getObjects().get(i);
				if (o != null && o != parent && o.isSolid && o.intersects(s)) {
					collision = o;
				}
			}
			s.setCollision(collision);
		}
	}

	private void generateSensors() {
		left = new Sensor(this, parent, 0);
		up = new Sensor(this, parent, 1);
		right = new Sensor(this, parent, 0);
		down = new Sensor(this, parent, 1);
		sensors[0] = left;
		sensors[1] = up;
		sensors[2] = right;
		sensors[3] = down;
	}

	public void adjustSensors() {
		left.setLocation(parent.x - left.width, parent.y + Sensor.sheilding / 2);
		right.setLocation(parent.x + parent.width, parent.y + Sensor.sheilding / 2);
		up.setLocation(parent.x + Sensor.sheilding / 2, parent.y - up.height);
		down.setLocation(parent.x + Sensor.sheilding / 2, parent.y + parent.height);
	}

	public boolean collided(Sensor direction) {
		if (direction.hasCollided()) {
			if (direction.getCollision().intersects(parent.nextLocation())) {
				return true;
			}
		}
		return false;
	}

	public Sensor[] getSensors() {
		return sensors;
	}

	public void setSensors(Sensor[] sensors) {
		this.sensors = sensors;
	}

	public Sensor getLeft() {
		return left;
	}

	public void setLeft(Sensor left) {
		this.left = left;
	}

	public Sensor getUp() {
		return up;
	}

	public void setUp(Sensor up) {
		this.up = up;
	}

	public Sensor getRight() {
		return right;
	}

	public void setRight(Sensor right) {
		this.right = right;
	}

	public Sensor getDown() {
		return down;
	}

	public void setDown(Sensor down) {
		this.down = down;
	}

	public GameObject getParent() {
		return parent;
	}

	public void setParent(GameObject parent) {
		this.parent = parent;
	}

}
