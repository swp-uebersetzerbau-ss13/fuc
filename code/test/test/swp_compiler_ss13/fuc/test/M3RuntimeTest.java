package swp_compiler_ss13.fuc.test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swp_compiler_ss13.common.test.ExampleProgs;

/**
 * <p>
 * Tests for the M3 examples.
 * </p>
 * <p>
 * The tests check for results (return values and output) of the
 * execution of the translated examples. The tests require a LLVM installation
 * for executing the LLVM IR. All tests are ignored if no <code>lli</code> is
 * found.
 * </p>
 * <p>
 * All example progs can be found in {@link ExampleProgs}.
 * </p>
 *
 * @author Jens V. Fischer
 */
public class M3RuntimeTest extends TestBase {

	private static Logger logger = Logger.getLogger(M3RuntimeTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.getRootLogger().setLevel(Level.ERROR);
		assumeLLVMInstallation();
	}

	@Before
	public void setUp() throws Exception {
		compiler = new swp_compiler_ss13.fuc.test.Compiler();
	}

	@Test
	public void testFibProg() throws Exception {
		testProg(ExampleProgs.fibProg());
	}

	@Test
	public void testMatrixMultiplicationProg() throws Exception {
		testProg(ExampleProgs.matrixMultiplicationProg());
	}

	@Test
	public void testNewtonProg() throws Exception {
		testProg(ExampleProgs.newtonProg());
	}
}
