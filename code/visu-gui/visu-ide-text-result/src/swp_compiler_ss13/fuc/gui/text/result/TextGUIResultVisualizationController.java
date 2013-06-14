package swp_compiler_ss13.fuc.gui.text.result;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.text.Text_Controller;

/**
 * @author "Eduard Wolf"
 * 
 */
public class TextGUIResultVisualizationController extends Text_Controller {

	public TextGUIResultVisualizationController() {
		super(Position.RESULT);
		this.initModel(new TextGUIResultVisualizationModel(this));
	}

}
