//package swp_compiler_ss13.fuc.parser;
//
//import static org.junit.Assert.assertNotNull;
//
//import java.io.ByteArrayInputStream;
//
//import org.apache.log4j.BasicConfigurator;
//import org.junit.Test;
//
//import swp_compiler_ss13.common.ast.AST;
//import swp_compiler_ss13.common.lexer.Lexer;
//import swp_compiler_ss13.common.report.ReportLog;
//import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
//import swp_compiler_ss13.fuc.lexer.LexerImpl;
//import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
//import swp_compiler_ss13.fuc.parser.generator.LR1Generator;
//import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
//import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
//import swp_compiler_ss13.fuc.parser.grammar.Grammar;
//import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
//import swp_compiler_ss13.fuc.parser.parser.LRParser;
//import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
//import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
//
//public class M2PrintTest {
//	static {
//		BasicConfigurator.configure();
//	}
//
////	@Test
////	public void testPrint() {
////		// Generate parsing table
////		Grammar grammar = new ProjectGrammar.M1().getGrammar();
////		ALRGenerator<LR0Item, LR0State> generator = new LR0Generator(grammar);
////		LRParsingTable table = generator.getParsingTable();
////
////		// Simulate input
////		Lexer lexer = new TestLexer(
////				new TestToken("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
////				id("l"), t(assignop), num(10), t(plus), num(23), t(minus),
////				num(23), t(plus), num(100), t(div), num(2), t(minus), num(30),
////				t(minus), num(9), t(div), num(3), t(sem), t(returnn), id("l"),
////				t(sem), t(Terminal.EOF));
////
////		// Run LR-parser with table
////		LRParser lrParser = new LRParser();
////		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
////		ReportLog reportLog = new ReportLogImpl();
////		AST ast = lrParser.parse(lexWrapper, reportLog, table);
////
////		checkAst(ast);
////	}
//
////	@Test
////	public void testPrintOrgLexer() throws Exception {
////		String input = "# return 0\n"
////				+ "# prints:\n"
////				+ "# true\n"
////				+ "# 18121313223\n"
////				+ "# -2.323e-99\n"
////				+ "# jagÄrEttString\"\n"
////				+ "\n"
////				+ "long l;\n"
////				+ "double d;\n"
////				+ "string s;\n"
////				+ "bool b;\n"
////				+ "\n"
////				+ "string linebreak;\n"
////				+ "linebreak = \"\n\";\n"
////				+ "\n"
////				+ "b = true;\n"
////				+ "l = 18121313223;\n"
////				+ "d = -23.23e-100;\n"
////				+ "s = \"jagÄrEttString\"\n\";  # c-like escaping in strings\n"
////				+ "\n"
////				+ "print b; print linebreak;\n"
////				+ "print l; print linebreak;       # print one digit left of the radix point\n"
////				+ "print d; print linebreak;\n"
////				+ "print s;\n"
////				+ "\n"
////				+ "return;                    # equivalent to return EXIT_SUCCESS";
////		
////		// Generate parsing table
////		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
////		ALRGenerator<LR1Item, LR1State> generator = new LR1Generator(grammar);
////		LRParsingTable table = generator.getParsingTable();
////
////		// Simulate input
////		Lexer lexer = new LexerImpl();
////		lexer.setSourceStream(new ByteArrayInputStream(input.getBytes()));
////
////		// Run LR-parser with table
////		LRParser lrParser = new LRParser();
////		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
////		ReportLog reportLog = new ReportLogImpl();
////		AST ast = lrParser.parse(lexWrapper, reportLog, table);
////		checkAst(ast);
////	}
//
//	private static void checkAst(AST ast) {
//		assertNotNull(ast);
//		// TODO Validate ast
//	}
//}
