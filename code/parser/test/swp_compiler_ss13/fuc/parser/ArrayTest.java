package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.errorHandling.ParserASTXMLVisualization;

public class ArrayTest {
	static {
		BasicConfigurator.configure();
	}



	@Test
	public void testArrayTest() throws Exception {
		String input = "bool [ 5 ] b;\n" +
						"bool a;\n" + 
						"a = b [ 2 ] ;\n" +
						"return;";

		// Run LR-parser
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		System.out.println(new ParserASTXMLVisualization().visualizeAST(ast));

		// Create reference AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("b", new ArrayType(new BooleanType(), 5));
		factory.addDeclaration("a", new BooleanType());

		factory.addAssignment(factory.newBasicIdentifier("a"), factory.newArrayIdentifier(2, factory.newBasicIdentifier("b")));
		
		factory.addReturn(null);
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
