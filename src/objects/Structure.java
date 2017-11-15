package objects;

import fx.GameObject;
import fx.GameObject.BlockType;

public class Structure {

	private static int size = GameObject.tileSize;

	public static void spawnStructure(int x, int y, BlockType[][] array) {
		for (int i = 0; i < array.length; i++) {
			for (int j = 0; j < array[i].length; j++) {
				GameObject.spawnObject(x + size * j, y + size * (i - 3), array[i][j]);
			}
		}
	}

	public static void spawnGroundCluster(int x, int y, BlockType t) {
		BlockType[][] cluster = { { null, null, t, null }, { null, t, t, t }, { t, t, t, t, null } };
		spawnStructure(x, y, cluster);
	}
	
	public static void spawnCluster(int x, int y, BlockType t) {
		BlockType[][] cluster = { { null, t, null }, { t, t, t }, { null, t, null} };
		spawnStructure(x, y, cluster);
	}

}
