package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;

import java.io.ByteArrayInputStream;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
import swp_compiler_ss13.fuc.parser.generator.LR1Generator;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.parser.LRParser;
import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

public class ArrayTest {
	static {
		BasicConfigurator.configure();
	}

	public void testArray() {
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.Complete().getGrammar();
		ALRGenerator<LR1Item, LR1State> generator = new LR1Generator(grammar);
		LRParsingTable table = generator.getParsingTable();

		// Simulate input
		Lexer lexer = new TestLexer(
				new TestToken("bool", TokenType.BOOL_SYMBOL), new TestToken("[", TokenType.LEFT_BRACKET),
				new NumTokenImpl("3",TokenType.NUM,0,0), new TestToken("]",TokenType.RIGHT_BRACKET), id("b"));

		// Run LR-parser with table
		LRParser lrParser = new LRParser();
		
		LexerWrapper lexWrapper = new LexerWrapper(lexer, grammar);
		ReportLog reportLog = new ReportLogImpl();
		AST ast = lrParser.parse(lexWrapper, reportLog, table);

//		checkAst(ast);
	}

	@Test
	public void testCondOrgLexer() throws Exception {
		String input = "bool [ 5 ] b;\n";
		
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

//		// Create reference AST
//		ASTFactory factory = new ASTFactory();
//		factory.addDeclaration("b", new BooleanType());
//		factory.addDeclaration("c", new BooleanType());
//		factory.addDeclaration("l", new LongType());
//
//		factory.addDeclaration("bla", new StringType(LRParser.STRING_LENGTH));
//		factory.addAssignment(factory.newBasicIdentifier("bla"), factory.newLiteral("\"bla\"", new StringType(5L)));
//
//		factory.addAssignment(factory.newBasicIdentifier("b"), factory.newLiteral("true", new BooleanType()));
//		factory.addAssignment(factory.newBasicIdentifier("c"), factory.newLiteral("false", new BooleanType()));
//
//		factory.addAssignment(factory.newBasicIdentifier("l"), factory.newLiteral("4", new LongType()));
//		
//		BranchNode iff = factory.addBranch(factory.newBasicIdentifier("b"));
//		BranchNode ifElse = new BranchNodeImpl();
//		ifElse.setCondition(factory.newBinaryExpression(
//				BinaryOperator.LOGICAL_OR,
//				factory.newBasicIdentifier("c"),
//				factory.newUnaryExpression(UnaryOperator.LOGICAL_NEGATE,
//						factory.newBasicIdentifier("b"))));
//		ifElse.setStatementNodeOnTrue(factory.newPrint(factory.newBasicIdentifier("bla")));
//		ifElse.setStatementNodeOnFalse(factory.newAssignment(factory.newBasicIdentifier("l"), factory.newLiteral("5", new LongType())));
//		iff.setStatementNodeOnTrue(ifElse);
//		factory.goToParent();
//		
//		factory.addReturn(factory.newBasicIdentifier("l"));
//		
//		AST expected = factory.getAST();
//		ASTComparator.compareAST(expected, ast);
	}
}
