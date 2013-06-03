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
	 * @param displayAlways
	 *            if true the menu will be displayed all the time. if false the
	 *            menu will only be shown, when the component is currently
	 *            visible (the tab is actvie)
	 */
	public void addMenu(JMenu menu, Position position, boolean displayAlways);

	/**
	 * Add your own button to the button bar
	 * 
	 * @param button
	 *            The button to add
	 * @param position
	 *            The position of the new button
	 * @param displayAlways
	 *            if true the button will be displayed all the time. if false
	 *            the button will only be shown, when the component is currently
	 *            visible (the tab is actvie)
	 */
	public void addButton(JButton button, Position position, boolean displayAlways);

	/**
	 * Add your own label to the status bar
	 * 
	 * @param label
	 *            The label to add
	 * @param position
	 *            The position of the new label
	 * @param displayAlways
	 *            if true the label will be displayed all the time. if false the
	 *            label will only be shown, when the component is currently
	 *            visible (the tab is actvie)
	 */
	public void addStatusLabel(JLabel label, Position position, boolean displayAlways);

	/**
	 * Set the source code that is used for the compiler
	 * 
	 * @param sourceCode
	 *            the source code that should be compiled
	 */
	public void setSourceCode(String sourceCode);
}
