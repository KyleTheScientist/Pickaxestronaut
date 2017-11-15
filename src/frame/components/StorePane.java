package frame.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.RenderingHints;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;

import entity.Player;
import fx.GameObject;
import loaders.ImageLoader;

public class StorePane extends JPanel {
	private static final long serialVersionUID = -1360936880197091374L;
	private int size = GameObject.tileSize;
	Font buttonFont = new Font("Impact", GameObject.tileSize / 2, GameObject.tileSize / 2);
	private StoreButton miningSpeed, jumps;
	
	Color paneColor = HUD.paneColor;
	Font hotbarFont = HUD.hotbarFont;

	private JPanel barPanel, storePanel;

	public StorePane(Dimension d) {
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		setVisible(false);
		setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setOpaque(false);
		setupPanels(d);
	}

	private void setupPanels(Dimension d) {
		int hotbarWidth = size + size / 2;
		int hotbarHeight = (int) (size + size / 4);

		barPanel = new JPanel();
		barPanel.setLayout(new GridBagLayout());
		barPanel.setOpaque(false);
		
		storePanel = new JPanel();
		setupStore(d);

		barPanel.setOpaque(false);
		storePanel.setOpaque(false);

		add(barPanel);
		add(storePanel);
	}

	public void toggle() {
		if (isVisible()) {
			setVisible(false);
		} else {
			update();
			setVisible(true);
		}
	}

	public void update() {
		miningSpeed.value = Player.damage;
		jumps.value = Player.maxJumps;
	}

	private void setupStore(Dimension d) {
		storePanel.setLayout(new GridLayout(6, 1, 10, 10));
		Dimension bd = new Dimension(d.width, d.height / 4);

		miningSpeed = new StoreButton(bd, ImageLoader.load("res/icons/pickaxe.png"));
		miningSpeed.kText = "Upgrade Mining Speed";
		miningSpeed.addActionListener(e -> Player.upgrade(Player.MINING_SPEED));
		storePanel.add(miningSpeed);

		jumps = new StoreButton(bd, ImageLoader.load("res/icons/jump.png"));
		jumps.kText = "Upgrade Jumps";
		jumps.addActionListener(e -> Player.upgrade(Player.MAX_JUMPS));
		storePanel.add(jumps);
	}

	public class StoreButton extends JButton {

		private static final long serialVersionUID = -1107255902281260535L;
		private Image image;
		private String kText;
		private int value;
		int imageSize;

		public StoreButton(Dimension d, Image image) {
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
			requestFocusInWindow();

			setFocusPainted(false);
			setBorderPainted(false);
			setOpaque(false);
			setBackground(HUD.paneColor);

			this.image = image;
		}

		public void paint(Graphics g) {
			
			imageSize = getHeight() / 2;
			// super.paintComponent(g);
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			if (getModel().isPressed()) {
				g2.setColor(HUD.paneColor.brighter());
			} else {
				g2.setColor(HUD.paneColor);
			}

			g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);
			g2.setColor(Color.white);
			g2.setFont(new Font("Impact", 20, getHeight() / 3));
			FontMetrics f = g2.getFontMetrics();
			int height = f.getAscent();
			int xPadText = 20;
			g2.drawString(kText + ": " + value, xPadText, getHeight() / 2 + height / 2);
			int xPad = -10;
			g2.drawImage(image, getWidth() - imageSize + xPad, getHeight() / 2 - imageSize/2, imageSize, imageSize,
					null);
		}

		public Image getImage() {
			return image;
		}

		public void setImage(Image image) {
			this.image = image;
		}

		public String getText() {
			return kText;
		}

		public void setText(String text) {
			this.kText = text;
		}
		
		public void setValue(int value) {
			this.value = value;
		}
	}
}
