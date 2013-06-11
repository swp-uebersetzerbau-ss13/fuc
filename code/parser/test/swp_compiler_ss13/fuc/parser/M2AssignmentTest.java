package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.plus;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.sem;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.returnn;

import java.io.ByteArrayInputStream;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
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

public class M2AssignmentTest {
	static {
		BasicConfigurator.configure();
	}

	@Test
	public void testAssignment() {
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
		ALRGenerator<LR1Item, LR1State> generator = new LR1Generator(grammar);
		LRParsingTable table = generator.getParsingTable();

		// Simulate input
		Lexer lexer = new TestLexer(
				new TestToken("long", TokenType.LONG_SYMBOL), id("a"), t(sem),
				new TestToken("long", TokenType.LONG_SYMBOL), id("b"), t(sem),
				new TestToken("long", TokenType.LONG_SYMBOL), id("c"), t(sem),
				id("a"), t(assignop), num(4), t(sem),
				id("b"), t(assignop), num(3), t(sem),
				id("c"), t(assignop), num(2), t(sem),
				id("a"), t(assignop), id("b"), t(assignop), num(4), t(sem),
				id("c"), t(assignop), id("a"), t(plus), id("b"), t(plus), id("c"), t(sem),
				t(returnn), id("c"), t(sem),
				t(Terminal.EOF));

		// Run LR-parser with table
		LRParser lrParser = new LRParser();
		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
		ReportLog reportLog = new ReportLogImpl();
		AST ast = lrParser.parse(lexWrapper, reportLog, table);

		checkAst(ast);
	}

 
	@Test
	public void testAssignmentOrgLexer() throws Exception {
		String input = "# returns 10\n"
				+ "# prints nothing\n"
				+ "long a;\n"
				+ "long b;\n"
				+ "long c;\n"
				+ "\n"
				+ "\n"
				+ "a = 4;\n"
				+ "b = 3;\n"
				+ "c = 2;\n"
				+ "\n"
				+ "a = b = 4;\n"
				+ "c = a + b + c;\n"
				+ "\n"
				+ "return c;\n";
//		String input = GrammarTestHelper.loadExample("test2.prog");
//		String input = "\"test1\ntest2\"";
//		String input = "string s;\ns = \"Hallo\nWelt\";";
		
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

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("a", new LongType());
		factory.addDeclaration("b", new LongType());
		factory.addDeclaration("c", new LongType());
		factory.addAssignment(factory.newBasicIdentifier("a"), factory.newLiteral("4", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("b"), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("c"), factory.newLiteral("2", new LongType()));
		
		factory.addAssignment(factory.newBasicIdentifier("a"),
					factory.newAssignment(factory.newBasicIdentifier("b"), 
					factory.newLiteral("4", new LongType())));
		
		factory.addAssignment(factory.newBasicIdentifier("c"),
				factory.newBinaryExpression(BinaryOperator.ADDITION,
						factory.newBinaryExpression(BinaryOperator.ADDITION, factory.newBasicIdentifier("a"), factory.newBasicIdentifier("b")),
						factory.newBasicIdentifier("c")));
		factory.addReturn(factory.newBasicIdentifier("c"));
		
		AST expectedAst = factory.getAST();
		ASTComparator.compareAST(expectedAst, ast);
	}
}
