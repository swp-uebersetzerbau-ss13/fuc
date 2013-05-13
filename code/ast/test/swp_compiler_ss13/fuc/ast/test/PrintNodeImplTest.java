package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;
import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.PrintNodeImpl;

public class PrintNodeImplTest {

	private PrintNode node;

	@Before
	public void setUp() throws Exception {
		this.node = new PrintNodeImpl();
	}

	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.PrintNode, this.node.getNodeType());
	}

	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
		BasicIdentifierNode id = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "node", id);
		assertEquals(2, (int) this.node.getNumberOfNodes());
	}

	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());
		BasicIdentifierNode id = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "node", id);
		assertEquals(1, this.node.getChildren().size());
		assertSame(id, this.node.getChildren().get(0));
	}

	@Test
	public void testSetRightValue() {
		try {
			this.node.setRightValue(null);
			fail("Expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
		BasicIdentifierNode id = new BasicIdentifierNodeImpl();
		this.node.setRightValue(id);
		assertSame(id, PA.getValue(this.node, "node"));
	}

	@Test
	public void testGetRightValue() {
		assertEquals(null, this.node.getRightValue());
		BasicIdentifierNode id = new BasicIdentifierNodeImpl();
		PA.setValue(this.node, "node", id);
		assertSame(id, this.node.getRightValue());
	}
}
