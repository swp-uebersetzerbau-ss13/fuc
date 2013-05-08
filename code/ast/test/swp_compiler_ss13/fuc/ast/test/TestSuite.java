package swp_compiler_ss13.fuc.ast.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * Run all test in ast package
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
@RunWith(Suite.class)
@SuiteClasses({
		ArithmeticBinaryExpressionNodeImplTest.class,
		ArithmeticUnaryExpressionNodeImplTest.class,
		ArrayIdentifierNodeImplTest.class,
		AssignmentNodeImplTest.class,
		BasicIdentifierNodeImplTest.class,
		BlockNodeImplTest.class,
		BranchNodeImplTest.class,
		LogicBinaryExpressionNodeImplTest.class,
		LogicUnaryExpressionNodeImplTest.class,
		RelationExpressionNodeImplTest.class,
		ReturnNodeImplTest.class,
		StructIdentifierNodeImplTest.class,
		UnaryExpressionNodeImplTest.class,
		BinaryExpressionNodeImplTest.class,
		LiteralNodeImplTest.class,
		DeclarationNodeImplTest.class,
		PrintNodeImplTest.class,
		ReturnNodeImplTest.class,
		BreakNodeImplTest.class,
		DoWhileNodeImplTest.class,
		WhileNodeImplTest.class,
		ASTNodeImplTest.class,
		ASTImplTest.class })
public class TestSuite {
}
