package frame.components;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

import loaders.ImageLoader;

public class Background extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 6470810450332026741L;
	private Image image = ImageLoader.load("res/icons/background.png");
	
	public Background(Dimension d) {
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		requestFocusInWindow();
	}
	
	public void paintComponent(Graphics g){
		super.paintComponent(g);
		g.drawImage(image, 0, 0, this.getWidth(), this.getHeight(), this);
	}

}
