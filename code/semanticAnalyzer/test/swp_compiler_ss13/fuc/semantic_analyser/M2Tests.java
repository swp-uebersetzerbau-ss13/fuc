package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.BranchNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.PrintNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

// TODO refine ASTs
public class M2Tests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public M2Tests() {
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
	 * # returns 10<br/>
	 * # prints nothing<br/>
	 * long a;<br/>
	 * long b;<br/>
	 * long c;<br/>
	 * <br/>
	 * a = 4;<br/>
	 * b = 3;<br/>
	 * c = 2;<br/>
	 * <br/>
	 * a = b = 4;<br/>
	 * c = a + b + c;<br/>
	 * <br/>
	 * return c;
	 */
	@Test
	public void testAssignmentProg() {
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new LongType());
		symbolTable.insert("b", new LongType());
		symbolTable.insert("c", new LongType());

		// long a; long b; long c;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new LongType());
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new LongType());
		DeclarationNode declaration_c = new DeclarationNodeImpl();
		declaration_c.setIdentifier("c");
		declaration_c.setType(new LongType());

		// a = 4;
		BasicIdentifierNode identifier_a1 = new BasicIdentifierNodeImpl();
		identifier_a1.setIdentifier("a");
		LiteralNode literal_4 = new LiteralNodeImpl();
		literal_4.setLiteral("4");
		literal_4.setLiteralType(new LongType());

		AssignmentNode assignment_a1 = new AssignmentNodeImpl();
		assignment_a1.setLeftValue(identifier_a1);
		assignment_a1.setRightValue(literal_4);
		identifier_a1.setParentNode(assignment_a1);
		literal_4.setParentNode(assignment_a1);

		// b = 3;
		BasicIdentifierNode identifier_b1 = new BasicIdentifierNodeImpl();
		identifier_b1.setIdentifier("b");
		LiteralNode literal_3 = new LiteralNodeImpl();
		literal_3.setLiteral("3");
		literal_3.setLiteralType(new LongType());

		AssignmentNode assignment_b1 = new AssignmentNodeImpl();
		assignment_b1.setLeftValue(identifier_b1);
		assignment_b1.setRightValue(literal_3);
		identifier_b1.setParentNode(assignment_b1);
		literal_3.setParentNode(assignment_b1);

		// c = 2;
		BasicIdentifierNode identifier_c1 = new BasicIdentifierNodeImpl();
		identifier_c1.setIdentifier("c");
		LiteralNode literal_2 = new LiteralNodeImpl();
		literal_2.setLiteral("2");
		literal_2.setLiteralType(new LongType());

		AssignmentNode assignment_c1 = new AssignmentNodeImpl();
		assignment_c1.setLeftValue(identifier_c1);
		assignment_c1.setRightValue(literal_2);
		identifier_c1.setParentNode(assignment_c1);
		literal_2.setParentNode(assignment_c1);

		// a = b = 4;
		BasicIdentifierNode identifier_a2 = new BasicIdentifierNodeImpl();
		identifier_a2.setIdentifier("a");
		BasicIdentifierNode identifier_b2 = new BasicIdentifierNodeImpl();
		identifier_b2.setIdentifier("b");
		LiteralNode literal_4_2 = new LiteralNodeImpl();
		literal_4_2.setLiteral("4");
		literal_4_2.setLiteralType(new LongType());

		AssignmentNode assignment_b2 = new AssignmentNodeImpl();
		assignment_b2.setLeftValue(identifier_b2);
		assignment_b2.setRightValue(literal_4_2);
		identifier_b2.setParentNode(assignment_b2);
		literal_4_2.setParentNode(assignment_b2);

		AssignmentNode assignment_a2 = new AssignmentNodeImpl();
		assignment_a2.setLeftValue(identifier_a2);
		assignment_a2.setRightValue(assignment_b2);
		identifier_a2.setParentNode(assignment_a2);
		assignment_b2.setParentNode(assignment_a2);

		// c = a + b + c;
		BasicIdentifierNode identifier_c2 = new BasicIdentifierNodeImpl();
		identifier_c2.setIdentifier("c");
		BasicIdentifierNode identifier_c3 = new BasicIdentifierNodeImpl();
		identifier_c3.setIdentifier("c");
		BasicIdentifierNode identifier_a3 = new BasicIdentifierNodeImpl();
		identifier_a3.setIdentifier("a");
		BasicIdentifierNode identifier_b3 = new BasicIdentifierNodeImpl();
		identifier_b3.setIdentifier("b");

		ArithmeticBinaryExpressionNode add2 = new ArithmeticBinaryExpressionNodeImpl();
		add2.setOperator(BinaryOperator.ADDITION);
		add2.setLeftValue(identifier_a3);
		add2.setRightValue(identifier_b3);
		identifier_a3.setParentNode(add2);
		identifier_b3.setParentNode(add2);
		ArithmeticBinaryExpressionNode add1 = new ArithmeticBinaryExpressionNodeImpl();
		add1.setOperator(BinaryOperator.ADDITION);
		add1.setLeftValue(add2);
		add1.setRightValue(identifier_c3);
		add2.setParentNode(add1);
		identifier_c3.setParentNode(add1);

		AssignmentNode assignment_c2 = new AssignmentNodeImpl();
		assignment_c2.setLeftValue(identifier_c2);
		assignment_c2.setRightValue(add1);
		identifier_c2.setParentNode(assignment_c2);
		add1.setParentNode(assignment_c2);

		// return c;
		BasicIdentifierNode identifier_c4 = new BasicIdentifierNodeImpl();
		identifier_c4.setIdentifier("c");

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier_c4);
		identifier_c4.setParentNode(returnNode);

		// main block
		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_b);
		blockNode.addDeclaration(declaration_c);
		blockNode.addStatement(assignment_a1);
		blockNode.addStatement(assignment_b1);
		blockNode.addStatement(assignment_c1);
		blockNode.addStatement(assignment_a2);
		blockNode.addStatement(assignment_c2);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		declaration_c.setParentNode(blockNode);
		assignment_a1.setParentNode(blockNode);
		assignment_b1.setParentNode(blockNode);
		assignment_c1.setParentNode(blockNode);
		assignment_a2.setParentNode(blockNode);
		assignment_c2.setParentNode(blockNode);
		returnNode.setParentNode(blockNode);
		
		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	/**
	 * # return 5<br/>
	 * # prints nothing<br/>
	 * <br/>
	 * bool b;<br/>
	 * bool c;<br/>
	 * long l;<br/>
	 * <br/>
	 * string bla;<br/>
	 * bla = "bla";<br/>
	 * <br/>
	 * b = true;<br/>
	 * c = false;<br/>
	 * <br/>
	 * l = 4;<br/>
	 * <br/>
	 * # dangling-else should be resolved as given by indentation<br/>
	 * <br/>
	 * if ( b )<br/>
	 * if ( c || ! b )<br/>
	 * print bla;<br/>
	 * else<br/>
	 * l = 5;<br/>
	 * <br/>
	 * return l;
	 */
	@Test
	public void testCondProg() {
		// declarations
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());
		DeclarationNode declaration_c = new DeclarationNodeImpl();
		declaration_c.setIdentifier("c");
		declaration_c.setType(new BooleanType());
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());
		DeclarationNode declaration_bla = new DeclarationNodeImpl();
		declaration_bla.setIdentifier("bla");
		declaration_bla.setType(new StringType(new Long(16)));

		// initializations
		BasicIdentifierNode identifier_b1 = new BasicIdentifierNodeImpl();
		identifier_b1.setIdentifier("b");
		BasicIdentifierNode identifier_c1 = new BasicIdentifierNodeImpl();
		identifier_c1.setIdentifier("c");
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");
		BasicIdentifierNode identifier_bla1 = new BasicIdentifierNodeImpl();
		identifier_bla1.setIdentifier("bla");

		LiteralNode literal_4 = new LiteralNodeImpl();
		literal_4.setLiteral("4");
		literal_4.setLiteralType(new LongType());

		LiteralNode literal_bla = new LiteralNodeImpl();
		literal_bla.setLiteral("bla");
		literal_bla.setLiteralType(new StringType(new Long(4)));

		LiteralNode literal_true = new LiteralNodeImpl();
		literal_true.setLiteral("true");
		literal_true.setLiteralType(new BooleanType());
		
		LiteralNode literal_false = new LiteralNodeImpl();
		literal_false.setLiteral("false");
		literal_false.setLiteralType(new BooleanType());

		AssignmentNode assignment_b = new AssignmentNodeImpl();
		assignment_b.setLeftValue(identifier_b1);
		assignment_b.setRightValue(literal_true);
		identifier_b1.setParentNode(assignment_b);
		literal_true.setParentNode(assignment_b);
		AssignmentNode assignment_c = new AssignmentNodeImpl();
		assignment_c.setLeftValue(identifier_c1);
		assignment_c.setRightValue(literal_false);
		identifier_c1.setParentNode(assignment_c);
		literal_false.setParentNode(assignment_c);
		AssignmentNode assignment_l1 = new AssignmentNodeImpl();
		assignment_l1.setLeftValue(identifier_l1);
		assignment_l1.setRightValue(literal_4);
		identifier_l1.setParentNode(assignment_l1);
		literal_4.setParentNode(assignment_l1);
		AssignmentNode assignment_bla = new AssignmentNodeImpl();
		assignment_bla.setLeftValue(identifier_bla1);
		assignment_bla.setRightValue(literal_bla);
		identifier_bla1.setParentNode(assignment_bla);
		literal_bla.setParentNode(assignment_bla);

		// l = 5;
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		LiteralNode literal_5 = new LiteralNodeImpl();
		literal_5.setLiteral("5");
		literal_5.setLiteralType(new LongType());

		AssignmentNode assignment_l2 = new AssignmentNodeImpl();
		assignment_l2.setLeftValue(identifier_l2);
		assignment_l2.setRightValue(literal_5);
		identifier_l2.setParentNode(assignment_l2);
		literal_5.setParentNode(assignment_l2);

		// c || !b
		BasicIdentifierNode identifier_c2 = new BasicIdentifierNodeImpl();
		identifier_c2.setIdentifier("c");
		BasicIdentifierNode identifier_b3 = new BasicIdentifierNodeImpl();
		identifier_b3.setIdentifier("b");

		LogicUnaryExpressionNode not_b = new LogicUnaryExpressionNodeImpl();
		not_b.setOperator(UnaryOperator.LOGICAL_NEGATE);
		not_b.setRightValue(identifier_b3);
		identifier_b3.setParentNode(not_b);

		LogicBinaryExpressionNode c_or_not_b = new LogicBinaryExpressionNodeImpl();
		c_or_not_b.setOperator(BinaryOperator.LOGICAL_OR);
		c_or_not_b.setLeftValue(identifier_c2);
		c_or_not_b.setRightValue(not_b);
		identifier_c2.setParentNode(c_or_not_b);
		not_b.setParentNode(c_or_not_b);

		// print bla;
		BasicIdentifierNode identifier_bla2 = new BasicIdentifierNodeImpl();
		identifier_bla2.setIdentifier("bla");

		PrintNode print = new PrintNodeImpl();
		print.setRightValue(identifier_bla2);
		identifier_bla2.setParentNode(print);

		// if (...) if (...) ... else ...
		BranchNode innerBranch = new BranchNodeImpl();
		innerBranch.setCondition(c_or_not_b);
		innerBranch.setStatementNodeOnTrue(print);
		innerBranch.setStatementNodeOnFalse(assignment_l2);
		c_or_not_b.setParentNode(innerBranch);
		print.setParentNode(innerBranch);
		assignment_l2.setParentNode(innerBranch);

		BasicIdentifierNode identifier_b2 = new BasicIdentifierNodeImpl();
		identifier_b2.setIdentifier("b");

		BranchNode outerBranch = new BranchNodeImpl();
		outerBranch.setCondition(identifier_b1);
		outerBranch.setStatementNodeOnTrue(innerBranch);
		identifier_b2.setParentNode(outerBranch);
		innerBranch.setParentNode(outerBranch);

		// return l;
		BasicIdentifierNode identifier_l3 = new BasicIdentifierNodeImpl();
		identifier_l3.setIdentifier("l");

		ReturnNode returnNode = new ReturnNodeImpl();
		returnNode.setRightValue(identifier_l3);
		identifier_l3.setParentNode(returnNode);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("b", new BooleanType());
		symbolTable.insert("c", new BooleanType());
		symbolTable.insert("l", new LongType());
		symbolTable.insert("bla", new StringType(new Long(16)));

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_b);
		blockNode.addDeclaration(declaration_c);
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_bla);
		blockNode.addStatement(assignment_b);
		blockNode.addStatement(assignment_c);
		blockNode.addStatement(assignment_l1);
		blockNode.addStatement(assignment_bla);
		blockNode.addStatement(outerBranch);
		blockNode.addStatement(returnNode);
		blockNode.setSymbolTable(symbolTable);
		declaration_b.setParentNode(blockNode);
		declaration_c.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
		declaration_bla.setParentNode(blockNode);
		assignment_b.setParentNode(blockNode);
		assignment_c.setParentNode(blockNode);
		assignment_l1.setParentNode(blockNode);
		assignment_bla.setParentNode(blockNode);
		outerBranch.setParentNode(blockNode);
		returnNode.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyser.analyse(ast);
		
		System.out.println(log);
		assertFalse(log.hasErrors());
	}

	/**
	 * # return 0<br/>
	 * # prints:<br/>
	 * # true<br/>
	 * # 18121313223<br/>
	 * # -2.323e-99<br/>
	 * # jagÄrEttString"<br/>
	 * <br/>
	 * long l;<br/>
	 * double d;<br/>
	 * string s;<br/>
	 * bool b;<br/>
	 * <br/>
	 * b = true;<br/>
	 * l = 18121313223;<br/>
	 * d = -23.23e-100;<br/>
	 * s = "jagÄrEttString\"\n"; # c-like escaping in strings<br/>
	 * <br/>
	 * print b; print "\n";<br/>
	 * print l; print "\n"; # print one digit left of the radix point<br/>
	 * print d; print "\n";<br/>
	 * print s;<br/>
	 * <br/>
	 * return; # equivalent to return EXIT_SUCCESS
	 */
	@Test
	public void testPrintProg() {
		// declarations
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());
		DeclarationNode declaration_d = new DeclarationNodeImpl();
		declaration_d.setIdentifier("d");
		declaration_d.setType(new DoubleType());
		DeclarationNode declaration_s = new DeclarationNodeImpl();
		declaration_s.setIdentifier("s");
		declaration_s.setType(new StringType(new Long(20)));
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());
		DeclarationNode declaration_linebreak = new DeclarationNodeImpl();
		declaration_linebreak.setIdentifier("linebreak");
		declaration_linebreak.setType(new StringType(new Long(20)));

		// initializations
		BasicIdentifierNode identifier_linebreak1 = new BasicIdentifierNodeImpl();
		identifier_linebreak1.setIdentifier("linebreak");
		BasicIdentifierNode identifier_b1 = new BasicIdentifierNodeImpl();
		identifier_b1.setIdentifier("b");
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");
		BasicIdentifierNode identifier_d1 = new BasicIdentifierNodeImpl();
		identifier_d1.setIdentifier("d");
		BasicIdentifierNode identifier_s1 = new BasicIdentifierNodeImpl();
		identifier_s1.setIdentifier("s");

		LiteralNode literal_true = new LiteralNodeImpl();
		literal_true.setLiteral("true");
		literal_true.setLiteralType(new BooleanType());
		LiteralNode literal_long = new LiteralNodeImpl();
		literal_long.setLiteral("18121313223");
		literal_long.setLiteralType(new LongType());
		LiteralNode literal_double = new LiteralNodeImpl();
		literal_double.setLiteral("-23.23e-100");
		literal_double.setLiteralType(new DoubleType());
		LiteralNode literal_string = new LiteralNodeImpl();
		literal_string.setLiteral("jagÄrEttString\"\n");
		literal_string.setLiteralType(new StringType(new Long(20)));
		LiteralNode literal_linebreak = new LiteralNodeImpl();
		literal_linebreak.setLiteral("\n");
		literal_linebreak.setLiteralType(new StringType(new Long(20)));
		
		AssignmentNode assignment_linebreak = new AssignmentNodeImpl();
		assignment_linebreak.setLeftValue(identifier_s1);
		assignment_linebreak.setRightValue(literal_linebreak);
		identifier_s1.setParentNode(assignment_linebreak);
		literal_linebreak.setParentNode(assignment_linebreak);
		AssignmentNode assignment_b = new AssignmentNodeImpl();
		assignment_b.setLeftValue(identifier_b1);
		assignment_b.setRightValue(literal_true);
		identifier_b1.setParentNode(assignment_b);
		literal_true.setParentNode(assignment_b);
		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l1);
		assignment_l.setRightValue(literal_long);
		identifier_l1.setParentNode(assignment_l);
		literal_long.setParentNode(assignment_l);
		AssignmentNode assignment_d = new AssignmentNodeImpl();
		assignment_d.setLeftValue(identifier_d1);
		assignment_d.setRightValue(literal_double);
		identifier_d1.setParentNode(assignment_d);
		literal_double.setParentNode(assignment_d);
		AssignmentNode assignment_s = new AssignmentNodeImpl();
		assignment_s.setLeftValue(identifier_s1);
		assignment_s.setRightValue(literal_string);
		identifier_s1.setParentNode(assignment_s);
		literal_string.setParentNode(assignment_s);
		
		// prints
		BasicIdentifierNode identifier_linebreak2 = new BasicIdentifierNodeImpl();
		identifier_linebreak2.setIdentifier("linebreak");
		BasicIdentifierNode identifier_linebreak3 = new BasicIdentifierNodeImpl();
		identifier_linebreak3.setIdentifier("linebreak");
		BasicIdentifierNode identifier_linebreak4 = new BasicIdentifierNodeImpl();
		identifier_linebreak4.setIdentifier("linebreak");
		BasicIdentifierNode identifier_b2 = new BasicIdentifierNodeImpl();
		identifier_b2.setIdentifier("b");
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		BasicIdentifierNode identifier_d2 = new BasicIdentifierNodeImpl();
		identifier_d2.setIdentifier("d");
		BasicIdentifierNode identifier_s2 = new BasicIdentifierNodeImpl();
		identifier_s2.setIdentifier("s");
		
		PrintNode print1 = new PrintNodeImpl();
		print1.setRightValue(identifier_b2);
		identifier_b2.setParentNode(print1);
		PrintNode print2 = new PrintNodeImpl();
		print2.setRightValue(identifier_linebreak2);
		identifier_linebreak2.setParentNode(print2);
		PrintNode print3 = new PrintNodeImpl();
		print3.setRightValue(identifier_l2);
		identifier_l2.setParentNode(print3);
		PrintNode print4 = new PrintNodeImpl();
		print4.setRightValue(identifier_linebreak3);
		identifier_linebreak3.setParentNode(print4);
		PrintNode print5 = new PrintNodeImpl();
		print5.setRightValue(identifier_d2);
		identifier_d2.setParentNode(print5);
		PrintNode print6 = new PrintNodeImpl();
		print6.setRightValue(identifier_linebreak4);
		identifier_linebreak4.setParentNode(print6);
		PrintNode print7 = new PrintNodeImpl();
		print7.setRightValue(identifier_s2);
		identifier_s2.setParentNode(print7);
		
		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("d", new DoubleType());
		symbolTable.insert("b", new BooleanType());
		symbolTable.insert("s", new StringType(new Long(20)));

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_d);
		blockNode.addDeclaration(declaration_s);
		blockNode.addDeclaration(declaration_b);
		blockNode.addDeclaration(declaration_linebreak);
		blockNode.addStatement(assignment_linebreak);
		blockNode.addStatement(assignment_b);
		blockNode.addStatement(assignment_l);
		blockNode.addStatement(assignment_d);
		blockNode.addStatement(assignment_s);
		blockNode.addStatement(print1);
		blockNode.addStatement(print2);
		blockNode.addStatement(print3);
		blockNode.addStatement(print4);
		blockNode.addStatement(print5);
		blockNode.addStatement(print6);
		blockNode.addStatement(print7);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_d.setParentNode(blockNode);
		declaration_s.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		declaration_linebreak.setParentNode(blockNode);
		assignment_linebreak.setParentNode(blockNode);
		assignment_b.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);
		assignment_d.setParentNode(blockNode);
		assignment_s.setParentNode(blockNode);
		print1.setParentNode(blockNode);
		print2.setParentNode(blockNode);
		print3.setParentNode(blockNode);
		print4.setParentNode(blockNode);
		print5.setParentNode(blockNode);
		print6.setParentNode(blockNode);
		print7.setParentNode(blockNode);
		
		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);

		analyser.analyse(ast);

		assertFalse(log.hasErrors());
	}
}
