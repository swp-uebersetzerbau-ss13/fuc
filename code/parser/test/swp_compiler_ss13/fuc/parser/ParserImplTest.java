package swp_compiler_ss13.fuc.parser;

import org.junit.Test;
import static org.junit.Assert.*;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;

import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class ParserImplTest {
	@Test
	public void testMissingReportLogAndLexer() {
		Parser parser = new ParserImpl();
		
		try {
			parser.getParsedAST();
			fail("Expected NPE!");
		} catch (NullPointerException err) {
			// Success: Neither ReportLog nor Lexer is set
		}
		
		parser.setLexer(new TestLexer(t(Terminal.EOF)));

		try {
			parser.getParsedAST();
			fail("Expected NPE!");
		} catch (NullPointerException err) {
			// Success: Still ReportLog is missing
		}
		
		parser.setLexer(null);
		parser.setReportLog(new ReportLogImpl());

		try {
			parser.getParsedAST();
			fail("Expected NPE!");
		} catch (NullPointerException err) {
			// Success: Now Lexer isn't there
		}
		
		parser.setLexer(new TestLexer(t(Terminal.EOF)));
		assertNotNull(parser.getParsedAST());
	}
}
