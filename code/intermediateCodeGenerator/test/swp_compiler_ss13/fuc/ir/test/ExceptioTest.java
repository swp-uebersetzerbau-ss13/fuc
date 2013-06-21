package swp_compiler_ss13.fuc.ir.test;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ExceptioTest {

	private AST ast;
	private AST ast2;
	private AST ast3;

	@Before
	public void setUp() throws IntermediateCodeGeneratorException {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("b", new LongType());
		astf.addDeclaration("c", new LongType());

		astf.addAssignment(astf.newBasicIdentifier("b"),
				astf.newLiteral("6", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("c"),
				astf.newLiteral("7", new LongType()));

		astf.addAssignment(
				astf.newBasicIdentifier("b"),
				astf.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						astf.newBasicIdentifier("b"),
						astf.newBasicIdentifier("c")));

		ast = astf.getAST();

		ASTFactory astf1 = new ASTFactory();
		astf1.addDeclaration("a", new LongType());
		astf1.addDeclaration("b", new LongType());
		astf1.addDeclaration("c", new BooleanType());

		astf1.addAssignment(astf1.newBasicIdentifier("a"),
				astf1.newLiteral("4", new LongType()));

		astf1.addAssignment(astf1.newBasicIdentifier("b"),
				astf1.newLiteral("3", new LongType()));

		astf1.addAssignment(astf1.newBasicIdentifier("c"),
				astf1.newLiteral("TRUE", new BooleanType()));

		astf1.addAssignment(
				astf1.newBasicIdentifier("a"),
				astf1.newAssignment(astf1.newBasicIdentifier("b"),
						astf1.newLiteral("4", new LongType())));
		astf1.addAssignment(astf1.newBasicIdentifier("c"), astf1
				.newBinaryExpression(BinaryOperator.INEQUAL, astf1
						.newBinaryExpression(BinaryOperator.ADDITION,
								astf1.newBasicIdentifier("a"),
								astf1.newBasicIdentifier("b")), astf1
						.newBasicIdentifier("c")));
		astf1.addReturn(astf1.newBasicIdentifier("c"));

		ast3 = astf1.getAST();

	}

	@Test
	public void test() {
		try {
			IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
			List<Quadruple> irc = irg.generateIntermediateCode(ast);

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

	@Test
	public void test2() {
		try {
			IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
			List<Quadruple> irc = irg.generateIntermediateCode(ast2);

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

	@Test
	public void test3() {
		try {
			IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
			List<Quadruple> irc = irg.generateIntermediateCode(ast3);

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
