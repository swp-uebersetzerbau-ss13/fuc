package swp_compiler_ss13.fuc.ast;

import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
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
	private BlockNode trueBlock;

	/**
	 * The block when the expression evaluates to false
	 */
	private BlockNode falseBlock;

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
		return this.condition.getNumberOfNodes() + this.trueBlock.getNumberOfNodes()
				+ this.falseBlock.getNumberOfNodes() + 1;
	}

	@Override
	public List<ASTNode> getChildren() {
		List<ASTNode> children = new LinkedList<>();
		children.add(this.condition);
		children.add(this.trueBlock);
		children.add(this.falseBlock);
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
	public void setBlockNodeOnTrue(BlockNode block) {
		if (block == null) {
			logger.error("The argument block can not be null!");
			throw new IllegalArgumentException("The argument block can not be null!");
		}
		this.trueBlock = block;
	}

	@Override
	public BlockNode getBlockNodeOnTrue() {
		if (this.trueBlock == null) {
			logger.warn("Returning null as a block");
		}
		return this.trueBlock;
	}

	@Override
	public void setBlockNodeOnFalse(BlockNode block) {
		if (block == null) {
			logger.error("The argument block can not be null!");
			throw new IllegalArgumentException("The argument block can not be null!");
		}
		this.falseBlock = block;
	}

	@Override
	public BlockNode getBlockNodeOnFalse() {
		if (this.falseBlock == null) {
			logger.warn("Returning null as a block");
		}
		return this.falseBlock;
	}

}
