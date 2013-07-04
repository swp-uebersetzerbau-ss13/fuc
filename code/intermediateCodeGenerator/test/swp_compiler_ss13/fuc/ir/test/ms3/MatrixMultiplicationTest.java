package swp_compiler_ss13.fuc.ir.test.ms3;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class MatrixMultiplicationTest {

	private AST ast;

	@Before
	public void setUp() throws Exception {
		PA.setValue(SymbolTableImpl.class, "ext", 0);
		ASTFactory astf = new ASTFactory();

		astf.addDeclaration("ax", new LongType());
		astf.addDeclaration("ay", new LongType());
		astf.addDeclaration("bx", new LongType());
		astf.addDeclaration("by", new LongType());

		astf.addDeclaration("ix", new LongType());
		astf.addDeclaration("iy", new LongType());
		astf.addDeclaration("i", new LongType());

		astf.addDeclaration("sep", new StringType(2L));
		astf.addDeclaration("br", new StringType(3L));

		astf.addDeclaration("a", new ArrayType(new ArrayType(new LongType(), 4), 3));
		astf.addDeclaration("b", new ArrayType(new ArrayType(new LongType(), 3), 2));
		astf.addDeclaration("c", new ArrayType(new ArrayType(new LongType(), 4), 2));

		astf.addAssignment(astf.newBasicIdentifier("sep"), astf.newLiteral("|", new StringType(2L)));
		astf.addAssignment(astf.newBasicIdentifier("br"), astf.newLiteral("\\n", new StringType(3L)));

		astf.addAssignment(astf.newBasicIdentifier("ax"), astf.newLiteral("4", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("ay"), astf.newLiteral("3", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("bx"), astf.newLiteral("3", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("by"), astf.newLiteral("2", new LongType()));
		astf.addAssignment(astf.newBasicIdentifier("ix"), astf.newLiteral("0", new LongType()));

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("0", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("1", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("0", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("2", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("0", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("2", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("3", new LongType())
				);

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("1", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("2", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("1", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("4", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("1", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("2", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("6", new LongType())
				);

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("2", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("3", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("2", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("6", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("2", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("2", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("9", new LongType())
				);

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("3", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("4", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("3", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("8", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("3", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("2", new LongType()),
								astf.newBasicIdentifier("a")
								)
						),
				astf.newLiteral("12", new LongType())
				);

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("0", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("b")
								)
						),
				astf.newLiteral("1", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("0", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("b")
								)
						),
				astf.newLiteral("5", new LongType())
				);

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("1", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("b")
								)
						),
				astf.newLiteral("2", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("1", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("b")
								)
						),
				astf.newLiteral("7", new LongType())
				);

		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("2", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("0", new LongType()),
								astf.newBasicIdentifier("b")
								)
						),
				astf.newLiteral("3", new LongType())
				);
		astf.addAssignment(
				astf.newArrayIdentifier(
						astf.newLiteral("2", new LongType()),
						astf.newArrayIdentifier(
								astf.newLiteral("1", new LongType()),
								astf.newBasicIdentifier("b")
								)
						),
				astf.newLiteral("9", new LongType())
				);

		astf.addWhile(astf.newBinaryExpression(BinaryOperator.LESSTHAN, astf.newBasicIdentifier("ix"),
				astf.newBasicIdentifier("ax")));
		astf.addBlock();

		astf.addAssignment(astf.newBasicIdentifier("iy"), astf.newLiteral("0", new LongType()));

		astf.addWhile(astf.newBinaryExpression(
				BinaryOperator.LOGICAL_AND,
				astf.newBinaryExpression(BinaryOperator.LESSTHAN, astf.newBasicIdentifier("i"),
						astf.newBasicIdentifier("bx")),
				astf.newBinaryExpression(BinaryOperator.LESSTHAN, astf.newBasicIdentifier("i"),
						astf.newBasicIdentifier("ay"))));
		astf.addBlock();
		astf.addAssignment(
				astf.newArrayIdentifier(astf.newBasicIdentifier("ix"),
						astf.newArrayIdentifier(astf.newBasicIdentifier("iy"), astf.newBasicIdentifier("c"))),
				astf.newBinaryExpression(
						BinaryOperator.ADDITION,
						astf.newBinaryExpression(
								BinaryOperator.MULTIPLICATION,
								astf.newArrayIdentifier(
										astf.newBasicIdentifier("ix"),
										astf.newArrayIdentifier(astf.newBasicIdentifier("i"),
												astf.newBasicIdentifier("a"))),
								astf.newArrayIdentifier(
										astf.newBasicIdentifier("i"),
										astf.newArrayIdentifier(astf.newBasicIdentifier("iy"),
												astf.newBasicIdentifier("b")))),
						astf.newArrayIdentifier(astf.newBasicIdentifier("ix"),
								astf.newArrayIdentifier(astf.newBasicIdentifier("iy"), astf.newBasicIdentifier("c")))));

		astf.goToParent();
		astf.goToParent();
		astf.goToParent();
		astf.goToParent();

		astf.addAssignment(astf.newBasicIdentifier("ix"), astf.newLiteral("0", new LongType()));
		astf.addWhile(astf.newBinaryExpression(BinaryOperator.LESSTHAN, astf.newBasicIdentifier("ix"),
				astf.newBasicIdentifier("ax")));
		astf.addBlock();

		astf.addAssignment(astf.newBasicIdentifier("iy"), astf.newLiteral("0", new LongType()));
		astf.addWhile(astf.newBinaryExpression(BinaryOperator.LESSTHAN, astf.newBasicIdentifier("iy"),
				astf.newBasicIdentifier("by")));
		astf.addBlock();

		astf.addPrint(astf.newArrayIdentifier(
				astf.newBasicIdentifier("ix"),
				astf.newArrayIdentifier(astf.newBasicIdentifier("iy"),
						astf.newBasicIdentifier("c"))));

		astf.addBranch(astf.newBinaryExpression(
				BinaryOperator.INEQUAL,
				astf.newBinaryExpression(BinaryOperator.ADDITION, astf.newBasicIdentifier("iy"),
						astf.newLiteral("1", new LongType())),
				astf.newBasicIdentifier("by")));
		astf.addPrint(astf.newBasicIdentifier("sep"));
		astf.goToParent();
		astf.goToParent();
		astf.goToParent();
		astf.addPrint(astf.newBasicIdentifier("br"));

		astf.goToParent();
		astf.goToParent();
		astf.addReturn(null);

		this.ast = astf.getAST();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException, IOException, InterruptedException, BackendException {
		IntermediateCodeGeneratorImpl irg = new IntermediateCodeGeneratorImpl();
		List<Quadruple> irc = irg.generateIntermediateCode(this.ast);

		StringBuilder b = new StringBuilder();
		for (Quadruple q : irc) {
			b.append(String.format("%s|%s|%s|%s\n", q.getOperator(),
					q.getArgument1(), q.getArgument2(), q.getResult()));
		}
		String actual = b.toString();
		String expected = "DECLARE_LONG|!|!|ax\n" +
				"DECLARE_LONG|!|!|ay\n" +
				"DECLARE_LONG|!|!|bx\n" +
				"DECLARE_LONG|!|!|by\n" +
				"DECLARE_LONG|!|!|ix\n" +
				"DECLARE_LONG|!|!|iy\n" +
				"DECLARE_LONG|!|!|i\n" +
				"DECLARE_STRING|!|!|sep\n" +
				"DECLARE_STRING|!|!|br\n" +
				"DECLARE_ARRAY|#3|!|a\n" +
				"DECLARE_ARRAY|#4|!|!\n" +
				"DECLARE_LONG|!|!|!\n" +
				"DECLARE_ARRAY|#2|!|b\n" +
				"DECLARE_ARRAY|#3|!|!\n" +
				"DECLARE_LONG|!|!|!\n" +
				"DECLARE_ARRAY|#2|!|c\n" +
				"DECLARE_ARRAY|#4|!|!\n" +
				"DECLARE_LONG|!|!|!\n" +
				"ASSIGN_STRING|#\"|\"|!|sep\n" +
				"ASSIGN_STRING|#\"\\n\"|!|br\n" +
				"ASSIGN_LONG|#4|!|ax\n" +
				"ASSIGN_LONG|#3|!|ay\n" +
				"ASSIGN_LONG|#3|!|bx\n" +
				"ASSIGN_LONG|#2|!|by\n" +
				"ASSIGN_LONG|#0|!|ix\n" +
				"DECLARE_REFERENCE|!|!|tmp0\n" +
				"ARRAY_GET_REFERENCE|a|#0|tmp0\n" +
				"ARRAY_SET_LONG|tmp0|#0|#1\n" +
				"DECLARE_REFERENCE|!|!|tmp1\n" +
				"ARRAY_GET_REFERENCE|a|#1|tmp1\n" +
				"ARRAY_SET_LONG|tmp1|#0|#2\n" +
				"DECLARE_REFERENCE|!|!|tmp2\n" +
				"ARRAY_GET_REFERENCE|a|#2|tmp2\n" +
				"ARRAY_SET_LONG|tmp2|#0|#3\n" +
				"DECLARE_REFERENCE|!|!|tmp3\n" +
				"ARRAY_GET_REFERENCE|a|#0|tmp3\n" +
				"ARRAY_SET_LONG|tmp3|#1|#2\n" +
				"DECLARE_REFERENCE|!|!|tmp4\n" +
				"ARRAY_GET_REFERENCE|a|#1|tmp4\n" +
				"ARRAY_SET_LONG|tmp4|#1|#4\n" +
				"DECLARE_REFERENCE|!|!|tmp5\n" +
				"ARRAY_GET_REFERENCE|a|#2|tmp5\n" +
				"ARRAY_SET_LONG|tmp5|#1|#6\n" +
				"DECLARE_REFERENCE|!|!|tmp6\n" +
				"ARRAY_GET_REFERENCE|a|#0|tmp6\n" +
				"ARRAY_SET_LONG|tmp6|#2|#3\n" +
				"DECLARE_REFERENCE|!|!|tmp7\n" +
				"ARRAY_GET_REFERENCE|a|#1|tmp7\n" +
				"ARRAY_SET_LONG|tmp7|#2|#6\n" +
				"DECLARE_REFERENCE|!|!|tmp8\n" +
				"ARRAY_GET_REFERENCE|a|#2|tmp8\n" +
				"ARRAY_SET_LONG|tmp8|#2|#9\n" +
				"DECLARE_REFERENCE|!|!|tmp9\n" +
				"ARRAY_GET_REFERENCE|a|#0|tmp9\n" +
				"ARRAY_SET_LONG|tmp9|#3|#4\n" +
				"DECLARE_REFERENCE|!|!|tmp10\n" +
				"ARRAY_GET_REFERENCE|a|#1|tmp10\n" +
				"ARRAY_SET_LONG|tmp10|#3|#8\n" +
				"DECLARE_REFERENCE|!|!|tmp11\n" +
				"ARRAY_GET_REFERENCE|a|#2|tmp11\n" +
				"ARRAY_SET_LONG|tmp11|#3|#12\n" +
				"DECLARE_REFERENCE|!|!|tmp12\n" +
				"ARRAY_GET_REFERENCE|b|#0|tmp12\n" +
				"ARRAY_SET_LONG|tmp12|#0|#1\n" +
				"DECLARE_REFERENCE|!|!|tmp13\n" +
				"ARRAY_GET_REFERENCE|b|#1|tmp13\n" +
				"ARRAY_SET_LONG|tmp13|#0|#5\n" +
				"DECLARE_REFERENCE|!|!|tmp14\n" +
				"ARRAY_GET_REFERENCE|b|#0|tmp14\n" +
				"ARRAY_SET_LONG|tmp14|#1|#2\n" +
				"DECLARE_REFERENCE|!|!|tmp15\n" +
				"ARRAY_GET_REFERENCE|b|#1|tmp15\n" +
				"ARRAY_SET_LONG|tmp15|#1|#7\n" +
				"DECLARE_REFERENCE|!|!|tmp16\n" +
				"ARRAY_GET_REFERENCE|b|#0|tmp16\n" +
				"ARRAY_SET_LONG|tmp16|#2|#3\n" +
				"DECLARE_REFERENCE|!|!|tmp17\n" +
				"ARRAY_GET_REFERENCE|b|#1|tmp17\n" +
				"ARRAY_SET_LONG|tmp17|#2|#9\n" +
				"LABEL|label0|!|!\n" +
				"DECLARE_BOOLEAN|!|!|tmp18\n" +
				"COMPARE_LONG_L|ix|ax|tmp18\n" +
				"BRANCH|label1|label2|tmp18\n" +
				"LABEL|label1|!|!\n" +
				"ASSIGN_LONG|#0|!|iy\n" +
				"LABEL|label3|!|!\n" +
				"DECLARE_BOOLEAN|!|!|tmp19\n" +
				"COMPARE_LONG_L|i|bx|tmp19\n" +
				"DECLARE_BOOLEAN|!|!|tmp20\n" +
				"COMPARE_LONG_L|i|ay|tmp20\n" +
				"DECLARE_BOOLEAN|!|!|tmp21\n" +
				"AND_BOOLEAN|tmp19|tmp20|tmp21\n" +
				"BRANCH|label4|label5|tmp21\n" +
				"LABEL|label4|!|!\n" +
				"DECLARE_REFERENCE|!|!|tmp22\n" +
				"ARRAY_GET_REFERENCE|c|iy|tmp22\n" +
				"DECLARE_REFERENCE|!|!|tmp23\n" +
				"ARRAY_GET_REFERENCE|a|i|tmp23\n" +
				"DECLARE_LONG|!|!|tmp24\n" +
				"ARRAY_GET_LONG|tmp23|ix|tmp24\n" +
				"DECLARE_REFERENCE|!|!|tmp25\n" +
				"ARRAY_GET_REFERENCE|b|iy|tmp25\n" +
				"DECLARE_LONG|!|!|tmp26\n" +
				"ARRAY_GET_LONG|tmp25|i|tmp26\n" +
				"DECLARE_LONG|!|!|tmp27\n" +
				"MUL_LONG|tmp24|tmp26|tmp27\n" +
				"DECLARE_REFERENCE|!|!|tmp28\n" +
				"ARRAY_GET_REFERENCE|c|iy|tmp28\n" +
				"DECLARE_LONG|!|!|tmp29\n" +
				"ARRAY_GET_LONG|tmp28|ix|tmp29\n" +
				"DECLARE_LONG|!|!|tmp30\n" +
				"ADD_LONG|tmp27|tmp29|tmp30\n" +
				"ARRAY_SET_LONG|tmp22|ix|tmp30\n" +
				"BRANCH|label3|!|!\n" +
				"LABEL|label5|!|!\n" +
				"BRANCH|label0|!|!\n" +
				"LABEL|label2|!|!\n" +
				"ASSIGN_LONG|#0|!|ix\n" +
				"LABEL|label6|!|!\n" +
				"DECLARE_BOOLEAN|!|!|tmp31\n" +
				"COMPARE_LONG_L|ix|ax|tmp31\n" +
				"BRANCH|label7|label8|tmp31\n" +
				"LABEL|label7|!|!\n" +
				"ASSIGN_LONG|#0|!|iy\n" +
				"LABEL|label9|!|!\n" +
				"DECLARE_BOOLEAN|!|!|tmp32\n" +
				"COMPARE_LONG_L|iy|by|tmp32\n" +
				"BRANCH|label10|label11|tmp32\n" +
				"LABEL|label10|!|!\n" +
				"DECLARE_REFERENCE|!|!|tmp33\n" +
				"ARRAY_GET_REFERENCE|c|iy|tmp33\n" +
				"DECLARE_LONG|!|!|tmp34\n" +
				"ARRAY_GET_LONG|tmp33|ix|tmp34\n" +
				"DECLARE_STRING|!|!|tmp35\n" +
				"LONG_TO_STRING|tmp34|!|tmp35\n" +
				"PRINT_STRING|tmp35|!|!\n" +
				"DECLARE_LONG|!|!|tmp36\n" +
				"ADD_LONG|iy|#1|tmp36\n" +
				"DECLARE_BOOLEAN|!|!|tmp37\n" +
				"COMPARE_LONG_E|tmp36|by|tmp37\n" +
				"DECLARE_BOOLEAN|!|!|tmp38\n" +
				"NOT_BOOLEAN|tmp37|!|tmp38\n" +
				"BRANCH|label12|label13|tmp38\n" +
				"LABEL|label12|!|!\n" +
				"PRINT_STRING|sep|!|!\n" +
				"BRANCH|label14|!|!\n" +
				"LABEL|label13|!|!\n" +
				"LABEL|label14|!|!\n" +
				"BRANCH|label9|!|!\n" +
				"LABEL|label11|!|!\n" +
				"PRINT_STRING|br|!|!\n" +
				"BRANCH|label6|!|!\n" +
				"LABEL|label8|!|!\n" +
				"RETURN|#0|!|!\n";
		System.out.println(actual);
		assertEquals(expected, actual);
	}

}
