package swp_compiler_ss13.fuc.ast.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.ASTNode.ASTNodeType;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;

/**
 * Test the block node
 * 
 * @author "Frank Zechert"
 */
public class BlockNodeImplTest {

	/**
	 * the node under test
	 */
	private BlockNode node;

	/**
	 * set up the test
	 * 
	 * @throws Exception
	 *             set up failed
	 */
	@Before
	public void setUp() throws Exception {
		this.node = new BlockNodeImpl();
	}

	/**
	 * Test the getNodeType()
	 */
	@Test
	public void testGetNodeType() {
		assertEquals(ASTNodeType.BlockNode, this.node.getNodeType());
	}

	/**
	 * test getnumberofnodes
	 */
	@Test
	public void testGetNumberOfNodes() {
		assertEquals(1, (int) this.node.getNumberOfNodes());

		this.node.addDeclaration(new DeclarationNodeImpl());
		assertEquals(2, (int) this.node.getNumberOfNodes());

		this.node.addDeclaration(new DeclarationNodeImpl());
		assertEquals(3, (int) this.node.getNumberOfNodes());

		this.node.addStatement(new BlockNodeImpl());
		assertEquals(4, (int) this.node.getNumberOfNodes());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(new DeclarationNodeImpl());
		blockNode.addStatement(new BlockNodeImpl());
		this.node.addStatement(blockNode);
		assertEquals(7, (int) this.node.getNumberOfNodes());
	}

	/**
	 * Test getChildren()
	 */
	@Test
	public void testGetChildren() {
		assertEquals(0, this.node.getChildren().size());

		this.node.addDeclaration(new DeclarationNodeImpl());
		assertEquals(1, this.node.getChildren().size());

		this.node.addDeclaration(new DeclarationNodeImpl());
		assertEquals(2, this.node.getChildren().size());

		this.node.addStatement(new BlockNodeImpl());
		assertEquals(3, this.node.getChildren().size());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(new DeclarationNodeImpl());
		blockNode.addStatement(new BlockNodeImpl());
		this.node.addStatement(blockNode);
		assertEquals(4, this.node.getChildren().size());
	}

	/**
	 * test addDeclaration
	 */
	@Test
	public void testAddDeclaration() {
		DeclarationNode n1 = new DeclarationNodeImpl();
		DeclarationNode n2 = new DeclarationNodeImpl();
		DeclarationNode n3 = new DeclarationNodeImpl();
		DeclarationNode n4 = new DeclarationNodeImpl();

		this.node.addDeclaration(n1);
		this.node.addDeclaration(n2);
		this.node.addDeclaration(n3);
		this.node.addDeclaration(n4);

		@SuppressWarnings("unchecked")
		List<DeclarationNode> declarationNodes = (List<DeclarationNode>) PA.getValue(this.node, "declarationNodes");

		assertTrue(declarationNodes.contains(n1));
		assertTrue(declarationNodes.contains(n2));
		assertTrue(declarationNodes.contains(n3));
		assertTrue(declarationNodes.contains(n4));
	}

	/**
	 * test addStatement
	 */
	@Test
	public void testAddStatement() {
		StatementNode n1 = new BlockNodeImpl();
		StatementNode n2 = new BlockNodeImpl();
		StatementNode n3 = new BlockNodeImpl();
		StatementNode n4 = new BlockNodeImpl();

		this.node.addStatement(n1);
		this.node.addStatement(n2);
		this.node.addStatement(n3);
		this.node.addStatement(n4);

		@SuppressWarnings("unchecked")
		List<StatementNode> statementNodes = (List<StatementNode>) PA.getValue(this.node, "statementNodes");

		assertTrue(statementNodes.contains(n1));
		assertTrue(statementNodes.contains(n2));
		assertTrue(statementNodes.contains(n3));
		assertTrue(statementNodes.contains(n4));
	}

	/**
	 * test getDeclarationList
	 */
	@Test
	public void testGetDeclarationList() {
		DeclarationNode n1 = new DeclarationNodeImpl();
		DeclarationNode n2 = new DeclarationNodeImpl();
		DeclarationNode n3 = new DeclarationNodeImpl();
		DeclarationNode n4 = new DeclarationNodeImpl();

		List<DeclarationNode> declarationNodes = new LinkedList<>();
		declarationNodes.add(n1);
		declarationNodes.add(n2);
		declarationNodes.add(n3);
		declarationNodes.add(n4);

		PA.setValue(this.node, "declarationNodes", declarationNodes);
		assertSame(declarationNodes, this.node.getDeclarationList());
	}

