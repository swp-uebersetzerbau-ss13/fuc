package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;

public class StructTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public StructTests() {
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
	 * # no errors expected<br/>
	 * record{bool b;} s;<br/>
	 * bool b;<br/>
	 * <br/>
	 * b = s.b;
	 */
	@Test
	public void testStructAssigment() {
		Type structType = new StructType(new Member("b", new BooleanType()));
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		astFactory.addDeclaration("b", new BooleanType());
		IdentifierNode identifier_b = astFactory.newBasicIdentifier("b");
		IdentifierNode identifier_s = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_b = astFactory.newStructIdentifier("b",
				identifier_s);
		astFactory.addAssignment(identifier_b, identifier_s_b);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		assertFalse(log.hasErrors());
	}

	/**
	 * <pre>
	 * # error: invalid assignment
	 * record{bool t; record{long t;} s;} s;
	 * bool b;
	 * long l;
	 * 
	 * l = s.s.t;
	 * b = s.s.t;
	 * </pre>
	 */
	@Test
	public void tsetNestedStructAssigmentTypeError() {
		Member longMember = new Member("t", new LongType());
		Type innerStructType = new StructType(longMember);
		Member boolMember = new Member("t", new BooleanType());
		Member structMember = new Member("s", innerStructType);
		Type structType = new StructType(boolMember, structMember);
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		astFactory.addDeclaration("b", new BooleanType());
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_b = astFactory.newBasicIdentifier("b");
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		IdentifierNode identifier_s1 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s2 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_s1 = astFactory.newStructIdentifier("s",
				identifier_s1);
		IdentifierNode identifier_s_s2 = astFactory.newStructIdentifier("s",
				identifier_s2);
		IdentifierNode identifier_s_s_t1 = astFactory.newStructIdentifier("t",
				identifier_s_s1);
		IdentifierNode identifier_s_s_t2 = astFactory.newStructIdentifier("t",
				identifier_s_s2);
		astFactory.addAssignment(identifier_l, identifier_s_s_t2);
		astFactory.addAssignment(identifier_b, identifier_s_s_t1);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: assignment of bool to long
	 * record{bool b;} s;
	 * long l;
	 * 
	 * l = s.b;
	 * </pre>
	 */
	@Test
	public void testStructAssigmentTypeError() {
		Type structType = new StructType(new Member("b", new BooleanType()));
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		astFactory.addDeclaration("l", new LongType());
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		IdentifierNode identifier_s = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_b = astFactory.newStructIdentifier("b",
				identifier_s);
		astFactory.addAssignment(identifier_l, identifier_s_b);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: assignment of long to bool
	 * record{bool b; long l;} s;
	 * 
	 * s.b = s.l;
	 * </pre>
	 */
	@Test
	public void testInnerStructAssigmentTypeError() {
		Member boolMember = new Member("b", new BooleanType());
		Member longMember = new Member("l", new LongType());
		Type structType = new StructType(boolMember, longMember);
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s", structType);
		IdentifierNode identifier_s1 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s2 = astFactory.newBasicIdentifier("s");
		IdentifierNode identifier_s_b = astFactory.newStructIdentifier("b",
				identifier_s1);
		IdentifierNode identifier_s_l = astFactory.newStructIdentifier("l",
				identifier_s2);
		astFactory.addAssignment(identifier_s_b, identifier_s_l);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: struct access of a non-struct type
	 * long l;
	 * bool b;
	 * 
	 * b = l.b;
	 * </pre>
	 */
	@Test
	public void testStructAccessOfNonStructTypeError() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("l", new LongType());
		astFactory.addDeclaration("b", new BooleanType());
		IdentifierNode identifier_b = astFactory.newBasicIdentifier("b");
		IdentifierNode identifier_l = astFactory.newBasicIdentifier("l");
		IdentifierNode identifier_l_b = astFactory.newStructIdentifier("b",
				identifier_l);
		astFactory.addAssignment(identifier_b, identifier_l_b);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: invalid arithmetic operation
	 * 
	 * record{long l; double d;} s1;
	 * record{bool b; double d;} s2;
	 * 
	 * s1.d = s1.l + s2.d;
	 * s1.d = (s2.b + s1.d) - s1.l;
	 * </pre>
	 */
	@Test
	public void testStructFieldsArithmeticTypeError() {
		ASTFactory astFactory = new ASTFactory();
		astFactory.addDeclaration("s1", new StructType(new Member("l",
				new LongType()), new Member("d", new DoubleType())));
		astFactory.addDeclaration("s2", new StructType(new Member("b",
				new BooleanType()), new Member("d", new DoubleType())));

		astFactory.addAssignment(astFactory.newStructIdentifier("d",
				astFactory.newBasicIdentifier("s1")), astFactory
				.newBinaryExpression(
						BinaryOperator.ADDITION,
						astFactory.newStructIdentifier("l",
								astFactory.newBasicIdentifier("s1")),
						astFactory.newStructIdentifier("d",
								astFactory.newBasicIdentifier("s2"))));
		BinaryExpressionNode add = astFactory.newBinaryExpression(
				BinaryOperator.ADDITION,
				astFactory.newStructIdentifier("b",
						astFactory.newBasicIdentifier("s2")),
				astFactory.newStructIdentifier("d",
						astFactory.newBasicIdentifier("s1")));
		astFactory.addAssignment(astFactory.newStructIdentifier("d",
				astFactory.newBasicIdentifier("s1")), astFactory
				.newBinaryExpression(
						BinaryOperator.SUBSTRACTION,
						add,
						astFactory.newStructIdentifier("l",
								astFactory.newBasicIdentifier("s1"))));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # no errors expected
	 * record{long l; bool b;}[2] s;
	 * 
	 * s[0].l = 2;
	 * s[0].b = s[0].l < s[1].l;
	 * </pre>
	 */
	@Test
	public void testStructArray() {
		ASTFactory astFactory = new ASTFactory();

		StructType structType = new StructType(new Member("l", new LongType()),
				new Member("b", new BooleanType()));
		astFactory.addDeclaration("s", new ArrayType(structType, 2));

		astFactory.addAssignment(astFactory.newStructIdentifier(
				"l",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("s"))), astFactory
				.newLiteral("2", new LongType()));
		IdentifierNode s0b = astFactory.newStructIdentifier(
				"b",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("s")));
		IdentifierNode s0l = astFactory.newStructIdentifier(
				"l",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("s")));
		IdentifierNode s1l = astFactory.newStructIdentifier(
				"l",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("1", new LongType()),
						astFactory.newBasicIdentifier("s")));
		astFactory.addAssignment(s0b, astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, s0l, s1l));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	/**
	 * <pre>
	 * # error: assignment of long to bool
	 * record{long l; bool b;}[2] s;
	 * 
	 * s[0].l = 2;
	 * s[0].b = s[0].l + s[1].l;
	 * </pre>
	 */
	@Test
	public void testStructArrayFieldTypeError() {
		ASTFactory astFactory = new ASTFactory();

		StructType structType = new StructType(new Member("l", new LongType()),
				new Member("b", new BooleanType()));
		astFactory.addDeclaration("s", new ArrayType(structType, 2));

		astFactory.addAssignment(astFactory.newStructIdentifier(
				"l",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("s"))), astFactory
				.newLiteral("2", new LongType()));
		IdentifierNode s0b = astFactory.newStructIdentifier(
				"b",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("s")));
		IdentifierNode s0l = astFactory.newStructIdentifier(
				"l",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("s")));
		IdentifierNode s1l = astFactory.newStructIdentifier(
				"l",
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("1", new LongType()),
						astFactory.newBasicIdentifier("s")));
		astFactory.addAssignment(s0b, astFactory.newBinaryExpression(
				BinaryOperator.ADDITION, s0l, s1l));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * <pre>
	 * # error: assignment of bool to long
	 * record{long[1] l;} s;
	 * 
	 * s.l[0] = 2;
	 * s.l[0] = true;
	 * </pre>
	 */
	@Test
	public void testArrayFieldTypeError() {
		ASTFactory astFactory = new ASTFactory();

		astFactory.addDeclaration("s", new StructType(new Member("l",
				new ArrayType(new LongType(), 1))));

		astFactory.addAssignment(
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newStructIdentifier("l",
								astFactory.newBasicIdentifier("s"))),
				astFactory.newLiteral("2", new LongType()));
		astFactory.addAssignment(
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newStructIdentifier("l",
								astFactory.newBasicIdentifier("s"))),
				astFactory.newLiteral("true", new BooleanType()));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
}
