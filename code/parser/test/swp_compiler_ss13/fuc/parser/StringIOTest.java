package swp_compiler_ss13.fuc.parser;

import static swp_compiler_ss13.fuc.parser.parser.LRParser.STRING_LENGTH;

import java.io.ByteArrayInputStream;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
import swp_compiler_ss13.fuc.parser.generator.LR1Generator;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.parser.LRParser;
import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

public class StringIOTest {
	@Test
	public void testLinebreakInString() {
		String input = "string s;\ns = \"test1\\ntest2\";\nreturn;";
		
		// Parse
		AST ast = parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("s", new StringType(STRING_LENGTH));
		factory.addAssignment(factory.newBasicIdentifier("s"), factory.newLiteral("\"test1\\ntest2\"", new StringType(14L)));
		factory.addReturn(null);
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
	
	@Test
	public void testConditional() {
		String input = "bool b;\nlong l;\nif ( b ) l = 1;\nreturn l;";
		
		// Parse
		AST ast = parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("b", new BooleanType());
		factory.addDeclaration("l", new LongType());
		BranchNode iff = factory.addBranch(factory.newBasicIdentifier("b"));
		iff.setStatementNodeOnTrue(factory.newAssignment(factory.newBasicIdentifier("l"), factory.newLiteral("1", new LongType())));
		factory.goToParent();
		factory.addReturn(factory.newBasicIdentifier("l"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
	
	@Test
	public void testHalloWelt() {
		String input = "string s;\ns = \"Hallo\\nWelt\";return s;";
		
		// Parse
		AST ast = parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("s", new StringType(LRParser.STRING_LENGTH));
		factory.addAssignment(factory.newBasicIdentifier("s"), factory.newLiteral("\"Hallo\\nWelt\"", new StringType(13L)));
		factory.addReturn(factory.newBasicIdentifier("s"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
	
	private static AST parseToAst(String input) {
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
		return lrParser.parse(lexWrapper, reportLog, table);
	}
}
