/**
 * 
 */
package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.EmptyIterator;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;

/**
 * @author "Frank Zechert"
 * 
 */
public class LiteralNodeImplTest {

	/**
	 * the node under test
	 */
	private LiteralNode node;

	/**
	 * set up
	 * 
	 * @throws java.lang.Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new LiteralNodeImpl();
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#getNodeType()}.
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.LiteralNode, this.node.getNodeType());
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#getNumberOfNodes()}.
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#getChildren()}.
	 */
	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#getLiteral()}.
	 */
	@Test
	public void testGetLiteral() {
		assertEquals(null, this.node.getLiteral());
		PA.setValue(this.node, "literal", "");
		assertEquals("", this.node.getLiteral());
		PA.setValue(this.node, "literal", "test");
		assertEquals("test", this.node.getLiteral());
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#getLiteralType()}.
	 */
	@Test
	public void testGetLiteralType() {
		assertEquals(null, this.node.getLiteralType());
		Type t = new LongType();
		PA.setValue(this.node, "literalType", t);
		assertSame(t, this.node.getLiteralType());
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#setLiteral(java.lang.String)}
	 * .
	 */
	@Test
	public void testSetLiteral() {
		try {
			this.node.setLiteral(null);
			fail("expected illegal argument exception");
		} catch (IllegalArgumentException e) {
		}
		this.node.setLiteral("");
		this.node.setLiteral("test");
		assertEquals("test", PA.getValue(this.node, "literal"));
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#setLiteralType(swp_compiler_ss13.common.types.Type)}
	 * .
	 */
	@Test
	public void testSetLiteralType() {
		try {
			this.node.setLiteralType(null);
			fail("expected illegal argument exception");
		} catch (IllegalArgumentException e) {
		}
		Type t = new LongType();
		this.node.setLiteralType(t);
		assertSame(t, PA.getValue(this.node, "literalType"));
	}

	/**
	 * Test method for
	 * {@link swp_compiler_ss13.fuc.ast.LiteralNodeImpl#getDFSLTRNodeIterator()}
	 * .
	 */
	@Test
	public void testGetDFSLTRNodeIterator() {
		Iterator<ASTNode> it = this.node.getDFSLTRNodeIterator();
		assertTrue((it instanceof EmptyIterator));
	}

}
