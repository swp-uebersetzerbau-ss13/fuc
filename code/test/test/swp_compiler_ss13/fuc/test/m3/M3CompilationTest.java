package swp_compiler_ss13.fuc.test.m3;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.test.TestBase;

@Ignore
public class M3CompilationTest extends TestBase {

	private static Logger logger = Logger.getLogger(M3CompilationTest.class);

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
		testProgCompilation(ExampleProgs.fibProg());
	}

	@Test
	public void testMatrixMultiplicationProg() throws Exception {
		testProgCompilation(ExampleProgs.matrixMultiplicationProg());
	}

	@Test
	public void testNewtonProg() throws Exception {
		testProgCompilation(ExampleProgs.newtonProg());
	}
}
