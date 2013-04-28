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
@SuiteClasses({ ArithmeticBinaryExpressionNodeImplTest.class, ArithmeticUnaryExpressionNodeImplTest.class,
		ArrayIdentifierNodeImplTest.class, AssignmentNodeImplTest.class })
public class TestSuite {
}
