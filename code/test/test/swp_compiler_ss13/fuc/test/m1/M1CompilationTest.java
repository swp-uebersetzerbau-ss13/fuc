package swp_compiler_ss13.fuc.test.m1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.test.Compiler;
import swp_compiler_ss13.fuc.test.TestBase;

import java.io.IOException;

/**
 * <p>
 * Compilations tests for the M1 examples. Tests, if the examples compile, that
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
public class M1CompilationTest extends TestBase {

	private static Logger logger = Logger.getLogger(M1CompilationTest.class);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.WARN);
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@Test
	public void testSimpleAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.simpleAddProg());
	}

	@Test
	public void testAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.addProg());
	}

	@Test
	public void testSimpleMulProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.simpleMulProg());
	}

	@Test
	public void testParenthesesProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.parenthesesProg());
	}

}
