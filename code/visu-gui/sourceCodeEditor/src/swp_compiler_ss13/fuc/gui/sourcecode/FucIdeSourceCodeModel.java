package swp_compiler_ss13.fuc.gui.sourcecode;

import swp_compiler_ss13.fuc.gui.ide.mvc.AbstractModel;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;

public class FucIdeSourceCodeModel extends AbstractModel {

	private Controller controller;
	private String sourcecode;

	public FucIdeSourceCodeModel(Controller c) {
		this.controller = c;
	}

	@Override
	public Controller getController() {
		return this.controller;
	}

	@Override
	public void setSourceCode(String sourceCode) {
		this.sourcecode = sourceCode;
	}

}
