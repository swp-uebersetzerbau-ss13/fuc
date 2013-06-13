package swp_compiler_ss13.fuc.test;

import java.io.*;
import java.util.List;
import java.util.Map;

import junit.extensions.PA;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Backend;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser;
import swp_compiler_ss13.fuc.backend.TACExecutor;
import swp_compiler_ss13.fuc.errorLog.LogEntry;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Runtime tests base class.
 * <p>
 * The runtime tests require a LLVM installation for executing the LLVM IR. All
 * tests are ignored if no <code>lli</code> is found.
 * </p>
 * 
 * @author Jens V. Fischer
 */
public abstract class TestBase {

	protected static Lexer lexer;
	protected static Parser parser;
	protected static SemanticAnalyser analyser;
	protected static IntermediateCodeGenerator irgen;
	protected static Backend backend;
	protected static ReportLogImpl errlog;
	protected static Logger logger = Logger.getLogger(TestBase.class);


	/*
	 * Check if lli is correctly installed.
	 */
	protected static boolean checkForLLIInstallation() {

		Level level = Logger.getRootLogger().getLevel();

		Logger.getRootLogger().setLevel(Level.FATAL);
		boolean hasLLI;
		try {
			PA.invokeMethod(TACExecutor.class, "tryToStartLLI()");
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

	protected void testProgCompilation(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		InputStream res = compile((String) prog[0]);
		assertTrue(res != null);
	}

	protected void testProgRuntime(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		TACExecutor.ExecutionResult res = compileAndExecute((String)prog[0]);
		assertEquals(prog[2], res.output);
		assertEquals(prog[1], res.exitCode);
	}

	protected void testProgHasError(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		List<LogEntry> logEntries = compileForError((String) prog[0]);
		assertTrue(logEntries.size()>0);
	}

	protected void testProgForErrorMsg(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		LogEntry logEntry = compileForError((String) prog[0]).get(0);
		assertEquals(prog[2], logEntry.toString());
	}

	protected List<LogEntry> compileForError(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		if (errlog.hasErrors()){
			return errlog.getErrors();
		}
		analyser.setReportLog(errlog);
		analyser.analyse(ast);
		return errlog.getEntries();
	}

	protected InputStream compile(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		List<Quadruple> tac = irgen.generateIntermediateCode(ast);
		Map<String, InputStream> targets = backend.generateTargetCode("", tac);
		return targets.get(".ll");
	}

	protected TACExecutor.ExecutionResult compileAndExecute(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		return TACExecutor.runIR(compile(prog));
	}

}
