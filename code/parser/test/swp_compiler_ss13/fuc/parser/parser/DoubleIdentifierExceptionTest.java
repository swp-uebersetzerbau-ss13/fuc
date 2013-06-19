package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parser.DoubleIdentifierException;
import swp_compiler_ss13.fuc.parser.parser.ParserException;

public class DoubleIdentifierExceptionTest {
	
	static String test = "Test";
	static DoubleIdentifierException test1 = new DoubleIdentifierException(test);
	
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
