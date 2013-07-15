package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.LoopNode;

/**
 * LoopNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public abstract class LoopNodeImpl extends ASTNodeImpl implements LoopNode {

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(LoopNodeImpl.class);

	/**
	 * body block
	 */
	protected StatementNode body;

	/**
	 * condition
	 */
	protected ExpressionNode condition;

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.condition != null) {
			children.add(this.condition);
		} else {
			logger.warn("The condition is null and was not added as a child!");
		}

		if (this.body != null) {
			children.add(this.body);
		} else {
			logger.warn("The body is null and was not added as a child!");
		}
		return children;
	}

	@Override
	public void setLoopBody(StatementNode stmt) {
		if (stmt == null) {
			logger.error("The argument stmt can not be null!");
			throw new IllegalArgumentException("The argument stmt can not be null!");
		}

		stmt.setParentNode(this);

		this.body = stmt;
	}

	@Override
	public StatementNode getLoopBody() {
		if (this.body == null) {
			logger.warn("Returning null as the loop body block");
		}
		return this.body;
	}

	@Override
	public void setCondition(ExpressionNode condition) {
		if (condition == null) {
			logger.error("The argument condition can not be null!");
			throw new IllegalArgumentException("The argument condition can not be null!");
		}

		condition.setParentNode(this);

		this.condition = condition;
	}

	@Override
	public ExpressionNode getCondition() {
		if (this.condition == null) {
			logger.warn("Returning null as the loop condition");
		}
		return this.condition;
	}

}
