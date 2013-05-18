package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import java.io.ByteArrayInputStream;

import lexer.LexerImpl;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;
import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
import swp_compiler_ss13.fuc.parser.generator.LR0Generator;
import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR0State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.parser.LRParser;
import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

public class M1SimpleAddTest {
	static {
		BasicConfigurator.configure();
	}

//	@Test
//	public void testAdd() {
//		// String input = "# return 27\n"
//		// + "long l;\n"
//		// + "l = 10 +\n"
//		// + "23 # - 23\n"
//		// + "- 23\n"
//		// + "+ 100 /\n"
//		// + "\n"
//		// + "2\n"
//		// + "- 30\n"
//		// + "- 9 / 3;\n"
//		// + "return l;\n";
//		// Generate parsing table
//		Grammar grammar = new ProjectGrammar.M1().getGrammar();
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
//		// Run LR-parser with table
//		LRParser lrParser = new LRParser();
//		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
//		ReportLog reportLog = new ReportLogImpl();
//		AST ast = lrParser.parse(lexWrapper, reportLog, table);
//
//		checkAst(ast);
//	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		// TODO Validate ast
	}

	@Test
	public void testSimpleAddOrgLexer() {
		String input = "# returns 6\n"
				+ "long l;\n"
				+ "l = 3 + 3;\n"
				+ "return l;";
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.M1().getGrammar();
		ALRGenerator<LR0Item, LR0State> generator = new LR0Generator(grammar);
		LRParsingTable table = generator.getParsingTable();

		// Simulate input
		Lexer lexer = new LexerImpl();
		lexer.setSourceStream(new ByteArrayInputStream(input.getBytes()));

		// Run LR-parser with table
		LRParser lrParser = new LRParser();
		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
		ReportLog reportLog = new ReportLogImpl();
		AST ast = lrParser.parse(lexWrapper, reportLog, table);

		checkAst(ast);
	}
}
