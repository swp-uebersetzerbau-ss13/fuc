package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class M1SimpleMulTest {
//	@Test
//	public void testSimpleMul() {
//		// Simulate input
//		Lexer lexer = new TestLexer(
//				new TestToken("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
//				id("l"), t(assignop), num(10), t(plus), num(23), t(minus),
//				num(23), t(plus), num(100), t(div), num(2), t(minus), num(30),
//				t(minus), num(9), t(div), num(3), t(sem), t(returnn), id("l"),
//				t(sem), t(Terminal.EOF));
//	
//	// Check output
//	AST ast = GrammarTestHelper.parseToAst(lexer);
//	checkAst(ast);
//	}

	@Test
	public void testSimpleMulOrgLexer() throws Exception {
		String input = "# return 9\n"
				+ "long l;\n"
				+ "l = 3 * 3;\n"
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
				factory.newBinaryExpression(BinaryOperator.MULTIPLICATION,
						factory.newLiteral("3", new LongType()),
						factory.newLiteral("3", new LongType())));
		factory.addReturn(factory.newBasicIdentifier("l"));
		AST expected = factory.getAST();
		
		ASTComparator.compareAST(expected, ast);
	}
}
