package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;

public class M3Tests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public M3Tests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyser = new SemanticAnalyser();
		analyser.setReportLog(log);
	}

	@After
	public void tearDown() {
		analyser = null;
		log = null;
	}

	/**
	 * # no errors expected
	 * 
	 * @see <a
	 *      href="https://github.com/swp-uebersetzerbau-ss13/common/blob/master/examples/m3/fib.prog">fib.prog</a>
	 **/
	@Test
	public void testFibProg() {
		ASTFactory astFactory = new ASTFactory();

		// long[21] numbers;
		astFactory.addDeclaration("numbers", new ArrayType(new LongType(), 21));
		// long i;
		astFactory.addDeclaration("i", new LongType());
		// i = 0;
		astFactory.addAssignment(astFactory.newBasicIdentifier("i"),
				astFactory.newLiteral("0", new LongType()));
		// begin block
		astFactory.addBlock();
		// long i;
		astFactory.addDeclaration("i", new LongType());
		// i = 2;
		astFactory.addAssignment(astFactory.newBasicIdentifier("i"),
				astFactory.newLiteral("2", new LongType()));
		// numbers[0] = 0;
		astFactory.addAssignment(
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("0", new LongType()),
						astFactory.newBasicIdentifier("numbers")),
				astFactory.newLiteral("0", new LongType()));
		// numbers[1] = 1;
		astFactory.addAssignment(
				astFactory.newArrayIdentifier(
						astFactory.newLiteral("1", new LongType()),
						astFactory.newBasicIdentifier("numbers")),
				astFactory.newLiteral("1", new LongType()));
		// while (i < 21)
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, astFactory.newBasicIdentifier("i"),
				astFactory.newLiteral("21", new LongType())));
		// begin while block
		astFactory.addBlock();
		// numbers[i] = numbers[i-1] + numbers[i-2];
		astFactory.addAssignment(astFactory.newArrayIdentifier(
				astFactory.newBasicIdentifier("i"),
				astFactory.newBasicIdentifier("numbers")), astFactory
				.newBinaryExpression(BinaryOperator.ADDITION, astFactory
						.newArrayIdentifier(astFactory.newBinaryExpression(
								BinaryOperator.SUBSTRACTION,
								astFactory.newBasicIdentifier("i"),
								astFactory.newLiteral("1", new LongType())),
								astFactory.newBasicIdentifier("numbers")),
						astFactory.newArrayIdentifier(astFactory
								.newBinaryExpression(
										BinaryOperator.SUBSTRACTION, astFactory
												.newBasicIdentifier("i"),
										astFactory.newLiteral("2",
												new LongType())), astFactory
								.newBasicIdentifier("numbers"))));
		// i = i + 1;
		astFactory.addAssignment(astFactory.newBasicIdentifier("i"), astFactory
				.newBinaryExpression(BinaryOperator.ADDITION,
						astFactory.newBasicIdentifier("i"),
						astFactory.newLiteral("1", new LongType())));
		// end while block
		astFactory.goToParent(); // -> WhileNode
		astFactory.goToParent(); // -> BlockNode around WhileNode
		// print numbers[20];
		astFactory.addPrint(astFactory.newArrayIdentifier(
				astFactory.newLiteral("20", new LongType()),
				astFactory.newBasicIdentifier("numbers")));
		// return numbers[15];
		astFactory.addReturn(astFactory.newArrayIdentifier(
				astFactory.newLiteral("15", new LongType()),
				astFactory.newBasicIdentifier("numbers")));

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	/**
	 * # no errors expected
	 * 
	 * @see <a
	 *      href="https://github.com/swp-uebersetzerbau-ss13/common/blob/master/examples/m3/matrixMultiplication.prog">matrixMultiplication.prog</a>
	 **/
	@Test
	public void testMatrixMultiplicationProg() {
		ASTFactory astFactory = new ASTFactory();
		// # constants
		astFactory.addDeclaration("ax", new LongType());
		astFactory.addDeclaration("ay", new LongType());
		astFactory.addDeclaration("bx", new LongType());
		astFactory.addDeclaration("by", new LongType());
		// # temps
		astFactory.addDeclaration("ix", new LongType());
		astFactory.addDeclaration("iy", new LongType());
		astFactory.addDeclaration("i", new LongType());
		// # matrix
		astFactory.addDeclaration("a", new ArrayType(new ArrayType(
				new LongType(), 4), 3));
		astFactory.addDeclaration("b", new ArrayType(new ArrayType(
				new LongType(), 3), 2));
		astFactory.addDeclaration("c", new ArrayType(new ArrayType(
				new LongType(), 4), 2));
		// string sep;
		astFactory.addDeclaration("sep", new StringType(20L));
		// string br;
		astFactory.addDeclaration("br", new StringType(20L));
		// sep = "|";
		astFactory.addAssignment(astFactory.newBasicIdentifier("sep"),
				astFactory.newLiteral("|", new StringType(20L)));
		// br = "\n";
		astFactory.addAssignment(astFactory.newBasicIdentifier("br"),
				astFactory.newLiteral("\\n", new StringType(20L)));
		// init ax - ix
		astFactory.addAssignment(astFactory.newBasicIdentifier("ax"),
				astFactory.newLiteral("4", new LongType()));
		astFactory.addAssignment(astFactory.newBasicIdentifier("ay"),
				astFactory.newLiteral("3", new LongType()));
		astFactory.addAssignment(astFactory.newBasicIdentifier("bx"),
				astFactory.newLiteral("3", new LongType()));
		astFactory.addAssignment(astFactory.newBasicIdentifier("by"),
				astFactory.newLiteral("2", new LongType()));
		astFactory.addAssignment(astFactory.newBasicIdentifier("ix"),
				astFactory.newLiteral("0", new LongType()));

		// # init a
		addMatrixInit(astFactory, "0", "0", "a", "1");
		addMatrixInit(astFactory, "0", "1", "a", "2");
		addMatrixInit(astFactory, "0", "2", "a", "3");

		addMatrixInit(astFactory, "1", "0", "a", "2");
		addMatrixInit(astFactory, "1", "1", "a", "4");
		addMatrixInit(astFactory, "1", "2", "a", "6");

		addMatrixInit(astFactory, "2", "0", "a", "3");
		addMatrixInit(astFactory, "2", "1", "a", "6");
		addMatrixInit(astFactory, "2", "2", "a", "9");

		addMatrixInit(astFactory, "3", "0", "a", "4");
		addMatrixInit(astFactory, "3", "1", "a", "8");
		addMatrixInit(astFactory, "3", "2", "a", "12");

		// # init b
		addMatrixInit(astFactory, "0", "0", "b", "1");
		addMatrixInit(astFactory, "0", "1", "b", "5");

		addMatrixInit(astFactory, "1", "0", "b", "2");
		addMatrixInit(astFactory, "1", "1", "b", "7");

		addMatrixInit(astFactory, "2", "0", "b", "3");
		addMatrixInit(astFactory, "2", "1", "b", "9");

		// # init c
		addMatrixInit(astFactory, "0", "0", "c", "0");
		addMatrixInit(astFactory, "1", "0", "c", "0");
		addMatrixInit(astFactory, "2", "0", "c", "0");
		addMatrixInit(astFactory, "3", "0", "c", "0");

		addMatrixInit(astFactory, "0", "1", "c", "0");
		addMatrixInit(astFactory, "1", "1", "c", "0");
		addMatrixInit(astFactory, "2", "1", "c", "0");
		addMatrixInit(astFactory, "3", "1", "c", "0");

		// 1. while loop
		// while (ix < ax )
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, astFactory.newBasicIdentifier("ix"),
				astFactory.newBasicIdentifier("ax")));
		astFactory.addBlock();
		// iy = 0;
		astFactory.addAssignment(astFactory.newBasicIdentifier("iy"),
				astFactory.newLiteral("0", new LongType()));

		// 2. while loop
		// while (iy < by)
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, astFactory.newBasicIdentifier("iy"),
				astFactory.newBasicIdentifier("by")));
		astFactory.addBlock();
		//
		astFactory.addAssignment(astFactory.newBasicIdentifier("i"),
				astFactory.newLiteral("0", new LongType()));

		// 3. while loop
		// while (i < bx && i < ay)
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.LOGICAL_AND, astFactory.newBinaryExpression(
						BinaryOperator.LESSTHAN,
						astFactory.newBasicIdentifier("i"),
						astFactory.newBasicIdentifier("bx")), astFactory
						.newBinaryExpression(BinaryOperator.LESSTHAN,
								astFactory.newBasicIdentifier("i"),
								astFactory.newBasicIdentifier("ay"))));
		astFactory.addBlock();
		// c[ix][iy] = a[ix][i] * b[i][iy] + c[ix][iy];
		astFactory
				.addAssignment(
				// c[ix][iy]
						astFactory.newArrayIdentifier(astFactory
								.newBasicIdentifier("iy"), astFactory
								.newArrayIdentifier(
										astFactory.newBasicIdentifier("ix"),
										astFactory.newBasicIdentifier("c"))),
						// a[ix][i]
						astFactory
								.newBinaryExpression(
										BinaryOperator.ADDITION,
										// b[i][iy]
										astFactory.newBinaryExpression(
												BinaryOperator.MULTIPLICATION,

												astFactory.newArrayIdentifier(
														astFactory
																.newBasicIdentifier("i"),
														astFactory
																.newArrayIdentifier(
																		astFactory
																				.newBasicIdentifier("ix"),
																		astFactory
																				.newBasicIdentifier("a"))),
												astFactory.newArrayIdentifier(
														astFactory
																.newBasicIdentifier("iy"),
														astFactory
																.newArrayIdentifier(
																		astFactory
																				.newBasicIdentifier("i"),
																		astFactory
																				.newBasicIdentifier("b")))),
										// c[ix][iy]
										astFactory.newArrayIdentifier(
												astFactory
														.newBasicIdentifier("iy"),
												astFactory.newArrayIdentifier(
														astFactory
																.newBasicIdentifier("ix"),
														astFactory
																.newBasicIdentifier("c")))));

		astFactory.goToParent(); // -> 3. loop
		astFactory.goToParent(); // -> 2. loops body
		astFactory.goToParent(); // -> 2. loop
		astFactory.goToParent(); // -> 1. loops body
		astFactory.goToParent(); // -> 1. loop
		astFactory.goToParent(); // -> root-body

		// ix = 0;
		astFactory.addAssignment(astFactory.newBasicIdentifier("ix"),
				astFactory.newLiteral("0", new LongType()));
		// 4. while loop
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, astFactory.newBasicIdentifier("ix"),
				astFactory.newBasicIdentifier("ax")));
		astFactory.addBlock();
		// iy = 0;
		astFactory.addAssignment(astFactory.newBasicIdentifier("iy"),
				astFactory.newLiteral("0", new LongType()));

		// 5. while loop
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN, astFactory.newBasicIdentifier("iy"),
				astFactory.newBasicIdentifier("by")));
		astFactory.addBlock();
		// print c[ix][iy];
		astFactory.addPrint(astFactory.newArrayIdentifier(
				astFactory.newBasicIdentifier("iy"),
				astFactory.newArrayIdentifier(
						astFactory.newBasicIdentifier("ix"),
						astFactory.newBasicIdentifier("c"))));
		// if ( (iy+1) != by )
		astFactory.addBranch(astFactory.newBinaryExpression(
				BinaryOperator.INEQUAL, astFactory.newBinaryExpression(
						BinaryOperator.ADDITION,
						astFactory.newBasicIdentifier("iy"),
						astFactory.newLiteral("1", new LongType())), astFactory
						.newBasicIdentifier("by")));
		// print sep;
		astFactory.addPrint(astFactory.newBasicIdentifier("sep"));

		astFactory.goToParent(); // -> 5. loop body
		astFactory.goToParent(); // -> 5. loop
		astFactory.goToParent(); // -> 4. loop body

		astFactory.addPrint(astFactory.newBasicIdentifier("br"));

		astFactory.goToParent(); // -> 4. loop
		astFactory.goToParent(); // -> root body

		astFactory.addReturn(null);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	private void addMatrixInit(ASTFactory astFactory, String index0,
			String index1, String basicId, String val) {
		astFactory.addAssignment(astFactory.newArrayIdentifier(
				astFactory.newLiteral(index1, new LongType()),
				astFactory.newArrayIdentifier(
						astFactory.newLiteral(index0, new LongType()),
						astFactory.newBasicIdentifier(basicId))), astFactory
				.newLiteral(val, new LongType()));
	}

	/**
	 * # no errors expected
	 * 
	 * @see <a
	 *      href="https://github.com/swp-uebersetzerbau-ss13/common/blob/master/examples/m3/newton.prog">newton.prog</a>
	 **/
	@Test
	public void testNewtonProg() {
		ASTFactory astFactory = new ASTFactory();

		// declarations
		astFactory.addDeclaration("radicand", new DoubleType());
		astFactory.addDeclaration("guess", new DoubleType());
		astFactory.addDeclaration("error", new DoubleType());
		astFactory.addDeclaration("res", new StringType(20L));
		// radicand = 2;
		astFactory.addAssignment(astFactory.newBasicIdentifier("radicand"),
				astFactory.newLiteral("2", new LongType()));
		// guess = 1;
		astFactory.addAssignment(astFactory.newBasicIdentifier("guess"),
				astFactory.newLiteral("1", new LongType()));
		// error = radicand;
		astFactory.addAssignment(astFactory.newBasicIdentifier("error"),
				astFactory.newBasicIdentifier("radicand"));
		// res = "i hate floating point numbers";
		astFactory.addAssignment(astFactory.newBasicIdentifier("res"),
				astFactory.newLiteral("i hate floating point numbers",
						new StringType(4L)));

		// while (error >= 0.0001)
		astFactory.addWhile(astFactory.newBinaryExpression(
				BinaryOperator.GREATERTHANEQUAL,
				astFactory.newBasicIdentifier("error"),
				astFactory.newLiteral(" 0.0001", new DoubleType())));
		astFactory.addBlock();
		// (radicand/guess)
		BinaryExpressionNode radGuessDiv = astFactory.newBinaryExpression(
				BinaryOperator.DIVISION,
				astFactory.newBasicIdentifier("radicand"),
				astFactory.newBasicIdentifier("guess"));
		// + guess
		BinaryExpressionNode guessAdd = astFactory.newBinaryExpression(
				BinaryOperator.ADDITION, radGuessDiv,
				astFactory.newBasicIdentifier("guess"));
		// guess = ... / 2.0;
		astFactory.addAssignment(astFactory.newBasicIdentifier("guess"),
				astFactory.newBinaryExpression(BinaryOperator.DIVISION,
						guessAdd,
						astFactory.newLiteral("2.0", new DoubleType())));
		// error = guess * guess - radicand;
		astFactory.addAssignment(astFactory.newBasicIdentifier("error"),
				astFactory.newBinaryExpression(BinaryOperator.SUBSTRACTION,
						astFactory.newBinaryExpression(
								BinaryOperator.MULTIPLICATION,
								astFactory.newBasicIdentifier("guess"),
								astFactory.newBasicIdentifier("guess")),
						astFactory.newBasicIdentifier("radicand")));
		// if (error < 0)
		astFactory.addBranch(astFactory.newBinaryExpression(
				BinaryOperator.LESSTHAN,
				astFactory.newBasicIdentifier("error"),
				astFactory.newLiteral("0", new LongType())));
		astFactory.addBlock();
		// error = error * -1;
		astFactory.addAssignment(astFactory.newBasicIdentifier("error"),
				astFactory.newBinaryExpression(BinaryOperator.MULTIPLICATION,
						astFactory.newBasicIdentifier("error"),
						astFactory.newLiteral("-1", new LongType())

				));
		astFactory.goToParent(); // -> branch head
		astFactory.goToParent(); // -> while body
		astFactory.goToParent(); // -> while head
		astFactory.goToParent(); // -> main block

		// print res;
		astFactory.addPrint(astFactory.newBasicIdentifier("res"));
		// print guess;
		astFactory.addPrint(astFactory.newBasicIdentifier("guess"));
		// return;
		astFactory.addReturn(null);

		AST ast = astFactory.getAST();
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}
}
