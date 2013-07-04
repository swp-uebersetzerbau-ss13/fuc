package swp_compiler_ss13.fuc.test.additional;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.test.Compiler;
import swp_compiler_ss13.fuc.test.TestBase;

import java.io.IOException;

/**
 * <p>
 * Compilations tests for the additional examples. Tests, if the examples compile, that
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
public class AdditionalCompilationTest extends TestBase {

	private static Logger logger = Logger.getLogger(AdditionalCompilationTest.class);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.WARN);
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@Test
	public void testEmptyProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgCompilation(ExampleProgs.emptyProg());
	}

	/* regression test against return bug */
	@Test
	public void testReturnProg() throws Exception {
		testProgCompilation(ExampleProgs.returnProg());
	}

	@Test
	public void testArrayProg1() throws Exception {
		testProgCompilation(ExampleProgs.arrayProg1());
	}

	@Test
	public void testArrayProg2() throws Exception {
		testProgCompilation(ExampleProgs.arrayProg2());
	}

	@Test
	public void testArrayProg3() throws Exception {
		testProgCompilation(ExampleProgs.arrayProg3());
	}
	@Ignore("Array of records not working")
	@Test
	public void testCalendarProg() throws Exception {
		testProgCompilation(ExampleProgs.calendarProg());
	}

}
