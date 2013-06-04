package swp_compiler_ss13.fuc.ir.test;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;

public class CastingTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("a", new LongType());
		astf.addDeclaration("b", new LongType());
		astf.addDeclaration("c", new DoubleType());
		astf.addDeclaration("d", new DoubleType());

		astf.addAssignment(astf.newBasicIdentifier("a"),
				astf.newLiteral("4", new LongType()));

		astf.addAssignment(astf.newBasicIdentifier("b"),
				astf.newLiteral("3", new LongType()));

		astf.addAssignment(astf.newBasicIdentifier("c"),
				astf.newLiteral("2", new DoubleType()));

		astf.addAssignment(astf.newBasicIdentifier("d"),
				astf.newLiteral("6", new DoubleType()));

		astf.addAssignment(
				astf.newBasicIdentifier("c"),
				astf.newBinaryExpression(
						BinaryOperator.ADDITION,
						astf.newBinaryExpression(BinaryOperator.ADDITION,
								astf.newBasicIdentifier("a"),
								astf.newBasicIdentifier("b")),
						astf.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

		astf.addAssignment(
				astf.newBasicIdentifier("c"),
				astf.newBinaryExpression(
						BinaryOperator.ADDITION,
						astf.newBinaryExpression(BinaryOperator.ADDITION,
								astf.newBasicIdentifier("d"),
								astf.newBasicIdentifier("a")),
						astf.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

		astf.addDeclaration("Long", new LongType());
		astf.addDeclaration("Double", new DoubleType());

		astf.addAssignment(astf.newBasicIdentifier("Long"),
				astf.newLiteral("6", new DoubleType()));
		astf.addAssignment(astf.newBasicIdentifier("Double"),
				astf.newLiteral("6", new LongType()));

		ast = astf.getAST();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(ast);

		StringBuilder actual = new StringBuilder();
		for (Quadruple q : irc) {
			actual.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		System.out.println(actual);
	}
}
