package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.WhileNodeImpl;

public class WhileNodeImplTest {

	private WhileNode node = new WhileNodeImpl();

	@Before
	public void setUp() throws Exception {
		this.node = new WhileNodeImpl();
	}

	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.WhileNode, this.node.getNodeType());
	}

	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());

		ExpressionNode expr = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "condition", expr);
		assertEquals(2, (int) this.node.getNumberOfNodes());

		BlockNode block = new BlockNodeImpl();
		PA.setValue(this.node, "body", block);
		assertEquals(3, (int) this.node.getNumberOfNodes());
	}

	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());

		ExpressionNode expr = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "condition", expr);
		assertEquals(1, this.node.getChildren().size());
		assertSame(expr, this.node.getChildren().get(0));

		BlockNode block = new BlockNodeImpl();
		PA.setValue(this.node, "body", block);
		assertEquals(2, this.node.getChildren().size());
		assertSame(expr, this.node.getChildren().get(0));
		assertSame(block, this.node.getChildren().get(1));
	}

	@Test
	public void testSetLoopBody() {
		try {
			this.node.setLoopBody(null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
		BlockNode block = new BlockNodeImpl();
		this.node.setLoopBody(block);
		assertSame(block, PA.getValue(this.node, "body"));
	}

	@Test
	public void testGetLoopBody() {
		assertEquals(null, this.node.getLoopBody());
		BlockNode block = new BlockNodeImpl();
		PA.setValue(this.node, "body", block);
		assertSame(block, this.node.getLoopBody());
	}

	@Test
	public void testSetCondition() {
		try {
			this.node.setCondition(null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
		ExpressionNode expr = new BasicIdentifierNodeImpl();
		this.node.setCondition(expr);
		assertSame(expr, PA.getValue(this.node, "condition"));
	}

	@Test
	public void testGetCondition() {
		assertEquals(null, this.node.getCondition());
		ExpressionNode expr = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "condition", expr);
		assertSame(expr, this.node.getCondition());
	}
}
