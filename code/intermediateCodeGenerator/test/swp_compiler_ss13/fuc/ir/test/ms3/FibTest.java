package swp_compiler_ss13.fuc.ir.test.ms3;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class FibTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();

		astf.addDeclaration("numbers", new ArrayType(new LongType(), 21));
		astf.addDeclaration("i", new LongType());
		astf.addAssignment(astf.newBasicIdentifier("i"), astf.newLiteral("0",
				new LongType()));
		astf.addBlock();
		astf.addDeclaration("i", new LongType());
		astf.addAssignment(astf.newBasicIdentifier("i"), astf.newLiteral("2", new LongType()));
		astf.addAssignment(
				astf.newArrayIdentifier(astf.newLiteral("0", new LongType()), astf.newBasicIdentifier("numbers")),
				astf.newLiteral("0", new LongType()));
		astf.addAssignment(
				astf.newArrayIdentifier(astf.newLiteral("1", new LongType()), astf.newBasicIdentifier("numbers")),
				astf.newLiteral("1", new LongType()));

		astf.addWhile(astf.newBinaryExpression(BinaryOperator.LESSTHAN, astf.newBasicIdentifier("i"),
				astf.newLiteral("21", new LongType())));
		astf.addBlock();
		astf.addAssignment(astf.newArrayIdentifier(astf.newBasicIdentifier("i"), astf.newBasicIdentifier("numbers")),
				astf.newBinaryExpression(BinaryOperator.ADDITION,
						astf.newArrayIdentifier(astf.newBinaryExpression(BinaryOperator.SUBSTRACTION,
								astf.newBasicIdentifier("i"), astf.newLiteral("1", new LongType())), astf
								.newBasicIdentifier("numbers")),
						astf.newArrayIdentifier(astf.newBinaryExpression(BinaryOperator.SUBSTRACTION,
								astf.newBasicIdentifier("i"), astf.newLiteral("2", new LongType())), astf
								.newBasicIdentifier("numbers"))
						)
				);

		astf.addAssignment(
				astf.newBasicIdentifier("i"),
				astf.newBinaryExpression(BinaryOperator.ADDITION, astf.newBasicIdentifier("i"),
						astf.newLiteral("1", new LongType())));
		astf.goToParent();
		astf.goToParent();

		astf.addPrint(astf.newArrayIdentifier(astf.newLiteral("20", new LongType()), astf.newBasicIdentifier("numbers")));
		astf.addReturn(astf.newArrayIdentifier(astf.newLiteral("15", new LongType()),
				astf.newBasicIdentifier("numbers")));

		this.ast = astf.getAST();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException, IOException, InterruptedException, BackendException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(this.ast);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("%s|%s|%s|%s\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		String expected = "DECLARE_ARRAY|#21|!|numbers\n" +
				"DECLARE_LONG|!|!|!\n" +
				"DECLARE_LONG|!|!|i\n" +
				"ASSIGN_LONG|#0|!|i\n" +
				"DECLARE_LONG|!|!|tmp0\n" +
				"ASSIGN_LONG|#2|!|tmp0\n" +
				"ARRAY_SET_LONG|numbers|#0|#0\n" +
				"ARRAY_SET_LONG|numbers|#1|#1\n" +
				"LABEL|label0|!|!\n" +
				"DECLARE_BOOLEAN|!|!|tmp1\n" +
				"COMPARE_LONG_L|tmp0|#21|tmp1\n" +
				"BRANCH|label1|label2|tmp1\n" +
				"LABEL|label1|!|!\n" +
				"DECLARE_LONG|!|!|tmp2\n" +
				"SUB_LONG|tmp0|#1|tmp2\n" +
				"DECLARE_LONG|!|!|tmp3\n" +
				"ARRAY_GET_LONG|numbers|tmp2|tmp3\n" +
				"DECLARE_LONG|!|!|tmp4\n" +
				"SUB_LONG|tmp0|#2|tmp4\n" +
				"DECLARE_LONG|!|!|tmp5\n" +
				"ARRAY_GET_LONG|numbers|tmp4|tmp5\n" +
				"DECLARE_LONG|!|!|tmp6\n" +
				"ADD_LONG|tmp3|tmp5|tmp6\n" +
				"ARRAY_SET_LONG|numbers|tmp0|tmp6\n" +
				"DECLARE_LONG|!|!|tmp7\n" +
				"ADD_LONG|tmp0|#1|tmp7\n" +
				"ASSIGN_LONG|tmp7|!|tmp0\n" +
				"BRANCH|label0|!|!\n" +
				"LABEL|label2|!|!\n" +
				"DECLARE_LONG|!|!|tmp8\n" +
				"ARRAY_GET_LONG|numbers|#20|tmp8\n" +
				"DECLARE_STRING|!|!|tmp9\n" +
				"LONG_TO_STRING|tmp8|!|tmp9\n" +
				"PRINT_STRING|tmp9|!|!\n" +
				"DECLARE_LONG|!|!|tmp10\n" +
				"ARRAY_GET_LONG|numbers|#15|tmp10\n" +
				"RETURN|tmp10|!|!\n";
		assertEquals(expected, actual);
	}

}
