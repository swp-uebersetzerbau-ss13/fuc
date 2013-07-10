package swp_compiler_ss13.fuc.test;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.test.ExampleProgs;

import java.io.IOException;

/**
 * <p>
 * Runtime tests for the additional examples.
 * </p>
 * <p>
 * The runtime tests check for results (return values and output) of the
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
public class AdditionalRuntimeTest extends TestBase {

	private static Logger logger = Logger.getLogger(AdditionalRuntimeTest.class);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.getRootLogger().setLevel(Level.ERROR);
		assumeLLVMInstallation();
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@Test
	public void testEmptyProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.emptyProg());
	}

	/* regression test against return bug */
	@Test
	public void testReturnProg() throws Exception {
		testProg(ExampleProgs.returnProg());
	}

	@Test
	public void testArrayProg1() throws Exception {
		testProg(ExampleProgs.arrayProg1());
	}

	@Test
	public void testArrayProg2() throws Exception {
		testProg(ExampleProgs.arrayProg2());
	}

	@Test
	public void testArrayProg3() throws Exception {
		testProg(ExampleProgs.arrayProg3());
	}

	@Test
	public void testNestedLoopsProg() throws Exception {
		testProg(ExampleProgs.nestedLoopsProg());
	}

	@Test
	public void testSimpleRecordProg() throws Exception {
		testProg(ExampleProgs.simpleRecordProg());
	}

	@Test
	public void testRecordProg() throws Exception {
		testProg(ExampleProgs.recordProg());
	}

	@Test
	public void testCalendarProg() throws Exception {
		testProg(ExampleProgs.calendarProg());
	}
	
}
