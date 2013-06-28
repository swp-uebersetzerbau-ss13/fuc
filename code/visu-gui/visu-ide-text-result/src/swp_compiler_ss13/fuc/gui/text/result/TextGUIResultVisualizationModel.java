package swp_compiler_ss13.fuc.gui.text.result;

import swp_compiler_ss13.fuc.gui.text.Text_Controller;
import swp_compiler_ss13.fuc.gui.text.Text_Model;

/**
 * @author "Eduard Wolf"
 * 
 */
public class TextGUIResultVisualizationModel extends Text_Model {

	public TextGUIResultVisualizationModel(Text_Controller controller) {
		super(controller, ModelType.RESULT);
	}
	
	@Override
	protected String resultToString(String target) {
		return target;
	}
}
