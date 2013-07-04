package swp_compiler_ss13.fuc.parser;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;

public class ComparisonTest {
	@Test
	public void testComparison() {
		String input = "long l;\n" +
				"bool result;\n" +
				"result = true;\n" +
				"\n" +
				"result = result && (3 < 4);\n" +
				"result = result && (3 <= 3);\n" +
				"result = result && (4 > 3);\n" +
				"result = result && (4 >= 4);\n" +
				"result = result && (true || false);\n" +
				"result = result && (true && true);\n" +
				"result = result && (3 == 3);\n" +
				"result = result && (3 != 4);\n" +
				"\n" +
				"if (result)\n" +
				"l = 1;\n" +
				"else\n" +
				"l = 0;\n" +				
				"return l;";
		ReportLog log = new ReportLogImpl();
		AST actual = GrammarTestHelper.parseToAst(input, log);
		
		// Created expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("l", new LongType());
		factory.addDeclaration("result", new BooleanType());
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newLiteral("true", new BooleanType()));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.LESSTHAN,
								factory.newLiteral("3", new LongType()),
								factory.newLiteral("4", new LongType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.LESSTHANEQUAL,
								factory.newLiteral("3", new LongType()),
								factory.newLiteral("3", new LongType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.GREATERTHAN,
								factory.newLiteral("4", new LongType()),
								factory.newLiteral("3", new LongType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.GREATERTHANEQUAL,
								factory.newLiteral("4", new LongType()),
								factory.newLiteral("4", new LongType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.LOGICAL_OR,
								factory.newLiteral("true", new BooleanType()),
								factory.newLiteral("false", new BooleanType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
								factory.newLiteral("true", new BooleanType()),
								factory.newLiteral("true", new BooleanType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.EQUAL,
								factory.newLiteral("3", new LongType()),
								factory.newLiteral("3", new LongType()))));
		factory.addAssignment(factory.newBasicIdentifier("result"),
				factory.newBinaryExpression(BinaryOperator.LOGICAL_AND,
						factory.newBasicIdentifier("result"),
						factory.newBinaryExpression(BinaryOperator.INEQUAL,
								factory.newLiteral("3", new LongType()),
								factory.newLiteral("4", new LongType()))));
		
		BranchNode iff = factory.addBranch(factory.newBasicIdentifier("result"));
		iff.setStatementNodeOnTrue(
				factory.newAssignment(
						factory.newBasicIdentifier("l"),
						factory.newLiteral("1", new LongType())));
		iff.setStatementNodeOnFalse(
				factory.newAssignment(
						factory.newBasicIdentifier("l"),
						factory.newLiteral("0", new LongType())));
		factory.goToParent();
		factory.addReturn(factory.newBasicIdentifier("l"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, actual);
	}
}
