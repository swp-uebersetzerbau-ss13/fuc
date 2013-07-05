package swp_compiler_ss13.fuc.test;

import junit.extensions.PA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Assume;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.fuc.backend.LLVMExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;
import static org.junit.Assert.assertArrayEquals;

/**
 * Base class for tests, providing methods used in the integration tests.
 * <p>
 * The runtime tests require a LLVM installation for executing the LLVM IR. All
 * tests are ignored if no <code>lli</code> is found.
 * </p>
 * 
 * @author Jens V. Fischer
 */
public abstract class TestBase {

	protected static Compiler compiler;
	protected static Logger logger = Logger.getLogger(TestBase.class);


	/*
	 * Check if lli is correctly installed.
	 */
	protected static boolean checkForLLIInstallation() {

		Level level = Logger.getRootLogger().getLevel();

		Logger.getRootLogger().setLevel(Level.FATAL);
		boolean hasLLI;
		try {
			PA.invokeMethod(LLVMExecutor.class, "tryToStartLLI()");
			hasLLI = true;
		} catch (Exception e) {
			hasLLI = false;
		}

		Logger.getRootLogger().setLevel(Level.INFO);

		if (!hasLLI) {
			logger.warn("Runtime tests are ignored, because of missing LLVM lli installation.");
			String infoMsg = "If you have LLVM installed you might need to check your $PATH: "
					+ "Intellij IDEA: Run -> Edit Configurations -> Environment variables; "
					+ "Eclipse: Run Configurations -> Environment; " + "Shell: Check $PATH";
			logger.info(infoMsg);
		}
		Logger.getRootLogger().setLevel(level);

		return hasLLI;
	}

	protected InputStream testProgCompilation(Object[] prog) throws BackendException, IntermediateCodeGeneratorException,
			IOException, InterruptedException {

		InputStream compilationResult = compiler.compile((String) prog[0]);

		ReportLogImpl log = compiler.getErrlog();
		ReportType[] expectedReportTypes = (ReportType[]) prog[3];

		/* test for expected report log entries from parser if program does not compile */
		if (compiler.errlogAfterParser.hasErrors()){
			String msg = "Error in Parser: Expected ReportLog entries: " + Arrays.deepToString(expectedReportTypes)
					+ ". Actual: " + log.getEntries().toString();
			assertArrayEquals(msg, (Object[]) prog[3], compiler.getErrlogAfterParser().getEntries().toArray());
			return null;
		}

		/* test for expected report log entries from analyzer if program does not compile */
		if (compiler.errlogAfterAnalyzer.hasErrors()){
			String msg = "Error in Analyzer: Expected ReportLog entries: " + Arrays.deepToString(expectedReportTypes)
					+ ". Actual: " + log.getEntries().toString();
			assertArrayEquals(msg, expectedReportTypes, compiler.getErrlogAfterAnalyzer().getEntries().toArray());
			return null;
		}

		/* test for expected report log entries (i.e. warnings), if program compiles */
		String msg = "Unexpected warnings after successfull compilation.\n Expected ReportLog entries: " + Arrays.deepToString(expectedReportTypes)
				+ ". Actual: " + log.getEntries().toString();
		assertArrayEquals(msg, expectedReportTypes, log.getEntries().toArray());

		return compilationResult;

	}

	protected void testProgRuntime(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		InputStream compilationResult = testProgCompilation(prog);
		LLVMExecutor.ExecutionResult executionResult = LLVMExecutor.runIR(compilationResult);
		assertEquals(prog[1], executionResult.exitCode);
		assertEquals(prog[2], executionResult.output);
	}

}
