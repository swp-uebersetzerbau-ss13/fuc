package swp_compiler_ss13.fuc.gui.ide.data;

import javax.swing.JComponent;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.Position;

/**
 * Generated tab container class
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class FucIdeTab implements Comparable<FucIdeTab> {
	/**
	 * The position to display this item at
	 */
	private Position position;
	/**
	 * The menu to display
	 */
	private JComponent component;
	/**
	 * The controller
	 */
	private Controller controller;
	/**
	 * The name
	 */
	private String name;

	/**
	 * Create a new instance
	 * 
	 * @param c
	 *            controller
	 * @param component
	 *            gui component
	 * @param name
	 *            name
	 * @param position
	 *            position
	 */
	public FucIdeTab(Controller c, JComponent component, String name, Position position) {
		this.component = component;
		this.controller = c;
		this.name = name;
		this.position = position;
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
	public JComponent getComponent() {
		return this.component;
	}

	/**
	 * @param component
	 *            the component to set
	 */
	public void setComponent(JComponent component) {
		this.component = component;
	}

	/**
	 * @return the controller
	 */
	public Controller getController() {
		return this.controller;
	}

	/**
	 * @param controller
	 *            the controller to set
	 */
	public void setController(Controller controller) {
		this.controller = controller;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int compareTo(FucIdeTab other) {
		int myPos = this.position.ordinal();
		int otherPos = other.position.ordinal();
		if (myPos < otherPos) {
			return -1;
		}
		if (myPos > otherPos) {
			return 1;
		}
		return this.name.compareTo(other.name);
	}
}
