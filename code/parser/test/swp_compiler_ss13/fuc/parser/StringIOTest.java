package swp_compiler_ss13.fuc.parser;

import static swp_compiler_ss13.fuc.parser.parser.LRParser.STRING_LENGTH;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.parser.parser.LRParser;

public class StringIOTest {
	@Test
	public void testLinebreakInString() {
		String input = "string s;\ns = \"test1\\ntest2\";\nreturn;";
		
		// Parse
		AST ast = GrammarTestHelper.parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("s", new StringType(STRING_LENGTH));
		factory.addAssignment(factory.newBasicIdentifier("s"), factory.newLiteral("\"test1\\ntest2\"", new StringType(14L)));
		factory.addReturn(null);
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
	
	@Test
	public void testConditional() {
		String input = "bool b;\nlong l;\nif ( b ) l = 1;\nreturn l;";
		
		// Parse
		AST ast = GrammarTestHelper.parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("b", new BooleanType());
		factory.addDeclaration("l", new LongType());
		BranchNode iff = factory.addBranch(factory.newBasicIdentifier("b"));
		iff.setStatementNodeOnTrue(factory.newAssignment(factory.newBasicIdentifier("l"), factory.newLiteral("1", new LongType())));
		factory.goToParent();
		factory.addReturn(factory.newBasicIdentifier("l"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
	
	@Test
	public void testHalloWelt() {
		String input = "string s;\ns = \"Hallo\\nWelt\";return s;";
		
		// Parse
		AST ast = GrammarTestHelper.parseToAst(input);
		
		// Construct expected AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("s", new StringType(LRParser.STRING_LENGTH));
		factory.addAssignment(factory.newBasicIdentifier("s"), factory.newLiteral("\"Hallo\\nWelt\"", new StringType(13L)));
		factory.addReturn(factory.newBasicIdentifier("s"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
