package swp_compiler_ss13.fuc.ir.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class RelationTest {

	AST astGreater;
	private AST astLess;
	private AST astDGreater;
	private AST astDLess;
	private AST astGreaterE;
	private AST astLessE;
	private AST astDGreaterE;
	private AST astDLessE;
	private AST astEqual;
	private AST astDEqual;
	private AST astInEqual;
	private AST astEX;

	@Before
	public void setUp() throws IntermediateCodeGeneratorException {
		PA.setValue(SymbolTableImpl.class, "ext", 0);

		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("l", new LongType());
		astf.addDeclaration("r", new BooleanType());
		astf.addAssignment(astf.newBasicIdentifier("l"),
				astf.newLiteral("10", new LongType()));
		astf.addAssignment(
				astf.newBasicIdentifier("r"),
				astf.newBinaryExpression(BinaryOperator.GREATERTHAN,
						astf.newBasicIdentifier("l"),
						astf.newLiteral("5", new LongType())));
		astGreater = astf.getAST();

		ASTFactory astf2 = new ASTFactory();
		astf2.addDeclaration("l", new LongType());
		astf2.addDeclaration("r", new BooleanType());
		astf2.addAssignment(astf2.newBasicIdentifier("l"),
				astf2.newLiteral("10", new LongType()));
		astf2.addAssignment(
				astf2.newBasicIdentifier("r"),
				astf2.newBinaryExpression(BinaryOperator.LESSTHAN,
						astf2.newBasicIdentifier("l"),
						astf2.newLiteral("5", new LongType())));
		astLess = astf2.getAST();

		ASTFactory astf3 = new ASTFactory();
		astf3.addDeclaration("g", new DoubleType());
		astf3.addDeclaration("r", new BooleanType());
		astf3.addAssignment(astf3.newBasicIdentifier("g"),
				astf3.newLiteral("10", new DoubleType()));
		astf3.addAssignment(
				astf3.newBasicIdentifier("r"),
				astf3.newBinaryExpression(BinaryOperator.GREATERTHAN,
						astf3.newBasicIdentifier("g"),
						astf3.newLiteral("5", new DoubleType())));
		astDGreater = astf3.getAST();

		ASTFactory astf4 = new ASTFactory();
		astf4.addDeclaration("l", new DoubleType());
		astf4.addDeclaration("r", new BooleanType());
		astf4.addAssignment(astf4.newBasicIdentifier("l"),
				astf4.newLiteral("10", new DoubleType()));
		astf4.addAssignment(
				astf4.newBasicIdentifier("r"),
				astf4.newBinaryExpression(BinaryOperator.LESSTHAN,
						astf4.newBasicIdentifier("l"),
						astf4.newLiteral("5", new DoubleType())));
		astDLess = astf4.getAST();

		ASTFactory astf5 = new ASTFactory();
		astf5.addDeclaration("l", new LongType());
		astf5.addDeclaration("r", new BooleanType());
		astf5.addAssignment(astf5.newBasicIdentifier("l"),
				astf5.newLiteral("10", new LongType()));
		astf5.addAssignment(astf5.newBasicIdentifier("r"), astf5
				.newBinaryExpression(BinaryOperator.GREATERTHANEQUAL,
						astf5.newBasicIdentifier("l"),
						astf5.newLiteral("5", new LongType())));
		astGreaterE = astf5.getAST();

		ASTFactory astf6 = new ASTFactory();
		astf6.addDeclaration("l", new LongType());
		astf6.addDeclaration("r", new BooleanType());
		astf6.addAssignment(astf6.newBasicIdentifier("l"),
				astf6.newLiteral("10", new LongType()));
		astf6.addAssignment(
				astf6.newBasicIdentifier("r"),
				astf6.newBinaryExpression(BinaryOperator.LESSTHANEQUAL,
						astf6.newBasicIdentifier("l"),
						astf6.newLiteral("5", new LongType())));
		astLessE = astf6.getAST();

		ASTFactory astf7 = new ASTFactory();
		astf7.addDeclaration("g", new DoubleType());
		astf7.addDeclaration("r", new BooleanType());
		astf7.addAssignment(astf7.newBasicIdentifier("g"),
				astf7.newLiteral("10", new DoubleType()));
		astf7.addAssignment(astf7.newBasicIdentifier("r"), astf7
				.newBinaryExpression(BinaryOperator.GREATERTHANEQUAL,
						astf7.newBasicIdentifier("g"),
						astf7.newLiteral("5", new DoubleType())));
		astDGreaterE = astf7.getAST();

		ASTFactory astf8 = new ASTFactory();
		astf8.addDeclaration("l", new DoubleType());
		astf8.addDeclaration("r", new BooleanType());
		astf8.addAssignment(astf8.newBasicIdentifier("l"),
				astf8.newLiteral("10", new DoubleType()));
		astf8.addAssignment(
				astf8.newBasicIdentifier("r"),
				astf8.newBinaryExpression(BinaryOperator.LESSTHANEQUAL,
						astf8.newBasicIdentifier("l"),
						astf8.newLiteral("5", new DoubleType())));
		astDLessE = astf8.getAST();

		ASTFactory astf9 = new ASTFactory();
		astf9.addDeclaration("l", new LongType());
		astf9.addDeclaration("r", new BooleanType());
		astf9.addAssignment(astf9.newBasicIdentifier("l"),
				astf9.newLiteral("10", new LongType()));
		astf9.addAssignment(
				astf9.newBasicIdentifier("r"),
				astf9.newBinaryExpression(BinaryOperator.EQUAL,
						astf9.newBasicIdentifier("l"),
						astf9.newLiteral("5", new LongType())));
		astEqual = astf9.getAST();

		ASTFactory astf10 = new ASTFactory();
		astf10.addDeclaration("g", new DoubleType());
		astf10.addDeclaration("r", new BooleanType());
		astf10.addAssignment(astf10.newBasicIdentifier("g"),
				astf10.newLiteral("10", new DoubleType()));
		astf10.addAssignment(
				astf10.newBasicIdentifier("r"),
				astf10.newBinaryExpression(BinaryOperator.EQUAL,
						astf10.newBasicIdentifier("g"),
						astf10.newLiteral("5", new DoubleType())));
		astDEqual = astf10.getAST();

		ASTFactory astf11 = new ASTFactory();
		astf11.addDeclaration("l", new LongType());
		astf11.addDeclaration("r", new BooleanType());
		astf11.addAssignment(astf11.newBasicIdentifier("l"),
				astf11.newLiteral("10", new LongType()));
		astf11.addAssignment(
				astf11.newBasicIdentifier("r"),
				astf11.newBinaryExpression(BinaryOperator.INEQUAL,
						astf11.newBasicIdentifier("l"),
						astf11.newLiteral("5", new LongType())));
		astInEqual = astf11.getAST();

		ASTFactory astf12 = new ASTFactory();
		astf12.addDeclaration("l", new LongType());
		astf12.addDeclaration("r", new BooleanType());
		astf12.addAssignment(astf12.newBasicIdentifier("l"),
				astf12.newLiteral("10", new LongType()));
		astf12.addAssignment(
				astf12.newBasicIdentifier("r"),
				astf12.newBinaryExpression(BinaryOperator.ADDITION,
						astf12.newBasicIdentifier("l"),
						astf12.newLiteral("5", new LongType())));
		astEX = astf12.getAST();
	}

	@Test
	public void testGreaterThan() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astGreater);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_LONG|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_G|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testLessThan() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astLess);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_LONG|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_L|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testDGreater() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astDGreater);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_DOUBLE|!|!|g)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_DOUBLE|#10|!|g)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_DOUBLE_G|g|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testDLess() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astDLess);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_DOUBLE|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_DOUBLE|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_DOUBLE_L|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testDGreaterE() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astDGreaterE);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_DOUBLE|!|!|g)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_DOUBLE|#10|!|g)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_DOUBLE_GE|g|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testDLessE() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astDLessE);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_DOUBLE|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_DOUBLE|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_DOUBLE_LE|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testLGreaterE() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astGreaterE);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_LONG|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_GE|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testLLessE() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astLessE);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_LONG|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_LE|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testDEqual() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astDEqual);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_DOUBLE|!|!|g)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_DOUBLE|#10|!|g)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_DOUBLE_E|g|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testEqual() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astEqual);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_LONG|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_E|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testInEqual() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(astInEqual);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n" + "(ASSIGN_LONG|#10|!|l)"
				+ "\n" + "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|tmp1)" + "\n"
				+ "(COMPARE_LONG_E|l|#5|tmp1)" + "\n"
				+ "(NOT_BOOLEAN|tmp1|!|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testEX() {
		try {
			IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
			List<Quadruple> irc = irg.generateIntermediateCode(astEX);

			StringBuilder b = new StringBuilder();
			for (Quadruple q : irc) {
				b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
						q.getArgument1(), q.getArgument2(), q.getResult()));
			}
			String actual = b.toString();
			System.out.println(actual);

		} catch (IntermediateCodeGeneratorException x) {
			System.out.println(x);
			return;
		}

	}

}
