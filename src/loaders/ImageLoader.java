package loaders;

import java.awt.Image;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

final public class ImageLoader {

	public ImageLoader() {

	}

	public static Image load(String path) {
		Image img = null;
		if (path.charAt(0) != '/') {
			path = "/" + path;
		}
		try {
			img = new ImageIcon(ImageIO.read(ImageLoader.class.getResourceAsStream(path))).getImage();
		} catch (IOException e) {
		}

		return img;

	}

}
