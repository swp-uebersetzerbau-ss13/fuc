package swp_compiler_ss13.fuc.semantic_analyser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;

public class OtherTests {

	private SemanticAnalyser analyzer;
	private ReportLogImpl log;
	
	public OtherTests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyzer = new SemanticAnalyser(this.log);
	}

	@After
	public void tearDown() {
		analyzer = null;
		log = null;
	}
	
	@Test
	public void testStatmentAfterReturnError(){
		// TODO
	}
	
	@Test
	public void testReturnTypeError(){
		// TODO
	}
	
	@Test
	public void testMultipleErrors(){
		// TODO
	}
}
