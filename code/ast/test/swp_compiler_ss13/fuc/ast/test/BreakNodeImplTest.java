package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;
import swp_compiler_ss13.fuc.ast.BreakNodeImpl;
import swp_compiler_ss13.fuc.ast.EmptyIterator;

public class BreakNodeImplTest {

	private BreakNode node;

	@Before
	public void setUp() throws Exception {
		this.node = new BreakNodeImpl();
	}

	@Test
	public void testGetDFSLTRNodeIterator() {
		assertTrue(this.node.getDFSLTRNodeIterator() instanceof EmptyIterator);
	}

	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.BreakNode, this.node.getNodeType());
	}

	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());
	}

}
