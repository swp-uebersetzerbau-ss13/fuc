package swp_compiler_ss13.fuc.gui.ast;

import swp_compiler_ss13.common.ast.ASTNode;

public class Semantic_Controller extends AST_Controller {

	private AstGuiElementFactory factory;

	public Semantic_Controller() {
		super(true);
	}

	public Semantic_Controller(AST_Controller root, boolean checkedAST) {
		super(root, checkedAST);
	}

	@Override
	protected AstGuiElementFactory getAstGuiElementFactory() {
		if (factory == null) {
			factory = new AstGuiElementFactory() {

				@Override
				public NodeComponent getNodeComponent(final ASTNode node) {
					return new SemanticNodeComponent(node);
				}

				@Override
				public AST_Controller getController(final AST_Controller root,
						final boolean checkedAST) {
					return new Semantic_Controller(root, checkedAST);
				}
			};
		}
		return factory;
	}

}
