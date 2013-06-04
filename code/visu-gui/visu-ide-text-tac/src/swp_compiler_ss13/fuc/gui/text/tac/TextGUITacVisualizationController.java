package swp_compiler_ss13.fuc.gui.text.tac;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.text.Text_Controller;

/**
 * @author "Eduard Wolf"
 *
 */
public class TextGUITacVisualizationController extends Text_Controller {

	public TextGUITacVisualizationController() {
		super(Position.TAC);
		initModel(new TextGUITacVisualizationModel(this));
	}

}
