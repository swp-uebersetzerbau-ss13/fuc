package swp_compiler_ss13.fuc.gui.ide.mvc;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JMenu;

/**
 * Interface for access to the GUI panels.
 * 
 * @author "Frank Zechert"
 * 
 */
public interface IDE {
	/**
	 * Add your own menu to the menu bar
	 * 
	 * @param menu
	 *            The menu to add
	 * @param position
	 *            The position of the new menu
	 */
	public void addMenu(JMenu menu, Position position);

	/**
	 * Add your own button to the button bar
	 * 
	 * @param button
	 *            The button to add
	 * @param position
	 *            The position of the new button
	 */
	public void addButton(JButton button, Position position);

	/**
	 * Add your own label to the status bar
	 * 
	 * @param label
	 *            The label to add
	 * @param position
	 *            The position of the new label
	 */
	public void addStatusLabel(JLabel label, Position position);
}
