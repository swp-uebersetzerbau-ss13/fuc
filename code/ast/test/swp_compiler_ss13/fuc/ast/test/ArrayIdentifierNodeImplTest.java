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
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;

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
			this.node.setIndexNode(null);
			fail("Expected IllegalArgumentException for setIndex(null) not thrown.");
		} catch (IllegalArgumentException e) {
		}

		LiteralNodeImpl index = new LiteralNodeImpl();
		index.setLiteral("0");
		index.setLiteralType(new LongType());
		this.node.setIndexNode(index);
		assertEquals(index, PA.getValue(this.node, "index"));

		index.setLiteral("111");
		this.node.setIndexNode(index);
		assertEquals(index, PA.getValue(this.node, "index"));
	}

	/**
	 * Test the getIndex() method
	 */
	@Test
	public void testGetIndex() {
		assertEquals(null, this.node.getIndexNode());

		LiteralNodeImpl index = new LiteralNodeImpl();
		index.setLiteral("0");
		index.setLiteralType(new LongType());
		PA.setValue(this.node, "index", index);
		assertEquals(index, this.node.getIndexNode());

		index.setLiteral("50");
		PA.setValue(this.node, "index", index);
		assertEquals(index, this.node.getIndexNode());
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
