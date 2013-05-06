package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;

/**
 * 
 * @author Danny Maasch
 * @version 1
 * 
 */
public class ReturnNodeImplTest {

	/**
	 * the node under test
	 */
	private ReturnNode node;

	/**
	 * test set up
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new ReturnNodeImpl();
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.ReturnNode, this.node.getNodeType());
	}

	/**
	 * test get number of nodes
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test the getRightValue method for null
	 */
	@Test
	public void testGetRightValueNull() {
		IdentifierNode actualValue = this.node.getRightValue();
		assertEquals(null, actualValue);
	}

}
