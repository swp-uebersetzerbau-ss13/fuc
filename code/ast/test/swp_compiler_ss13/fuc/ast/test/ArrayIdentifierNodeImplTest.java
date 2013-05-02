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
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;

/**
 * Test the ArrayIdentifierNodeImpl
 * 
 * @author "Frank Zechert"
 * 
 */
public class ArrayIdentifierNodeImplTest {

	/**
	 * The arrayIdentifierNode under test
	 */
	private ArrayIdentifierNode node;

	/**
	 * Set up the test
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new ArrayIdentifierNodeImpl();
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.ArrayIdentifierNode, this.node.getNodeType());
	}

	/**
	 * Test the getNumberOfNodes method
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());

		ArrayIdentifierNode innerNode = new ArrayIdentifierNodeImpl();
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

		BasicIdentifierNodeImpl inner2Node = new BasicIdentifierNodeImpl();
		PA.setValue(innerNode, "identifier", inner2Node);
		children = this.node.getChildren();
		assertEquals(1, children.size());

	}

	/**
	 * Test the setIndex method
	 */
	@Test
	public void testSetIndex() {
		try {
			this.node.setIndex(null);
			fail("Expected IllegalArgumentException for setIndex(null) not thrown.");
		} catch (IllegalArgumentException e) {
		}

		try {
			this.node.setIndex(-1);
			fail("Expected IllegalArgumentException for setIndex(-1) not thrown.");
		} catch (IllegalArgumentException e) {
		}

		this.node.setIndex(0);
		assertEquals(0, PA.getValue(this.node, "index"));

		this.node.setIndex(111);
		assertEquals(111, PA.getValue(this.node, "index"));
	}

	/**
	 * Test the getIndex() method
	 */
	@Test
	public void testGetIndex() {
		assertEquals(null, this.node.getIndex());

		PA.setValue(this.node, "index", 0);
		assertEquals(0, (int) this.node.getIndex());

		PA.setValue(this.node, "index", 50);
		assertEquals(50, (int) this.node.getIndex());
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
	 * Test the getParentNode method
	 */
	@Test
	public void testGetParentNode() {
		assertEquals(null, this.node.getParentNode());

		IdentifierNode idnode = new ArrayIdentifierNodeImpl();
		PA.setValue(this.node, "parent", idnode);
		assertSame(idnode, this.node.getParentNode());
	}

	/**
	 * Test the setParentNode method
	 */
	@Test
	public void testSetParentNode() {
		this.node.setParentNode(null);
		assertEquals(null, PA.getValue(this.node, "parent"));

		IdentifierNode idnode = new ArrayIdentifierNodeImpl();
		this.node.setParentNode(idnode);
		assertSame(idnode, PA.getValue(this.node, "parent"));
	}

}
