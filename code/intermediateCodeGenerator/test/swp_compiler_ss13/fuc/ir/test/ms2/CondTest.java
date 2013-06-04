package swp_compiler_ss13.fuc.ir.test.ms2;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;

public class CondTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("b", new BooleanType());
		astf.addDeclaration("c", new BooleanType());
		astf.addDeclaration("l", new LongType());
		astf.addDeclaration("abc", new StringType(3L));

		astf.addAssignment(astf.newBasicIdentifier("b"), astf.newLiteral("true", new BooleanType()));
		astf.addAssignment(astf.newBasicIdentifier("c"), astf.newLiteral("false", new BooleanType()));
		astf.addAssignment(astf.newBasicIdentifier("l"), astf.newLiteral("4", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("abc"), astf.newLiteral("bla", new StringType(3L)));

		astf.addBranch(astf.newBasicIdentifier("b"));
		astf.addBlock();
		astf.addBranch(astf.newBinaryExpression(BinaryOperator.LOGICAL_OR, astf.newBasicIdentifier("c"),
				astf.newUnaryExpression(UnaryOperator.LOGICAL_NEGATE, astf.newBasicIdentifier("b"))));
		astf.addBlock();
		astf.addPrint(astf.newBasicIdentifier("abc"));
		astf.goToParent();
		astf.addBlock();
		astf.addAssignment(astf.newBasicIdentifier("l"), astf.newLiteral("5", new LongType()));
		astf.goToParent();
		astf.goToParent();
		astf.goToParent();
		astf.goToParent();
		astf.addReturn(astf.newBasicIdentifier("l"));

		this.ast = astf.getAST();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(this.ast);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(), q.getArgument1(), q.getArgument2(),
					q.getResult()));
		}
		String actual = b.toString();
		System.out.println(actual);

		String expected = ""
				+ "(DECLARE_BOOLEAN|!|!|b)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|c)" + "\n"
				+ "(DECLARE_LONG|!|!|l)" + "\n"
				+ "(DECLARE_STRING|!|!|abc)" + "\n"
				+ "(ASSIGN_BOOLEAN|#TRUE|!|b)" + "\n"
				+ "(ASSIGN_BOOLEAN|#FALSE|!|c)" + "\n"
				+ "(ASSIGN_LONG|#4|!|l)" + "\n"
				+ "(ASSIGN_STRING|#\"bla\"|!|abc)" + "\n"
				+ "(BRANCH|label0|label1|b)" + "\n"
				+ "(LABEL|label0|!|!)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|tmp0)" + "\n"
				+ "(NOT_BOOLEAN|b|!|tmp0)" + "\n"
				+ "(DECLARE_BOOLEAN|!|!|tmp1)" + "\n"
				+ "(OR_BOOLEAN|c|tmp0|tmp1)" + "\n"
				+ "(BRANCH|label3|label4|tmp1)" + "\n"
				+ "(LABEL|label3|!|!)" + "\n"
				+ "(PRINT_STRING|abc|!|!)" + "\n"
				+ "(BRANCH|label5|!|!)" + "\n"
				+ "(LABEL|label4|!|!)" + "\n"
				+ "(ASSIGN_LONG|#5|!|l)" + "\n"
				+ "(LABEL|label5|!|!)" + "\n"
				+ "(BRANCH|label2|!|!)" + "\n"
				+ "(LABEL|label1|!|!)" + "\n"
				+ "(LABEL|label2|!|!)" + "\n"
				+ "(RETURN|l|!|!)" + "\n";
		assertEquals(expected, actual);
	}
}
