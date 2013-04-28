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
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;

/**
 * Test the AssignmentNodeImpl
 * 
 * @author "Frank Zechert"
 * 
 */
public class AssignmentNodeImplTest {

	/**
	 * The assignmentNodeImpl under test
	 */
	private AssignmentNode node;

	/**
	 * Set up the test
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new AssignmentNodeImpl();
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.AssignmentNode, this.node.getNodeType());
	}

	/**
	 * Test the getNumberOfNodes method
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());

		IdentifierNode leftNode = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "leftNode", leftNode);
		assertEquals(2, (int) this.node.getNumberOfNodes());

		IdentifierNode rightNode = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "leftNode", null);
		PA.setValue(this.node, "rightNode", rightNode);
		assertEquals(2, (int) this.node.getNumberOfNodes());

		PA.setValue(this.node, "leftNode", leftNode);
		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test GetChildren method
	 */
	@Test
	public void testGetChildren() {
		List<ASTNode> children;
		children = this.node.getChildren();
		assertEquals(0, children.size());

		ArrayIdentifierNode innerNode = new ArrayIdentifierNodeImpl();
		PA.setValue(this.node, "leftNode", innerNode);
		children = this.node.getChildren();
		assertEquals(1, children.size());

		BasicIdentifierNodeImpl inner2Node = new BasicIdentifierNodeImpl();
		PA.setValue(innerNode, "identifier", inner2Node);
		children = this.node.getChildren();
		assertEquals(1, children.size());

		ArrayIdentifierNode inner3Node = new ArrayIdentifierNodeImpl();
		PA.setValue(this.node, "rightNode", inner3Node);
		children = this.node.getChildren();
		assertEquals(2, children.size());

		BasicIdentifierNodeImpl inner4Node = new BasicIdentifierNodeImpl();
		PA.setValue(inner3Node, "identifier", inner4Node);
		children = this.node.getChildren();
		assertEquals(2, children.size());

	}

	/**
	 * Test the getLeftValue method
	 */
	@Test
	public void testGetLeftValue() {
		IdentifierNode actualValue = this.node.getLeftValue();
		assertEquals(null, actualValue);

		IdentifierNode leftTestValue = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "leftNode", leftTestValue);

		actualValue = this.node.getLeftValue();
		assertSame(leftTestValue, actualValue);
	}

	/**
	 * Test the setLeftValue
	 */
	@Test
	public void testSetLeftValue() {
		try {
			this.node.setLeftValue(null);
			fail("Expected IllegalArgumentException was not thrown!");
		} catch (IllegalArgumentException x) {
		}
		IdentifierNode leftValue = new BasicIdentifierNodeImpl();
		this.node.setLeftValue(leftValue);

		assertSame(leftValue, PA.getValue(this.node, "leftNode"));
	}

	/**
	 * test getRightValue() method
	 */
	@Test
	public void testGetRightValue() {
		StatementNode actualValue = this.node.getRightValue();
		assertEquals(null, actualValue);

		StatementNode rightTestValue = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "rightNode", rightTestValue);

		actualValue = this.node.getRightValue();
		assertSame(rightTestValue, actualValue);
	}

	/**
	 * test setRightValue() method
	 */
	@Test
	public void testSetRightValue() {
		try {
			this.node.setRightValue(null);
			fail("Expected IllegalArgumentException was not thrown!");
		} catch (IllegalArgumentException x) {
		}
		IdentifierNode rightValue = new BasicIdentifierNodeImpl();
		this.node.setRightValue(rightValue);

		assertSame(rightValue, PA.getValue(this.node, "rightNode"));
	}

	/**
	 * Test the getParentNode method
	 */
	@Test
	public void testGetParentNode() {
		assertEquals(null, this.node.getParentNode());

		AssignmentNode idnode = new AssignmentNodeImpl();
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

		AssignmentNode idnode = new AssignmentNodeImpl();
		this.node.setParentNode(idnode);
		assertSame(idnode, PA.getValue(this.node, "parent"));
	}

}
