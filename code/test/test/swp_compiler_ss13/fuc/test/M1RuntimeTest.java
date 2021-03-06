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
 * Tests for the M1 examples.
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
public class M1RuntimeTest extends TestBase {

	private static Logger logger = Logger.getLogger(M1RuntimeTest.class);


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
	public void testSimpleAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.simpleAddProg());
	}

	@Test
	public void testAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.addProg());
	}

	@Test
	public void testSimpleMulProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.simpleMulProg());
	}

	@Test
	public void testParenthesesProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.parenthesesProg());
	}


	@Test
	public void testDoubleDeclaration() throws Exception {
		testProg(ExampleProgs.doubleDeclaration());
	}

	@Test
	public void testInvalidIds() throws Exception {
		testProg(ExampleProgs.invalidIds());
	}

	@Test
	public void testMultipleMinusENotation() throws Exception {
		testProg(ExampleProgs.multipleMinusENotation());
	}

	@Test
	public void testMultiplePlusesInExp() throws Exception {
		testProg(ExampleProgs.multiplePlusesInExp());
	}

	@Test
	public void testUndefReturn() throws Exception {
		testProg(ExampleProgs.undefReturnProg());
	}

}
