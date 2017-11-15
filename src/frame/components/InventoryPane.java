package frame.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;

import entity.Player;
import fx.GameObject;
import fx.GameObject.BlockType;
import loaders.ImageLoader;

public class InventoryPane extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 4919866708597155244L;
	private int size = GameObject.tileSize;
	public Font hotbarFont = new Font("Impact", size / 3, size / 3);
	Font buttonFont = new Font("Impact", GameObject.tileSize / 4, GameObject.tileSize / 4);
	Color buttonColor = new Color(100, 100, 100);
	InventorySlot[][] slots = new InventorySlot[4][4];
	private Image inventorySlot = ImageLoader.load("res/icons/inventorySlot.png");
	int hotbarWidth = size + size / 2;
	int hotbarHeight = (int) (size + size / 4);
	JPanel inventory;
	Hotbar hotbar;

	public InventoryPane(Dimension d) {
		// setPreferredSize(d);
		// setMinimumSize(d);
		// setMaximumSize(d);
		requestFocusInWindow();
		setVisible(true);
		setLayout(new GridLayout(4, 4, 10, 10));
		setBackground(new Color(80, 80, 80));
		setOpaque(true);
		setup(d);
		update();
	}

	private void setup(Dimension d) {
		hotbar = new Hotbar(new Dimension(hotbarWidth, hotbarHeight));
		// setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
		setLayout(new GridBagLayout());
		inventory = new JPanel();
		Dimension iDim = new Dimension(d.width/2, d.height/2);
		inventory.setPreferredSize(iDim);
		inventory.setMinimumSize(iDim);
		inventory.setMaximumSize(iDim);
		requestFocusInWindow();
		inventory.setOpaque(false);
		inventory.setVisible(false);
		setupSlots(d, inventory);

		GridBagConstraints rightC = new GridBagConstraints();
		rightC.fill = GridBagConstraints.BOTH;
		// hotbar
		rightC.gridy = 0;
		add(hotbar, rightC);
		// inventory

		rightC.gridy = 1;
		rightC.weightx = 1;
		rightC.weighty = 2;
		add(inventory, rightC);

	}

	private void setupSlots(Dimension d, JPanel panel) {
		panel.setLayout(new GridBagLayout());
		GridBagConstraints btnC = new GridBagConstraints();
		btnC.fill = GridBagConstraints.BOTH;
		btnC.insets = new Insets(5, 5, 5, 5);
		int w = d.width / slots.length;
		for (int i = 0; i < slots.length; i++) {
			int h = d.width / slots[i].length;
			for (int j = 0; j < slots[i].length; j++) {
				slots[i][j] = new InventorySlot();
				btnC.gridx = j;
				btnC.gridy = i  ;
				btnC.weightx = 1;
				btnC.weighty = 1;
				panel.add(slots[i][j], btnC);
			}
		}
	}

	public void toggle() {
		if (inventory.isVisible()) {
			inventory.setVisible(false);
		} else {
			inventory.setVisible(true);
		}
	}

	public void update() {
		BlockType[] types = BlockType.values();
		clearSlots();
		ArrayList<InventorySlot> newSlots = new ArrayList<InventorySlot>();
		for (InventorySlot[] sa : slots) {
			for (InventorySlot s : sa) {
				newSlots.add(s);
			}
		}
		InventorySlot[] slots1D = newSlots.toArray(new InventorySlot[newSlots.size()]);
		for (BlockType t : types) {
			if (t.getCollected() > 0 && t != BlockType.Player) {
				for (InventorySlot s : slots1D) {
					if (s.getType() == null) {
						s.setType(t);
						break;
					}
				}
			}
		}
	}

	private void clearSlots() {
		for (int i = 0; i < slots.length; i++) {
			for (int j = 0; j < slots.length; j++) {
				slots[i][j].setType(null);
			}
		}
	}

	public class InventorySlot extends JButton {

		private static final long serialVersionUID = 225791121261019130L;
		private BlockType type;
		private static final int itemSize = (int) (GameObject.tileSize /2f);

		public InventorySlot() {
			setFocusPainted(false);
			setBorder(BorderFactory.createLineBorder(Color.black));
			setBorderPainted(false);
			setOpaque(false);
			addActionListener(e -> {
				if (type != null) {
					Player.player.itemType = type;
				}
			});
		}

		public void paint(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;
			g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
			// super.paint(g);
			if (getModel().isPressed()) {
				g2.setColor(HUD.paneColor.darker());
			} else if (getModel().isRollover()) {
				g2.setColor(HUD.paneColor.brighter());
			} else {
				g2.setColor(HUD.paneColor);
			}
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), 20, 20);

			if (type != null) {
				Rectangle item = new Rectangle(getWidth() / 2 - itemSize / 2, getHeight() / 2 - itemSize / 2, itemSize,
						itemSize);
				g2.setColor(type.getColor());
				g2.fill(item);
				g2.setFont(buttonFont);
				g2.setColor(Color.white);
				// g2.drawString("x" + type.getCollected(), item.x - item.width
				// / 2, item.y + item.height);
				FontMetrics f = g2.getFontMetrics();
				String txt = "x" + type.getCollected();
				int l = f.stringWidth(txt);
				int xOff = -5, yOff = 5;
				g2.drawString("x" + type.getCollected(), item.x + item.width - l + xOff,
						item.y + f.getAscent() - f.getDescent() + yOff);
			}
		}

		public BlockType getType() {
			return type;
		}

		public void setType(BlockType type) {
			this.type = type;
		}
	}

	public class Hotbar extends JPanel {

		/**
		 * 
		 */
		private static final long serialVersionUID = 8468691750900419653L;

		public Hotbar(Dimension d) {
			setPreferredSize(d);
			setMinimumSize(d);
			setMaximumSize(d);
		}

		public void paintComponent(Graphics g) {
			Graphics2D g2 = (Graphics2D) g;

			Rectangle item = new Rectangle(getWidth() / 2 - size / 2, size / 8, size, size);

			g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
			g2.setColor(HUD.paneColor);
			g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);

			g2.setColor(Player.player.getItemType().getColor());
			g2.fill(item);
			g2.drawImage(inventorySlot, item.x, item.y, item.width, item.height, null);

			g2.setColor(new Color(255, 255, 255));
			g2.setFont(hotbarFont);
			String s = Player.player.getItemType().getCollected() + "x";
			FontMetrics f = g.getFontMetrics();
			int sWidth = f.stringWidth(s);
			int sHeight = f.getAscent();
			int trueStringHeight = f.getAscent() - f.getDescent();
			int stringWidth = f.stringWidth(s);
			g2.drawString(s, item.x + item.width/2 - stringWidth/2 , item.y + item.height / 2 + trueStringHeight / 2);
		}
	}
}
