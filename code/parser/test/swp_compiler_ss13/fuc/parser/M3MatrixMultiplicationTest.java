package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.apache.log4j.BasicConfigurator;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.errorHandling.ParserASTXMLVisualization;
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
				+ "br = \"\\n\";\n"
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

	private static void checkAst(AST actual) {
		assertNotNull(actual);
		
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
		factory.addAssignment(factory.newBasicIdentifier("sep"), factory.newLiteral("\"|\"", new StringType(20L)));
		factory.addAssignment(factory.newBasicIdentifier("br"), factory.newLiteral("\"\\n\"", new StringType(20L)));
		//value
		factory.addAssignment(factory.newBasicIdentifier("ax"), factory.newLiteral("4", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("ay"), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("bx"), factory.newLiteral("3", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("by"), factory.newLiteral("2", new LongType()));
		factory.addAssignment(factory.newBasicIdentifier("ix"), factory.newLiteral("0", new LongType()));
		
		//init a
		addMatrixInit(factory, "0", "0", "a", "1");
		addMatrixInit(factory, "0", "1", "a", "2");
		addMatrixInit(factory, "0", "2", "a", "3");

		addMatrixInit(factory, "1", "0", "a", "2");
		addMatrixInit(factory, "1", "1", "a", "4");
		addMatrixInit(factory, "1", "2", "a", "6");
		
		addMatrixInit(factory, "2", "0", "a", "3");
		addMatrixInit(factory, "2", "1", "a", "6");
		addMatrixInit(factory, "2", "2", "a", "9");
		
		addMatrixInit(factory, "3", "0", "a", "4");
		addMatrixInit(factory, "3", "1", "a", "8");
		addMatrixInit(factory, "3", "2", "a", "12");
		
		// Init b
		addMatrixInit(factory, "0", "0", "b", "1");		
		addMatrixInit(factory, "0", "1", "b", "5");
		
		addMatrixInit(factory, "1", "0", "b", "2");
		addMatrixInit(factory, "1", "1", "b", "7");
		
		addMatrixInit(factory, "2", "0", "b", "3");
		addMatrixInit(factory, "2", "1", "b", "9");
		
		// Init c
		addMatrixInit(factory, "0", "0", "c", "0");
		addMatrixInit(factory, "1", "0", "c", "0");
		addMatrixInit(factory, "2", "0", "c", "0");
		addMatrixInit(factory, "3", "0", "c", "0");
		
		addMatrixInit(factory, "0", "1", "c", "0");
		addMatrixInit(factory, "1", "1", "c", "0");
		addMatrixInit(factory, "2", "1", "c", "0");
		addMatrixInit(factory, "3", "1", "c", "0");
		
		// 1st while loop
		factory.addWhile(
        		factory.newBinaryExpression(
        				BinaryOperator.LESSTHAN,
        				factory.newBasicIdentifier("ix"),
        				factory.newBasicIdentifier("ax")));
			
			factory.addBlock();
			factory.addAssignment(factory.newBasicIdentifier("iy"), factory.newLiteral("0", new LongType()));
			
			// 2nd while loop
			factory.addWhile(
	        		factory.newBinaryExpression(
	        				BinaryOperator.LESSTHAN,
	        				factory.newBasicIdentifier("iy"),
	        				factory.newBasicIdentifier("by")));
				factory.addBlock();
				factory.addAssignment(factory.newBasicIdentifier("i"), factory.newLiteral("0", new LongType()));
				
				// 3rd while loop with three  conditions
				factory.addWhile(
						factory.newBinaryExpression( 	
        						BinaryOperator.LOGICAL_AND,
        						factory.newBinaryExpression( 	
		        						BinaryOperator.LESSTHAN,
				        				factory.newBasicIdentifier("i"),
				        				factory.newBasicIdentifier("bx")),
				        		factory.newBinaryExpression( 	
		        						BinaryOperator.LESSTHAN,
		        						factory.newBasicIdentifier("i"),
				        				factory.newBasicIdentifier("ay"))));
					factory.addBlock();
					//c[ix][iy] = a[ix][i] * b[i][iy] + c[ix][iy];
					factory.addAssignment(
							//c[ix][iy]
							factory.newArrayIdentifier(
									factory.newBasicIdentifier("iy"),
									factory.newArrayIdentifier(
											factory.newBasicIdentifier("ix"),
											factory.newBasicIdentifier("c"))),
									//a[ix][i]
									factory.newBinaryExpression(
			        						BinaryOperator.ADDITION,
									//b[i][iy]
									factory.newBinaryExpression(
					        				BinaryOperator.MULTIPLICATION,
											
										factory.newArrayIdentifier(
												factory.newBasicIdentifier("i"),
												factory.newArrayIdentifier(
														factory.newBasicIdentifier("ix"),
														factory.newBasicIdentifier("a"))),
										factory.newArrayIdentifier(
												factory.newBasicIdentifier("iy"),
												factory.newArrayIdentifier(
														factory.newBasicIdentifier("i"),
														factory.newBasicIdentifier("b")))
											),
									//c[ix][iy]		
									factory.newArrayIdentifier(
											factory.newBasicIdentifier("iy"),
											factory.newArrayIdentifier(
													factory.newBasicIdentifier("ix"),
													factory.newBasicIdentifier("c")))));
					
					factory.goToParent();	// -> 3rd loop
					factory.goToParent();	// -> 2nd loops body
				factory.goToParent();	// -> 2nd loop
				factory.goToParent();	// -> 1st loops body
			factory.goToParent();	// -> 1st loop
			factory.goToParent();	// -> root-body
					
		factory.addAssignment(factory.newBasicIdentifier("ix"), factory.newLiteral("0", new LongType()));
		// 4th while loop
		factory.addWhile(
        		factory.newBinaryExpression(
        				BinaryOperator.LESSTHAN,
        				factory.newBasicIdentifier("ix"),
        				factory.newBasicIdentifier("ax")));
			
			factory.addBlock();
			factory.addAssignment(factory.newBasicIdentifier("iy"), factory.newLiteral("0", new LongType()));
			
			// 5th while loop
			factory.addWhile(
	        		factory.newBinaryExpression(
	        				BinaryOperator.LESSTHAN,
	        				factory.newBasicIdentifier("iy"),
	        				factory.newBasicIdentifier("by")));
			
				factory.addBlock();
				factory.addPrint(
					factory.newArrayIdentifier(
						factory.newBasicIdentifier("iy"),
						factory.newArrayIdentifier(
								factory.newBasicIdentifier("ix"),
								factory.newBasicIdentifier("c"))
								));
				
				// if statement
				factory.addBranch(
						factory.newBinaryExpression(
		        				BinaryOperator.INEQUAL,
						factory.newBinaryExpression(
		        				BinaryOperator.ADDITION,
        		        				factory.newBasicIdentifier("iy"),
        		        				factory.newLiteral("1", new LongType())),
		        				factory.newBasicIdentifier("by")));
					factory.addPrint( 
							factory.newBasicIdentifier("sep"));
        						
        			factory.goToParent();	// -> 5th loop body
    			factory.goToParent();	// -> 5th loop
				factory.goToParent();	// -> 4th loop body
				
				factory.addPrint(factory.newBasicIdentifier("br"));
				
			factory.goToParent();	// -> 4th loop
			factory.goToParent();	// -> root body
		
		factory.addReturn(null);
		
		ParserASTXMLVisualization vis = new ParserASTXMLVisualization();
 		System.out.println(vis.visualizeAST(actual));
 		
 		AST expected = factory.getAST();
 		System.out.println(vis.visualizeAST(expected));
		ASTComparator.compareAST(expected, actual);
	}
	
	private static void addMatrixInit(ASTFactory factory, String index0, String index1, String basicId, String val) {
		factory.addAssignment(
				factory.newArrayIdentifier(
						factory.newLiteral(index1, new LongType()),
						factory.newArrayIdentifier(factory.newLiteral(index0, new LongType()),
								factory.newBasicIdentifier(basicId))),
				factory.newLiteral(val, new LongType()));
	}
}
