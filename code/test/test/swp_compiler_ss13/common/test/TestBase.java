package swp_compiler_ss13.common.test;

import java.io.*;
import java.util.List;
import java.util.Map;

import junit.extensions.PA;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import org.junit.Assert;
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
		ReportLogImpl log = compileForError( (String) prog[0]);
		String msg = null;
		if (log.hasErrors())
			msg = "ReportLog Error (first only): " + log.getErrors().get(0).getMessage();
		assertFalse(msg, log.hasErrors());
		InputStream res = compile((String) prog[0]);
		assertTrue(res != null);
	}

	protected void testProgCompilationWOAnalyser(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		InputStream res = compileWOAnalyser((String) prog[0]);
		assertTrue(res != null);
	}

	protected void testProgRuntime(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		TACExecutor.ExecutionResult res = execute(compile( (String) prog[0]) );
		assertEquals(prog[1], res.exitCode);
		assertEquals(prog[2], res.output);
	}

	protected void testProgRuntimeWOAnalyser(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		TACExecutor.ExecutionResult res = execute(compileWOAnalyser( (String) prog[0]) );
		assertEquals(prog[1], res.exitCode);
		assertEquals(prog[2], res.output);
	}

	protected void testProgHasError(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		ReportLogImpl log = compileForError((String) prog[0]);
		assertTrue(log.hasErrors());
	}

	protected void testProgHasWarnings(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		ReportLogImpl log = compileForError((String) prog[0]);
		assertTrue(log.hasWarnings());
	}

	protected void testProgForErrorMsg(Object[] prog) throws BackendException, IntermediateCodeGeneratorException, IOException, InterruptedException {
		LogEntry logEntry = compileForError((String) prog[0]).getEntries().get(0);
		assertEquals(prog[2], logEntry.toString());
	}

	protected ReportLogImpl compileForError(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		if (errlog.hasErrors()){
			return errlog;
		}
		analyser.setReportLog(errlog);
		analyser.analyse(ast);
		return errlog;
	}

	protected InputStream compile(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		analyser.setReportLog(errlog);
		AST ast2 = analyser.analyse(ast);
		List<Quadruple> tac = irgen.generateIntermediateCode(ast2);
		Map<String, InputStream> targets = backend.generateTargetCode("prog", tac);
		return targets.get(targets.keySet().iterator().next());
	}

	protected InputStream compileWOAnalyser(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));
		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		List<Quadruple> tac = irgen.generateIntermediateCode(ast);
		Map<String, InputStream> targets = backend.generateTargetCode("", tac);
		return targets.get(targets.keySet().iterator().next());
	}

	protected TACExecutor.ExecutionResult execute(InputStream prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
		return TACExecutor.runIR(prog);
	}

}
