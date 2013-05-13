package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;

/**
 * Test the BasicIdentifierNodeImpl
 * 
 * @author "Frank Zechert"
 * 
 */
public class BasicIdentifierNodeImplTest {

	/**
	 * the node under test
	 */
	private BasicIdentifierNode node;

	/**
	 * Set up the test
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new BasicIdentifierNodeImpl();
	}

	/**
	 * Test the empty leaf iterator
	 */
	@Test
	public void testGetDFSLTRNodeIterator() {
		Iterator<ASTNode> iterator = this.node.getDFSLTRNodeIterator();
		assertTrue(iterator != null);

		assertTrue(!iterator.hasNext());

		try {
			iterator.next();
			fail("NoSuchElementException expected but not thrown!");
		} catch (NoSuchElementException e) {

		}

		try {
			iterator.remove();
		} catch (UnsupportedOperationException e) {
		}
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.BasicIdentifierNode, this.node.getNodeType());
	}

	/**
	 * Test getNumberOfNodes()
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getChildren
	 */
	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());
	}

	/**
	 * Test get identifier
	 */
	@Test
	public void testGetIdentifier() {
		assertEquals(null, this.node.getIdentifier());

		PA.setValue(this.node, "identifier", "");
		assertEquals("", this.node.getIdentifier());

		PA.setValue(this.node, "identifier", "test");
		assertEquals("test", this.node.getIdentifier());
	}

	/**
	 * test setIdentifier
	 */
	@Test
	public void testSetIdentifier() {
		try {
			this.node.setIdentifier(null);
			fail("Expected IllegalArgumentException not thrown!");
		} catch (IllegalArgumentException e) {
		}

		this.node.setIdentifier("");
		assertEquals("", PA.getValue(this.node, "identifier"));

		this.node.setIdentifier("test");
		assertEquals("test", PA.getValue(this.node, "identifier"));
	}

	/**
	 * test getParentNode
	 */
	@Test
	public void testGetParentNode() {
		assertEquals(null, this.node.getParentNode());

		ASTNode parent = new BlockNodeImpl();
		PA.setValue(this.node, "parent", parent);
		assertSame(parent, this.node.getParentNode());
	}

	/**
	 * test setParentNode
	 */
	@Test
	public void testSetParentNode() {
		ASTNode parent = new BlockNodeImpl();
		this.node.setParentNode(parent);
		assertSame(parent, PA.getValue(this.node, "parent"));

		this.node.setParentNode(parent);
		assertSame(parent, PA.getValue(this.node, "parent"));
	}

}
