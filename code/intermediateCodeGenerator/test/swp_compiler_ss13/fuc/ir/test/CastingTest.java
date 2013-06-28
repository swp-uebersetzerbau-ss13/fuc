package swp_compiler_ss13.fuc.ir.test;

import java.util.List;

import junit.extensions.PA;

import org.junit.Assert;
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
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class CastingTest {

	private AST ast;

	@Before
	public void setUp() throws IntermediateCodeGeneratorException {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("a", new LongType());
		astf.addDeclaration("b", new LongType());
		astf.addDeclaration("c", new DoubleType());
		astf.addDeclaration("d", new DoubleType());

		astf.addAssignment(astf.newBasicIdentifier("a"), astf.newLiteral("4", new LongType()));

		astf.addAssignment(astf.newBasicIdentifier("b"), astf.newLiteral("3", new LongType()));

		astf.addAssignment(astf.newBasicIdentifier("c"), astf.newLiteral("2", new DoubleType()));

		astf.addAssignment(astf.newBasicIdentifier("d"), astf.newLiteral("6", new DoubleType()));

		astf.addAssignment(
				astf.newBasicIdentifier("c"),
				astf.newBinaryExpression(
						BinaryOperator.ADDITION,
						astf.newBinaryExpression(BinaryOperator.ADDITION,
								astf.newBasicIdentifier("a"), astf.newBasicIdentifier("b")),
						astf.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

		astf.addAssignment(
				astf.newBasicIdentifier("c"),
				astf.newBinaryExpression(
						BinaryOperator.ADDITION,
						astf.newBinaryExpression(BinaryOperator.ADDITION,
								astf.newBasicIdentifier("d"), astf.newBasicIdentifier("a")),
						astf.newBasicIdentifier("c")));
		astf.addReturn(astf.newBasicIdentifier("c"));

		this.ast = astf.getAST();

	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(this.ast);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(), q.getArgument1(),
					q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|a)" + "\n" + "(DECLARE_LONG|!|!|b)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|c)" + "\n" + "(DECLARE_DOUBLE|!|!|d)" + "\n"
				+ "(ASSIGN_LONG|#4|!|a)" + "\n" + "(ASSIGN_LONG|#3|!|b)" + "\n"
				+ "(ASSIGN_DOUBLE|#2|!|c)" + "\n" + "(ASSIGN_DOUBLE|#6|!|d)" + "\n"
				+ "(DECLARE_LONG|!|!|tmp0)" + "\n" + "(ADD_LONG|a|b|tmp0)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|tmp1)" + "\n" + "(LONG_TO_DOUBLE|tmp0|!|tmp1)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|tmp2)" + "\n" + "(ADD_DOUBLE|tmp1|c|tmp2)" + "\n"
				+ "(ASSIGN_DOUBLE|tmp2|!|c)" + "\n" + "(RETURN|c|!|!)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|tmp3)" + "\n" + "(LONG_TO_DOUBLE|a|!|tmp3)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|tmp4)" + "\n" + "(ADD_DOUBLE|d|tmp3|tmp4)" + "\n"
				+ "(DECLARE_DOUBLE|!|!|tmp5)" + "\n" + "(ADD_DOUBLE|tmp4|c|tmp5)" + "\n"
				+ "(ASSIGN_DOUBLE|tmp5|!|c)" + "\n" + "(RETURN|c|!|!)" + "\n";

		Assert.assertEquals(expected, actual);
	}

}