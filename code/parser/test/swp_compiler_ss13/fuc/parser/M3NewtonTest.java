package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.parser.LRParser;

public class M3NewtonTest {
	@Test
	@Ignore
	public void testNewton() {
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
	public void testNewtonOrgLexer() throws Exception {
		String input = "# returns 0\n"
				+ "# outputs:\n"
				+ "# i hate floating point numbers\n"
				+ "# 1.4142<irgendwas>\n"
				+ "\n"
				+ "double radicand;\n"
				+ "double guess;\n"
				+ "string res;\n"
				+ "\n"
				+ "radicand = 2;\n"
				+ "guess = 1;\n"
				+ "error = radicand;\n"
				+ "res = \"i hate floating point numbers\";\n"
				+ "\n"
				+ "while (error >= 0.0001) {\n"
				+ "guess = ((radicand/guess) + guess) / 2.0;\n"
				+ "error = guess * guess - radicand;\n"
				+ "if (error < 0) {\n"
				+ "error = error * -1;\n"
				+ "}\n"
				+ "}\n"
				+ "\n"
				+ "print res;\n"
				+ "print guess;\n"
				+ "return;\n";
		
		// Generate parsing table
		AST ast = GrammarTestHelper.parseToAst(input);
		checkAst(ast);
	}

	private static void checkAst(AST ast) {
		assertNotNull(ast);
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("radicand", new DoubleType());
 		factory.addDeclaration("guess", new DoubleType());
 		factory.addDeclaration("res", new StringType(LRParser.STRING_LENGTH));
 		//Assign values
 		factory.addAssignment(factory.newBasicIdentifier("radicand"),
 				factory.newLiteral("2", new LongType()));
 		factory.addAssignment(factory.newBasicIdentifier("guess"),
 				factory.newLiteral("1", new LongType()));
 		factory.addAssignment(factory.newBasicIdentifier("error"),
 				factory.newBasicIdentifier("radicand"));
 		factory.addAssignment(factory.newBasicIdentifier("res"),
 				factory.newLiteral("\"i hate floating point numbers\"", new StringType(4L)));
 		 
 		//while loop
 		factory.addWhile(
        		factory.newBinaryExpression(
        				BinaryOperator.GREATERTHANEQUAL,
        				factory.newBasicIdentifier("error"),
        				factory.newLiteral("0.0001", new DoubleType())));
 				factory.addBlock();
        		factory.addAssignment(        				 
          				factory.newBasicIdentifier("guess"),
          				factory.newBinaryExpression(
          						BinaryOperator.DIVISION,
          						factory.newBinaryExpression(
									BinaryOperator.ADDITION,
									factory.newBinaryExpression(
											BinaryOperator.DIVISION,
											factory.newBasicIdentifier("radicand"),
											factory.newBasicIdentifier("guess")),
									factory.newBasicIdentifier("guess")),
								factory.newLiteral("2.0", new DoubleType())));
 				// another assignment
				factory.addAssignment(  
						factory.newBasicIdentifier("error"),
		          				factory.newBinaryExpression(
										BinaryOperator.SUBSTRACTION,
										factory.newBinaryExpression(
												BinaryOperator.MULTIPLICATION,
												factory.newBasicIdentifier("guess"),
												factory.newBasicIdentifier("guess")),
										factory.newBasicIdentifier("radicand")));
        					 
        		// if statement
				factory.addBranch(
						factory.newBinaryExpression(
		        				BinaryOperator.LESSTHAN,
		        				factory.newBasicIdentifier("error"),
		        				factory.newLiteral("0", new LongType())));
        				factory.addBlock();
        				factory.addAssignment(
        						factory.newBasicIdentifier("error"), 
        						factory.newBinaryExpression(
        		        				BinaryOperator.MULTIPLICATION,
        		        				factory.newBasicIdentifier("error"),
        		        				factory.newUnaryExpression(
        		        						UnaryOperator.MINUS,
        		        						factory.newLiteral("1", new LongType()))));
        	 				 
		factory.goToParent();	// -> if
		factory.goToParent();	// -> while block
		factory.goToParent();	// -> while
		factory.goToParent();	// -> root node
 		
 		//print
 		factory.addPrint(factory.newBasicIdentifier("res"));
 		factory.addPrint(factory.newBasicIdentifier("guess"));
 		factory.addReturn(null);
		
 		
 		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
