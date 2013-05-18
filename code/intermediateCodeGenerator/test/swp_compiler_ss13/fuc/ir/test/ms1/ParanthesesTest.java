package swp_compiler_ss13.fuc.ir.test.ms1;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ParanthesesTest {

	private static ASTImpl ast;
	private static final String temporaryName = "tmp";

	@Before
	public void setUp() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);

		ast = new ASTImpl();
		BlockNode program = new BlockNodeImpl();
		program.setSymbolTable(new SymbolTableImpl());
		ast.setRootNode(program);

		DeclarationNode l = new DeclarationNodeImpl();
		l.setIdentifier("l");
		l.setType(new LongType());
		l.setParentNode(program);
		program.addDeclaration(l);

		program.getSymbolTable().insert("l", new LongType());

		LiteralNode ln16 = new LiteralNodeImpl();
		ln16.setLiteral("16");
		ln16.setLiteralType(new LongType());

		LiteralNode ln8 = new LiteralNodeImpl();
		ln8.setLiteral("8");
		ln8.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode n16div8 = new ArithmeticBinaryExpressionNodeImpl();
		n16div8.setOperator(BinaryOperator.DIVISION);
		n16div8.setLeftValue(ln16);
		n16div8.setRightValue(ln8);
		ln16.setParentNode(n16div8);
		ln8.setParentNode(n16div8);

		LiteralNode ln2 = new LiteralNodeImpl();
		ln2.setLiteral("2");
		ln2.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode n2plus2 = new ArithmeticBinaryExpressionNodeImpl();
		n2plus2.setOperator(BinaryOperator.ADDITION);
		n2plus2.setLeftValue(ln2);
		n2plus2.setRightValue(n16div8);
		ln2.setParentNode(n2plus2);
		n16div8.setParentNode(n2plus2);

		BasicIdentifierNode lb = new BasicIdentifierNodeImpl();
		lb.setIdentifier("l");

		AssignmentNode assign = new AssignmentNodeImpl();
		assign.setLeftValue(lb);
		assign.setRightValue(n2plus2);
		lb.setParentNode(assign);
		n2plus2.setParentNode(assign);

		LiteralNode ln3 = new LiteralNodeImpl();
		ln3.setLiteral("3");
		ln3.setLiteralType(new LongType());

		LiteralNode ln3_2 = new LiteralNodeImpl();
		ln3_2.setLiteral("3");
		ln3_2.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode n3plus3 = new ArithmeticBinaryExpressionNodeImpl();
		n3plus3.setOperator(BinaryOperator.ADDITION);
		n3plus3.setLeftValue(ln3);
		n3plus3.setRightValue(ln3_2);
		ln3.setParentNode(n3plus3);
		ln3_2.setParentNode(n3plus3);

		LiteralNode ln2_2 = new LiteralNodeImpl();
		ln2_2.setLiteral("2");
		ln2_2.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode nmult2 = new ArithmeticBinaryExpressionNodeImpl();
		nmult2.setOperator(BinaryOperator.MULTIPLICATION);
		nmult2.setLeftValue(n3plus3);
		nmult2.setRightValue(ln2_2);
		n3plus3.setParentNode(nmult2);
		ln2_2.setParentNode(nmult2);

		ArithmeticBinaryExpressionNode nminus = new ArithmeticBinaryExpressionNodeImpl();
		nminus.setOperator(BinaryOperator.SUBSTRACTION);
		nminus.setLeftValue(nmult2);
		nminus.setRightValue(assign);
		nmult2.setParentNode(nminus);
		assign.setParentNode(nminus);

		BasicIdentifierNode bl2 = new BasicIdentifierNodeImpl();
		bl2.setIdentifier("l");

		AssignmentNode assign2 = new AssignmentNodeImpl();
		assign2.setLeftValue(bl2);
		assign2.setRightValue(nminus);
		bl2.setParentNode(assign2);
		nminus.setParentNode(assign2);

		program.addStatement(assign2);
		assign2.setParentNode(program);
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGenerator gen = new IntermediateCodeGeneratorImpl();
		List<Quadruple> tac = gen.generateIntermediateCode(ast);

		String result = "" +
				"(DECLARE_LONG|!|!|l)" + "\n" +
				"(DECLARE_LONG|!|!|" + temporaryName + "0)" + "\n" +
				"(ADD_LONG|#3|#3|" + temporaryName + "0)" + "\n" +
				"(DECLARE_LONG|!|!|" + temporaryName + "1)" + "\n" +
				"(MUL_LONG|" + temporaryName + "0|#2|" + temporaryName + "1)" + "\n" +
				"(DECLARE_LONG|!|!|" + temporaryName + "2)" + "\n" +
				"(DIV_LONG|#16|#8|" + temporaryName + "2)" + "\n" +
				"(DECLARE_LONG|!|!|" + temporaryName + "3)" + "\n" +
				"(ADD_LONG|#2|" + temporaryName + "2|" + temporaryName + "3)" + "\n" +
				"(ASSIGN_LONG|" + temporaryName + "3|!|l)" + "\n" +
				"(DECLARE_LONG|!|!|" + temporaryName + "4)" + "\n" +
				"(SUB_LONG|" + temporaryName + "1|l|" + temporaryName + "4)" + "\n" +
				"(ASSIGN_LONG|" + temporaryName + "4|!|l)" + "\n";

		StringBuilder actual = new StringBuilder();
		for (Quadruple q : tac) {
			actual.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(), q.getArgument1(), q.getArgument2(),
					q.getResult()));
		}

		assertEquals(result, actual.toString());
	}
}
