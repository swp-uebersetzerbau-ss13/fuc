package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;

public class SemanticAnalyserTest {

	private SemanticAnalyser analyzer;
	private TestReportLog log;

	public SemanticAnalyserTest() {}

	@BeforeClass
	public static void setUpClass() {}

	@AfterClass
	public static void tearDownClass() {}

	@Before
	public void setUp() {
		log = new TestReportLog();
		analyzer = new SemanticAnalyser(log);
	}

	@After
	public void tearDown() {
		analyzer = null;
		log = null;
	}

	@Test
	public void testInitialization() {
		AST ast = createErrorUndefReturnAst();
		analyzer.analyse(ast);
		assertEquals(1, log.errors.size());
		// TODO report log contains error
	}

	private AST createErrorUndefReturnAst() {
		SymbolTable symbolTable = new TestSymbolTable();
		symbolTable.insert("spam", new LongType());
		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("spam");
		BasicIdentifierNode identifier = new BasicIdentifierNodeImpl();
		identifier.setIdentifier("spam");
		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier);
		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		return ast;
	}

	/**
	 * Test of analyse method, of class SemanticAnalyser.
	 */
	@Test
	public void testAnalyse() {
		System.out.println("analyse");
		swp_compiler_ss13.common.ast.AST ast = null;
		swp_compiler_ss13.common.parser.ReportLog log = null;
		SemanticAnalyser instance = new SemanticAnalyser(log);
		swp_compiler_ss13.common.ast.AST expResult = null;
		swp_compiler_ss13.common.ast.AST result = instance.analyse(ast);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to
		// fail.
		fail("The test case is a prototype.");
	}
}