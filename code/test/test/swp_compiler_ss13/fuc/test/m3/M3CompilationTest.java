package swp_compiler_ss13.fuc.test.m3;

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
 * Compilations tests for the M3 examples. Tests, if the examples compile, that
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
public class M3CompilationTest extends TestBase {

	private static Logger logger = Logger.getLogger(M3CompilationTest.class);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.WARN);
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@Test
	public void testFibProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.fibProg());
	}

	@Test
	public void testMatrixMultiplicationProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.matrixMultiplicationProg());
	}

	@Test
	public void testNewtonProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.newtonProg());
	}

}
