package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.BasicConfigurator;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.parser.LRParser;

public class M3MatrixMultiplicationTest {
	static {
		BasicConfigurator.configure();
	}
	
	@Test
	@Ignore
	public void testMatrixMultiplication() {
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
	public void testMatrixMultiplicationOrgLexer() throws Exception {
		String input = "# return 0\n"
				+ "# prints:\n"
				+ "# 14 46\n"
				+ "# 28 92\n"
				+ "# 42 138\n"
				+ "# 56 18\n"
				+ "\n"
				+ "# constants\n"
				+ "long ax;\n"
				+ "long ay;\n"
				+ "long bx;\n"
				+ "long by;\n"
				+ "\n"
				+ "# temps\n"
				+ "long ix;\n"
				+ "long iy;\n"
				+ "long i;\n"
				+ "\n"
				+ "# matrix\n"
				+ "long[4][3] a;\n"
				+ "long[3][2] b;\n"
				+ "long[4][2] c;\n"
				+ "\n"
				+ "string sep;\n"
				+ "string br;\n"
				+ "\n"
				+ "sep = \"|\";\n"
				+ "br = \"\n\";\n"
				+ "\n"
				+ "ax = 4;\n"
				+ "ay = 3;\n"
				+ "bx = 3;\n"
				+ "by = 2;\n"
				+ "ix = 0;\n"
				+ "\n"
				+ "# init a\n"
				+ "a[0][0] = 1; a[0][1] = 2; a[0][2] = 3;\n"
				+ "a[1][0] = 2; a[1][1] = 4; a[1][2] = 6;\n"
				+ "a[2][0] = 3; a[2][1] = 6; a[2][2] = 9;\n"
				+ "a[3][0] = 4; a[3][1] = 8; a[3][2] = 12;\n"
				+ "\n"
				+ "# init b\n"
				+ "b[0][0] = 1; b[0][1] = 5;\n"
				+ "b[1][0] = 2; b[1][1] = 7;\n"
				+ "b[2][0] = 3; b[2][1] = 9;\n"
				+ "\n"
				+ "# init c\n"
				+ "c[0][0]=0;\n"
				+ "c[1][0]=0;\n"
				+ "c[2][0]=0;\n"
				+ "c[3][0]=0;\n"
				+ "c[0][1]=0;\n"
				+ "c[1][1]=0;\n"
				+ "c[2][1]=0;\n"
				+ "c[3][1]=0;\n"
				+ "\n"
				+ "while (ix < ax ) {\n"
				+ "iy = 0;\n"
				+ "while (iy < by) {\n"
				+ "i = 0;\n"
				+ "while (i < bx && i < ay) {\n"
				+ "c[ix][iy] = a[ix][i] * b[i][iy] + c[ix][iy];\n"
				+ "}\n"
				+ "}\n"
				+ "}\n"
				+ "\n"
				+ "ix = 0;\n"
				+ "while (ix < ax) {\n"
				+ "iy = 0;\n"
				+ "while (iy < by){\n"
				+ "print c[ix][iy];\n"
				+ "if ( (iy+1) != by )\n"
				+ "print sep;\n"
				+ "}\n"
				+ "print br;\n"
				+ "}\n"
				+ "\n"
				+ "return;\n";
		
		// Generate parsing table
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		
		ASTFactory factory = new ASTFactory();factory.addDeclaration("ax", new LongType());
		factory.addDeclaration("ay", new LongType());
		factory.addDeclaration("bx", new LongType());
		factory.addDeclaration("by", new LongType());
		factory.addDeclaration("ix", new LongType());
		factory.addDeclaration("iy", new LongType());
		factory.addDeclaration("i", new LongType());		 
		//Matrix 
		factory.addDeclaration("a", new ArrayType(new ArrayType(new LongType(), 4), 3));
		factory.addDeclaration("b", new ArrayType(new ArrayType(new LongType(), 3), 2));
		factory.addDeclaration("c", new ArrayType(new ArrayType(new LongType(), 4), 2));
		//String		 
		factory.addDeclaration("sep", new StringType(LRParser.STRING_LENGTH));
		factory.addDeclaration("br", new StringType(LRParser.STRING_LENGTH));
		factory.addAssignment(factory.newBasicIdentifier("sep"), factory.newLiteral("|", new StringType(20L)));
		factory.addAssignment(factory.newBasicIdentifier("br"), factory.newLiteral("\n", new StringType(20L)));
		//value
		factory.addAssignment(factory.newBasicIdentifier("ax"), factory.newLiteral("4", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("ay"), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("bx"), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("by"), factory.newLiteral("2", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("ix"), factory.newLiteral("0", new LongType()));
		
		//init a
		 	
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("00"), factory.newBasicIdentifier("a")), factory.newLiteral("1", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("01"), factory.newBasicIdentifier("a")), factory.newLiteral("2", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("02"), factory.newBasicIdentifier("a")), factory.newLiteral("3", new LongType()));
		
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("10"), factory.newBasicIdentifier("a")), factory.newLiteral("2", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("11"), factory.newBasicIdentifier("a")), factory.newLiteral("4", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("12"), factory.newBasicIdentifier("a")), factory.newLiteral("6", new LongType()));
		
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("20"), factory.newBasicIdentifier("a")), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("21"), factory.newBasicIdentifier("a")), factory.newLiteral("6", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("22"), factory.newBasicIdentifier("a")), factory.newLiteral("9", new LongType()));
		
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("30"), factory.newBasicIdentifier("a")), factory.newLiteral("4", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("31"), factory.newBasicIdentifier("a")), factory.newLiteral("8", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("32"), factory.newBasicIdentifier("a")), factory.newLiteral("12", new LongType()));
		//init b
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("00"), factory.newBasicIdentifier("b")), factory.newLiteral("1", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("01"), factory.newBasicIdentifier("b")), factory.newLiteral("5", new LongType()));
		
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("10"), factory.newBasicIdentifier("b")), factory.newLiteral("2", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("11"), factory.newBasicIdentifier("b")), factory.newLiteral("7", new LongType()));
		
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("20"), factory.newBasicIdentifier("b")), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("21"), factory.newBasicIdentifier("b")), factory.newLiteral("9", new LongType()));
		//init c
		
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("00"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("01"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		 
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("10"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("11"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		 
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("20"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("21"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		 
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("30"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		factory.addAssignment(factory.newArrayIdentifier(factory.newBasicIdentifier("31"), factory.newBasicIdentifier("c")), factory.newLiteral("0", new LongType()));
		 
		
		// while loop
		// to be continued
	 
		
		
		
		factory.addReturn(null);
		
		
		 
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
