package swp_compiler_ss13.fuc.gui.ide.mvc;


/**
 * Controller interface for MVC Pattern
 * 
 * @author "Frank Zechert"
 * 
 */
public interface Controller {
	/**
	 * Get the View associated with this controller
	 * 
	 * @return the view
	 */
	public View getView();

	/**
	 * Get the model associated with this controller
	 * 
	 * @return the model
	 */
	public Model getModel();

	/**
	 * Notify the controller about changes in the model
	 */
	public void notifyModelChanged();

	/**
	 * Called when the component is initialized
	 * 
	 * @param ide
	 *            The ide the component is displayed in
	 */
	public void init(IDE ide);
}
