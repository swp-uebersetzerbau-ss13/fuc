package m1;

import static org.junit.Assert.assertEquals;

import java.io.*;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import junit.extensions.PA;
import swp_compiler_ss13.common.lexer.LexerImpl;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
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
public class M1RuntimeTest {

	private static Lexer lexer;
	private static Parser parser;
	private static IntermediateCodeGenerator irgen;
	private static Backend backend;
	private static ReportLogImpl errlog;
	private static Logger logger = Logger.getLogger(M1RuntimeTest.class);

	private String prog;
	private int expectedExitCode;

	public M1RuntimeTest(String progName, String prog, int expectedExitCode) {
		this.prog = prog;
		this.expectedExitCode = expectedExitCode;
	}

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		 /* only run tests if lli (dynamic compiler from LLVM) is found */
		Assume.assumeTrue(checkForLLIInstallation());
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

	@Before
	public void setUp() throws Exception {
		lexer = new LexerImpl();
		parser = new ParserImpl();
		irgen = new IntermediateCodeGeneratorImpl();
		backend = new LLVMBackend();
		errlog = new ReportLogImpl();
	}



	@Test
	public void runtimeTest() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		TACExecutor.ExecutionResult res = compileAndExecute(this.prog);
		assertEquals(this.expectedExitCode, res.exitCode);
	}


	/*
	 * Check if lli is correctly installed.
	 */
	private static boolean checkForLLIInstallation() {

		Level level = Logger.getRootLogger().getLevel();

		Logger.getRootLogger().setLevel(Level.FATAL);
		boolean hasLLI;
		try {
			PA.invokeMethod(TACExecutor.class, "tryToStartLLI()");
			hasLLI = true;
		} catch (Exception e) {
			hasLLI = false;
		}

		Logger.getRootLogger().setLevel(level);

		if (!hasLLI) {
			logger.warn("Runtime tests are ignored, because of missing LLVM lli installation.");
			String infoMsg = "If you have LLVM installed you might need to check your $PATH: " +
					"Intellij IDEA: Run -> Edit Configurations -> Environment variables; " +
					"Eclipse: Run Configurations -> Environment; " +
					"Shell: Check $PATH";
			logger.info(infoMsg);
		}
		return hasLLI;
	}

	private TACExecutor.ExecutionResult compileAndExecute(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		List<Quadruple> tac = irgen.generateIntermediateCode(ast);
		Map<String, InputStream> targets = backend.generateTargetCode("", tac);
		InputStream irCode = targets.get(".ll");
		return TACExecutor.runIR(irCode);
	}
}
