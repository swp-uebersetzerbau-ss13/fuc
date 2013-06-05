package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;

/**
 * PrintNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class AssignmentNodeImpl extends ASTNodeImpl implements AssignmentNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(PrintNodeImpl.class);

	/**
	 * left node
	 */
	private IdentifierNode leftNode;

	/**
	 * right node
	 */
	private ExpressionNode rightNode;

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.AssignmentNode;
	}

	@Override
	public Integer getNumberOfNodes() {
		Integer nodes = 1;
		if (this.leftNode != null) {
			nodes += this.leftNode.getNumberOfNodes();
		}
		if (this.rightNode != null) {
			nodes += this.rightNode.getNumberOfNodes();
		}
		return nodes;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> nodes = new LinkedList<>();
		if (this.leftNode != null) {
			nodes.add(this.leftNode);
		}
		if (this.rightNode != null) {
			nodes.add(this.rightNode);
		}
		return nodes;
	}

	@Override
	public IdentifierNode getLeftValue() {
		if (this.leftNode == null) {
			logger.warn("returning null as the left value for the assignment!");
		}
		return this.leftNode;
	}

	@Override
	public void setLeftValue(IdentifierNode identifier) {
		if (identifier == null) {
			logger.error("The argument identifier can not be null!");
			throw new IllegalArgumentException("The argument identifier can not be null!");
		}
		this.leftNode = identifier;
	}

	@Override
	public ExpressionNode getRightValue() {
		if (this.rightNode == null) {
			logger.warn("returning null as the right value for the assignment!");
		}
		return this.rightNode;
	}

	@Override
	public void setRightValue(ExpressionNode node) {
		if (node == null) {
			logger.error("The argument identifier can not be null!");
			throw new IllegalArgumentException("The argument identifier can not be null!");
		}
		this.rightNode = node;
	}

}
