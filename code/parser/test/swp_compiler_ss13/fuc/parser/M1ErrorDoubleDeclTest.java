package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertFalse;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;

public class M1ErrorDoubleDeclTest {
//	@Test
//	public void testErrorDoubleDecl() {
//		// Generate parsing table
//		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
//		ALRGenerator<LR0Item, LR0State> generator = new LR0Generator(grammar);
//		LRParsingTable table = generator.getParsingTable();
//
//		// Simulate input
//		Lexer lexer = new TestLexer(
//				new TestToken("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
//				id("l"), t(assignop), num(10), t(plus), num(23), t(minus),
//				num(23), t(plus), num(100), t(div), num(2), t(minus), num(30),
//				t(minus), num(9), t(div), num(3), t(sem), t(returnn), id("l"),
//				t(sem), t(Terminal.EOF));
//	
//		ReportLog reportLog = new ReportLogImpl();
//
//		// Check output
//		try {
//		GrammarTestHelper.parseToAst(lexer, reportLog);
//			fail("Expected double id exception!");
//		} catch (ParserException err) {
//			// Check for correct error
//			GrammarTestHelper.compareReportLogEntries(createExpectedEntries(), reportLog.getEntries(), false);
//		}
//	}

	@Test
	public void testErrorDoubleDeclOrgLexer() throws Exception {
		String input = "# error: two decls for same id i\n"
				+ "long i;\n"
				+ "long i;\n";
		ReportLogImpl reportLog = new ReportLogImpl();
		AST ast = GrammarTestHelper.parseToAst(input, reportLog);
		assertFalse(reportLog.hasErrors() || reportLog.hasWarnings());
		
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("i", new LongType());
		factory.addDeclaration("i", new LongType());
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
