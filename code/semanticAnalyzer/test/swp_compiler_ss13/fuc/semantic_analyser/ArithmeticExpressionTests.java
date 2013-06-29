package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ArithmeticExpressionTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public ArithmeticExpressionTests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyser = new SemanticAnalyser();
		analyser.setReportLog(log);
	}

	@After
	public void tearDown() {
		analyser = null;
		log = null;
	}

	/**
	 * # error: usage of boolean within an arithmetic expression<br/>
	 * long l;<br/>
	 * bool b;<br/>
	 * <br/>
	 * l = 1 + b - 2;
	 */
	@Test
	public void testArithmeticExpressionTypeError() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// long b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_l.setType(new BooleanType());

		// l = 1 + b - 2;
		LiteralNode literal_1 = new LiteralNodeImpl();
		literal_1.setLiteral("1");
		literal_1.setLiteralType(new LongType());
		LiteralNode literal_2 = new LiteralNodeImpl();
		literal_2.setLiteral("2");
		literal_2.setLiteralType(new LongType());
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_b = new BasicIdentifierNodeImpl();
		identifier_b.setIdentifier("b");

		ArithmeticBinaryExpressionNode add = new ArithmeticBinaryExpressionNodeImpl();
		add.setOperator(BinaryOperator.ADDITION);
		add.setLeftValue(literal_1);
		add.setRightValue(identifier_b);
		literal_1.setParentNode(add);
		identifier_b.setParentNode(add);

		ArithmeticBinaryExpressionNode sub = new ArithmeticBinaryExpressionNodeImpl();
		sub.setOperator(BinaryOperator.SUBSTRACTION);
		sub.setLeftValue(add);
		sub.setRightValue(literal_2);
		add.setParentNode(sub);
		literal_2.setParentNode(sub);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(sub);
		identifier_l.setParentNode(assignment_l);
		sub.setParentNode(assignment_l);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("b", new BooleanType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_b);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * # error: usage of bool within an unary arithmetic expression<br/>
	 * long l;<br/>
	 * bool b;<br/>
	 * <br/>
	 * l = -b;
	 */
	@Test
	public void testArithmeticUnaryExpressionTypeError() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// long b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());

		// l = -b;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_b = new BasicIdentifierNodeImpl();
		identifier_b.setIdentifier("b");

		ArithmeticUnaryExpressionNode minus_b = new ArithmeticUnaryExpressionNodeImpl();
		minus_b.setOperator(UnaryOperator.MINUS);
		minus_b.setRightValue(identifier_b);
		identifier_b.setParentNode(minus_b);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(minus_b);
		identifier_l.setParentNode(assignment_l);
		minus_b.setParentNode(assignment_l);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("b", new BooleanType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_b);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);

	}

	/**
	 * # no error expected<br/>
	 * long l;<br/>
	 * double d;<br/>
	 * <br/>
	 * l = l + d;
	 */
	@Test
	public void testLongAndDouble() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// double d;
		DeclarationNode declaration_d = new DeclarationNodeImpl();
		declaration_d.setIdentifier("d");
		declaration_d.setType(new DoubleType());

		// l = l + d;
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("d");
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		BasicIdentifierNode identifier_d2 = new BasicIdentifierNodeImpl();
		identifier_d2.setIdentifier("d");

		ArithmeticBinaryExpressionNode add = new ArithmeticBinaryExpressionNodeImpl();
		add.setOperator(BinaryOperator.ADDITION);
		add.setLeftValue(identifier_l2);
		add.setRightValue(identifier_d2);
		identifier_l2.setParentNode(add);
		identifier_d2.setParentNode(add);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l1);
		assignment_l.setRightValue(add);
		identifier_l1.setParentNode(assignment_l);
		add.setParentNode(assignment_l);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("d", new DoubleType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_d);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_d.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		assertFalse(log.hasErrors());
	}

	/**
	 * # error: division by zero<br/>
	 * long l;<br/>
	 * <br/>
	 * l = 2 / 0;
	 */
	@Test
	public void testDivisionByZeroSimple() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		LiteralNode literal_2 = astFactory.newLiteral("2", new LongType());
		LiteralNode literal_0 = astFactory.newLiteral("0", new LongType());
		BinaryExpressionNode div = astFactory.newBinaryExpression(
				BinaryOperator.DIVISION, literal_2, literal_0);
		astFactory.addAssignment(identifier_l, div);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.DIVISION_BY_ZERO);
	}

	/**
	 * # error: division by zero<br/>
	 * long l;<br/>
	 * <br/>
	 * l = 2 / l;
	 */
	@Test
	@Ignore
	public void testDivisionByZeroImplicit() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		IdentifierNode identifier_l2 = astFactory.newBasicIdentifier("l");
		LiteralNode literal_0 = astFactory.newLiteral("0", new LongType());
		BinaryExpressionNode div = astFactory.newBinaryExpression(
				BinaryOperator.DIVISION, identifier_l2, literal_0);
		astFactory.addAssignment(identifier_l, div);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.DIVISION_BY_ZERO);
	}

	/**
	 * # error: division by zero<br/>
	 * long l;<br/>
	 * <br/>
	 * l = 2 / (1 - 1);
	 */
	@Test
	public void testDivisionByZeroComplex() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		LiteralNode literal_2 = astFactory.newLiteral("2", new LongType());
		LiteralNode literal_1_left = astFactory.newLiteral("1", new LongType());
		LiteralNode literal_1_right = astFactory
				.newLiteral("1", new LongType());
		BinaryExpressionNode sub = astFactory.newBinaryExpression(
				BinaryOperator.SUBSTRACTION, literal_1_left, literal_1_right);
		BinaryExpressionNode div = astFactory.newBinaryExpression(
				BinaryOperator.DIVISION, literal_2, sub);
		astFactory.addAssignment(identifier_l, div);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.DIVISION_BY_ZERO);
	}

	/**
	 * <pre>
	 * # error: division by zero
	 * double d;
	 * 
	 * d = 1.0 / ((1 * 2.0 - ((1.6 / 2) + 1.2));
	 * </pre>
	 */
	@Test
	public void testDivisionByZeroComplexDouble() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("d", new DoubleType());
		IdentifierNode identifier_d = astFactory.newBasicIdentifier("d");
		LiteralNode literal_1d0 = astFactory
				.newLiteral("1.0", new DoubleType());
		LiteralNode literal_1 = astFactory.newLiteral("1", new LongType());
		LiteralNode literal_2d0 = astFactory
				.newLiteral("2.0", new DoubleType());
		LiteralNode literal_1d6 = astFactory
				.newLiteral("1.6", new DoubleType());
		LiteralNode literal_2 = astFactory.newLiteral("2", new LongType());
		LiteralNode literal_1d2 = astFactory
				.newLiteral("1.2", new DoubleType());
		BinaryExpressionNode mul = astFactory.newBinaryExpression(
				BinaryOperator.MULTIPLICATION, literal_1, literal_2d0);
		BinaryExpressionNode inner_div = astFactory.newBinaryExpression(
				BinaryOperator.DIVISION, literal_1d6, literal_2);
		BinaryExpressionNode add = astFactory.newBinaryExpression(
				BinaryOperator.ADDITION, inner_div, literal_1d2);
		BinaryExpressionNode sub = astFactory.newBinaryExpression(
				BinaryOperator.SUBSTRACTION, mul, add);
		BinaryExpressionNode outer_div = astFactory.newBinaryExpression(
				BinaryOperator.DIVISION, literal_1d0, sub);
		astFactory.addAssignment(identifier_d, outer_div);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.DIVISION_BY_ZERO);
	}
}
