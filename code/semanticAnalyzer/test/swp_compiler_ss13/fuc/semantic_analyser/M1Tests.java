package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
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
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

// TODO add missing tests and adjust existing to interface modifications
public class M1Tests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public M1Tests() {
	}

	@Before
	public void setUp() {
		this.log = new ReportLogImpl();
		this.analyser = new SemanticAnalyser(this.log);
	}

	@After
	public void tearDown() {
		this.analyser = null;
		this.log = null;
	}

	/**
	 * # error: id spam is not initialized and returned<br/>
	 * long spam;<br/>
	 * return spam;
	 */
	@Test
	public void testErrorUndefReturnProg() {
		// long spam;
		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("spam");
		declaration.setType(new LongType());

		// return spam;
		BasicIdentifierNode identifier = new BasicIdentifierNodeImpl();
		identifier.setIdentifier("spam");
		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier);
		identifier.setParentNode(returnNode);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("spam", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		declaration.setParentNode(blockNode);
		returnNode.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		assertFalse(this.log.hasErrors());
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
		// long l;
		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");
		declaration.setType(new LongType());

		// l = ...;
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");

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
		literal10.setParentNode(add1);
		literal23_1.setParentNode(add1);
		ArithmeticBinaryExpressionNode sub1 = new ArithmeticBinaryExpressionNodeImpl();
		sub1.setOperator(BinaryOperator.SUBSTRACTION);
		sub1.setLeftValue(add1);
		sub1.setRightValue(literal23_2);
		add1.setParentNode(sub1);
		literal23_2.setParentNode(sub1);
		ArithmeticBinaryExpressionNode div1 = new ArithmeticBinaryExpressionNodeImpl();
		div1.setOperator(BinaryOperator.DIVISION);
		div1.setLeftValue(literal100);
		div1.setRightValue(literal2);
		literal100.setParentNode(div1);
		literal2.setParentNode(div1);
		ArithmeticBinaryExpressionNode add2 = new ArithmeticBinaryExpressionNodeImpl();
		add2.setOperator(BinaryOperator.ADDITION);
		add2.setLeftValue(sub1);
		add2.setRightValue(div1);
		sub1.setParentNode(add2);
		div1.setParentNode(add2);
		ArithmeticBinaryExpressionNode add3 = new ArithmeticBinaryExpressionNodeImpl();
		add3.setOperator(BinaryOperator.ADDITION);
		add3.setLeftValue(add2);
		add3.setRightValue(literal30);
		add2.setParentNode(add3);
		literal30.setParentNode(add3);
		ArithmeticBinaryExpressionNode div2 = new ArithmeticBinaryExpressionNodeImpl();
		div2.setOperator(BinaryOperator.DIVISION);
		div2.setLeftValue(literal9);
		div2.setRightValue(literal3);
		literal9.setParentNode(div2);
		literal3.setParentNode(div2);
		ArithmeticBinaryExpressionNode sub2 = new ArithmeticBinaryExpressionNodeImpl();
		sub2.setOperator(BinaryOperator.SUBSTRACTION);
		sub2.setLeftValue(add3);
		sub2.setRightValue(div2);
		add3.setParentNode(sub2);
		div2.setParentNode(sub2);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier_l1);
		assignment.setRightValue(sub2);
		identifier_l1.setParentNode(assignment);
		sub2.setParentNode(assignment);

		// return l;
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier_l2);
		identifier_l2.setParentNode(returnNode);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		declaration.setParentNode(blockNode);
		assignment.setParentNode(blockNode);
		returnNode.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		this.analyser.analyse(ast);
		assertFalse(this.log.hasErrors());
	}

	/**
	 * # return 6 <br/>
	 * long l;<br/>
	 * l = 3 + 3;<br/>
	 * return l;
	 */
	@Test
	public void simpleAddProg() {
		// long l;
		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");
		declaration.setType(new LongType());

		// l = 3 + 3;
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");
		LiteralNode literal3_1 = new LiteralNodeImpl();
		literal3_1.setLiteral("3");
		literal3_1.setLiteralType(new LongType());
		LiteralNode literal3_2 = new LiteralNodeImpl();
		literal3_2.setLiteral("3");
		literal3_2.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode add = new ArithmeticBinaryExpressionNodeImpl();
		add.setOperator(BinaryOperator.ADDITION);
		add.setLeftValue(literal3_1);
		add.setRightValue(literal3_2);
		literal3_1.setParentNode(add);
		literal3_2.setParentNode(add);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier_l1);
		assignment.setRightValue(add);
		identifier_l1.setParentNode(assignment);
		add.setParentNode(assignment);

		// return l;
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier_l2);
		identifier_l2.setParentNode(returnNode);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		declaration.setParentNode(blockNode);
		assignment.setParentNode(blockNode);
		returnNode.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		this.analyser.analyse(ast);

		assertFalse(this.log.hasErrors());
	}

	/**
	 * # return 9 <br/>
	 * long l;<br/>
	 * l = 3 * 3;<br/>
	 * return l;
	 */
	@Test
	public void simpleMulProg() {
		// long l;
		DeclarationNode declaration = new DeclarationNodeImpl();
		declaration.setIdentifier("l");
		declaration.setType(new LongType());

		// l = 3 * 3;
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");
		LiteralNode literal3_1 = new LiteralNodeImpl();
		literal3_1.setLiteral("3");
		literal3_1.setLiteralType(new LongType());
		LiteralNode literal3_2 = new LiteralNodeImpl();
		literal3_2.setLiteral("3");
		literal3_2.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode mul = new ArithmeticBinaryExpressionNodeImpl();
		mul.setOperator(BinaryOperator.MULTIPLICATION);
		mul.setLeftValue(literal3_1);
		mul.setRightValue(literal3_2);
		literal3_1.setParentNode(mul);
		literal3_2.setParentNode(mul);

		AssignmentNode assignment = new AssignmentNodeImpl();
		assignment.setLeftValue(identifier_l1);
		assignment.setRightValue(mul);
		identifier_l1.setParentNode(assignment);
		mul.setParentNode(assignment);

		// return l;
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier_l2);
		identifier_l2.setParentNode(returnNode);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration);
		blockNode.addStatement(assignment);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		declaration.setParentNode(blockNode);
		assignment.setParentNode(blockNode);
		returnNode.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		this.analyser.analyse(ast);

		assertFalse(this.log.hasErrors());
	}

}
