package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ArrayTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public ArrayTests() {
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
	 * # error: assignment of bool-array-field to long<br/>
	 * bool [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = a[0];
	 */
	@Test
	public void testAssignmentFromArrayTypeError() {
		// bool [1] a;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new ArrayType(new BooleanType(), 1));

		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// l = a[0];
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_a1 = new BasicIdentifierNodeImpl();
		identifier_a1.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a1 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a1.setIdentifierNode(identifier_a1);
		LiteralNode value1 = new LiteralNodeImpl();
		value1.setLiteral("0");
		value1.setLiteralType(new LongType());
		arrayIdentifier_a1.setIndexNode(value1);
		identifier_a1.setParentNode(arrayIdentifier_a1);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(arrayIdentifier_a1);
		identifier_l.setParentNode(assignment_l);
		arrayIdentifier_a1.setParentNode(assignment_l);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new ArrayType(new BooleanType(), 1));
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
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
	 * # error: assignment of long to bool-array-field<br/>
	 * bool [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * a[0] = l;
	 */
	@Test
	public void testAssignmentToArrayTypeError() {
		// bool [1] a;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new ArrayType(new BooleanType(), 1));

		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// a[0] = l;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_a = new BasicIdentifierNodeImpl();
		identifier_a.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a2 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a2.setIdentifierNode(identifier_a);
		LiteralNode value = new LiteralNodeImpl();
		value.setLiteral("0");
		value.setLiteralType(new LongType());
		arrayIdentifier_a2.setIndexNode(value);
		identifier_a.setParentNode(arrayIdentifier_a2);

		AssignmentNode assignment_a = new AssignmentNodeImpl();
		assignment_a.setLeftValue(arrayIdentifier_a2);
		assignment_a.setRightValue(identifier_l);
		identifier_l.setParentNode(assignment_a);
		arrayIdentifier_a2.setParentNode(assignment_a);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new ArrayType(new BooleanType(), 1));
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_a);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
		assignment_a.setParentNode(blockNode);

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
	 * # no errors expected<br/>
	 * long [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = a[0];<br/>
	 * a[0] = l;
	 */
	@Test
	public void testArrayAssignments() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("l", new LongType());

		astFactory.addAssignment(
				astFactory.newBasicIdentifier("l"),
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("a")));
		astFactory.addAssignment(
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("a")),
				astFactory.newBasicIdentifier("l"));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	/**
	 * # error: array out-of-bounds access<br/>
	 * long [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = a[1];
	 */
	@Test
	public void testOutOfBoundsError() {
		// long [1] a;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new ArrayType(new LongType(), 1));

		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// l = a[2];
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_a = new BasicIdentifierNodeImpl();
		identifier_a.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a.setIdentifierNode(identifier_a);
		LiteralNode value = new LiteralNodeImpl();
		value.setLiteral("1");
		value.setLiteralType(new LongType());
		arrayIdentifier_a.setIndexNode(value);
		identifier_a.setParentNode(arrayIdentifier_a);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(arrayIdentifier_a);
		identifier_l.setParentNode(assignment_l);
		arrayIdentifier_a.setParentNode(assignment_l);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new ArrayType(new LongType(), 1));
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
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
	 * <pre>
	 * # no errors expected
	 * long [7][3] a;
	 * long [4][8] a;
	 * long l;
	 * 
	 * l = a[2][6];
	 * b[7][3] = l;
	 * </pre>
	 */
	@Test
	public void testMultiDimensionalArray() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new ArrayType(
				new LongType(), 7), 3));
		astFactory.addDeclaration("b", new ArrayType(new ArrayType(
				new LongType(), 4), 8));
		astFactory.addDeclaration("l", new LongType());

		astFactory.addAssignment(astFactory.newBasicIdentifier("l"), astFactory
				.newArrayIdentifier(astFactory.newLiteral("6", new LongType()),
						astFactory.newArrayIdentifier(
								astFactory.newLiteral("2", new LongType()),
								astFactory.newBasicIdentifier("a"))));
		astFactory.addAssignment(astFactory.newArrayIdentifier(
				astFactory.newLiteral("3", new LongType()),
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("7", new LongType()),
						astFactory.newBasicIdentifier("b"))), astFactory
				.newBasicIdentifier("l"));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	/**
	 * <pre>
	 * # a[6][2] is out of bound
	 * long [7][3] a;
	 * long l;
	 * 
	 * l = a[6][2];
	 * </pre>
	 */
	@Test
	public void testMultiDimensionalArrayOutOfBoundError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new ArrayType(
				new LongType(), 7), 3));
		astFactory.addDeclaration("l", new LongType());

		astFactory.addAssignment(astFactory.newBasicIdentifier("l"), astFactory
				.newArrayIdentifier(astFactory.newLiteral("2", new LongType()),
						astFactory.newArrayIdentifier(
								astFactory.newLiteral("6", new LongType()),
								astFactory.newBasicIdentifier("a"))));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * # error: assignment of bool-array to long-array<br/>
	 * long [1] a;<br/>
	 * bool [1] b;<br/>
	 * <br/>
	 * a = b;
	 */
	@Test
	public void testArrayAssignmentError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("b", new ArrayType(new LongType(), 1));

		astFactory.addAssignment(astFactory.newBasicIdentifier("a"),
				astFactory.newBasicIdentifier("b"));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * # error: assignment of 1-dimensional-array to 2-dimensional-array and
	 * vice versa<br/>
	 * long [1][1] a;<br/>
	 * long [1] b;<br/>
	 * <br/>
	 * a = b;<br/>
	 * b = a;
	 */
	@Test
	public void testMultiDimensionalArrayAssignmentError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new ArrayType(
				new LongType(), 1), 1));
		astFactory.addDeclaration("b", new ArrayType(new LongType(), 1));

		astFactory.addAssignment(astFactory.newBasicIdentifier("a"),
				astFactory.newBasicIdentifier("b"));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"),
				astFactory.newBasicIdentifier("a"));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 2);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
		assertEquals(errors.get(1).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * # error: assignment of arrays of different lengths <br/>
	 * long [1] a;<br/>
	 * long [2] b;<br/>
	 * <br/>
	 * a = b;
	 */
	@Test
	public void testDifferentLengthsAssignmentError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("b", new ArrayType(new LongType(), 2));

		astFactory.addAssignment(astFactory.newBasicIdentifier("a"),
				astFactory.newBasicIdentifier("b"));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: invalid array index type
	 * long [1] a;
	 * long l;
	 * 
	 * l = a[1.0];
	 * </pre>
	 */
	@Test
	public void testArrayIndexTypeError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("l", new LongType());

		astFactory.addAssignment(astFactory.newBasicIdentifier("l"), astFactory
				.newArrayIdentifier(
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newBasicIdentifier("a")));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: array access to non array type
	 * long a;
	 * long l;
	 * 
	 * l = a[1];
	 * </pre>
	 */
	@Test
	public void testNonArrayAccessError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new LongType());
		astFactory.addDeclaration("l", new LongType());

		astFactory.addAssignment(
				astFactory.newBasicIdentifier("l"),
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("1", new LongType()),
						astFactory.newBasicIdentifier("a")));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: negative array index
	 * long[1] a;
	 * 
	 * a[-1] = 1;
	 * </pre>
	 */
	@Test
	public void testNegativeArrayIndexError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a", new ArrayType(new LongType(), 1));
		astFactory.addAssignment(
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("-1", new LongType()),
						astFactory.newBasicIdentifier("a")),
				astFactory.newLiteral("1", new LongType()));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: addition with array
	 * double d;
	 * long[1] a1;
	 * double[1][1] a2;
	 * 
	 * d = d - (a2[0] + a1[0]);
	 * </pre>
	 */
	@Test
	public void testArrayArithmeticTypeError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("d", new DoubleType());
		astFactory.addDeclaration("a1", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("a2", new ArrayType(new ArrayType(
				new LongType(), 1), 1));

		BinaryExpressionNode add = astFactory.newBinaryExpression(
				BinaryOperator.ADDITION,
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("a2")),
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("a1")));
		astFactory.addAssignment(astFactory.newBasicIdentifier("d"), astFactory
				.newBinaryExpression(BinaryOperator.SUBSTRACTION,
						astFactory.newBasicIdentifier("d"), add));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
}
