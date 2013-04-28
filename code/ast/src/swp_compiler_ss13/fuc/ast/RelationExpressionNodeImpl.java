package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;

/**
 * RelationExpressionNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class RelationExpressionNodeImpl extends BinaryExpressionNodeImpl implements RelationExpressionNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.RelationExpressionNode;
	}

}
