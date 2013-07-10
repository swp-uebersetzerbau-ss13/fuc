package swp_compiler_ss13.fuc.test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;
import org.junit.experimental.categories.Category;
import swp_compiler_ss13.common.test.ExampleProgs;

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

	@Category(IgnoredTest.class)
	@Test
	public void testNewtonProg() throws Exception {
		testProg(ExampleProgs.newtonProg());
	}
}
