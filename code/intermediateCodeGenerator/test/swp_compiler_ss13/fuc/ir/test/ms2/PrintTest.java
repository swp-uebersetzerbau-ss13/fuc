package swp_compiler_ss13.fuc.ir.test.ms2;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;

public class PrintTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("l", new LongType());
		astf.addDeclaration("d", new DoubleType());
		astf.addDeclaration("s", new StringType(255L));
		astf.addDeclaration("b", new BooleanType());

		astf.addDeclaration("newline", new StringType(2L));
		astf.addAssignment(astf.newBasicIdentifier("newline"), astf.newLiteral("\n", new StringType(2L)));

		astf.addAssignment(astf.newBasicIdentifier("b"), astf.newLiteral("true", new BooleanType()));
		astf.addAssignment(astf.newBasicIdentifier("l"), astf.newLiteral("18121313223", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("d"), astf.newLiteral("-23.23e-100", new DoubleType()));
		astf.addAssignment(astf.newBasicIdentifier("s"), astf.newLiteral("jagÄrEttString\"\\n", new StringType(255L)));

		astf.addPrint(astf.newBasicIdentifier("b"));
		astf.addPrint(astf.newBasicIdentifier("newline"));
		astf.addPrint(astf.newBasicIdentifier("l"));
		astf.addPrint(astf.newBasicIdentifier("newline"));
		astf.addPrint(astf.newBasicIdentifier("d"));
		astf.addPrint(astf.newBasicIdentifier("newline"));
		astf.addPrint(astf.newBasicIdentifier("s"));

		astf.addReturn(null);
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
	}

}
