package swp_compiler_ss13.fuc.ir.test.ms2;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;

public class assignmentProg {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("a", new LongType());
		astf.addDeclaration("b", new LongType());
		astf.addDeclaration("c", new LongType());

		astf.addAssignment(astf.newBasicIdentifier("a"),
				astf.newLiteral("4", new LongType()));

		astf.addAssignment(astf.newBasicIdentifier("b"),
				astf.newLiteral("3", new LongType()));

		astf.addAssignment(astf.newBasicIdentifier("c"),
				astf.newLiteral("2", new LongType()));

		astf.addAssignment(
				astf.newBasicIdentifier("a"),
				astf.newAssignment(astf.newBasicIdentifier("b"),
						astf.newLiteral("4", new LongType())));
		astf.addAssignment(
				astf.newBasicIdentifier("c"),
				astf.newBinaryExpression(
						BinaryOperator.ADDITION,
						astf.newBinaryExpression(BinaryOperator.ADDITION,
								astf.newBasicIdentifier("a"),
								astf.newBasicIdentifier("b")),
						astf.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

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

		String expected = "" + "DECLARE_LONG|!|!|a)" + "\n"
				+ "(DECLARE_LONG|!|!|b)" + "\n" + "(DECLARE_LONG|!|!|c)" + "\n"
				+ "(ASSIGN_LONG|#4|!|a)" + "\n" + "(ASSIGN_LONG|#3|!|b)" + "\n"
				+ "(ASSIGN_LONG|#2|!|c)" + "\n" + "(ASSIGN_LONG|#4|!|b)" + "\n"
				+ "(ASSIGN_LONG|b|!|a)" + "\n" + "(DECLARE_LONG|!|!|tmp0)"
				+ "\n" + "(ADD_LONG|a|b|tmp0)" + "\n"
				+ "(DECLARE_LONG|!|!|tmp1)" + "\n" + "(ADD_LONG|tmp0|c|tmp1)"
				+ "\n" + "(ASSIGN_LONG|tmp1|!|c)" + "\n" + "(RETURN|c|!|!)"
				+ "\n";
		assertEquals(expected, actual);
	}
}
