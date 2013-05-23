package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;

/**
 * BranchNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class BranchNodeImpl extends ASTNodeImpl implements BranchNode {

	/**
	 * The expression to evaluate
	 */
	private ExpressionNode condition;

	/**
	 * The block when the expression evaluates to true
	 */
	private StatementNode trueBlock;

	/**
	 * The block when the expression evaluates to false
	 */
	private StatementNode falseBlock;

	/**
	 * The logger
	 */
	private static Logger logger = Logger.getLogger(BlockNodeImpl.class);

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.BranchNode;
	}

	@Override
	public Integer getNumberOfNodes() {
		Integer nodes = 1;
		if (this.condition != null) {
			nodes += this.condition.getNumberOfNodes();
		}
		if (this.trueBlock != null) {
			nodes += this.trueBlock.getNumberOfNodes();
		}
		if (this.falseBlock != null) {
			nodes += this.falseBlock.getNumberOfNodes();
		}
		return nodes;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		if (this.condition != null) {
			children.add(this.condition);
		}
		if (this.trueBlock != null) {
			children.add(this.trueBlock);
		}
		if (this.falseBlock != null) {
			children.add(this.falseBlock);
		}
		return children;
	}

	@Override
	public void setCondition(ExpressionNode condition) {
		if (condition == null) {
			logger.error("The argument condition can not be null!");
			throw new IllegalArgumentException("The argument condition can not be null!");
		}
		this.condition = condition;
	}

	@Override
	public ExpressionNode getCondition() {
		if (this.condition == null) {
			logger.warn("Returning null as a condition");
		}
		return this.condition;
	}

	@Override
	public void setBlockNodeOnTrue(StatementNode block) {
		if (block == null) {
			logger.error("The argument block can not be null!");
			throw new IllegalArgumentException("The argument block can not be null!");
		}
		this.trueBlock = block;
	}

	@Override
	public StatementNode getBlockNodeOnTrue() {
		if (this.trueBlock == null) {
			logger.warn("Returning null as a block");
		}
		return this.trueBlock;
	}

	@Override
	public void setBlockNodeOnFalse(StatementNode block) {
		if (block == null) {
			logger.error("The argument block can not be null!");
			throw new IllegalArgumentException("The argument block can not be null!");
		}
		this.falseBlock = block;
	}

	@Override
	public StatementNode getBlockNodeOnFalse() {
		if (this.falseBlock == null) {
			logger.warn("Returning null as a block");
		}
		return this.falseBlock;
	}

}
