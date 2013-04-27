package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;

/**
 * ArrayIdentifierNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class ArrayIdentifierNodeImpl extends ASTNodeImpl implements ArrayIdentifierNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(DeclarationNodeImpl.class);

	/**
	 * The identifier name
	 */
	private IdentifierNode identifier;

	/**
	 * the array index
	 */
	private Integer index;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.ArrayIdentifierNode;
	}

	@Override
	public Integer getNumberOfNodes() {
		if (this.identifier == null) {
			return 1;
		}
		return 1 + this.identifier.getNumberOfNodes();
	}

	@Override
	public Iterator<ASTNode> getDFSLTRNodeIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.identifier != null) {
			children.add(this.identifier);
		}
		return children;
	}

	@Override
	public void setIndex(Integer index) {
		if (index == null) {
			logger.error("The given index can not be null");
			throw new IllegalArgumentException("The given index can not be null");
		}
		if (index < 0) {
			logger.error("The given index can not be less than 0");
			throw new IllegalArgumentException("The given index can not be less than 0");
		}
		this.index = index;
	}

	@Override
	public Integer getIndex() {
		if (this.index == null) {
			logger.warn("Returning null as array index.");
		}
		return this.index;
	}

	@Override
	public void setIdentifierNode(IdentifierNode node) {
		if (node == null) {
			logger.error("The given node can not be null");
			throw new IllegalArgumentException("The given node can not be null");
		}
		this.identifier = node;
	}

	@Override
	public IdentifierNode getIdentifierNode() {
		if (this.index == null) {
			logger.warn("Returning null as identifier node.");
		}
		return this.identifier;
	}

}
