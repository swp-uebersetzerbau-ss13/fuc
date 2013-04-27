package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;

/**
 * UnaryExpressionNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public abstract class UnaryExpressionNodeImpl extends ASTNodeImpl implements UnaryExpressionNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(UnaryExpressionNodeImpl.class);
	/**
	 * The right expression
	 */
	private ExpressionNode rightExpression;
	/**
	 * The right operator
	 */
	private UnaryOperator operator;

	@Override
	public Integer getNumberOfNodes() {
		Integer nodes = 1;
		if (this.rightExpression != null) {
			nodes += this.rightExpression.getNumberOfNodes();
		}
		return nodes;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.rightExpression != null) {
			children.add(this.rightExpression);
		}
		return children;
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
	public void setOperator(UnaryOperator operator) {
		this.operator = operator;
	}

	@Override
	public UnaryOperator getOperator() {
		if (this.operator == null) {
			logger.warn("Returning null as operator");
		}
		return this.operator;
	}

}
