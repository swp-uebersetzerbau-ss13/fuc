package swp_compiler_ss13.fuc.ast;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;

/**
 * BinaryExpressionNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public abstract class BinaryExpressionNodeImpl extends ASTNodeImpl implements BinaryExpressionNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(BinaryExpressionNodeImpl.class);
	/**
	 * The left expression
	 */
	private ExpressionNode leftExpression;
	/**
	 * The right expression
	 */
	private ExpressionNode rightExpression;
	/**
	 * The right operator
	 */
	private BinaryOperator operator;

	@Override
	public Integer getNumberOfNodes() {
		Integer nodes = 1;
		if (this.leftExpression != null) {
			nodes += this.leftExpression.getNumberOfNodes();
		}
		if (this.rightExpression != null) {
			nodes += this.rightExpression.getNumberOfNodes();
		}
		return nodes;
	}

	@Override
	public Iterator<ASTNode> getDFSLTRNodeIterator() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.leftExpression != null) {
			children.add(this.leftExpression);
		}
		if (this.rightExpression != null) {
			children.add(this.rightExpression);
		}
		return children;
	}

	@Override
	public void setLeftValue(ExpressionNode expr) {
		if (expr == null) {
			logger.error("The argument expr can not be null");
			throw new IllegalArgumentException("The argument expr can not be null");
		}
		this.leftExpression = expr;
	}

	@Override
	public ExpressionNode getLeftValue() {
		if (this.leftExpression == null) {
			logger.warn("Returning null as left expression");
		}
		return this.leftExpression;
	}

	@Override
	public void setRightValue(ExpressionNode expr) {
		if (expr == null) {
			logger.error("The argument expr can not be null");
			throw new IllegalArgumentException("The argument expr can not be null");
		}
		this.rightExpression = expr;
	}

	@Override
	public ExpressionNode getRightValue() {
		if (this.rightExpression == null) {
			logger.warn("Returning null as right expression");
		}
		return this.rightExpression;
	}

	@Override
	public void setOperator(BinaryOperator operator) {
		this.operator = operator;
	}

	@Override
	public BinaryOperator getOperator() {
		if (this.operator == null) {
			logger.warn("Returning null as operator");
		}
		return this.operator;
	}

}
