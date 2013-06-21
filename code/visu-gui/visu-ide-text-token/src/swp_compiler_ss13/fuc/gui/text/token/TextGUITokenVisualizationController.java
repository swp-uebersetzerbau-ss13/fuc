package swp_compiler_ss13.fuc.gui.text.token;

import swp_compiler_ss13.fuc.gui.ide.mvc.Position;
import swp_compiler_ss13.fuc.gui.text.Text_Controller;

/**
 * @author "Eduard Wolf"
 *
 */
public class TextGUITokenVisualizationController extends Text_Controller {

	private final TextGUITokenVisualizationModel tokenModel;

	public TextGUITokenVisualizationController() {
		super(Position.TOKENS);
		tokenModel = new TextGUITokenVisualizationModel(this);
		initModel(tokenModel);
	}

	public void toggleLineColumnInfo() {
		tokenModel.toggleLineColumnInfo();
		notifyModelChanged();
	}

}
