package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;

/**
 * ArithmeticUnaryExpressionNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class ArithmeticUnaryExpressionNodeImpl extends UnaryExpressionNodeImpl implements ArithmeticUnaryExpressionNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.ArithmeticUnaryExpressionNode;
	}

}
