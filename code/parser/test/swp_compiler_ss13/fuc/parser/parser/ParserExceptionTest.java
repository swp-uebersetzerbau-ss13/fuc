package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

public class ParserExceptionTest {
	static String test = "Test";
	static ParserException test1 = new ParserException(test);
	
	@BeforeClass
    public static void setUpEarly() {			 
		test1.addReportLogMessage(test);
		test1.addReportLogText(test);
		
	}

	 

	@Test
	public final void testGetReportLogMessage() {
		assertTrue(test==test1.getReportLogMessage());
	}
	 

	@Test
	public final void testGetReportLogText() {
		assertTrue(test==test1.getReportLogText());
	}

}
