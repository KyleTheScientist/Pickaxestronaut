package frame.components;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;

import javax.swing.JPanel;

import fx.GameObject;

public class HUD extends JPanel {

	/**
	 * 
	 */
	private static final long serialVersionUID = -2069752213748244646L;
	private static int size = GameObject.tileSize;
	public static Font hotbarFont = new Font("Impact", size / 3, size / 3);
	public static Color paneColor = new Color(50, 50, 50, 255);
	public static Color buttonColor = new Color(100, 100, 100, 255);

	JPanel rightContainer, leftContainer;
	public InventoryPane inventory;
	public StorePane store;

	public HUD(Dimension d) {
		setPreferredSize(d);
		setMinimumSize(d);
		setMaximumSize(d);
		requestFocusInWindow();
		setOpaque(false);

		int paneWidth = d.width / 2;
		int paneHeight = d.height / 2;

		inventory = new InventoryPane(new Dimension(paneWidth, paneHeight));
		store = new StorePane(new Dimension(paneWidth, paneHeight));

		GridBagConstraints hudC = new GridBagConstraints();
		hudC.insets = new Insets(10, 10, 10, 10);
		setLayout(new GridBagLayout());
		hudC.anchor = GridBagConstraints.NORTHEAST;
		hudC.gridx = 1;
		hudC.gridy = 0;
		hudC.weightx = 1;
		hudC.weighty = 1;
		add(inventory, hudC);
		hudC.anchor = GridBagConstraints.NORTHWEST;
		hudC.gridx = 0;
		hudC.gridy = 0;
		hudC.weightx = 1;
		hudC.weighty = 1;
		add(store, hudC);

		requestFocusInWindow();

	}

	public void update() {
		if (store.isVisible())
			store.update();
		if (inventory.isVisible()) {
			inventory.update();
		}
	}
}
