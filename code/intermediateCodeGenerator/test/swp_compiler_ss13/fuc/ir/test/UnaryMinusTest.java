package swp_compiler_ss13.fuc.ir.test;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class UnaryMinusTest {

	private AST ast;

	@Before
	public void setUp() {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("a", new LongType());
		astf.addAssignment(astf.newBasicIdentifier("a"),
				astf.newLiteral("6", new LongType()));
		astf.addExpression(astf.newUnaryExpression(UnaryOperator.MINUS,
				astf.newBasicIdentifier("a")));
		astf.addDeclaration("b", new DoubleType());
		astf.addAssignment(astf.newBasicIdentifier("a"),
				astf.newLiteral("6.0", new DoubleType()));
		astf.addExpression(astf.newUnaryExpression(UnaryOperator.MINUS,
				astf.newBasicIdentifier("b")));

		astf.addDeclaration("s", new BooleanType());
		astf.addAssignment(astf.newBasicIdentifier("s"),
				astf.newLiteral("true", new BooleanType()));

		astf.addExpression(astf.newUnaryExpression(UnaryOperator.MINUS,
				astf.newBasicIdentifier("s")));

		ast = astf.getAST();
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
}
