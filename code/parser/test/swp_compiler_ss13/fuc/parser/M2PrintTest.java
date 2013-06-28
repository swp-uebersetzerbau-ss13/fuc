package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.b;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.minus;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.print;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.returnn;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.sem;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.lexer.token.RealTokenImpl;
import swp_compiler_ss13.fuc.parser.errorHandling.ParserASTXMLVisualization;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.LRParser;

public class M2PrintTest {
	static {
		BasicConfigurator.configure();
	}
	
	@Test
	public void testPrint() {
		// Simulate input
		Lexer lexer = new TestLexer(
		t("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
		t("double", TokenType.DOUBLE_SYMBOL), id("d"), t(sem),
		t("string", TokenType.STRING_SYMBOL), id("s"), t(sem),
		t("bool", TokenType.BOOL_SYMBOL), id("b"), t(sem),
		t("string", TokenType.STRING_SYMBOL), id("linebreak"), t(sem),
		id("linebreak"), t(assignop),t("\"\\n\"", TokenType.STRING), t(sem),
		id("b"), t(assignop), b(true), t(sem),
		id("l"), t(assignop), num(18121313223L), t(sem),
		id("d"), t(assignop), t(minus), new RealTokenImpl("23.23e-100", TokenType.REAL, -1, -1), t(sem),
		id("s"), t(assignop), t("\"jagÄrEttString\\\"\\n\"", TokenType.STRING), t(sem),
		t(print), id("b"), t(sem),t(print), id("linebreak"), t(sem),
		t(print), id("l"), t(sem),t(print), id("linebreak"), t(sem),
		t(print), id("d"), t(sem),t(print), id("linebreak"), t(sem),
		t(print), id("s"), t(sem),
		t(returnn),t(sem),t(Terminal.EOF));
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(lexer);
		checkAst(ast);
	}

	@Test
	public void testPrintOrgLexer() throws Exception {
		String input = "# return 0\n"
				+ "# prints:\n"
				+ "# true\n"
				+ "# 18121313223\n"
				+ "# -2.323e-99\n"
				+ "# jagÄrEttString\"\n"
				+ "\n"
				+ "long l;\n"
				+ "double d;\n"
				+ "string s;\n"
				+ "bool b;\n"
				+ "\n"
				+ "string linebreak;\n"
				+ "linebreak = \"\\n\";\n"
				+ "\n"
				+ "b = true;\n"
				+ "l = 18121313223;\n"
				+ "d = -23.23e-100;\n"
				+ "s = \"jagÄrEttString\\\"\\n\";  # c-like escaping in strings\n"
				+ "\n"
				+ "print b; print linebreak;\n"
				+ "print l; print linebreak;       # print one digit left of the radix point\n"
				+ "print d; print linebreak;\n"
				+ "print s;\n"
				+ "\n"
				+ "return;                    # equivalent to return EXIT_SUCCESS";
//		String input = GrammarTestHelper.loadExample("print.prog");
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("l", new LongType());
		factory.addDeclaration("d", new DoubleType());
		factory.addDeclaration("s", new StringType(LRParser.STRING_LENGTH));
		factory.addDeclaration("b", new BooleanType());

		factory.addDeclaration("linebreak", new StringType(LRParser.STRING_LENGTH));
		factory.addAssignment(factory.newBasicIdentifier("linebreak"), factory.newLiteral("\"\\n\"", new StringType(4L)));
		factory.addAssignment(factory.newBasicIdentifier("b"), factory.newLiteral("true", new BooleanType()));
		factory.addAssignment(factory.newBasicIdentifier("l"), factory.newLiteral("18121313223", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("d"), factory.newUnaryExpression(UnaryOperator.MINUS, factory.newLiteral("23.23e-100", new DoubleType())));
		factory.addAssignment(factory.newBasicIdentifier("s"), factory.newLiteral("\"jagÄrEttString\\\"\\n\"", new StringType(20L)));
		
		factory.addPrint(factory.newBasicIdentifier("b")); factory.addPrint(factory.newBasicIdentifier("linebreak"));
		factory.addPrint(factory.newBasicIdentifier("l")); factory.addPrint(factory.newBasicIdentifier("linebreak"));
		factory.addPrint(factory.newBasicIdentifier("d")); factory.addPrint(factory.newBasicIdentifier("linebreak"));
		factory.addPrint(factory.newBasicIdentifier("s"));
		
		factory.addReturn(null);
		
		AST expected = factory.getAST();
		ParserASTXMLVisualization vis = new ParserASTXMLVisualization();
		vis.visualizeAST(expected);
		ASTComparator.compareAST(expected, ast);
	}
}
