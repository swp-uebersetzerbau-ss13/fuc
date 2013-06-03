package swp_compiler_ss13.fuc.gui.ide.data;

import javax.swing.JMenu;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

/**
 * Generated menu container class
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeMenu implements Comparable<FucIdeMenu> {
	/**
	 * Whether this menu is always visible
	 */
	private boolean alwaysVisible;
	/**
	 * The position to display this item at
	 */
	private Position position;
	/**
	 * The menu to display
	 */
	private JMenu menu;

	/**
	 * The constructor
	 * 
	 * @param alwaysVisible
	 *            whether this menu is always visible
	 * @param position
	 *            the position to display this item at
	 * @param menu
	 *            the menu to display
	 */
	public FucIdeMenu(boolean alwaysVisible, Position position, JMenu menu) {
		super();
		this.alwaysVisible = alwaysVisible;
		this.position = position;
		this.menu = menu;
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
	 * @return the menu
	 */
	public JMenu getMenu() {
		return this.menu;
	}

	/**
	 * @param menu
	 *            the menu to set
	 */
	public void setMenu(JMenu menu) {
		this.menu = menu;
	}

	@Override
	public int compareTo(FucIdeMenu arg0) {

		if (this.position.ordinal() < arg0.position.ordinal()) {
			return -1;
		}
		if (this.position.ordinal() > arg0.position.ordinal()) {
			return 1;
		}
		return this.menu.getText().compareTo(arg0.menu.getText());
	}
}
