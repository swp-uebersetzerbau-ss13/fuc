package swp_compiler_ss13.fuc.gui.ide.data;

import javax.swing.JLabel;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

/**
 * Generated status label container class
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeStatusLabel implements Comparable<FucIdeStatusLabel> {
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
	private JLabel label;

	/**
	 * The constructor
	 * 
	 * @param alwaysVisible
	 *            whether this menu is always visible
	 * @param position
	 *            the position to display this item at
	 * @param label
	 *            the label to display
	 */
	public FucIdeStatusLabel(boolean alwaysVisible, Position position, JLabel label) {
		super();
		this.alwaysVisible = alwaysVisible;
		this.position = position;
		this.label = label;
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
	public JLabel getLabel() {
		return this.label;
	}

	/**
	 * @param label
	 *            the component to set
	 */
	public void setMenu(JLabel label) {
		this.label = label;
	}

	@Override
	public int compareTo(FucIdeStatusLabel arg0) {
		if (this.position.ordinal() < arg0.position.ordinal()) {
			return -1;
		}
		if (this.position.ordinal() > arg0.position.ordinal()) {
			return 1;
		}
		return this.label.getText().compareTo(arg0.label.getText());
	}
}
