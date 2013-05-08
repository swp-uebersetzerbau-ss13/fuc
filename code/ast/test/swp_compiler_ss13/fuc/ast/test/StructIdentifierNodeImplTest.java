package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.StructIdentifierNodeImpl;

/**
 * 
 * @author Danny Maasch
 * @version 1
 * 
 */
public class StructIdentifierNodeImplTest {

	/**
	 * the node under test
	 */
	private StructIdentifierNode node;

	/**
	 * Set up the test
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new StructIdentifierNodeImpl();
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.StructIdentifierNode, this.node.getNodeType());
	}

	/**
	 * Test getNumberOfNodes()
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());

		StructIdentifierNode innerNode = new StructIdentifierNodeImpl();
		PA.setValue(this.node, "identifier", innerNode);
		assertEquals(2, (int) this.node.getNumberOfNodes());

		BasicIdentifierNodeImpl inner2Node = new BasicIdentifierNodeImpl();
		PA.setValue(innerNode, "identifier", inner2Node);
		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test the getChildren method
	 */
	@Test
	public void testGetChildren() {
		List<ASTNode> children;
		children = this.node.getChildren();
		assertEquals(0, children.size());

		ArrayIdentifierNode innerNode = new ArrayIdentifierNodeImpl();
		PA.setValue(this.node, "identifier", innerNode);
		children = this.node.getChildren();
		assertEquals(1, children.size());
	}

	/**
	 * test SetIdentifierNode() method
	 */
	@Test
	public void testSetIdentifierNode() {
		IdentifierNode idnode = new BasicIdentifierNodeImpl();
		this.node.setIdentifierNode(idnode);
		assertSame(idnode, PA.getValue(this.node, "identifier"));

		try {
			this.node.setIdentifierNode(null);
			fail("Expected IllegalArgumentException not thrown for setIdentifierNode(null)");
		} catch (IllegalArgumentException e) {

		}
	}

	/**
	 * Test getIdentifierNode method
	 */
	@Test
	public void testGetIdentifierNode() {
		assertEquals(null, this.node.getIdentifierNode());

		IdentifierNode idnode = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "identifier", idnode);
		assertSame(idnode, this.node.getIdentifierNode());
	}

	/**
	 * Test setFieldName method
	 */
	// @Test
	// public void setFieldName() {
	// this.node.setFieldName(null);
	// assertEquals(null, PA.getValue(this.node "field"));
	// }
}
