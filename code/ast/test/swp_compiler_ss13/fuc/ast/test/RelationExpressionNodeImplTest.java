package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.fuc.ast.RelationExpressionNodeImpl;

/**
 * 
 * @author Danny Maasch
 * @version 1
 * 
 */
public class RelationExpressionNodeImplTest {

	/**
	 * the node under test
	 */
	private RelationExpressionNode node;

	/**
	 * Set up test environment
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new RelationExpressionNodeImpl();
	}

	/**
	 * Test if the node returns the correct node type
	 */
	@Test
	public void testGetNodeType() {
		ASTNodeType nodeType = this.node.getNodeType();
		assertEquals(ASTNodeType.RelationExpressionNode, nodeType);
	}

}
