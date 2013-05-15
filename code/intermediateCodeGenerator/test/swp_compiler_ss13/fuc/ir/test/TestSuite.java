package swp_compiler_ss13.fuc.ir.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

import swp_compiler_ss13.fuc.ir.test.ms1.AddProgTest;
import swp_compiler_ss13.fuc.ir.test.ms1.SimpleAddTest;
import swp_compiler_ss13.fuc.ir.test.ms1.SimpleMulTest;

/**
 * Run all test in ast package
 * 
 * @author "Frank Zechert, Danny Maasch"
 * @version 1
 */
@RunWith(Suite.class)
@SuiteClasses({
		AddProgTest.class,
		SimpleAddTest.class,
		SimpleMulTest.class,
		CastingFactoryTest.class })
public class TestSuite {
}
