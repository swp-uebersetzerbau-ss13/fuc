package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.id;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.num;
import static swp_compiler_ss13.fuc.parser.GrammarTestHelper.t;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.Complete.assignop;
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

public class M2AssignmentTest {
	@Test
	public void testAssignment() {
		// Simulate input
		Lexer lexer = new TestLexer(
				t("long", TokenType.LONG_SYMBOL), id("a"), t(sem),
				t("long", TokenType.LONG_SYMBOL), id("b"), t(sem),
				t("long", TokenType.LONG_SYMBOL), id("c"), t(sem),
				id("a"), t(assignop), num(4), t(sem),
				id("b"), t(assignop), num(3), t(sem),
				id("c"), t(assignop), num(2), t(sem),
				id("a"), t(assignop), id("b"), t(assignop), num(4), t(sem),
				id("c"), t(assignop), id("a"), t(plus), id("b"), t(plus), id("c"), t(sem),
				t(returnn), id("c"), t(sem),
				t(Terminal.EOF));
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(lexer);
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
		
		// Check output
		AST ast = GrammarTestHelper.parseToAst(input);
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
