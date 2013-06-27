package swp_compiler_ss13.fuc.gui.ast;

import swp_compiler_ss13.common.ast.ASTNode;

public interface AstGuiElementFactory {

	public static final AstGuiElementFactory DEFAULT_FACTORY = new AstGuiElementFactory() {
		@Override
		public NodeComponent getNodeComponent(ASTNode node) {
			return new NodeComponent(node);
		}

		@Override
		public AST_Controller getController(AST_Controller root, boolean checkedAST) {
			return new AST_Controller(root, checkedAST);
		}
	};

	public NodeComponent getNodeComponent(ASTNode node);

	public AST_Controller getController(AST_Controller root, boolean checkedAST);
}
