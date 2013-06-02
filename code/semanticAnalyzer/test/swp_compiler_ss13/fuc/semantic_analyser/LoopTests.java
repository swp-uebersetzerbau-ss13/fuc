package swp_compiler_ss13.fuc.semantic_analyser;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;

public class LoopTests {

	private SemanticAnalyser analyzer;
	private ReportLogImpl log;
	
	public LoopTests() {
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
	public void testWhileConditionTypeError(){
		// TODO
	}
	
	@Test
	public void testDoWhileConditionTypeError(){
		// TODO
	}
	
	@Test
	public void testInnerBreak(){
		// TODO
	}
	
	@Test
	public void testBreakOtusideLoopError(){
		// TODO
	}
}
