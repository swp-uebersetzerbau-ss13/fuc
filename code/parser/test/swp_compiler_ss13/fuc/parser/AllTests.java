package swp_compiler_ss13.fuc.parser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ALRGeneratorTest.class, DoubleIdentifierExceptionTest.class,
		  M1AddTest.class, M1ErrorDoubleDeclTest.class,
		M1ErrorInvalidIdsTest.class, M1MultipleMinusENotationTest.class,
		M1MultiplePlusesInExpTest.class, M1ParathesesTest.class,
		M1SimpleAddTest.class, M1SimpleMulTest.class, M1UndefReturnTest.class,
		ParserExceptionTest.class,  ReduceTest.class,
		ShiftTest.class, TokenExTest.class })
public class AllTests {

}
