package swp_compiler_ss13.fuc.gui.text.target;

import swp_compiler_ss13.fuc.gui.text.Text_Controller;

/**
 * @author "Eduard Wolf"
 * 
 */
public class TextGUITargetVisualizationController extends Text_Controller {

	private final TextGUITargetVisualizationModel model;
	private final TextGUITargetVisualizationView view;

	public TextGUITargetVisualizationController() {
		model = new TextGUITargetVisualizationModel(this);
		this.initModel(model);
		view = new TextGUITargetVisualizationView(this);
		this.initView(view);
	}

	public void toggleFullCode() {
		model.toogleCodeAmount();
		notifyModelChanged();
	}

}
