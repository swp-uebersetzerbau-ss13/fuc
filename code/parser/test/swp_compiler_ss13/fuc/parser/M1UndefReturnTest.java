package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class M1UndefReturnTest {
	static {
		BasicConfigurator.configure();
	}

//	@Test
//	public void testErrorUndefReturn() {
//		// Simulate input
//		Lexer lexer = new TestLexer(
//				new TestToken("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
//				id("l"), t(assignop), num(10), t(plus), num(23), t(minus),
//				num(23), t(plus), num(100), t(div), num(2), t(minus), num(30),
//				t(minus), num(9), t(div), num(3), t(sem), t(returnn), id("l"),
//				t(sem), t(Terminal.EOF));
//	
//	// Check output
//	AST ast = GrammarTestHelper.parseToAst(lexer);
//	checkAst(ast);
//	}

	@Test
	public void testErrorUndefReturnOrgLexer() throws Exception {
		String input = "# error: id spam is not initialized and returned\n"
				+ "long spam;\n"
				+ "return spam;";
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}


	private static void checkAst(AST ast) {
		assertNotNull(ast);
		
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("spam", new LongType());
		factory.addReturn(factory.newBasicIdentifier("spam"));
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}