package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.BranchNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;

/**
 * Test the branch node
 * 
 * @author "Frank Zechert"
 * 
 */
public class BranchNodeImplTest {

	/**
	 * the node under test
	 */
	private BranchNode node;

	/**
	 * test set up
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new BranchNodeImpl();
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.BranchNode, this.node.getNodeType());
	}

	/**
	 * test get number of nodes
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());

		PA.setValue(this.node, "trueBlock", new BlockNodeImpl());
		assertEquals(2, (int) this.node.getNumberOfNodes());

		PA.setValue(this.node, "falseBlock", new BlockNodeImpl());
		assertEquals(3, (int) this.node.getNumberOfNodes());

		PA.setValue(this.node, "condition", new BasicIdentifierNodeImpl());
		assertEquals(4, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getChildren()
	 */
	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());

		this.node.setCondition(new BasicIdentifierNodeImpl());
		assertEquals(1, this.node.getChildren().size());

		this.node.setBlockNodeOnTrue(new BlockNodeImpl());
		assertEquals(2, this.node.getChildren().size());

		this.node.setBlockNodeOnFalse(new BlockNodeImpl());
		assertEquals(3, this.node.getChildren().size());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(new DeclarationNodeImpl());
		blockNode.addStatement(new BlockNodeImpl());
		this.node.setBlockNodeOnFalse(blockNode);
		assertEquals(3, this.node.getChildren().size());
		this.node.setBlockNodeOnTrue(blockNode);
		assertEquals(3, this.node.getChildren().size());
	}

	/**
	 * Test the getCondition method
	 */
	@Test
	public void testGetCondition() {
		ExpressionNode actualValue = this.node.getCondition();
		assertEquals(null, actualValue);

		ExpressionNode condTestvalue = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "condition", condTestvalue);

		actualValue = this.node.getCondition();
		assertSame(condTestvalue, actualValue);
	}

	/**
	 * Test the setCondition
	 */
	@Test
	public void testSetCondition() {
		try {
			this.node.setCondition(null);
			fail("Expected IllegalArgumentException was not thrown!");
		} catch (IllegalArgumentException x) {
		}
		ExpressionNode condValue = new BasicIdentifierNodeImpl();
		this.node.setCondition(condValue);

		assertSame(condValue, PA.getValue(this.node, "condition"));
	}

	/**
	 * test set block node on true
	 */
	@Test
	public void testSetBlockNodeOnTrue() {
		try {
			this.node.setBlockNodeOnTrue(null);
			fail("Expected IllegalArgumentException was not thrown!");
		} catch (IllegalArgumentException x) {
		}
		BlockNode blockNode = new BlockNodeImpl();
		this.node.setBlockNodeOnTrue(blockNode);

		assertSame(blockNode, PA.getValue(this.node, "trueBlock"));
	}

	/**
	 * test getBlockNodeOnTrue
	 */
	@Test
	public void testGetBlockNodeOnTrue() {
		BlockNode actualValue = this.node.getBlockNodeOnTrue();
		assertEquals(null, actualValue);

		BlockNode blockTestValue = new BlockNodeImpl();
		PA.setValue(this.node, "trueBlock", blockTestValue);

		actualValue = this.node.getBlockNodeOnTrue();
		assertSame(blockTestValue, actualValue);
	}

	/**
	 * test set block node on false
	 */
	@Test
	public void testSetBlockNodeOnFalse() {
		try {
			this.node.setBlockNodeOnFalse(null);
			fail("Expected IllegalArgumentException was not thrown!");
		} catch (IllegalArgumentException x) {
		}
		BlockNode blockNode = new BlockNodeImpl();
		this.node.setBlockNodeOnFalse(blockNode);

		assertSame(blockNode, PA.getValue(this.node, "falseBlock"));
	}

	/**
	 * test getBlockNodeOnTrue
	 */
	@Test
	public void testGetBlockNodeOnFalse() {
		BlockNode actualValue = this.node.getBlockNodeOnFalse();
		assertEquals(null, actualValue);

		BlockNode blockTestValue = new BlockNodeImpl();
		PA.setValue(this.node, "falseBlock", blockTestValue);

		actualValue = this.node.getBlockNodeOnFalse();
		assertSame(blockTestValue, actualValue);
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
