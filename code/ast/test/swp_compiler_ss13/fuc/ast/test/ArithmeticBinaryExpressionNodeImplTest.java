package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;

/**
 * Test the ArithmeticBinaryExpressionNodeImpl
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class ArithmeticBinaryExpressionNodeImplTest {

	private ArithmeticBinaryExpressionNode node;

	/**
	 * Set up test environment
	 * 
	 * @throws Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new ArithmeticBinaryExpressionNodeImpl();
	}

	/**
	 * Test if the node returns the correct node type
	 */
	@Test
	public void testGetNodeType() {
		ASTNodeType nodeType = this.node.getNodeType();
		assertEquals(ASTNodeType.ArithmeticBinaryExpressionNode, nodeType);
	}

	/**
	 * Test the returned children when left and right expressions are empty
	 */
	@Test
	public void testGetChildrenNone() {
		List<ASTNode> children = this.node.getChildren();
		assertEquals(0, children.size());
	}

	/**
	 * Test the returned children when left expression is empty
	 */
	@Test
	public void testGetChildrenRight() {
		this.node.setRightValue(new ArithmeticBinaryExpressionNodeImpl());
		List<ASTNode> children = this.node.getChildren();
		assertEquals(1, children.size());
	}

	/**
	 * Test the returned children when right expression is empty
	 */
	@Test
	public void testGetChildrenLeft() {
		this.node.setLeftValue(new ArithmeticBinaryExpressionNodeImpl());
		List<ASTNode> children = this.node.getChildren();
		assertEquals(1, children.size());
	}

	/**
	 * Test the returned children when right and left expressions are non empty
	 */
	@Test
	public void testGetChildrenBoth() {
		this.node.setLeftValue(new ArithmeticBinaryExpressionNodeImpl());
		this.node.setRightValue(new ArithmeticBinaryExpressionNodeImpl());
		List<ASTNode> children = this.node.getChildren();
		assertEquals(2, children.size());
	}

	/**
	 * Test the getLeftValue method
	 */
	@Test
	public void testGetLeftValue() {
		ExpressionNode leftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "leftExpression", leftTestValue);

		ExpressionNode actualValue = this.node.getLeftValue();
		assertSame(leftTestValue, actualValue);
	}

	/**
	 * Test the getLeftValue method for null
	 */
	@Test
	public void testGetLeftValueNull() {
		ExpressionNode actualValue = this.node.getLeftValue();
		assertEquals(null, actualValue);
	}
}
