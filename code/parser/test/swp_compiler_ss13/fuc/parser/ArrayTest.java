package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;

public class ArrayTest {
	@Test
	public void testArrayTest() throws Exception {
		String input = "bool [ 5 ] b;\n" +
						"bool a;\n" + 
						"a = b [ 2 ] ;\n" +
						"return;";

		// Run LR-parser
		AST ast = GrammarTestHelper.parseToAst(input);
		assertNotNull(ast);

		// Create reference AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("b", new ArrayType(new BooleanType(), 5));
		factory.addDeclaration("a", new BooleanType());

		factory.addAssignment(
				factory.newBasicIdentifier("a"),
				factory.newArrayIdentifier(
						factory.newLiteral("2", new LongType()),
						factory.newBasicIdentifier("b")));
		
		factory.addReturn(null);
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}

	@Test
	public void testArrayTest2() throws Exception {
		String input = "long [ 3 ] a; a [ 0 ] = 42; return a [ 0 ];";

		// Run LR-parser
		AST ast = GrammarTestHelper.parseToAst(input);
		assertNotNull(ast);

		// Create reference AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("a", new ArrayType(new LongType(), 3));

		factory.addAssignment(
				factory.newArrayIdentifier(
						factory.newLiteral("0", new LongType()),
						factory.newBasicIdentifier("a")),
				factory.newLiteral("42", new LongType()));
		
		factory.addReturn(
				factory.newArrayIdentifier(
						factory.newLiteral("0", new LongType()),
						factory.newBasicIdentifier("a")));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}

	@Test
	public void testArrayTest3() throws Exception {
		String input = "long l; long [ 3 ] a; a [ 0 ] = 42; l = a [ 0 ]; return l;";

		// Run LR-parser
		AST ast = GrammarTestHelper.parseToAst(input);
		assertNotNull(ast);

		// Create reference AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("l", new LongType());
		factory.addDeclaration("a", new ArrayType(new LongType(), 3));

		factory.addAssignment(
				factory.newArrayIdentifier(
						factory.newLiteral("0", new LongType()),
						factory.newBasicIdentifier("a")),
				factory.newLiteral("42", new LongType()));
		factory.addAssignment(
				factory.newBasicIdentifier("l"),
				factory.newArrayIdentifier(
						factory.newLiteral("0", new LongType()),
						factory.newBasicIdentifier("a")));
		
		factory.addReturn(factory.newBasicIdentifier("l"));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
	
	@Test
	public void testArrayTest4() {
		String input = "long[10] n;\n"
				+ "long i;\n"
				+ "print n[i];\n";

		// Run LR-parser
		AST ast = GrammarTestHelper.parseToAst(input);
		assertNotNull(ast);

		// Create reference AST
		ASTFactory factory = new ASTFactory();
		factory.addDeclaration("n", new ArrayType(new LongType(), 10));
		factory.addDeclaration("i", new LongType());

		factory.addPrint(
				factory.newArrayIdentifier(
						factory.newBasicIdentifier("i"),
						factory.newBasicIdentifier("n")));
		
		AST expected = factory.getAST();
		ASTComparator.compareAST(expected, ast);
	}
}
