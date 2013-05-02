package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;

public class SemanticAnalyserTest {

	private SemanticAnalyser analyzer;
	private TestReportLog log;

	public SemanticAnalyserTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

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

	/**
	 * # error: id spam is not initialized and returned<br/>
	 * long spam;<br/>
	 * return spam;
	 */
	@Test
	public void testInitialization() {
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
		analyzer.analyse(ast);
		assertEquals(1, log.errors.size());
		// TODO report log contains error
	}

	/**
	 * # return 27<br/>
	 * long l;<br/>
	 * l = 10 +<br/>
	 * 23 # - 23<br/>
	 * - 23<br/>
	 * + 100 /<br/>
	 * <br/>
	 * 2<br/>
	 * - 30<br/>
	 * - 9 / 3;<br/>
	 * return l;
	 */
	@Test
	public void testAddProg() {
		SymbolTable symbolTable = new TestSymbolTable();
		symbolTable.insert("l", new LongType());

		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");

		BasicIdentifierNode identifier = new BasicIdentifierNodeImpl();
		identifier.setIdentifier("l");

		LiteralNode literal10 = new LiteralNodeImpl();
		literal10.setLiteral("10");
		literal10.setLiteralType(new LongType());
		LiteralNode literal23_1 = new LiteralNodeImpl();
		literal23_1.setLiteral("23");
		literal23_1.setLiteralType(new LongType());
		LiteralNode literal23_2 = new LiteralNodeImpl();
		literal23_2.setLiteral("23");
		literal23_2.setLiteralType(new LongType());
		LiteralNode literal100 = new LiteralNodeImpl();
		literal100.setLiteral("100");
		literal100.setLiteralType(new LongType());
		LiteralNode literal2 = new LiteralNodeImpl();
		literal2.setLiteral("2");
		literal2.setLiteralType(new LongType());
		LiteralNode literal30 = new LiteralNodeImpl();
		literal30.setLiteral("30");
		literal30.setLiteralType(new LongType());
		LiteralNode literal9 = new LiteralNodeImpl();
		literal9.setLiteral("9");
		literal9.setLiteralType(new LongType());
		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");
		literal3.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode add1 = new ArithmeticBinaryExpressionNodeImpl();
		add1.setOperator(BinaryOperator.ADDITION);
		add1.setLeftValue(literal10);
		add1.setRightValue(literal23_1);
		ArithmeticBinaryExpressionNode sub1 = new ArithmeticBinaryExpressionNodeImpl();
		sub1.setOperator(BinaryOperator.SUBSTRACTION);
		sub1.setLeftValue(add1);
		sub1.setRightValue(literal23_2);
		ArithmeticBinaryExpressionNode div1 = new ArithmeticBinaryExpressionNodeImpl();
		div1.setOperator(BinaryOperator.DIVISION);
		div1.setLeftValue(literal100);
		div1.setRightValue(literal2);
		ArithmeticBinaryExpressionNode add2 = new ArithmeticBinaryExpressionNodeImpl();
		add2.setOperator(BinaryOperator.ADDITION);
		add2.setLeftValue(sub1);
		add2.setRightValue(div1);
		ArithmeticBinaryExpressionNode add3 = new ArithmeticBinaryExpressionNodeImpl();
		add3.setOperator(BinaryOperator.ADDITION);
		add3.setLeftValue(add2);
		add3.setRightValue(literal30);
		ArithmeticBinaryExpressionNode div2 = new ArithmeticBinaryExpressionNodeImpl();
		div2.setOperator(BinaryOperator.DIVISION);
		div2.setLeftValue(literal9);
		div2.setRightValue(literal3);
		ArithmeticBinaryExpressionNode sub2 = new ArithmeticBinaryExpressionNodeImpl();
		sub2.setOperator(BinaryOperator.SUBSTRACTION);
		sub2.setLeftValue(add3);
		sub2.setRightValue(div2);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier);
		assignment.setRightValue(sub2);

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier);

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);

		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyzer.analyse(ast);
		assertTrue(log.errors.isEmpty());
	}

	/**
	 * # returns 8 or does it?<br/>
	 * long l;<br/>
	 * l = ( 3 + 3 ) * 2 - ( l = ( 2 + ( 16 / 8 ) ) );<br/>
	 * return l;
	 */
	@Test
	public void parathesesProg() {
		SymbolTable symbolTable = new TestSymbolTable();
		symbolTable.insert("l", new LongType());

		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");

		BasicIdentifierNode identifier = new BasicIdentifierNodeImpl();
		identifier.setIdentifier("l");

		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");
		literal3.setLiteralType(new LongType());
		LiteralNode literal2 = new LiteralNodeImpl();
		literal2.setLiteral("2");
		literal2.setLiteralType(new LongType());
		LiteralNode literal16 = new LiteralNodeImpl();
		literal16.setLiteral("16");
		literal16.setLiteralType(new LongType());
		LiteralNode literal8 = new LiteralNodeImpl();
		literal8.setLiteral("8");
		literal8.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode div1 = new ArithmeticBinaryExpressionNodeImpl();
		div1.setOperator(BinaryOperator.DIVISION);
		div1.setLeftValue(literal16);
		div1.setRightValue(literal8);
		ArithmeticBinaryExpressionNode add2 = new ArithmeticBinaryExpressionNodeImpl();
		add2.setOperator(BinaryOperator.ADDITION);
		add2.setLeftValue(literal2);
		add2.setRightValue(div1);

		AssignmentNode assignment2 = new AssignmentNodeImpl();
		assignment2.setLeftValue(identifier);
		assignment2.setRightValue(add2);

		ArithmeticBinaryExpressionNode add1 = new ArithmeticBinaryExpressionNodeImpl();
		add1.setOperator(BinaryOperator.ADDITION);
		add1.setLeftValue(literal3);
		add1.setRightValue(literal3);
		ArithmeticBinaryExpressionNode mul1 = new ArithmeticBinaryExpressionNodeImpl();
		mul1.setOperator(BinaryOperator.MULTIPLICATION);
		mul1.setLeftValue(add1);
		mul1.setRightValue(literal2);
		ArithmeticBinaryExpressionNode sub1 = new ArithmeticBinaryExpressionNodeImpl();
		sub1.setOperator(BinaryOperator.SUBSTRACTION);
		sub1.setLeftValue(mul1);
		sub1.setRightValue(identifier);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier);
		assignment.setRightValue(sub1);

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier);

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment2);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);

		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyzer.analyse(ast);
		assertTrue(log.errors.isEmpty());
	}

	/**
	 * # return 6 <br/>
	 * long l;<br/>
	 * l = 3 + 3;<br/>
	 * return l;
	 */
	@Test
	public void simpleAddProg() {
		SymbolTable symbolTable = new TestSymbolTable();
		symbolTable.insert("l", new LongType());

		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");

		BasicIdentifierNode identifier = new BasicIdentifierNodeImpl();
		identifier.setIdentifier("l");

		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");

		ArithmeticBinaryExpressionNode add = new ArithmeticBinaryExpressionNodeImpl();
		add.setOperator(BinaryOperator.ADDITION);
		add.setLeftValue(literal3);
		add.setRightValue(literal3);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier);
		assignment.setRightValue(add);

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier);

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);

		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyzer.analyse(ast);
		assertTrue(log.errors.isEmpty());
	}

	/**
	 * # return 9 <br/>
	 * long l;<br/>
	 * l = 3 * 3;<br/>
	 * return l;
	 */
	@Test
	public void simpleMulProg() {
		SymbolTable symbolTable = new TestSymbolTable();
		symbolTable.insert("l", new LongType());

		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");

		BasicIdentifierNode identifier = new BasicIdentifierNodeImpl();
		identifier.setIdentifier("l");

		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");

		ArithmeticBinaryExpressionNode mul = new ArithmeticBinaryExpressionNodeImpl();
		mul.setOperator(BinaryOperator.MULTIPLICATION);
		mul.setLeftValue(literal3);
		mul.setRightValue(literal3);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier);
		assignment.setRightValue(mul);

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier);

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);

		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyzer.analyse(ast);
		assertTrue(log.errors.isEmpty());
	}

}