package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.NoSuchElementException;

import swp_compiler_ss13.common.ast.ASTNode;

/**
 * An empty iterator
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class EmptyIterator implements Iterator<ASTNode> {
	@Override
	public void remove() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ASTNode next() {
		throw new NoSuchElementException();
	}

	@Override
	public boolean hasNext() {
		return false;
	}
}