package swp_compiler_ss13.fuc.gui.ide.mvc;

import javax.swing.JComponent;


/**
 * View interface for the MVC pattern
 * 
 * @author "Frank Zechert"
 * 
 */
public interface View {
	/**
	 * Get the component to display this view
	 * 
	 * @return The component containing the gui component
	 */
	public JComponent getComponent();

	/**
	 * Get the name of this component
	 * 
	 * @return The name of this component
	 */
	public String getName();

	/**
	 * Get the position of this component
	 * 
	 * @return the position of this component
	 */
	public Position getPosition();

	/**
	 * Get the controller associated with this view
	 * 
	 * @return the controller
	 */
	public Controller getController();

	/**
	 * Called to initialize the gui component
	 * 
	 * @param ide
	 *            The ide the component is displayed in
	 */
	public void initComponents(IDE ide);
}