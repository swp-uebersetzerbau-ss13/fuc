package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.NoSuchElementException;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;

public class ASTNodeImplTest {

	private BlockNodeImpl outerBlock;
	private DeclarationNodeImpl declaration1;
	private DeclarationNodeImpl declaration2;
	private DeclarationNodeImpl declaration3;
	private BlockNodeImpl innerBlock;
	private DeclarationNodeImpl declaration4;
	private DeclarationNodeImpl declaration5;
	private DeclarationNodeImpl declaration6;
	private AssignmentNodeImpl statement1;
	private AssignmentNodeImpl statement2;
	private BasicIdentifierNodeImpl id1;
	private BasicIdentifierNodeImpl id2;
	private LiteralNodeImpl literal1;
	private LiteralNodeImpl literal2;

	@Before
	public void setUp() throws Exception {
		this.outerBlock = new BlockNodeImpl();
		this.declaration1 = new DeclarationNodeImpl();
		this.declaration2 = new DeclarationNodeImpl();
		this.declaration3 = new DeclarationNodeImpl();
		this.outerBlock.addDeclaration(this.declaration1);
		this.outerBlock.addDeclaration(this.declaration2);
		this.outerBlock.addDeclaration(this.declaration3);

		this.innerBlock = new BlockNodeImpl();
		this.declaration4 = new DeclarationNodeImpl();
		this.declaration5 = new DeclarationNodeImpl();
		this.declaration6 = new DeclarationNodeImpl();
		this.innerBlock.addDeclaration(this.declaration4);
		this.innerBlock.addDeclaration(this.declaration5);
		this.innerBlock.addDeclaration(this.declaration6);

		this.statement1 = new AssignmentNodeImpl();
		this.statement2 = new AssignmentNodeImpl();

		this.id1 = new BasicIdentifierNodeImpl();
		this.id2 = new BasicIdentifierNodeImpl();
		this.literal1 = new LiteralNodeImpl();
		this.literal2 = new LiteralNodeImpl();
		this.statement1.setLeftValue(this.id1);
		this.statement1.setRightValue(this.literal1);
		this.statement2.setLeftValue(this.id2);
		this.statement2.setRightValue(this.literal2);

		this.outerBlock.addStatement(this.innerBlock);
		this.outerBlock.addStatement(this.statement1);
		this.innerBlock.addStatement(this.statement2);

	}

	@Test
	public void getDFSLTRNodeIteratorTest() {
		Iterator<ASTNode> iterator = this.outerBlock.getDFSLTRNodeIterator();

		assertTrue(iterator.hasNext());
		assertSame(this.declaration1, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.declaration2, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.declaration3, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.innerBlock, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.declaration4, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.declaration5, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.declaration6, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.statement2, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.id2, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.literal2, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.statement1, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.id1, iterator.next());

		assertTrue(iterator.hasNext());
		assertSame(this.literal1, iterator.next());

		assertTrue(!iterator.hasNext());

		try {
			iterator.next();
			fail("expected NoSuchElementException");
		} catch (NoSuchElementException e) {
		}

		try {
			iterator.remove();
			fail("expected UnsupportedOperationException");
		} catch (UnsupportedOperationException e) {
		}
	}
}
