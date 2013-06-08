package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.*;

import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.*;

import java.io.ByteArrayInputStream;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
import swp_compiler_ss13.fuc.parser.generator.LR1Generator;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.LRParser;
import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

public class M2CondTest {
	static {
		BasicConfigurator.configure();
	}

	@Test
	public void testCond() {
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
		ALRGenerator<LR1Item, LR1State> generator = new LR1Generator(grammar);
		LRParsingTable table = generator.getParsingTable();

		// Simulate input
		Lexer lexer = new TestLexer(
				new TestToken("bool", TokenType.BOOL_SYMBOL), id("b"), t(sem),
				new TestToken("bool", TokenType.BOOL_SYMBOL), id("c"), t(sem),
				new TestToken("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
				new TestToken("string", TokenType.STRING_SYMBOL), id("bla"), t(sem),
				id("bla"), t(assignop), new TestToken("bla", TokenType.STRING), t(sem),
				id("b"), t(assignop), t(truee), t(sem),
				id("c"), t(assignop), t(falsee), t(sem),
				id("l"), t(assignop), num(4), t(sem),
				t(iff), t(lb), id("b"), t(rb),
				t(iff), t(lb), id("c"), t(orop), t(not), id("b"), t(rb),
				t(print), id("bla"), t(sem),
				t(elsee),
				id("l"), t(assignop), num(5), t(sem),
				t(returnn), id("l"), t(sem), t(Terminal.EOF));

		// Run LR-parser with table
		LRParser lrParser = new LRParser();
		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
		ReportLog reportLog = new ReportLogImpl();
		AST ast = lrParser.parse(lexWrapper, reportLog, table);

		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		// TODO Validate ast
	}

	@Test
	public void testCondOrgLexer() throws Exception {
		String input = "# return 5\n"
				+ "# prints nothing\n"
				+ "bool b;\n"
				+ "bool c;\n"
				+ "long l;\n"
				+ "\n"
				+ "string bla;\n"
				+ "bla = \"bla\";\n"
				+ "\n"
				+ "b = true;\n"
				+ "c = false;\n"
				+ "\n"
				+ "l = 4;\n"
				+ "\n"
				+ "# dangling-else should be resolved as given by indentation\n"
				+ "\n"
				+ "if ( b )\n"
				+ "  if ( c || ! b )\n"
				+ "    print bla;\n"
				+ "  else\n"
				+ "    l = 5;\n"
				+ "\n"
				+ "return l;\n";
		
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
		ALRGenerator<LR1Item, LR1State> generator = new LR1Generator(grammar);
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
