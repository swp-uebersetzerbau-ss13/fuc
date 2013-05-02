package swp_compiler_ss13.fuc.ast;

import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;

/**
 * DoWhileNode implementation
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
public class DoWhileNodeImpl extends LoopNodeImpl implements DoWhileNode {

	@Override
	public ASTNodeType getNodeType() {
		return ASTNodeType.DoWhileNode;
	}

}
