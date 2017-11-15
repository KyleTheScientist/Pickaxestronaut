package frame.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;

import frame.Game;

public class LoadingScreen extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5147359416035475771L;
	public int progress;
	public String currentTask = "";
	JComponent gameScreen;
	Font progressFont = new Font("Impact", 30, 75);

	public LoadingScreen(Dimension d, JComponent gameScreen) {
		this.gameScreen = gameScreen;
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setBackground(Display.display.getBackground());
	}

	public void switchDisplays() {
		JFrame frame = Game.getFrame();
		frame.remove(this);
		frame.add(gameScreen);
		frame.pack();
	}

	public void paintComponent(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		super.paintComponent(g);
		int barWidth = getWidth() / 2, barHeight = getHeight() / 12;
		int barX = getWidth() / 2 - barWidth / 2, barY = getHeight() / 2 - barHeight / 2;
		g2.setColor(Color.white);
		g2.setFont(progressFont);
		g2.drawRect(barX, barY, barWidth, barHeight);
		g2.fillRect(barX, barY, barWidth * progress / 100, barHeight);

		String progressString = "Progress..." + progress + "%";
		FontMetrics f = g.getFontMetrics();
		int sHeight = f.getAscent();
		int proStringWidth = f.stringWidth(progressString);
		int taskStringWidth = f.stringWidth(currentTask);

		g2.drawString(progressString, getWidth() / 2 - proStringWidth / 2, barY + barHeight + sHeight);
		g2.drawString(currentTask, getWidth() / 2 - taskStringWidth / 2, barY + barHeight + sHeight * 2);

	}

	public void setCurrentTaskString(String task){
		this.currentTask = task;
	}
	
	public void setProgress(int progress) {
		this.progress = progress;
		repaint();
	}
}
