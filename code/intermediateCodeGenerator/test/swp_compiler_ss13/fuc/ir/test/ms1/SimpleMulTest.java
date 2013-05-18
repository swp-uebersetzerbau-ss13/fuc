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
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
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
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class SimpleMulTest {

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

		LiteralNode literal3 = new LiteralNodeImpl();
		literal3.setLiteral("3");
		literal3.setLiteralType(new LongType());

		LiteralNode literal32 = new LiteralNodeImpl();
		literal32.setLiteral("3");
		literal32.setLiteralType(new LongType());

		ArithmeticBinaryExpressionNode mult = new ArithmeticBinaryExpressionNodeImpl();
		mult.setOperator(BinaryOperator.MULTIPLICATION);
		mult.setLeftValue(literal3);
		mult.setRightValue(literal32);
		literal3.setParentNode(mult);
		literal32.setParentNode(mult);

		BasicIdentifierNode lid = new BasicIdentifierNodeImpl();
		lid.setIdentifier("l");

		AssignmentNode assign = new AssignmentNodeImpl();
		assign.setLeftValue(lid);
		assign.setRightValue(mult);
		lid.setParentNode(assign);
		mult.setParentNode(assign);

		assign.setParentNode(program);
		program.addStatement(assign);

		BasicIdentifierNode lid2 = new BasicIdentifierNodeImpl();
		lid2.setIdentifier("l");

		ReturnNode ret = new ReturnNodeImpl();
		ret.setRightValue(lid2);
		lid2.setParentNode(ret);

		ret.setParentNode(program);
		program.addStatement(ret);
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		IntermediateCodeGenerator gen = new IntermediateCodeGeneratorImpl();
		List<Quadruple> tac = gen.generateIntermediateCode(ast);

		String result = "" +
				"(DECLARE_LONG|!|!|l)" + "\n" +
				"(DECLARE_LONG|!|!|" + temporaryName + "0)" + "\n" +
				"(MUL_LONG|#3|#3|" + temporaryName + "0)" + "\n" +
				"(ASSIGN_LONG|" + temporaryName + "0|!|l)" + "\n" +
				"(RETURN|l|!|!)" + "\n";

		StringBuilder actual = new StringBuilder();
		for (Quadruple q : tac) {
			actual.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(), q.getArgument1(), q.getArgument2(),
					q.getResult()));
		}

		assertEquals(result, actual.toString());
	}
}
