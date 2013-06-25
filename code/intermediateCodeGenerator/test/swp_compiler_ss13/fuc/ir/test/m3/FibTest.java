package swp_compiler_ss13.fuc.ir.test.m3;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
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
import swp_compiler_ss13.fuc.backend.TACExecutor;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class FibTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();

		astf.addDeclaration("numbers", new ArrayType(new LongType(), 21));
		// astf.addDeclaration("i", new LongType());
		// astf.addAssignment(astf.newBasicIdentifier("i"), astf.newLiteral("0",
		// new LongType()));
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
		System.out.println(actual);

		TACExecutor tace = new TACExecutor();
		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		InputStream s = new ByteArrayInputStream(actual.getBytes());
		System.out.println(tace.runTAC(s));
	}

}
