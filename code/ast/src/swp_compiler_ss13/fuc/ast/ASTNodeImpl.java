package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.List;

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

	@Override
	public Iterator<ASTNode> getDFSLTRNodeIterator() {
		final List<ASTNode> children = this.getChildren();
		final Iterator<ASTNode> childIterator = children.iterator();
		return new DFSLTRIterator(childIterator);
	}
}
