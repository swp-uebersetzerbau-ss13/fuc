package swp_compiler_ss13.fuc.test.m2;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.semantic_analyser.SemanticAnalyser;
import swp_compiler_ss13.fuc.test.*;
import swp_compiler_ss13.fuc.test.Compiler;

/**
 * <p>
 * Compilations tests for the M2 examples. Tests, if the examples compile, that
 * doesn't necessarily imply correct results. See runtime tests for testing
 * correct exitcodes or output.
 * </p>
 * <p>
 * All example progs can be found in
 * {@link swp_compiler_ss13.common.test.ExampleProgs}.
 * </p>
 * 
 * @author Jens V. Fischer
 */
public class M2CompilationTest extends TestBase {

	private static Logger logger = Logger.getLogger(M2CompilationTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.getRootLogger().setLevel(Level.WARN);
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@Test
	public void testAssignmentProg() throws Exception {
		testProgCompilation(ExampleProgs.assignmentProg());
	}

	@Test
	public void testCondProg() throws Exception {
		testProgCompilation(ExampleProgs.condProg());
	}

	@Test
	public void testPrintProg() throws Exception {
		testProgCompilation(ExampleProgs.printProg());
	}

	/* regression test against return bug */
	@Test
	public void testReturnProg() throws Exception {
		testProgCompilation(ExampleProgs.returnProg());
	}

	@Test
	@Ignore
	public void testArrayProg1() throws Exception {
		testProgCompilation(ExampleProgs.arrayProg1());
	}

	@Test
	public void testArrayProg2() throws Exception {
		testProgCompilation(ExampleProgs.arrayProg2());
	}

	@Test
	@Ignore
	public void testArrayProg3() throws Exception {
		testProgCompilation(ExampleProgs.arrayProg3());
	}
}
