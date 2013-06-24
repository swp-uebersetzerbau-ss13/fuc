package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.fail;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.returnn;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.sem;

import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.parser.GrammarTestHelper;
import swp_compiler_ss13.fuc.parser.TestLexer;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class LRParserTest {
	@Test
	public void testLRParser() {
		// Simulate input
		Lexer lexer = new TestLexer(
				t("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
				id("l"), t(assignop), num(10), t(sem), t(returnn), id("l"),
				t(sem), t(sem), t(Terminal.EOF));	// One semicolon too much!
		
		// Check output
		try {
			GrammarTestHelper.parseToAst(lexer, new ReportLogImpl());
			fail("Expected a Parserexception!");
		} catch (ParserException err) {
			// TODO PE: NO_RULE_FOR_THIS_TERMINAL
		}
	}
}
