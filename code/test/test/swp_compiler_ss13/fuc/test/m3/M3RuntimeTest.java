package swp_compiler_ss13.fuc.test.m3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.test.*;

@Ignore
public class M3RuntimeTest extends TestBase {

	private static Logger logger = Logger.getLogger(M3RuntimeTest.class);

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.ERROR);

		/* only run tests if lli (dynamic compiler from LLVM) is found */
		Assume.assumeTrue(checkForLLIInstallation());
	}

	@Before
	public void setUp() throws Exception {
		compiler = new swp_compiler_ss13.fuc.test.Compiler();
	}

	@Test
	public void testFibProg() throws Exception {
		testProgRuntime(ExampleProgs.fibProg());
	}

	@Test
	public void testMatrixMultiplicationProg() throws Exception {
		testProgRuntime(ExampleProgs.matrixMultiplicationProg());
	}

	@Test
	public void testNewtonProg() throws Exception {
		testProgRuntime(ExampleProgs.newtonProg());
	}
}
