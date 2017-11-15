package level;

import java.util.ArrayList;
import java.util.Random;

import entity.Player;
import frame.Game;
import frame.TimeCycle;
import frame.components.LoadingScreen;
import fx.GameObject;
import fx.GameObject.BlockType;
import objects.Tree;

public class Map {

	int size = GameObject.tileSize;
	private static final int mapWidth = 100;
	private static final int mapHeight = 100;
	private int absMapWidth = mapWidth * size, absMapHeight = mapHeight * size;
	public TimeCycle lightCycle = new TimeCycle(1000);
	public Grid grid;
	public LoadingScreen loadingScreen;

	private Random rand = new Random();
	int seed = 0;
	public boolean loaded = false;
	private long taskStartTime = -1;

	public Map() {
		grid = new Grid(mapWidth, mapHeight);
		new Player(absMapWidth / 2, absMapHeight / 2 - 10 * size);
	}

	public void generateWorld() {
		ProbabilitySetter ps = new ProbabilitySetter();
		ps.run();
		loadingScreen = Game.loadingScreen;

		generateSeed();
		GameObject[] terrain = generatePerlinTerrain();
		genVegetation(terrain);
		GameObject[] dirt = genDirt(terrain);
		genStone(dirt);
		genBedrock();
		loaded = true;
		loadingScreen.switchDisplays();
		lightCycle.start();
	}

	public void generateSeed() {
		int seedInt = 0;
		int digit = 1;
		for (int i = 1; i < 7; i++) {
			seedInt += rand.nextInt(10) * digit;
			digit *= 10;
		}

		seed = seedInt;
		loadingScreen.setProgress(1);
	}

	private GameObject[] generatePerlinTerrain() {
		recordTime();
		loadingScreen.setCurrentTaskString("(Defining Terrain...)");
		GameObject[] terrainDefinition = new GameObject[mapWidth];

		for (int i = 0; i < mapWidth; i++) {
			int yOffset = (int) (Noise.perlinNoise(i + seed, .4, 3, 3) * 100) / size;
			terrainDefinition[i] = GameObject.spawnObject(i * size, (Layer.SkyLayer.yLevel + yOffset) * size,
					BlockType.Ground, true);

		}
		loadingScreen.setProgress(2);
		recordTime();
		return terrainDefinition;

	}

	private void genVegetation(GameObject[] terrainDefinition) {
		loadingScreen.setCurrentTaskString("(Growing Trees...)");
		recordTime();
		for (GameObject o : terrainDefinition) {
			if (rand.nextInt(10) < 2) {
				Tree.spawnTree(o.x, o.y - size);
			}
		}
		loadingScreen.setProgress(5);
		recordTime();
	}

	private GameObject[] genDirt(GameObject[] terrainDefinition) {
		loadingScreen.setCurrentTaskString("(Filling in dirt...)");
		recordTime();
		GameObject[] bottomDirt = new GameObject[terrainDefinition.length];

		for (int i = 0; i < terrainDefinition.length; i++) {
			GameObject top = terrainDefinition[i];
			int topHeight = top.y / size;
			int height = (int) Math.abs((Noise.perlinNoise(i, .3, 1.5, 3) * 15)) + 20;

			for (int j = 1; j < height; j++) {
				GameObject dirt = GameObject.spawnObject(top.x, (j + topHeight) * size, pickBlockType(Layer.DirtLayer));
				if (dirt != null) {
					bottomDirt[i] = dirt;
				}
			}
			int progress = (int) ((double) i / mapWidth * 45) + 1;
			loadingScreen.setProgress(5 + progress);

		}
		recordTime();
		return bottomDirt;
	}

	private void genStone(GameObject[] bottomDirt) {
		recordTime();
		loadingScreen.setCurrentTaskString("(Filling in stone...)");
		for (int i = 0; i < bottomDirt.length; i++) {
			GameObject top = bottomDirt[i];
			int topHeight = top.y / size;
			int height = Layer.StoneLayer.yLevel - topHeight;

			for (int j = 1; j < height; j++) {
				GameObject.spawnObject(top.x, (j + topHeight) * size, pickBlockType(Layer.StoneLayer));
			}
			int progress = (int) ((double) i / mapWidth * 40) + 1;
			loadingScreen.setProgress(50 + progress);
		}

		loadingScreen.setProgress(90);
		recordTime();
	}

	private void genBedrock() {
		recordTime();
		loadingScreen.setCurrentTaskString("(Protecting you from the void...)");
		int height = Layer.BedrockLayer.yLevel - Layer.StoneLayer.yLevel;
		for (int i = 0; i < mapWidth; i++) {
			for (int j = 0; j < height; j++) {
				GameObject.spawnObject(i * size, (j + Layer.StoneLayer.yLevel) * size, BlockType.Bedrock);
			}
			int progress = (int) ((double) i / mapWidth * 10) + 1;
			loadingScreen.setProgress(90 + progress);
		}
		recordTime();
	}

	private void recordTime() {
		if (taskStartTime < 0) {
			taskStartTime = System.currentTimeMillis();
		} else {
			long time = System.currentTimeMillis() - taskStartTime;
			String timeMS = convertToMS(time);
			System.out.println("-Task completed in " + timeMS);
			taskStartTime = -1;
		}
	}

	private String convertToMS(long millis) {
		int totalSecs = (int) (millis / 1000);
		int minutes = totalSecs / 60;
		int seconds = totalSecs % 60;

		String MS;

		if (seconds < 10) {
			MS = minutes + ":0" + seconds;
		} else {
			MS = minutes + ":" + seconds;
		}
		return MS;
	}

	private BlockType pickBlockType(Layer layer) {
		double r = rand.nextDouble() * 100;
		double sum = 0;
		for (int i = 0; i < layer.probabilities.size(); i++) {
			sum += layer.probabilities.get(i);
			if (r < sum) {
				return layer.possibleBlocks.get(i);
			}
		}

		return null;
	}

	public enum Layer {

		SkyLayer(mapHeight / 2), DirtLayer(mapHeight * 3 / 4), StoneLayer(mapHeight - 3), BedrockLayer(mapHeight);

		public ArrayList<BlockType> possibleBlocks = new ArrayList<BlockType>();
		public ArrayList<Double> probabilities = new ArrayList<Double>();
		public int yLevel;

		Layer(int yLevel) {
			this.yLevel = yLevel;
		}

		public void addToPossibleBlocks(BlockType blocktype, double probability) {
			possibleBlocks.add(blocktype);
			probabilities.add(probability);
		}
	}

	public int getAbsMapWidth() {
		return absMapWidth;
	}

	public void setAbsMapWidth(int absMapWidth) {
		this.absMapWidth = absMapWidth;
	}

	public int getAbsMapHeight() {
		return absMapHeight;
	}

	public void setAbsMapHeight(int absMapHeight) {
		this.absMapHeight = absMapHeight;
	}

	public Grid getGrid() {
		return grid;
	}

	public void setGrid(Grid grid) {
		this.grid = grid;
	}

	public static int getMapWidth() {
		return mapWidth;
	}

	public static int getMapHeight() {
		return mapHeight;
	}

}
