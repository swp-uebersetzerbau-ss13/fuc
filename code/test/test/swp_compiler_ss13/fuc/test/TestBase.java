package swp_compiler_ss13.fuc.test;

import junit.extensions.PA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.fuc.backend.LLVMExecutor;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.Assert.*;

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

		String msg = "Expected ReportLog entries: " + new ArrayList<ReportType>(Arrays.asList((ReportType[]) prog[3]))
				+ ". Actual: " + log.getEntries().toString();

		/* test for expected report log entries (errors and warnings) if program does not compile */
		if (log.hasErrors()){
			assertArrayEquals("Compilation produces unexpected errors: " + msg, (Object[]) prog[3], log.getEntries().toArray());
			return null;
		}

		/* test for expected report log entries (i.e. warnings), if program compiles */
		assertArrayEquals("Compilation produces unexpected warnings: " + msg, (Object[]) prog[3], log.getEntries().toArray());

		/* assert that something was compiled*/
		assertTrue(compilationResult != null);

		return compilationResult;
	}

	protected void testProgRuntime(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		InputStream compilationResult = testProgCompilation(prog);
		LLVMExecutor.ExecutionResult executionResult = LLVMExecutor.runIR(compilationResult);
		assertEquals(prog[1], executionResult.exitCode);
		assertEquals(prog[2], executionResult.output);
	}

}
