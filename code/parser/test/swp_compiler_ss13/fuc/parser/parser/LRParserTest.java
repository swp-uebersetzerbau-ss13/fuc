package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.fail;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.returnn;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.sem;

import java.util.Arrays;

import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.errorLog.LogEntry.Type;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;
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
	
	@Test
	public void testError() {
		String input = "long l;\n"
				+ "l = 1;\n"
				+ "retunl;\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		try {
			GrammarTestHelper.parseToAst(input, reportLog);
			fail("Expected ParserException!");
		} catch (ParserException err) {
			LogEntry entry = new LogEntry(Type.ERROR, ReportType.UNDEFINED, Arrays.<Token>asList(new TokenImpl("retunl", TokenType.ID, 3, 1)), "");
			GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry), reportLog.getErrors());
		}
	}
	
	@Test
	public void testErrorRecoveryMissingSemicolon() {
		String input = "long l;\n"
				+ "l = 1\n"
				+ "return l;\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		GrammarTestHelper.parseToAst(input, reportLog);
		
		LogEntry entry = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(new TokenImpl("1", TokenType.NUM, 2, 5)), "");
		GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry), reportLog.getEntries());
	}
	
	@Test
	public void testErrorRecoveryMissingSemicola() {
		String input = "long l\n"
				+ "l = 1\n"
				+ "return l\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		GrammarTestHelper.parseToAst(input, reportLog);

		Token l = new TokenImpl("l", TokenType.ID, 1, 6);
		Token one = new TokenImpl("1", TokenType.NUM, 2, 5);
		Token l2 = new TokenImpl("l", TokenType.ID, 3, 8);
		LogEntry entry0 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(l), "");
		LogEntry entry1 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(one), "");
		LogEntry entry2 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(l2), "");
		GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry0, entry1, entry2), reportLog.getEntries());
	}
	
	@Test
	public void testErrorRecoveryMissingSemicolonFail() {
		String input = "long l\n"
				+ "l = 1\n"
				+ "retunl\n"
				+ "return l;\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		try {
			GrammarTestHelper.parseToAst(input, reportLog);
			fail("Expected ParserException!");
		} catch (ParserException err) {
			
		}
		
		Token l = new TokenImpl("l", TokenType.ID, 1, 6);
		Token one = new TokenImpl("1", TokenType.NUM, 2, 5);
		Token returnn = new TokenImpl("return", TokenType.RETURN, 4, 1);
		LogEntry entry0 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(l), "");
		LogEntry entry1 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(one), "");
		LogEntry entry2 = new LogEntry(Type.ERROR, ReportType.UNDEFINED, Arrays.<Token>asList(returnn), "");
		GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry0, entry1, entry2), reportLog.getEntries());
	}
	
	@Test
	public void testErrorRecoveryMissingClosingCurlyBraceIfElse() {
		String input = "long l;\n"
				+ "l = 1;\n"
				+ "if (l == 1) {\n"
				+ "print l;\n"
				+ "else {\n"
				+ "l = 2;\n"
				+ "}\n"
				+ "return l;\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		GrammarTestHelper.parseToAst(input, reportLog);
		
		LogEntry entry = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(new TokenImpl(";", TokenType.SEMICOLON, 4, 8)), "");
		GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry), reportLog.getEntries());
	}
	
	@Test
	public void testErrorRecoveryMissingClosingCurlyBraceIfEof() {
		String input = "long l;\n"
				+ "l = 1;\n"
				+ "if (l == 1) {\n"
				+ "print l;\n"
				+ "return l;\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		GrammarTestHelper.parseToAst(input, reportLog);
		
		LogEntry entry = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(new TokenImpl(";", TokenType.SEMICOLON, 5, 9)), "");
		GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry), reportLog.getEntries());
	}
	
	@Test
	public void testErrorRecoveryMissingIfExprBraces() {
		String input = "long l;\n"
				+ "l = 1;\n"
				+ "if l == 1 {\n"
				+ "print l;\n"
				+ "}\n"
				+ "return l;\n";
		
		ReportLogImpl reportLog = new ReportLogImpl();
		GrammarTestHelper.parseToAst(input, reportLog);
		
		LogEntry entry0 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(new TokenImpl("if", TokenType.IF, 3, 1)), "");
		LogEntry entry1 = new LogEntry(Type.WARNNING, ReportType.UNDEFINED, Arrays.<Token>asList(new TokenImpl("1", TokenType.NUM, 3, 9)), "");
		GrammarTestHelper.compareReportLogEntries(Arrays.asList(entry0, entry1), reportLog.getEntries());
	}
}