	/**
	 * test getStatementList
	 */
	@Test
	public void testGetStatementList() {
		StatementNode n1 = new BlockNodeImpl();
		StatementNode n2 = new BlockNodeImpl();
		StatementNode n3 = new BlockNodeImpl();
		StatementNode n4 = new BlockNodeImpl();

		List<StatementNode> statementNodes = new LinkedList<>();
		statementNodes.add(n1);
		statementNodes.add(n2);
		statementNodes.add(n3);
		statementNodes.add(n4);

		PA.setValue(this.node, "statementNodes", statementNodes);
		assertSame(statementNodes, this.node.getStatementList());
	}

	/**
	 * test getDeclarationIterator
	 */
	@Test
	public void testGetDeclarationIterator() {
		DeclarationNode n1 = new DeclarationNodeImpl();
		DeclarationNode n2 = new DeclarationNodeImpl();
		DeclarationNode n3 = new DeclarationNodeImpl();
		DeclarationNode n4 = new DeclarationNodeImpl();

		List<DeclarationNode> declarationNodes = new LinkedList<>();
		declarationNodes.add(n1);
		declarationNodes.add(n2);
		declarationNodes.add(n3);
		declarationNodes.add(n4);

		PA.setValue(this.node, "declarationNodes", declarationNodes);
		Iterator<DeclarationNode> expectedIterator = declarationNodes.iterator();
		Iterator<DeclarationNode> actualIterator = this.node.getDeclarationIterator();
		while (expectedIterator.hasNext()) {
			if (actualIterator.hasNext()) {
				assertEquals(expectedIterator.next(), actualIterator.next());
			} else {
				fail("actualIterator has too few elements");
			}
		}
		if (actualIterator.hasNext()) {
			fail("actualIterator has too many elements");
		}
	}

	/**
	 * test getStatementIterator
	 */
	@Test
	public void testGetStatementIterator() {
		StatementNode n1 = new BlockNodeImpl();
		StatementNode n2 = new BlockNodeImpl();
		StatementNode n3 = new BlockNodeImpl();
		StatementNode n4 = new BlockNodeImpl();

		List<StatementNode> statementNodes = new LinkedList<>();
		statementNodes.add(n1);
		statementNodes.add(n2);
		statementNodes.add(n3);
		statementNodes.add(n4);

		PA.setValue(this.node, "statementNodes", statementNodes);
		Iterator<StatementNode> expectedIterator = statementNodes.iterator();
		Iterator<StatementNode> actualIterator = this.node.getStatementIterator();
		while (expectedIterator.hasNext()) {
			if (actualIterator.hasNext()) {
				assertEquals(expectedIterator.next(), actualIterator.next());
			} else {
				fail("actualIterator has too few elements");
			}
		}
		if (actualIterator.hasNext()) {
			fail("actualIterator has too many elements");
		}
	}

	/**
	 * test getNumberOfDeclarations
	 */
	@Test
	public void testGetNumberOfDeclarations() {
		DeclarationNode n1 = new DeclarationNodeImpl();
		DeclarationNode n2 = new DeclarationNodeImpl();
		DeclarationNode n3 = new DeclarationNodeImpl();
		DeclarationNode n4 = new DeclarationNodeImpl();

		List<DeclarationNode> declarationNodes = new LinkedList<>();
		PA.setValue(this.node, "declarationNodes", declarationNodes);

		declarationNodes.add(n1);
		assertEquals(1, (int) this.node.getNumberOfDeclarations());
		declarationNodes.add(n2);
		assertEquals(2, (int) this.node.getNumberOfDeclarations());
		declarationNodes.add(n3);
		assertEquals(3, (int) this.node.getNumberOfDeclarations());
		declarationNodes.add(n4);
		assertEquals(4, (int) this.node.getNumberOfDeclarations());

	}

	/**
	 * Test getNumberOfStatements
	 */
	@Test
	public void testGetNumberOfStatements() {
		StatementNode n1 = new BlockNodeImpl();
		StatementNode n2 = new BlockNodeImpl();
		StatementNode n3 = new BlockNodeImpl();
		StatementNode n4 = new BlockNodeImpl();

		List<StatementNode> statementNodes = new LinkedList<>();
		PA.setValue(this.node, "statementNodes", statementNodes);

		statementNodes.add(n1);
		assertEquals(1, (int) this.node.getNumberOfStatements());
		statementNodes.add(n2);
		assertEquals(2, (int) this.node.getNumberOfStatements());
		statementNodes.add(n3);
		assertEquals(3, (int) this.node.getNumberOfStatements());
		statementNodes.add(n4);
		assertEquals(4, (int) this.node.getNumberOfStatements());

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
