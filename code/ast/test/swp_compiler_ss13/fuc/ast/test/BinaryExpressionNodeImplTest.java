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
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;

/**
 * 
 * @author Danny Maasch
 * @version 1
 * 
 */
public class BinaryExpressionNodeImplTest {

	/**
	 * the node under test
	 */
	private BinaryExpressionNode node;

	/**
	 * Set up test environment
	 * 
	 * @throws Exception
	 *             set up failed
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

	/**
	 * Test getNumberOfNodes for empty expressions
	 */
	@Test
	public void testGetNumberOfNodesEmpty() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with left expression
	 */
	@Test
	public void testGetNumberOfNodesLeft() {
		ExpressionNode leftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "leftExpression", leftTestValue);
		assertEquals(2, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with complex left expression
	 */
	@Test
	public void testGetNumberOfNodesComplexLeft() {
		ExpressionNode leftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "leftExpression", leftTestValue);

		ExpressionNode innerLeftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(leftTestValue, "leftExpression", innerLeftTestValue);

		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with right expression
	 */
	@Test
	public void testGetNumberOfNodesRight() {
		ExpressionNode rightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);
		assertEquals(2, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with complex right expression
	 */
	@Test
	public void testGetNumberOfNodesComplexRight() {
		ExpressionNode rightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);

		ExpressionNode innerRightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(rightTestValue, "rightExpression", innerRightTestValue);

		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with left and right expression
	 */
	@Test
	public void testGetNumberOfNodes() {
		ExpressionNode rightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);
		ExpressionNode leftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "leftExpression", leftTestValue);
		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getNumberOfNodes with complex left and right expression
	 */
	@Test
	public void testGetNumberOfNodesComplex() {
		ExpressionNode rightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);

		ExpressionNode innerRightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(rightTestValue, "rightExpression", innerRightTestValue);

		ExpressionNode leftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "leftExpression", leftTestValue);

		ExpressionNode innerLeftTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(leftTestValue, "leftExpression", innerLeftTestValue);

		assertEquals(5, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test the getOperator
	 */
	@Test
	public void testGetOperator() {
		PA.setValue(this.node, "operator", null);
		assertEquals(null, this.node.getOperator());

		PA.setValue(this.node, "operator", BinaryOperator.ADDITION);
		assertEquals(BinaryOperator.ADDITION, this.node.getOperator());

		PA.setValue(this.node, "operator", BinaryOperator.MULTIPLICATION);
		assertEquals(BinaryOperator.MULTIPLICATION, this.node.getOperator());

		PA.setValue(this.node, "operator", BinaryOperator.LOGICAL_AND);
		assertEquals(BinaryOperator.LOGICAL_AND, this.node.getOperator());
	}

	/**
	 * Test the getRightValue method
	 */
	@Test
	public void testGetRightValue() {
		ExpressionNode rightTestValue = new ArithmeticBinaryExpressionNodeImpl();
		PA.setValue(this.node, "rightExpression", rightTestValue);

		ExpressionNode actualValue = this.node.getRightValue();
		assertSame(rightTestValue, actualValue);
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
	 * Test the setLeftValue with null
	 */
	@Test
	public void testSetLeftValueNull() {
		try {
			this.node.setLeftValue(null);
		} catch (IllegalArgumentException x) {
			return;
		}
		fail("Expected IllegalArgumentException was not thrown!");
	}

	/**
	 * Test the setLeftValue
	 */
	@Test
	public void testSetLeftValue() {
		ExpressionNode leftExpression = new ArithmeticBinaryExpressionNodeImpl();
		this.node.setLeftValue(leftExpression);

		ExpressionNode actualExpression = (ExpressionNode) PA.getValue(
				this.node, "leftExpression");
		assertSame(leftExpression, actualExpression);
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
	 * Test the setRightValue
	 */
	@Test
	public void testSetRightValue() {
		ExpressionNode rightValue = new ArithmeticBinaryExpressionNodeImpl();
		this.node.setRightValue(rightValue);

		ExpressionNode actualExpression = (ExpressionNode) PA.getValue(
				this.node, "rightExpression");
		assertSame(rightValue, actualExpression);
	}

	/**
	 * Test the setOperator
	 */
	@Test
	public void testSetOperator() {
		this.node.setOperator(BinaryOperator.LESSTHAN);
		assertEquals(BinaryOperator.LESSTHAN,
				PA.getValue(this.node, "operator"));

		this.node.setOperator(BinaryOperator.INEQUAL);
		assertEquals(BinaryOperator.INEQUAL, PA.getValue(this.node, "operator"));

		this.node.setOperator(BinaryOperator.DIVISION);
		assertEquals(BinaryOperator.DIVISION,
				PA.getValue(this.node, "operator"));
	}

	/**
	 * Test setParent with null
	 */
	@Test
	public void testSetParentNull() {
		this.node.setParentNode(null);
		assertEquals(null, PA.getValue(this.node, "parent"));
	}

	/**
	 * Test setParent
	 */
	@Test
	public void testSetParent() {
		ExpressionNode parent = new ArithmeticBinaryExpressionNodeImpl();
		this.node.setParentNode(parent);
		assertEquals(parent, PA.getValue(this.node, "parent"));
	}

}
