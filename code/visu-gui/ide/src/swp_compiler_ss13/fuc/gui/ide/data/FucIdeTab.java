package swp_compiler_ss13.fuc.gui.ide.data;

import javax.swing.JComponent;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

/**
 * Generated tab container class
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeTab {
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
	private JComponent component;

	/**
	 * The constructor
	 * 
	 * @param alwaysVisible
	 *            whether this menu is always visible
	 * @param position
	 *            the position to display this item at
	 * @param component
	 *            the component to display
	 */
	public FucIdeTab(boolean alwaysVisible, Position position, JComponent component) {
		super();
		this.alwaysVisible = alwaysVisible;
		this.position = position;
		this.component = component;
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
	 * @return the component
	 */
	public JComponent getMenu() {
		return this.component;
	}

	/**
	 * @param component
	 *            the component to set
	 */
	public void setMenu(JComponent component) {
		this.component = component;
	}
}
