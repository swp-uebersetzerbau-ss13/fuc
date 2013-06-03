package swp_compiler_ss13.fuc.test.m2;

import lexer.LexerImpl;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assume;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.backend.TACExecutor;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;
import swp_compiler_ss13.fuc.test.base.RuntimeTestBase;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

/**
 * Runtime tests for the M2 examples. The runtime tests check for results
 * (return values and output) of the execution of the translated examples. The
 * tests require a LLVM installation for executing the LLVM IR. All tests are
 * ignored if no <code>lli</code> is found.
 * 
 * @author Jens V. Fischer
 */
@RunWith(value = Parameterized.class)
public class M2RuntimeTestBase extends RuntimeTestBase {

	private static boolean m2Implemented = true;

	private static Lexer lexer;
	private static Parser parser;
	private static IntermediateCodeGenerator irgen;
	private static Backend backend;
	private static ReportLogImpl errlog;
	private static Logger logger = Logger.getLogger(M2RuntimeTestBase.class);

	private String prog;
	private int expectedExitcode;
	private String expectedOutput;

	public M2RuntimeTestBase(String progName, String prog, int expectedExitcode, String expectedOutput) {
		this.prog = prog;
		this.expectedExitcode = expectedExitcode;
		this.expectedOutput = expectedOutput;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.INFO);

		if(!m2Implemented) {
			logger.warn("M2RuntimeTests are ignored, because m2Implemented is set to false.");
		}

		/* only run tests if m2Implemented is set to true */
		Assume.assumeTrue(m2Implemented);

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
		String assignmentProg = "" +
				"# returns 10\n" +
				"# prints nothing\n" +
				"long a;\n" +
				"long b;\n" +
				"long c;\n" +
				"\n" +
				"a = 4;\n" +
				"b = 3;\n" +
				"c = 2;\n" +
				"\n" +
				"a = b = 4;\n" +
				"c = a + b + c;\n" +
				"\n" +
				"return c;";
		String condProg = "" +
				"# return 5\n" +
				"# prints nothing\n" +
				"\n" +
				"bool b;\n" +
				"bool c;\n" +
				"long l;\n" +
				"\n" +
				"b = true;\n" +
				"c = false;\n" +
				"\n" +
				"l = 4;\n" +
				"\n" +
				"# dangling-else should be resolved as given by indentation\n" +
				"\n" +
				"if ( b )\n" +
				"  if ( c || ! b )\n" +
				"    print \"bla\";\n" +
				"  else\n" +
				"    l = 5;\n" +
				"\n" +
				"return l;";
		String printProg = "" +
				"# return 0\n" +
				"# prints:\n" +
				"# true\n" +
				"# 18121313223\n" +
				"# -2.323e-99\n" +
				"# jagÄrEttString\"\n" +
				"\n" +
				"long l;\n" +
				"double d;\n" +
				"string s;\n" +
				"bool b;\n" +
				"\n" +
				"b = true;\n" +
				"l = 18121313223;\n" +
				"d = -23.23e-100;\n" +
				"s = \"jagÄrEttString\\\"\\n\";  # c-like escaping in strings\n" +
				"\n" +
				"print b; print \"\\n\";\n" +
				"print l; print \"\\n\";       # print one digit left of the radix point\n" +
				"print d; print \"\\n\";\n" +
				"print s;\n" +
				"\n" +
				"return;                    # equivalent to return EXIT_SUCCESS";

		return Arrays.asList(new Object[][] {
				/* mask: {testName, progCode, expectedExitCode, expectedOutput} */
				{ "assignmentProg", assignmentProg, 10, ""},
				{ "condProg", condProg, 5, ""},
				{ "printProg", printProg, 0,
						"true\n" +
						"18121313223\n" +
						"-2.323e-99\n" +
						"jagÄrEttString\"" }});
	}

	@Test
	public void runtimeTest() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		TACExecutor.ExecutionResult res = compileAndExecute(this.prog);
		assertEquals(this.expectedExitcode, res.exitCode);
		assertEquals(this.expectedOutput, res.output);
	}

}