package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ASTImplTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		this.ast = new ASTImpl();
	}

	@Test
	public void testASTImpl() {
		BlockNode prog = new BlockNodeImpl();
		this.ast = new ASTImpl(prog);
		assertSame(prog, PA.getValue(this.ast, "rootNode"));

		try {
			new ASTImpl(null);
			fail("IllegalArgumentException expected");
		} catch (IllegalArgumentException e) {

		}
	}

	@Test
	public void testGetRootNode() {
		assertTrue(this.ast.getRootNode() instanceof BlockNode);
		BlockNode prog = new BlockNodeImpl();
		PA.setValue(this.ast, "rootNode", prog);
		assertSame(prog, this.ast.getRootNode());
	}

	@Test
	public void testSetRootNode() {
		BlockNode prog = new BlockNodeImpl();
		this.ast.setRootNode(prog);
		assertSame(prog, PA.getValue(this.ast, "rootNode"));
		try {
			this.ast.setRootNode(null);
			fail("expected IllegalArgumentException");
		} catch (IllegalArgumentException e) {
		}
	}

	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.ast.getNumberOfNodes());
		BlockNode prog = new BlockNodeImpl();
		PA.setValue(this.ast, "rootNode", prog);
		assertEquals(1, (int) this.ast.getNumberOfNodes());
		DeclarationNode decl = new DeclarationNodeImpl();
		prog.addDeclaration(decl);
		assertEquals(2, (int) this.ast.getNumberOfNodes());
	}

	@Test
	public void testGetDFSLTRIterator() {
		Iterator<ASTNode> it = this.ast.getDFSLTRIterator();
		assertTrue(it.hasNext());
		assertTrue(it.next() instanceof BlockNode);
		assertTrue(!it.hasNext());

		BlockNode prog = new BlockNodeImpl();
		PA.setValue(this.ast, "rootNode", prog);

		it = this.ast.getDFSLTRIterator();
		assertTrue(it.hasNext());
		assertSame(prog, it.next());
		assertTrue(!it.hasNext());

		DeclarationNode decl = new DeclarationNodeImpl();
		prog.addDeclaration(decl);

		it = this.ast.getDFSLTRIterator();
		assertTrue(it.hasNext());
		assertSame(prog, it.next());
		assertTrue(it.hasNext());
		assertSame(decl, it.next());
		assertTrue(!it.hasNext());

		try {
			it.remove();
			fail("expected unsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}

	}

	@Test
	public void testGetRootSymbolTable() {
		assertEquals(null, this.ast.getRootSymbolTable());

		BlockNode prog = new BlockNodeImpl();
		PA.setValue(this.ast, "rootNode", prog);

		assertEquals(null, this.ast.getRootSymbolTable());

		SymbolTableImpl st = new SymbolTableImpl();
		prog.setSymbolTable(st);

		assertSame(st, this.ast.getRootSymbolTable());

	}

}
