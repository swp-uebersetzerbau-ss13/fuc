package swp_compiler_ss13.fuc.gui.ast;

import org.apache.log4j.Logger;

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

	private static final Logger LOG = Logger.getLogger(AST_Controller.class);

	private final AST_Model model;
	private final AST_View view;

	private final AST_Controller root;

	private final boolean checkAST;

	public AST_Controller() {
		this(false);
	}

	public AST_Controller(boolean checkedAST) {
		this(null, checkedAST);
	}

	AST_Controller(AST_Controller root, boolean checkAST) {
		this.checkAST = checkAST;
		this.root = root == null ? this : root;
		this.model = new AST_Model(this);
		this.view = new AST_View(this, root == null, getAstGuiElementFactory(), checkAST);
	}

	protected AstGuiElementFactory getAstGuiElementFactory() {
		return AstGuiElementFactory.DEFAULT_FACTORY;
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
		this.root.view.recalculateLayout();
	}

	protected void notifyModelChangedWithoutLayoutChange() {
		LOG.trace("node: " + this.nodeToString(this.model.getNode()));
		this.view.setNode(this.model.getNode());
		AST_Controller ast_Controller;
		for (ASTNode node : this.model.getChildren()) {
			LOG.trace("childnode: " + this.nodeToString(node));
			ast_Controller = getAstGuiElementFactory().getController(this.root, checkAST);
			ast_Controller.model.setNode(node);
			ast_Controller.notifyModelChangedWithoutLayoutChange();
			this.view.addChild(ast_Controller.view);
		}
	}

	private String nodeToString(ASTNode node) {
		if (node == null) {
			return "null";
		}
		return "type: " + node.getNodeType().name() + ", amount of children: "
				+ node.getChildren().size();
	}

	@Override
	public void init(IDE ide) {
		this.view.initComponents(ide);
	}

	public void viewStateChanged() {
		this.view.changeChildState();
		this.root.view.recalculateLayout();
	}

	public void toggleNodeSize() {
		this.view.toggleNodeSize();
	}

}
