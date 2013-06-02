package swp_compiler_ss13.fuc.test.m1;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;

import lexer.LexerImpl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.fuc.test.base.RuntimeTestBase;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.backend.TACExecutor;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;

/**
 * Runtime tests for the M1 examples. The runtime tests check for results
 * (return values) of the execution of the translated examples. The tests
 * require a LLVM installation for executing the LLVM IR. All tests are ignored
 * if no <code>lli</code> is found.
 * 
 * @author Jens V. Fischer
 */
@RunWith(value = Parameterized.class)
public class M1RuntimeTestBase extends RuntimeTestBase {

	private String prog;
	private int expectedExitCode;

	public M1RuntimeTestBase(String progName, String prog, int expectedExitCode) {
		this.prog = prog;
		this.expectedExitCode = expectedExitCode;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.INFO);

		 /* only run tests if lli (dynamic compiler from LLVM) is found */
		Assume.assumeTrue(checkForLLIInstallation());
	}

	@Before
	public void setUp() throws Exception {
		lexer = new LexerImpl();
		parser = new ParserImpl();
		irgen = new IntermediateCodeGeneratorImpl();
		backend = new LLVMBackend();
		errlog = new ReportLogImpl();
	}

	@Parameterized.Parameters(name = "{index}: {0}")
	public static Collection<Object[]> data() {
		String simpleAddProg = "" +
				"# returns 6\n" +
				"long l;\n" +
				"l = 3 + 3;\n" +
				"return l;";
		String addProg = "" +
				"# return 27\n" +
				"long l;\n" +
				"l = 10 +\n" +
				"        23 # - 23\n" +
				"- 23\n" +
				"+ 100 /\n" +
				"\n" + "2\n" +
				"-       30 \n" +
				"      - 9 / 3;\n" +
				"return l;";
		String simpleMulProg = "" +
				"# return 9\n" +
				"long l;\n" +
				"l = 3 * 3;\n" +
				"return l;";
		String parenthesesProg = "" +
				"# returns 8 or does it?\n" +
				"long l;\n" +
				"l = ( 3 + 3 ) * 2 - ( l = ( 2 + ( 16 / 8 ) ) );\n" +
				"return l;";
		return Arrays.asList(new Object[][]{
				/* mask: {testName, progCode, expectedExitCode} */
				{"simpleAddProg", simpleAddProg, 6},
				{"addProg", addProg, 27},
				{"simpleMulProg", simpleMulProg, 9},
				{"parenthesesProg", parenthesesProg, 8}
		});
	}

	@Test
	public void runtimeTest() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		TACExecutor.ExecutionResult res = compileAndExecute(this.prog);
		assertEquals(this.expectedExitCode, res.exitCode);
	}

}
