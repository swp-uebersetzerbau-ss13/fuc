package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;

/**
 * LogicBinaryExpressionNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class LogicBinaryExpressionNodeImpl extends BinaryExpressionNodeImpl implements LogicBinaryExpressionNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.LogicBinaryExpressionNode;
	}

}
