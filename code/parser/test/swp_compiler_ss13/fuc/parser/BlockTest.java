package swp_compiler_ss13.fuc.parser;

import static swp_compiler_ss13.fuc.parser.parser.LRParser.STRING_LENGTH;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class BlockTest {
	@Test
	public void testIfElseBlocks() {
		String input = "bool b;\n"
				+ "string msgA;\n"
				+ "string msgB;\n"
				+ "long result;\n"
				+ "\n"
				+ "b = true;\n"
				+ "msgA = \"aaa\";\n"
				+ "msgB = \"bbb\";\n"
				+ "result = 0;\n"
				+ "\n"
				+ "if ( b ) {\n"
				+ "print msgA;\n"
				+ "result = 1;\n"
				+ "} else {\n"
				+ "long test1;\n"
				+ "test1 = 4;\n"
				+ "if ( test1 >= 4 ) {\n"
				+ "print msgB;\n"
				+ "result = 2;\n"
				+ "} else {\n"
				+ "result = 3;\n"
				+ "}\n"
				+ "}\n"
				+ "\n"
				+ "return result;\n";
		
		// Parse
		AST ast = GrammarTestHelper.parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("b", new BooleanType());
		factory.addDeclaration("msgA", new StringType(STRING_LENGTH));
		factory.addDeclaration("msgB", new StringType(STRING_LENGTH));
		factory.addDeclaration("result", new LongType());
		
		factory.addAssignment(factory.newBasicIdentifier("b"), factory.newLiteral("true", new BooleanType()));
		factory.addAssignment(factory.newBasicIdentifier("msgA"), factory.newLiteral("\"aaa\"", new StringType(STRING_LENGTH)));
		factory.addAssignment(factory.newBasicIdentifier("msgB"), factory.newLiteral("\"bbb\"", new StringType(STRING_LENGTH)));
		factory.addAssignment(factory.newBasicIdentifier("result"), factory.newLiteral("0", new LongType()));
		
		factory.addBranch(factory.newBasicIdentifier("b"));
		factory.addBlock();
		factory.addPrint(factory.newBasicIdentifier("msgA"));
		factory.addAssignment(factory.newBasicIdentifier("result"), factory.newLiteral("1", new LongType()));
		factory.goToParent();
		factory.addBlock();
		factory.addDeclaration("test1", new LongType());
		factory.addAssignment(factory.newBasicIdentifier("test1"), factory.newLiteral("4", new LongType()));
		factory.addBranch(factory.newBinaryExpression(BinaryOperator.GREATERTHANEQUAL, factory.newBasicIdentifier("test1"), factory.newLiteral("4", new LongType())));
		factory.addBlock();
		factory.addPrint(factory.newBasicIdentifier("msgB"));
		factory.addAssignment(factory.newBasicIdentifier("result"), factory.newLiteral("2", new LongType()));
		factory.goToParent();
		factory.addBlock();
		factory.addAssignment(factory.newBasicIdentifier("result"), factory.newLiteral("3", new LongType()));
		factory.goToParent();
		factory.goToParent();
		factory.addReturn(factory.newBasicIdentifier("result"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
