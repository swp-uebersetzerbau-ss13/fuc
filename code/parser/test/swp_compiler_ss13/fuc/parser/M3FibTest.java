package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.BasicConfigurator;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
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
	public void testFibOrgLexer() throws Exception {
		String input = "# returns 98\n"
				+ "# prints:\n"
				+ "# 6765\n"
				+ "\n"
				+ "long[21] numbers;\n"
				+ "long i;\n"
				+ "i = 0;\n"
				+ "\n"
				+ "{\n"
				+ "long i;\n"
				+ "i = 2;\n"
				+ "numbers[0] = 0;\n"
				+ "numbers[1] = 1;\n"
				+ "\n"
				+ "while ( i < 21 ) {\n"
				+ "numbers[i] = numbers[i - 1] + numbers[i - 2];\n"
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
		
		//Array
		factory.addDeclaration("numbers",  new ArrayType(new LongType(), 21));
		// long i;
		factory.addDeclaration("i", new LongType());
		//Assignment
		factory.addAssignment(factory.newBasicIdentifier("i"), factory.newLiteral("0", new LongType()));
		//Begin Block
		factory.addBlock();
		
		//Assignment
		factory.addDeclaration("i", new LongType());
		factory.addAssignment(
				factory.newBasicIdentifier("i"),
				factory.newLiteral("2", new LongType()));
		//Assignment for  the two arrays
        factory.addAssignment(
        		factory.newArrayIdentifier(
        				factory.newLiteral("0", new LongType()),
        				factory.newBasicIdentifier("numbers")),
        		factory.newLiteral("0", new LongType()));
        factory.addAssignment(
        		factory.newArrayIdentifier(
        				factory.newLiteral("1", new LongType()),
        				factory.newBasicIdentifier("numbers")),
        		factory.newLiteral("1", new LongType()));
		
        // while loop
        factory.addWhile(
        		factory.newBinaryExpression(
        				BinaryOperator.LESSTHAN,
        				factory.newBasicIdentifier("i"),
        				factory.newLiteral("21", new LongType())));
    	// begin While block
  		factory.addBlock();
  		
  		factory.addAssignment(
  				factory.newArrayIdentifier(
  						factory.newBasicIdentifier("i"),
  						factory.newBasicIdentifier("numbers")),
				factory.newBinaryExpression(
						BinaryOperator.ADDITION,
						factory.newArrayIdentifier(
								factory.newBinaryExpression(
										BinaryOperator.SUBSTRACTION,
										factory.newBasicIdentifier("i"),
										factory.newLiteral("1", new LongType())),
										factory.newBasicIdentifier("numbers")),
						factory.newArrayIdentifier(
								factory.newBinaryExpression(
										BinaryOperator.SUBSTRACTION,
										factory.newBasicIdentifier("i"),
										factory.newLiteral("2", new LongType())),
										factory.newBasicIdentifier("numbers"))));
  		factory.addAssignment(
  				factory.newBasicIdentifier("i"),
  				factory.newBinaryExpression(
  						BinaryOperator.ADDITION,
  						factory.newBasicIdentifier("i"),
  								factory.newLiteral("1", new LongType())));
  		
  		// end While block
		factory.goToParent();	// -> WhileNode
		factory.goToParent();	// -> BlockNode around WhileNode
       
 		//print numbers[20];
 		factory.addPrint(
 				factory.newArrayIdentifier(
 						factory.newLiteral("20", new LongType()),
 						factory.newBasicIdentifier("numbers")));
 		
 		//return numbers[15];
 		factory.addReturn(
 				factory.newArrayIdentifier(
 						factory.newLiteral("15", new LongType()),
 						factory.newBasicIdentifier("numbers")));
 		
 		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
