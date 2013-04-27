package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.ASTNode;

/**
 * AST Node implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public abstract class ASTNodeImpl implements ASTNode {

	/**
	 * parent node
	 */
	private ASTNode parent;

	@Override
	public ASTNode getParentNode() {
		return this.parent;
	}

	@Override
	public void setParentNode(ASTNode node) {
		this.parent = node;
	}

}
