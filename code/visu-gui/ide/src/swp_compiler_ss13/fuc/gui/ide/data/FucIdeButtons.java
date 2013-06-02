package swp_compiler_ss13.fuc.gui.ide.data;

import javax.swing.JButton;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

/**
 * Generated status label container class
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeButtons {
	/**
	 * Whether this menu is always visible
	 */
	private boolean alwaysVisible;
	/**
	 * The position to display this item at
	 */
	private Position position;
	/**
	 * The label to display
	 */
	private JButton button;

	/**
	 * The constructor
	 * 
	 * @param alwaysVisible
	 *            whether this menu is always visible
	 * @param position
	 *            the position to display this item at
	 * @param button
	 *            the button to display
	 */
	public FucIdeButtons(boolean alwaysVisible, Position position, JButton button) {
		super();
		this.alwaysVisible = alwaysVisible;
		this.position = position;
		this.button = button;
	}

	/**
	 * @return the alwaysVisible
	 */
	public boolean isAlwaysVisible() {
		return this.alwaysVisible;
	}

	/**
	 * @param alwaysVisible
	 *            the alwaysVisible to set
	 */
	public void setAlwaysVisible(boolean alwaysVisible) {
		this.alwaysVisible = alwaysVisible;
	}

	/**
	 * @return the position
	 */
	public Position getPosition() {
		return this.position;
	}

	/**
	 * @param position
	 *            the position to set
	 */
	public void setPosition(Position position) {
		this.position = position;
	}

	/**
	 * @return the button
	 */
	public JButton getMenu() {
		return this.button;
	}

	/**
	 * @param button
	 *            the button to set
	 */
	public void setMenu(JButton button) {
		this.button = button;
	}
}
