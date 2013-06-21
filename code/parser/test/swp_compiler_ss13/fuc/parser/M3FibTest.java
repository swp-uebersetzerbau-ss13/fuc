package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.BasicConfigurator;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class M3FibTest {
	static {
		BasicConfigurator.configure();
	}
	
	@Test
	@Ignore
	public void testFib() {
//		// Simulate input
//		Lexer lexer = new TestLexer(
//		t("long", TokenType.LONG_SYMBOL), id("l"), t(sem),
//		t("double", TokenType.DOUBLE_SYMBOL), id("d"), t(sem),
//		t("string", TokenType.STRING_SYMBOL), id("s"), t(sem),
//		t("bool", TokenType.BOOL_SYMBOL), id("b"), t(sem),
//		t("string", TokenType.STRING_SYMBOL), id("linebreak"), t(sem),
//		id("linebreak"), t(assignop),t("\"\\n\"", TokenType.STRING), t(sem),
//		id("b"), t(assignop), b(true), t(sem),
//		id("l"), t(assignop), num(18121313223L), t(sem),
//		id("d"), t(assignop), new RealTokenImpl("-23.23e-100", TokenType.REAL, -1, -1), t(sem),
//		id("s"), t(assignop), t("\"jagÄrEttString\\\"\\n\"", TokenType.STRING), t(sem),
//		t(print), id("b"), t(sem),t(print), id("linebreak"), t(sem),
//		t(print), id("l"), t(sem),t(print), id("linebreak"), t(sem),
//		t(print), id("d"), t(sem),t(print), id("linebreak"), t(sem),
//		t(print), id("s"), t(sem),
//		t(returnn),t(sem),t(Terminal.EOF));		
//
//		AST ast = GrammarTestHelper.parseToAst(lexer);
//		checkAst(ast);
	}

	@Test
	@Ignore
	public void testFibOrgLexer() throws Exception {
		String input = "# returns 98\n"
				+ "# prints:\n"
				+ "# 6765\n"
				+ "\n"
				+ "long[21] numbers;\n"
				+ "long i = 0;\n"
				+ "\n"
				+ "{\n"
				+ "long i;\n"
				+ "long i = 2;\n"
				+ "numbers[0] = 0;\n"
				+ "numbers[1] = 1;\n"
				+ "\n"
				+ "while ( i < 21 ) {\n"
				+ "numbers[i] = numbers[i-1] + numbers[i-2];\n"
				+ "i = i + 1;\n"
				+ "}\n"
				+ "\n"
				+ "print numbers[20];\n"
				+ "return numbers[15];\n"
				+ "}\n";
		
		// Generate parsing table
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		
		ASTFactory factory = new ASTFactory();
//		factory.addDeclaration("l", new LongType());
//		factory.addDeclaration("d", new DoubleType());
//		factory.addDeclaration("s", new StringType(LRParser.STRING_LENGTH));
//		factory.addDeclaration("b", new BooleanType());
//
//		factory.addDeclaration("linebreak", new StringType(LRParser.STRING_LENGTH));
//		factory.addAssignment(factory.newBasicIdentifier("linebreak"), factory.newLiteral("\"\\n\"", new StringType(4L)));
//		factory.addAssignment(factory.newBasicIdentifier("b"), factory.newLiteral("true", new BooleanType()));
//		factory.addAssignment(factory.newBasicIdentifier("l"), factory.newLiteral("18121313223", new LongType()));
//		factory.addAssignment(factory.newBasicIdentifier("d"), factory.newLiteral("-23.23e-100", new DoubleType()));
//		factory.addAssignment(factory.newBasicIdentifier("s"), factory.newLiteral("\"jagÄrEttString\\\"\\n\"", new StringType(20L)));
//		
//		factory.addPrint(factory.newBasicIdentifier("b")); factory.addPrint(factory.newBasicIdentifier("linebreak"));
//		factory.addPrint(factory.newBasicIdentifier("l")); factory.addPrint(factory.newBasicIdentifier("linebreak"));
//		factory.addPrint(factory.newBasicIdentifier("d")); factory.addPrint(factory.newBasicIdentifier("linebreak"));
//		factory.addPrint(factory.newBasicIdentifier("s"));
//		
//		factory.addReturn(null);
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
