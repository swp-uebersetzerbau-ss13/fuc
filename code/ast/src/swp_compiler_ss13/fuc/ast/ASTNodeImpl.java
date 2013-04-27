package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

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
		Iterator<ASTNode> iterator = new Iterator<ASTNode>() {

			Iterator<ASTNode> currentNodeIterator;
			Boolean outputNode = false;
			ASTNode nextChild;

			@Override
			public void remove() {
				throw new UnsupportedOperationException();
			}

			@Override
			public ASTNode next() {
				if (this.outputNode) {
					this.outputNode = false;
					return this.nextChild;
				}
				if (!this.hasNext()) {
					throw new NoSuchElementException();
				}
				return this.currentNodeIterator.next();
			}

			@Override
			public boolean hasNext() {
				if (this.currentNodeIterator != null) {
					if (this.currentNodeIterator.hasNext()) {
						return true;
					}
				}
				if (childIterator.hasNext()) {
					this.nextChild = childIterator.next();
					this.currentNodeIterator = this.nextChild.getDFSLTRNodeIterator();
					this.outputNode = true;
					return true;
				}
				return false;
			}
		};
		return iterator;
	}
}
