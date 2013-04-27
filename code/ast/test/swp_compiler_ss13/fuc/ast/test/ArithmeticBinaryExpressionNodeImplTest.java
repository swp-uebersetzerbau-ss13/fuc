package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;

/**
 * Test the ArithmeticBinaryExpressionNodeImpl
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class ArithmeticBinaryExpressionNodeImplTest {

	/**
	 * Test if the node returns the correct node type
	 */
	@Test
	public void testGetNodeType() {
		ArithmeticBinaryExpressionNode node = new ArithmeticBinaryExpressionNodeImpl();
		ASTNodeType nodeType = node.getNodeType();
		assertEquals(ASTNodeType.ArithmeticBinaryExpressionNode, nodeType);
	}

}
