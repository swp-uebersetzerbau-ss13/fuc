package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
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
	private ExpressionNode index;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.ArrayIdentifierNode;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.identifier != null) {
			children.add(this.identifier);
		}
		if (this.index != null) {
		   children.add(this.index);
		}
		return children;
	}

	@Override
	public void setIndexNode(ExpressionNode index) {
		if (index == null) {
			logger.error("The given index can not be null");
			throw new IllegalArgumentException("The given index can not be null");
		}

		this.index = index;
	}

	@Override
	public ExpressionNode getIndexNode() {
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
