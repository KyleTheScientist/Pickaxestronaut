package level;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import fx.GameObject.BlockType;
import level.Map.Layer;

public class ProbabilitySetter implements Runnable {

	public FileReader unbufferedReader;
	public BufferedReader reader;

	public String file = "C:/Users/Kylw/AppData/Roaming/build/probabilities.txt";

	public void run() {
		try {
			unbufferedReader = new FileReader(file);
			reader = new BufferedReader(unbufferedReader);

			String line;
			while ((line = reader.readLine()) != null) {
				for (BlockType type : BlockType.values()) {
					if (line.contains(type.toString() + "{")) {
						for (Layer layer : Layer.values()) {
							String nLine;
							if ((nLine = reader.readLine()).contains("}")) {
								break;
							}
							if (nLine.contains(layer.toString() + ":")) {
								int startIndex = nLine.indexOf(':') + 1;
								int endIndex = nLine.indexOf(';');
								String probString = nLine.substring(startIndex, endIndex);
								probString = probString.trim();
								double probability = Double.parseDouble(probString);
								layer.addToPossibleBlocks(type, probability);
							}
						}
					}
				}
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
