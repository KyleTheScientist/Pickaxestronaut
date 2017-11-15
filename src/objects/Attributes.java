package objects;

import java.awt.Color;

public class Attributes {

	public Color color;
	public boolean isSolid;
	public int durability;

	public Attributes(Color color, boolean isSolid, int durability) {
		this.color = color;
		this.isSolid = isSolid;
		this.durability = durability;
	}

}