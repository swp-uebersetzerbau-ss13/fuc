package swp_compiler_ss13.fuc.test.m1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.semantic_analyser.SemanticAnalyser;
import swp_compiler_ss13.fuc.test.*;
import swp_compiler_ss13.fuc.test.Compiler;

import java.io.IOException;

/**
 * <p>
 * Runtime tests for the M1 examples.
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
public class M1RuntimeTest extends TestBase {

	private static Logger logger = Logger.getLogger(M1RuntimeTest.class);


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.ERROR);

		 /* only run tests if lli (dynamic compiler from LLVM) is found */
		Assume.assumeTrue(checkForLLIInstallation());
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}

	@Test
	public void testSimpleAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgRuntime(ExampleProgs.simpleAddProg());
	}

	@Test
	public void testAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgRuntime(ExampleProgs.addProg());
	}

	@Test
	public void testSimpleMulProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgRuntime(ExampleProgs.simpleMulProg());
	}

	@Test
	public void testParenthesesProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgRuntime(ExampleProgs.parenthesesProg());
	}

	@Test
	public void testEmptyProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProgRuntime(ExampleProgs.emptyProg());
	}

}
