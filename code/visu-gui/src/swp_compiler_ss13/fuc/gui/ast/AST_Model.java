package swp_compiler_ss13.fuc.gui.ast;

import java.util.List;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.fuc.gui.ide.mvc.AbstractModel;
import swp_compiler_ss13.fuc.gui.ide.mvc.Controller;

public class AST_Model extends AbstractModel {

	private final AST_Controller controller;
	private ASTNode node;

	public AST_Model(AST_Controller controller) {
		this.controller = controller;
	}

	@Override
	public Controller getController() {
		return controller;
	}

	@Override
	public boolean setAST(AST ast) {
		setNode(ast.getRootNode());
		return true;
	}

	public void setNode(ASTNode node) {
		this.node = node;
	}

	public ASTNode getNode() {
		return node;
	}

	public List<ASTNode> getChildren() {
		return node.getChildren();
	}

}
