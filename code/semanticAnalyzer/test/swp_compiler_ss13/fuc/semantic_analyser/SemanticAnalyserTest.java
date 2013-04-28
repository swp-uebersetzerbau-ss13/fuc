package swp_compiler_ss13.fuc.semantic_analyser;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class SemanticAnalyserTest {

	public SemanticAnalyserTest() {
	}

	@BeforeClass
	public static void setUpClass() {
	}

	@AfterClass
	public static void tearDownClass() {
	}

	@Before
	public void setUp() {
	}

	@After
	public void tearDown() {
	}

	/**
	 * Test of analyse method, of class SemanticAnalyser.
	 */
	@Test
	public void testAnalyse() {
		System.out.println("analyse");
		swp_compiler_ss13.common.ast.AST ast = null;
		swp_compiler_ss13.common.parser.ReportLog log = null;
		SemanticAnalyser instance = new SemanticAnalyser(log);
		swp_compiler_ss13.common.ast.AST expResult = null;
		swp_compiler_ss13.common.ast.AST result = instance.analyse(ast);
		assertEquals(expResult, result);
		// TODO review the generated test code and remove the default call to fail.
		fail("The test case is a prototype.");
	}
}