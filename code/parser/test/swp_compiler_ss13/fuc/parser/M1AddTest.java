package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.div;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.minus;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.plus;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.returnn;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.sem;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class M1AddTest {
	@Test
	public void testAdd() {
		// Simulate input
		Lexer lexer = new TestLexer(
				t("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
				id("l"), t(assignop), num(10), t(plus), num(23), t(minus),
				num(23), t(plus), num(100), t(div), num(2), t(minus), num(30),
				t(minus), num(9), t(div), num(3), t(sem), t(returnn), id("l"),
				t(sem), t(Terminal.EOF));
		// Check output
		AST ast = GrammarTestHelper.parseToAst(lexer);
		checkAst(ast);
	}

	@Test
	public void testAddOrgLexer() throws Exception {
		 String input = "# return 27\n"
		 + "long l;\n"
		 + "l = 10 +\n"
		 + "23 # - 23\n"
		 + "- 23\n"
		 + "+ 100 /\n"
		 + "\n"
		 + "2\n"
		 + "- 30\n"
		 + "- 9 / 3;\n"
		 + "return l;\n";
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		
		// Create expected
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("l", new LongType());
		factory.addAssignment(factory.newBasicIdentifier("l"),
				factory.newBinaryExpression(BinaryOperator.SUBSTRACTION,
				factory.newBinaryExpression(BinaryOperator.SUBSTRACTION,
				factory.newBinaryExpression(BinaryOperator.ADDITION, 
				factory.newBinaryExpression(BinaryOperator.SUBSTRACTION,
				factory.newBinaryExpression(BinaryOperator.ADDITION,
						factory.newLiteral("10", new LongType()),
						factory.newLiteral("23", new LongType())),
						factory.newLiteral("23", new LongType())),
						factory.newBinaryExpression(BinaryOperator.DIVISION,
								factory.newLiteral("100", new LongType()),
								factory.newLiteral("2", new LongType()))),
						factory.newLiteral("30", new LongType())),
						factory.newBinaryExpression(BinaryOperator.DIVISION,
								factory.newLiteral("9", new LongType()),
								factory.newLiteral("3", new LongType()))));
		factory.addReturn(factory.newBasicIdentifier("l"));
		AST expected = factory.getAST();
		
		ASTComparator.compareAST(expected, ast);
	}
}
