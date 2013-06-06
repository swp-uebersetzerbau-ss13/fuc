package swp_compiler_ss13.fuc.gui.sourcecode;

import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Model;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

public class SourceCodeController implements Controller {

	private final SourceCodeView view;
	private final SourceCodeModell model;

	public SourceCodeController() {
		this.view = new SourceCodeView(this);
		this.model = new SourceCodeModell(this);
	}

	@Override
	public View getView() {
		return view;
	}

	@Override
	public Model getModel() {
		return model;
	}

	@Override
	public void notifyModelChanged() {
		view.setSourceCode(model.getSourceCode());
	}

	@Override
	public void init(IDE ide) {
		view.initComponents(ide);
	}

}
