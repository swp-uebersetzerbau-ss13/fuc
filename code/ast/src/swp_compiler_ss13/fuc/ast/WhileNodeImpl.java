package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;

/**
 * WhileNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class WhileNodeImpl extends LoopNodeImpl implements WhileNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.WhileNode;
	}
}
