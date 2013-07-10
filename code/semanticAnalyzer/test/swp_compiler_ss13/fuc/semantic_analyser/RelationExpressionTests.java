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
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.RelationExpressionNodeImpl;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class RelationExpressionTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public RelationExpressionTests() {
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
	 * # error: usage of long within an logic relation<br/>
	 * long l;<br/>
	 * bool b;<br/>
	 * <br/>
	 * b = b < l;
	 */
	@Test
	public void testRelationTypeError() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// bool b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());

		// b = b < l;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_b1 = new BasicIdentifierNodeImpl();
		identifier_b1.setIdentifier("b");
		BasicIdentifierNode identifier_b2 = new BasicIdentifierNodeImpl();
		identifier_b2.setIdentifier("b");

		RelationExpressionNode lt = new RelationExpressionNodeImpl();
		lt.setOperator(BinaryOperator.LESSTHAN);
		lt.setLeftValue(identifier_b2);
		lt.setRightValue(identifier_l);
		identifier_b2.setParentNode(lt);
		identifier_l.setParentNode(lt);

		AssignmentNode assignment_b = new AssignmentNodeImpl();
		assignment_b.setLeftValue(identifier_b1);
		assignment_b.setRightValue(lt);
		identifier_b1.setParentNode(assignment_b);
		lt.setParentNode(assignment_b);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("b", new BooleanType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_b);
		blockNode.addStatement(assignment_b);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		assignment_b.setParentNode(blockNode);

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
	 * bool b;<br/>
	 * <br/>
	 * b = l < d;
	 */
	@Test
	public void testRelationAssignment() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// double d;
		DeclarationNode declaration_d = new DeclarationNodeImpl();
		declaration_d.setIdentifier("d");
		declaration_d.setType(new DoubleType());

		// long b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());

		// b = l < d;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_d = new BasicIdentifierNodeImpl();
		identifier_d.setIdentifier("d");
		BasicIdentifierNode identifier_b = new BasicIdentifierNodeImpl();
		identifier_b.setIdentifier("b");

		RelationExpressionNode lt = new RelationExpressionNodeImpl();
		lt.setOperator(BinaryOperator.LESSTHAN);
		lt.setLeftValue(identifier_l);
		lt.setRightValue(identifier_d);
		identifier_l.setParentNode(lt);
		identifier_d.setParentNode(lt);

		AssignmentNode assignment_b = new AssignmentNodeImpl();
		assignment_b.setLeftValue(identifier_b);
		assignment_b.setRightValue(lt);
		identifier_b.setParentNode(assignment_b);
		lt.setParentNode(assignment_b);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("d", new DoubleType());
		symbolTable.insert("b", new BooleanType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_d);
		blockNode.addDeclaration(declaration_b);
		blockNode.addStatement(assignment_b);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_d.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		assignment_b.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		assertFalse(log.hasErrors());
	}

	/**
	 * <pre>
	 * # error: usage of bool for incompatible relation<br/>
	 * bool b1;
	 * bool b2;
	 * 
	 * b1 = b1 < b2;
	 * </pre>
	 */
	@Test
	public void testInvalidBoolComparisonError() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("b1", new BooleanType());
		astFactory.addDeclaration("b2", new BooleanType());

		BinaryExpressionNode lt = astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, astFactory.newBasicIdentifier("b1"),
				astFactory.newBasicIdentifier("b2"));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b1"), lt);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
	
	/**
	 * <pre>
	 * # no errors expected
	 * bool b;
	 * 
	 * b = 1 < 1;
	 * b = 1 <= 1;
	 * b = 1 > 1;
	 * b = 1 >= 1;
	 * b = 1 == 1;
	 * b = 1 != 1;
	 * </pre>
	 */
	@Test
	public void testStaticRelationsLong() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("b", new BooleanType());

		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.LESSTHAN,
						astFactory.newLiteral("1", new LongType()),
						astFactory.newLiteral("1", new LongType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.LESSTHANEQUAL,
						astFactory.newLiteral("1", new LongType()),
						astFactory.newLiteral("1", new LongType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.GREATERTHAN,
						astFactory.newLiteral("1", new LongType()),
						astFactory.newLiteral("1", new LongType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.GREATERTHANEQUAL,
						astFactory.newLiteral("1", new LongType()),
						astFactory.newLiteral("1", new LongType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.EQUAL,
						astFactory.newLiteral("1", new LongType()),
						astFactory.newLiteral("1", new LongType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.INEQUAL,
						astFactory.newLiteral("1", new LongType()),
						astFactory.newLiteral("1", new LongType())));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}
	
	/**
	 * <pre>
	 * # no errors expected
	 * bool b;
	 * 
	 * b = 1.0 < 1.0;
	 * b = 1.0 <= 1.0;
	 * b = 1.0 > 1.0;
	 * b = 1.0 >= 1.0;
	 * b = 1.0 == 1.0;
	 * b = 1.0 != 1.0;
	 * </pre>
	 */
	@Test
	public void testStaticRelationsDouble() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("b", new BooleanType());

		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.LESSTHAN,
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newLiteral("1.0", new DoubleType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.LESSTHANEQUAL,
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newLiteral("1.0", new DoubleType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.GREATERTHAN,
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newLiteral("1.0", new DoubleType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.GREATERTHANEQUAL,
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newLiteral("1.0", new DoubleType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.EQUAL,
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newLiteral("1.0", new DoubleType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.INEQUAL,
						astFactory.newLiteral("1.0", new DoubleType()),
						astFactory.newLiteral("1.0", new DoubleType())));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}
	
	/**
	 * <pre>
	 * # no errors expected
	 * bool b;
	 * 
	 * b = true == true;
	 * b = true != true;
	 * </pre>
	 */
	@Test
	public void testStaticRelationBool() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("b", new BooleanType());

		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.EQUAL,
						astFactory.newLiteral("true", new BooleanType()),
						astFactory.newLiteral("true", new BooleanType())));
		astFactory.addAssignment(astFactory.newBasicIdentifier("b"), astFactory
				.newBinaryExpression(BinaryOperator.INEQUAL,
						astFactory.newLiteral("true", new BooleanType()),
						astFactory.newLiteral("true", new BooleanType())));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}
	
	/**
	 * <pre>
	 * # error: comparison of arrays
	 * long[1] a1;
	 * long[1] a2;
	 * bool b;
	 * 
	 * b = a1 == a2;
	 * </pre>
	 */
	@Test
	public void testArrayComparisonError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("a1", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("a2", new ArrayType(new LongType(), 1));
		astFactory.addDeclaration("b", new BooleanType());

		astFactory.addAssignment(
				astFactory.newBasicIdentifier("b"),
				astFactory.newBinaryExpression(BinaryOperator.EQUAL,
						astFactory.newBasicIdentifier("a1"),
						astFactory.newBasicIdentifier("a2")));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
	
	/**
	 * <pre>
	 * # error: comparison of structs
	 * struct{long l} s1;
	 * struct{long l} s2;
	 * bool b;
	 * 
	 * b = s1 == s2;
	 * </pre>
	 */
	@Test
	public void testStructComparisonError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("s1", new StructType(new Member("l", new LongType())));
		astFactory.addDeclaration("s2", new StructType(new Member("l", new LongType())));
		astFactory.addDeclaration("b", new BooleanType());

		astFactory.addAssignment(
				astFactory.newBasicIdentifier("b"),
				astFactory.newBinaryExpression(BinaryOperator.EQUAL,
						astFactory.newBasicIdentifier("s1"),
						astFactory.newBasicIdentifier("s2")));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
}
