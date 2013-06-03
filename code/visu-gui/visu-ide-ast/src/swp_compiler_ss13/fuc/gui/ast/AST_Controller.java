package swp_compiler_ss13.fuc.gui.ast;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;
import swp_compiler_ss13.fuc.gui.ide.mvc.IDE;
import swp_compiler_ss13.fuc.gui.ide.mvc.Model;
import swp_compiler_ss13.fuc.gui.ide.mvc.View;

/**
 * 
 * @author "Eduard Wolf"
 * 
 */
public class AST_Controller implements Controller {

	private final AST_Model model;
	private final AST_View view;

	public AST_Controller() {
		this.model = new AST_Model(this);
		this.view = new AST_View(this);
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
		this.notifyModelChangedWithoutLayoutChange();
		this.view.recalculateLayout();
	}

	protected void notifyModelChangedWithoutLayoutChange() {
		this.view.setNode(this.model.getNode());
		AST_Controller ast_Controller;
		for (ASTNode node : this.model.getChildren()) {
			ast_Controller = new AST_Controller();
			ast_Controller.model.setNode(node);
			ast_Controller.notifyModelChangedWithoutLayoutChange();
		}
	}

	@Override
	public void init(IDE ide) {
		this.view.initComponents(ide);
	}

	public void viewStateChanged() {
		this.view.changeChildState();
	}

}
