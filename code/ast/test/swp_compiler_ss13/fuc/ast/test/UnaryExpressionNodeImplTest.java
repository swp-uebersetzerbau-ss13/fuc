package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.fuc.ast.ArithmeticUnaryExpressionNodeImpl;

/**
 * 
 * @author Danny
 * @version 1
 * 
 */

public class UnaryExpressionNodeImplTest {

	/**
	 * the node under test
	 */
	private UnaryExpressionNode node;

	/**
	 * Set up test environment
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new ArithmeticUnaryExpressionNodeImpl();
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
		this.node.setRightValue(new ArithmeticUnaryExpressionNodeImpl());
		List<ASTNode> children = this.node.getChildren();
		assertEquals(1, children.size());
	}

	/**
	 * Test getNumberOfNodes for empty expressions
	 */
	@Test
	public void testGetNumberOfNodesEmpty() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with right expression
	 */
	@Test
	public void testGetNumberOfNodesRight() {
		ExpressionNode rightTestValue = new ArithmeticUnaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);
		assertEquals(2, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test the getOperator
	 */
	@Test
	public void testGetOperator() {
		PA.setValue(this.node, "operator", null);
		assertEquals(null, this.node.getOperator());

		PA.setValue(this.node, "operator", UnaryOperator.MINUS);
		assertEquals(UnaryOperator.MINUS, this.node.getOperator());

		PA.setValue(this.node, "operator", UnaryOperator.LOGICAL_NEGATE);
		assertEquals(UnaryOperator.LOGICAL_NEGATE, this.node.getOperator());
	}

	/**
	 * Test the getRightValue method for null
	 */
	@Test
	public void testGetRightValueNull() {
		ExpressionNode actualValue = this.node.getRightValue();
		assertEquals(null, actualValue);
	}

	/**
	 * Test the getRightValue method
	 */
	@Test
	public void testGetRightValue() {
		ExpressionNode rightTestValue = new ArithmeticUnaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);

		ExpressionNode actualValue = this.node.getRightValue();
		assertSame(rightTestValue, actualValue);
	}

	/**
	 * Test getNumberOfNodes with complex right expression
	 */
	@Test
	public void testGetNumberOfNodesComplexRight() {
		ExpressionNode rightTestValue = new ArithmeticUnaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);

		ExpressionNode innerRightTestValue = new ArithmeticUnaryExpressionNodeImpl();
		PA.setValue(rightTestValue, "rightExpression", innerRightTestValue);

		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test the setRightValue
	 */
	@Test
	public void testSetRightValue() {
		ExpressionNode rightValue = new ArithmeticUnaryExpressionNodeImpl();
		this.node.setRightValue(rightValue);

		ExpressionNode actualExpression = (ExpressionNode) PA.getValue(
				this.node, "rightExpression");
		assertSame(rightValue, actualExpression);
	}

	/**
	 * Test the setRightValue with null
	 */
	@Test
	public void testSetRightValueNull() {
		try {
			this.node.setRightValue(null);
		} catch (IllegalArgumentException x) {
			return;
		}
		fail("Expected IllegalArgumentException was not thrown!");
	}

	/**
	 * Test the setOperator
	 */
	@Test
	public void testSetOperator() {
		this.node.setOperator(UnaryOperator.LOGICAL_NEGATE);
		assertEquals(UnaryOperator.LOGICAL_NEGATE,
				PA.getValue(this.node, "operator"));

		this.node.setOperator(UnaryOperator.MINUS);
		assertEquals(UnaryOperator.MINUS, PA.getValue(this.node, "operator"));
	}

}
