package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.NoSuchElementException;

import swp_compiler_ss13.common.ast.ASTNode;

public class DFSLTRIterator implements Iterator<ASTNode> {
	private Iterator<ASTNode> currentNodeIterator;
	private Iterator<ASTNode> childIterator;
	private Boolean outputNode = false;
	private ASTNode nextChild;
	private Boolean hasNextCalled = false;
	private Boolean hasNextResult = false;

	public DFSLTRIterator(Iterator<ASTNode> ci) {
		this.childIterator = ci;
	}

	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ASTNode next() {
		if (!this.hasNext()) {
			throw new NoSuchElementException();
		}
		this.hasNextCalled = false;
		if (this.outputNode) {
			this.outputNode = false;
			return this.nextChild;
		}
		return this.currentNodeIterator.next();
	}

	@Override
	public boolean hasNext() {
		if (this.hasNextCalled) {
			return this.hasNextResult;
		}
		if (this.currentNodeIterator != null) {
			if (this.currentNodeIterator.hasNext()) {
				this.hasNextCalled = true;
				this.hasNextResult = true;
				return true;
			}
		}
		if (this.childIterator.hasNext()) {
			this.nextChild = this.childIterator.next();
			this.currentNodeIterator = this.nextChild.getDFSLTRNodeIterator();
			this.outputNode = true;
			this.hasNextCalled = true;
			this.hasNextResult = true;
			return true;
		}
		this.hasNextCalled = true;
		this.hasNextResult = false;
		return false;
	}
}
