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
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class DoubleOperatorTest {

	private AST ast;

	@Before
	public void setUp() throws IntermediateCodeGeneratorException {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("a", new DoubleType());
		astf.addDeclaration("b", new DoubleType());
		astf.addDeclaration("c", new DoubleType());

		astf.addAssignment(astf.newBasicIdentifier("a"),
				astf.newLiteral("4", new DoubleType()));

		astf.addAssignment(astf.newBasicIdentifier("b"),
				astf.newLiteral("3", new DoubleType()));

		astf.addAssignment(astf.newBasicIdentifier("c"),
				astf.newLiteral("2", new DoubleType()));

		astf.addAssignment(
				astf.newBasicIdentifier("c"),
				astf.newBinaryExpression(
						BinaryOperator.MULTIPLICATION,
						astf.newBinaryExpression(BinaryOperator.DIVISION,
								astf.newBasicIdentifier("a"),
								astf.newBasicIdentifier("b")),
						astf.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

		astf.addAssignment(astf.newBasicIdentifier("c"), astf
				.newBinaryExpression(BinaryOperator.ADDITION, astf
						.newBinaryExpression(BinaryOperator.SUBSTRACTION,
								astf.newBasicIdentifier("a"),
								astf.newBasicIdentifier("b")), astf
						.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

		ast = astf.getAST();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(ast);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_DOUBLE|!|!|a)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|b)" + "\n" + "(DECLARE_DOUBLE|!|!|c)"
				+ "\n" + "(ASSIGN_DOUBLE|#4|!|a)" + "\n"
				+ "(ASSIGN_DOUBLE|#3|!|b)" + "\n" + "(ASSIGN_DOUBLE|#2|!|c)"
				+ "\n" + "(DECLARE_DOUBLE|!|!|tmp0)" + "\n"
				+ "(DIV_DOUBLE|a|b|tmp0)" + "\n" + "(DECLARE_DOUBLE|!|!|tmp1)"
				+ "\n" + "(MUL_DOUBLE|tmp0|c|tmp1)" + "\n"
				+ "(ASSIGN_DOUBLE|tmp1|!|c)" + "\n" + "(RETURN|c|!|!)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|tmp2)" + "\n" + "(SUB_DOUBLE|a|b|tmp2)"
				+ "\n" + "(DECLARE_DOUBLE|!|!|tmp3)" + "\n"
				+ "(ADD_DOUBLE|tmp2|c|tmp3)" + "\n"
				+ "(ASSIGN_DOUBLE|tmp3|!|c)" + "\n" + "(RETURN|c|!|!)" + "\n";
		assertEquals(expected, actual);
	}
}