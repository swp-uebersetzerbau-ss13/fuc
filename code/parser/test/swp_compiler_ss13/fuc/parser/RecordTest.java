package swp_compiler_ss13.fuc.parser;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class RecordTest {
	
	@Test
	public void testBasicRecord() {
		String input = "record {\n" +
				"long i;\n" +
				"long j;\n" +
				"} x;\n" +
				"x.i = 3;\n" +
				"x.j = 5;\n" +
				"if (x.i < x.j) {\n" +
				"print x.i;\n" +
				"x.i = x.j + 2;\n" +
				"} else {\n" +
				"x.j = x.i * 2;\n" +
				"print x.j;\n" +
				"}\n" +
				"return x.i;\n";
		
		// Parse
		AST ast = GrammarTestHelper.parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("x",
				new StructType("record",
						new Member("i", new LongType()),
						new Member("j", new LongType())));
		
		factory.addAssignment(
				factory.newStructIdentifier("i",
						factory.newBasicIdentifier("x")),
						factory.newLiteral("3", new LongType()));
		factory.addAssignment(
				factory.newStructIdentifier("j",
						factory.newBasicIdentifier("x")),
						factory.newLiteral("5", new LongType()));
		
		factory.addBranch(
				factory.newBinaryExpression(BinaryOperator.LESSTHAN,
						factory.newStructIdentifier("i",
								factory.newBasicIdentifier("x")),
								factory.newStructIdentifier("j",
										factory.newBasicIdentifier("x"))));
		factory.addBlock();	// stmt true
		factory.addPrint(
				factory.newStructIdentifier("i", factory.newBasicIdentifier("x")));
		factory.addAssignment(
				factory.newStructIdentifier("i",
						factory.newBasicIdentifier("x")),
						factory.newBinaryExpression(BinaryOperator.ADDITION,
								factory.newStructIdentifier("j",
										factory.newBasicIdentifier("x")),
								factory.newLiteral("2", new LongType())));
		factory.goToParent();	// -> if
		factory.addBlock();	// stmt false
		factory.addAssignment(
				factory.newStructIdentifier("j",
						factory.newBasicIdentifier("x")),
						factory.newBinaryExpression(BinaryOperator.MULTIPLICATION,
								factory.newStructIdentifier("i",
										factory.newBasicIdentifier("x")),
								factory.newLiteral("2", new LongType())));
		factory.addPrint(
				factory.newStructIdentifier("j", factory.newBasicIdentifier("x")));
		factory.goToParent();	// -> if
		factory.goToParent();	// -> root block
		
		factory.addReturn(
				factory.newStructIdentifier("i",
						factory.newBasicIdentifier("x")));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
