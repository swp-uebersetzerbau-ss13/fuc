package swp_compiler_ss13.fuc.ir.test.ms2;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class ArrayTest {

	private static AST ast;

	@Before
	public void setUp() throws Exception {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("l", new LongType());
		IdentifierNode identifier = f.newBasicIdentifier("l");
		ExpressionNode expression = f.newLiteral("3", new LongType());
		f.newAssignment(identifier, expression);
		f.addReturn(identifier);
		ast = f.getAST();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		// ASTXMLVisualization visualization = new ASTXMLVisualization();
		// visualization.visualizeAST(ast);

		// IntermediateCodeGenerator gen = new IntermediateCodeGeneratorImpl();
		// gen.generateIntermediateCode(ast);
	}
}
