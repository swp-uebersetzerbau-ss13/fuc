package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.div;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.lb;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.minus;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.plus;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.rb;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.returnn;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.sem;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.times;

import org.apache.log4j.BasicConfigurator;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class M1ParathesesTest {
	static {
		BasicConfigurator.configure();
	}

	@Test
	public void testParatheses() {
		// Simulate input
		Lexer lexer = new TestLexer(t("", TokenType.COMMENT),
				t("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
				id("l"), t(assignop), t(lb), num(3), t(plus), num(3), t(rb),
				t(times), num(2), t(minus), t(lb), id("l"), t(assignop), t(lb),
				num(2), t(plus), t(lb), num(16), t(div), num(8), t(rb), t(rb),
				t(rb), t(sem),
				t(returnn), id("l"), t(sem), t(Terminal.EOF));
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(lexer);
		checkAst(ast);
	}

	@Test
	public void testParathesesOrgLexer() throws Exception {
		String input = "# returns 8 or does it?\n"
				+ "long l;\n"
				+ "l = ( 3 + 3 ) * 2 - ( l = ( 2 + ( 16 / 8 ) ) );\n"
				+ "return l;\n";
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("l", new LongType());
		factory.addAssignment(
				factory.newBasicIdentifier("l"),
				factory.newBinaryExpression(BinaryOperator.SUBSTRACTION,
				factory.newBinaryExpression(BinaryOperator.MULTIPLICATION,
				factory.newBinaryExpression(BinaryOperator.ADDITION,
						factory.newLiteral("3", new LongType()),
						factory.newLiteral("3", new LongType())),
						factory.newLiteral("2",	new LongType())),
						factory.newAssignment(factory.newBasicIdentifier("l"),
								factory.newBinaryExpression(BinaryOperator.ADDITION,
										factory.newLiteral("2", new LongType()),
										factory.newBinaryExpression(BinaryOperator.DIVISION,
												factory.newLiteral("16", new LongType()),
												factory.newLiteral("8", new LongType()))))));
		factory.addReturn(factory.newBasicIdentifier("l"));
		AST expected = factory.getAST();
		
		ASTComparator.compareAST(expected, ast);
	}
}
