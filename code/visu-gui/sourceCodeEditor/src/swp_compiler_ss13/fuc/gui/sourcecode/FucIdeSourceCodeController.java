package swp_compiler_ss13.fuc.gui.sourcecode;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Model;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class FucIdeSourceCodeController implements Controller {

	private FucIdeSourceCodeView view;
	private FucIdeSourceCodeModel model;

	public FucIdeSourceCodeController() {
		this.model = new FucIdeSourceCodeModel(this);
		this.view = new FucIdeSourceCodeView(this);
	}

	@Override
	public View getView() {
		return this.view;
	}

	@Override
	public Model getModel() {
		return this.model;
	}

	@Override
	public void notifyModelChanged() {
		// TODO Auto-generated method stub

	}

	@Override
	public void init(IDE ide) {
		this.view.initComponents(ide);
	}

}
