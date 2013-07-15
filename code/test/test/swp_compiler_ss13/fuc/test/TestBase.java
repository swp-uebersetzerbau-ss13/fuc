package swp_compiler_ss13.fuc.test;

import junit.extensions.PA;
import org.apache.log4j.Logger;
import org.junit.Assume;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.test.Program;
import swp_compiler_ss13.fuc.backend.LLVMExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

/**
 * Base class for tests, providing methods used in the integration tests.
 * <p>
 * <p>
 * All integration test are runtime tests. The example programmes are compiled through
 * all compiler stages. The tests assert, that the the compilation either
 * produces expected errors or compiles through, producing some kind of target
 * code. The resulting target code is then executed and the result of the
 * execution is checked against the expected output (`print` statements) and
 * exit code (`return` statement).
 * </p>
 * The tests require a LLVM installation for executing the LLVM IR. All
 * tests are ignored if no <code>lli</code> is found.
 * </p>
 * 
 * @author Jens V. Fischer
 */
public abstract class TestBase {

	protected static Compiler compiler;
	protected static Logger logger = Logger.getLogger(TestBase.class);


	protected void testProg(Program prog) throws BackendException, IntermediateCodeGeneratorException,
			IOException, InterruptedException {

		InputStream compilationResult = compiler.compile(prog.programCode);

		ReportLogImpl log = compiler.getReportLog();
		ReportType[] expectedReportTypes = prog.expecteReportTypes;

		/* test for expected report log errors from parser if program does not compile */
		if (compiler.errlogAfterParser.hasErrors()){
			String msg = "Error in Parser: Expected ReportLog entries: " + Arrays.deepToString(expectedReportTypes)
					+ ". Actual: " + log.getErrors().toString();
			assertArrayEquals(msg, prog.expecteReportTypes, compiler.getReportLogAfterParser().getErrors().toArray());
			return;
		}

		/* test for expected report log errors from analyzer if program does not compile */
		if (compiler.errlogAfterAnalyzer.hasErrors()){
			String msg = "Error in Analyzer: Expected ReportLog entries: " + Arrays.deepToString(expectedReportTypes)
					+ ". Actual: " + log.getErrors().toString();
			assertArrayEquals(msg, expectedReportTypes, compiler.getReportLogAfterAnalyzer().getErrors().toArray());
			return;
		}

		/* test for expected report log errors if program compiles */
		String msg = "Unexpected warnings after successfull compilation.\n Expected ReportLog entries: " + Arrays.deepToString(expectedReportTypes)
				+ ". Actual: " + log.getErrors().toString();
		assertArrayEquals(msg, expectedReportTypes, log.getErrors().toArray());

		LLVMExecutor.ExecutionResult executionResult = LLVMExecutor.runIR(compilationResult);
		/* If the exit code is 1, lli may have crashed because it got invalid LLVM IR code
		 * from the backend. In this case print lli's output for verbosity. */
		if(executionResult.exitCode == 1) {
			assertEquals(executionResult.output, prog.expectedExitcode, (Integer) executionResult.exitCode);
		}
		else {
			assertEquals(prog.expectedExitcode, (Integer) executionResult.exitCode);
		}
		assertEquals(prog.expectedOutput, executionResult.output);
	}

	protected static void assumeLLVMInstallation(){
		try {
			PA.invokeMethod(LLVMExecutor.class, "tryToStartLLI()");
		} catch (Exception e) {
			Assume.assumeNoException("No LLVM Installation found", e);
		}
	}

}
