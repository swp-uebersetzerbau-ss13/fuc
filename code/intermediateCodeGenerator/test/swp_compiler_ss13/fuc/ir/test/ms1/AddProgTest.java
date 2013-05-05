package swp_compiler_ss13.fuc.ir.test.ms1;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class AddProgTest {

	private static ASTImpl ast;

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		ast = new ASTImpl();
		BlockNode program = new BlockNodeImpl();
		program.setSymbolTable(new SymbolTableImpl());
		ast.setRootNode(program);
		DeclarationNode l = new DeclarationNodeImpl();
		l.setIdentifier("l");
		l.setType(new LongType());
		l.setParentNode(program);
		program.addDeclaration(l);

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

		program.addStatement(abe7);
		abe7.setParentNode(program);
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGenerator gen = new IntermediateCodeGeneratorImpl();
		List<Quadruple> tac = gen.generateIntermediateCode(ast);
		for (Quadruple q : tac) {
			System.out.println(String.format("(%s|%s|%s|%s)", q.getOperator(), q.getArgument1(), q.getArgument2(),
					q.getResult()));
		}
	}

}
