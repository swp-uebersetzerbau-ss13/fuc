package swp_compiler_ss13.fuc.ir.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import swp_compiler_ss13.fuc.ir.test.ms1.AddProgTest;
import swp_compiler_ss13.fuc.ir.test.ms1.ParanthesesTest;
import swp_compiler_ss13.fuc.ir.test.ms1.SimpleAddTest;
import swp_compiler_ss13.fuc.ir.test.ms1.SimpleMulTest;
import swp_compiler_ss13.fuc.ir.test.ms2.ArrayTest;
import swp_compiler_ss13.fuc.ir.test.ms2.AssignmentProgTest;
import swp_compiler_ss13.fuc.ir.test.ms2.CondTest;
import swp_compiler_ss13.fuc.ir.test.ms2.PrintTest;

/**
 * Run all test in ast package
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
@RunWith(Suite.class)
@SuiteClasses({ AddProgTest.class, SimpleAddTest.class, SimpleMulTest.class, ParanthesesTest.class,
		ArrayTest.class, AssignmentProgTest.class, CondTest.class, IdentifierShadowTest.class,
		CastingTest.class, PrintTest.class, UnaryMinusTest.class, DoubleOperatorTest.class,
		RelationTest.class, ExceptioTest.class, BooleanTest.class })
public class TestSuite {
}
