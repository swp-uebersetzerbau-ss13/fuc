package swp_compiler_ss13.fuc.gui.sourcecode;

import swp_compiler_ss13.fuc.gui.ide.mvc.AbstractModel;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;

public class SourceCodeModell extends AbstractModel {

	private final SourceCodeController controller;
	private String sourceCode;

	public SourceCodeModell(SourceCodeController controller) {
		this.controller = controller;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public boolean setSourceCode(String sourceCode) {
		if (sourceCode == null) {
			sourceCode = "";
		}
		this.sourceCode = sourceCode;
		return true;
	}

	public String getSourceCode() {
		return sourceCode;
	}

}
