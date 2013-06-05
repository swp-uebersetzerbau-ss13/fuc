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
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class RelationTest {

	AST astGreater;
	private AST astLess;

	@Before
	public void setUp() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);

		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("l", new LongType());
		astf.addDeclaration("r", new BooleanType());
		astf.addAssignment(astf.newBasicIdentifier("l"), astf.newLiteral("10", new LongType()));
		astf.addAssignment(
				astf.newBasicIdentifier("r"),
				astf.newBinaryExpression(BinaryOperator.GREATERTHAN, astf.newBasicIdentifier("l"),
						astf.newLiteral("5", new LongType())));
		this.astGreater = astf.getAST();

		ASTFactory astf2 = new ASTFactory();
		astf2.addDeclaration("l", new LongType());
		astf2.addDeclaration("r", new BooleanType());
		astf2.addAssignment(astf2.newBasicIdentifier("l"), astf2.newLiteral("10", new LongType()));
		astf2.addAssignment(
				astf2.newBasicIdentifier("r"),
				astf2.newBinaryExpression(BinaryOperator.LESSTHAN, astf2.newBasicIdentifier("l"),
						astf2.newLiteral("5", new LongType())));
		this.astLess = astf2.getAST();
	}

	@Test
	public void testGreaterThan() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(this.astGreater);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n"
				+ "(ASSIGN_LONG|#10|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_G|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

	@Test
	public void testLessThan() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(this.astLess);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|r)" + "\n"
				+ "(ASSIGN_LONG|#10|!|l)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(COMPARE_LONG_L|l|#5|tmp0)" + "\n"
				+ "(ASSIGN_BOOLEAN|tmp0|!|r)" + "\n";

		assertEquals(expected, actual);
	}

}
