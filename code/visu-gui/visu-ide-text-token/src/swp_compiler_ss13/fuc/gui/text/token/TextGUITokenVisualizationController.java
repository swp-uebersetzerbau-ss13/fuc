package swp_compiler_ss13.fuc.gui.text.token;

import swp_compiler_ss13.fuc.gui.text.Text_Controller;

/**
 * @author "Eduard Wolf"
 * 
 */
public class TextGUITokenVisualizationController extends Text_Controller {

	private final TextGUITokenVisualizationModel tokenModel;
	private final TextGUITokenVisualizationView view;

	public TextGUITokenVisualizationController() {
		tokenModel = new TextGUITokenVisualizationModel(this);
		initModel(tokenModel);
		view = new TextGUITokenVisualizationView(this);
		initView(view);
	}

	public void toggleLineColumnInfo() {
		tokenModel.toggleLineColumnInfo();
		notifyModelChanged();
	}

}
