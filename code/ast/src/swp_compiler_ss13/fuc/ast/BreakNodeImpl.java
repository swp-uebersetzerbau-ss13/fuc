package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;

/**
 * BreakNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class BreakNodeImpl extends ASTNodeImpl implements BreakNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.BreakNode;
	}

	@Override
	public Iterator<ASTNode> getDFSLTRNodeIterator() {
		return new EmptyIterator();
	}

	@Override
	public List<ASTNode> getChildren() {
		return new LinkedList<>();
	}

}
