package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;

/**
 * ArithmeticBinaryExpressionNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class ArithmeticBinaryExpressionNodeImpl extends BinaryExpressionNodeImpl implements
		ArithmeticBinaryExpressionNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.ArithmeticBinaryExpressionNode;
	}

}
