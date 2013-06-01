package swp_compiler_ss13.fuc.gui.text;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Model;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class Text_Controller implements Controller {

	private final Text_View view;
	private final Text_Model model;

	public Text_Controller(Text_View view, Text_Model model) {
		this.view = view;
		this.model = model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public View getView() {
		return view;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Model getModel() {
		return model;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void notifyModelChanged() {
		view.setViewInformation(model.getViewInformation());
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void init(IDE ide) {
		view.initComponents(ide);
	}

}
