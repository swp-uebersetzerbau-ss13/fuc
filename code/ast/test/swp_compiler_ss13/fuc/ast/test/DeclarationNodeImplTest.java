package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.EmptyIterator;

public class DeclarationNodeImplTest {

	private DeclarationNode node;

	@Before
	public void setUp() throws Exception {
		this.node = new DeclarationNodeImpl();
	}

	@Test
	public void testGetDFSLTRNodeIterator() {
		assertTrue(this.node.getDFSLTRNodeIterator() instanceof EmptyIterator);
	}

	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());
	}

	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());
	}

	@Test
	public void testGetIdentifier() {
		assertEquals(null, this.node.getIdentifier());
		PA.setValue(this.node, "identifier", "");
		assertEquals("", this.node.getIdentifier());
		PA.setValue(this.node, "identifier", "test");
		assertEquals("test", this.node.getIdentifier());
	}

	@Test
	public void testGetType() {
		assertEquals(null, this.node.getType());
		Type t = new LongType();
		PA.setValue(this.node, "type", t);
		assertSame(t, this.node.getType());
	}

	@Test
	public void testSetIdentifier() {
		try {
			this.node.setIdentifier(null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
		this.node.setIdentifier("");
		assertEquals("", PA.getValue(this.node, "identifier"));
		this.node.setIdentifier("test");
		assertEquals("test", PA.getValue(this.node, "identifier"));
	}

	@Test
	public void testSetType() {
		try {
			this.node.setType(null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
		Type t = new LongType();
		this.node.setType(t);
		assertSame(t, PA.getValue(this.node, "type"));
	}

	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.DeclarationNode, this.node.getNodeType());
	}
}
