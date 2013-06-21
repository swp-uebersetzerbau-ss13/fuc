package swp_compiler_ss13.fuc.ast.test;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;
import swp_compiler_ss13.fuc.ast.visualization.ASTInfixVisualization;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ASTVisualizationTest {

	private static ASTImpl ast1;
	private static ASTImpl ast2;

	@Before
	public void setUp1() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ast1 = new ASTImpl();
		BlockNode program = new BlockNodeImpl();
		program.setSymbolTable(new SymbolTableImpl());
		ast1.setRootNode(program);
		DeclarationNode l = new DeclarationNodeImpl();
		l.setIdentifier("l");
		l.setType(new LongType());
		l.setParentNode(program);
		program.addDeclaration(l);

		program.getSymbolTable().insert("l", new LongType());

		LiteralNode literal10 = new LiteralNodeImpl();
		literal10.setLiteral("10");
		literal10.setLiteralType(new LongType());

		LiteralNode literal23 = new LiteralNodeImpl();
		literal23.setLiteral("23");
		literal23.setLiteralType(new LongType());

		LiteralNode literal232 = new LiteralNodeImpl();
		literal232.setLiteral("23");
		literal232.setLiteralType(new LongType());

		LiteralNode literal100 = new LiteralNodeImpl();
		literal100.setLiteral("100");
		literal100.setLiteralType(new LongType());

		LiteralNode literal2 = new LiteralNodeImpl();
		literal2.setLiteral("2");
		literal2.setLiteralType(new LongType());

		LiteralNode literal30 = new LiteralNodeImpl();
		literal30.setLiteral("30");
		literal30.setLiteralType(new LongType());

		LiteralNode literal9 = new LiteralNodeImpl();
		literal9.setLiteral("9");
		literal9.setLiteralType(new LongType());

		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");
		literal3.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode abe1 = new ArithmeticBinaryExpressionNodeImpl();
		abe1.setLeftValue(literal10);
		abe1.setRightValue(literal23);
		abe1.setOperator(BinaryOperator.ADDITION);
		literal10.setParentNode(abe1);
		literal23.setParentNode(abe1);

		ArithmeticBinaryExpressionNode abe2 = new ArithmeticBinaryExpressionNodeImpl();
		abe2.setLeftValue(abe1);
		abe2.setRightValue(literal232);
		abe2.setOperator(BinaryOperator.SUBSTRACTION);
		abe1.setParentNode(abe2);
		literal232.setParentNode(abe2);

		ArithmeticBinaryExpressionNode abe3 = new ArithmeticBinaryExpressionNodeImpl();
		abe3.setLeftValue(literal100);
		abe3.setRightValue(literal2);
		abe3.setOperator(BinaryOperator.DIVISION);
		literal100.setParentNode(abe3);
		literal2.setParentNode(literal2);

		ArithmeticBinaryExpressionNode abe4 = new ArithmeticBinaryExpressionNodeImpl();
		abe4.setLeftValue(abe2);
		abe4.setRightValue(abe3);
		abe4.setOperator(BinaryOperator.ADDITION);
		abe2.setParentNode(abe4);
		abe3.setParentNode(abe4);

		ArithmeticBinaryExpressionNode abe5 = new ArithmeticBinaryExpressionNodeImpl();
		abe5.setLeftValue(abe4);
		abe5.setRightValue(literal30);
		abe5.setOperator(BinaryOperator.SUBSTRACTION);
		literal30.setParentNode(abe5);
		abe4.setParentNode(abe5);

		ArithmeticBinaryExpressionNode abe6 = new ArithmeticBinaryExpressionNodeImpl();
		abe6.setLeftValue(literal9);
		abe6.setRightValue(literal3);
		abe6.setOperator(BinaryOperator.DIVISION);
		literal9.setParentNode(abe6);
		literal3.setParentNode(abe6);

		ArithmeticBinaryExpressionNode abe7 = new ArithmeticBinaryExpressionNodeImpl();
		abe7.setLeftValue(abe5);
		abe7.setRightValue(abe6);
		abe7.setOperator(BinaryOperator.SUBSTRACTION);
		abe5.setParentNode(abe7);
		abe6.setParentNode(abe7);

		BasicIdentifierNode bi1 = new BasicIdentifierNodeImpl();
		bi1.setIdentifier("l");

		AssignmentNode an1 = new AssignmentNodeImpl();
		an1.setLeftValue(bi1);
		an1.setRightValue(abe7);
		abe7.setParentNode(an1);
		bi1.setParentNode(an1);

		program.addStatement(an1);
		an1.setParentNode(program);

		ReturnNode ret = new ReturnNodeImpl();
		ret.setParentNode(program);
		ret.setRightValue(bi1);

		program.addStatement(ret);

	}

	@Before
	public void setUp2() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ast2 = new ASTImpl();
		BlockNode program = new BlockNodeImpl();
		program.setSymbolTable(new SymbolTableImpl());
		ast2.setRootNode(program);

		BlockNode block = new BlockNodeImpl();
		block.setSymbolTable(new SymbolTableImpl());

		DeclarationNode l = new DeclarationNodeImpl();
		l.setIdentifier("l");
		l.setType(new LongType());
		l.setParentNode(program);
		program.addDeclaration(l);

		program.getSymbolTable().insert("l", new LongType());

		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");
		literal3.setLiteralType(new LongType());

		LiteralNode literal32 = new LiteralNodeImpl();
		literal32.setLiteral("3");
		literal32.setLiteralType(new LongType());

		ArithmeticUnaryExpressionNode minus = new ArithmeticUnaryExpressionNodeImpl();
		minus.setOperator(UnaryOperator.MINUS);
		minus.setRightValue(literal32);
		literal32.setParentNode(minus);

		BasicIdentifierNode lid = new BasicIdentifierNodeImpl();
		lid.setIdentifier("l");

		AssignmentNode assign2 = new AssignmentNodeImpl();
		assign2.setLeftValue(lid);
		assign2.setRightValue(literal32);
		lid.setParentNode(assign2);
		literal32.setParentNode(assign2);

		ArithmeticBinaryExpressionNode add = new ArithmeticBinaryExpressionNodeImpl();
		add.setOperator(BinaryOperator.ADDITION);
		add.setLeftValue(assign2);
		add.setRightValue(minus);
		literal3.setParentNode(add);
		assign2.setParentNode(add);

		AssignmentNode assign = new AssignmentNodeImpl();
		assign.setLeftValue(lid);
		assign.setRightValue(add);
		lid.setParentNode(assign);
		add.setParentNode(assign);

		assign.setParentNode(block);
		program.addStatement(block);
		block.setParentNode(program);
		block.addStatement(assign);

		BasicIdentifierNode lid2 = new BasicIdentifierNodeImpl();
		lid2.setIdentifier("l");

		ReturnNode ret = new ReturnNodeImpl();
		ret.setRightValue(lid2);
		lid2.setParentNode(ret);

		ret.setParentNode(program);
		program.addStatement(ret);
	}

	@Test
	public void Infix() {
		(new ASTInfixVisualization()).visualizeAST(ast1);
		(new ASTInfixVisualization()).visualizeAST(ast2);
	}

	@Test
	public void Infix2() {
		ASTFactory astf = new ASTFactory();
		astf.addDeclaration("b", new BooleanType());
		astf.addDeclaration("c", new BooleanType());
		astf.addDeclaration("l", new LongType());
		astf.addDeclaration("abc", new StringType(3L));
		astf.addDeclaration("a", new ArrayType(new BooleanType(), 10));

		astf.addAssignment(astf.newBasicIdentifier("b"),
				astf.newArrayIdentifier(3, astf.newArrayIdentifier(5, astf.newBasicIdentifier("a"))));
		astf.addAssignment(astf.newBasicIdentifier("b"), astf.newLiteral("true", new BooleanType()));
		astf.addAssignment(astf.newBasicIdentifier("c"), astf.newLiteral("false", new BooleanType()));
		astf.addAssignment(astf.newBasicIdentifier("l"), astf.newLiteral("4", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("abc"), astf.newLiteral("bla", new StringType(3L)));

		astf.addBranch(astf.newBasicIdentifier("b"));
		astf.addBlock();
		astf.addBranch(astf.newBinaryExpression(BinaryOperator.LOGICAL_OR, astf.newBasicIdentifier("c"),
				astf.newUnaryExpression(UnaryOperator.LOGICAL_NEGATE, astf.newBasicIdentifier("b"))));
		astf.addBlock();
		astf.addPrint(astf.newBasicIdentifier("abc"));
		astf.goToParent();
		astf.addBlock();
		astf.addAssignment(astf.newBasicIdentifier("l"), astf.newLiteral("5", new LongType()));
		astf.goToParent();
		astf.goToParent();
		astf.goToParent();
		astf.goToParent();
		astf.addPrint(astf.newArrayIdentifier(5, astf.newBasicIdentifier("a")));
		astf.addBreak();
		astf.addReturn(astf.newBasicIdentifier("l"));

		(new ASTInfixVisualization()).visualizeAST(astf.getAST());
	}
}
