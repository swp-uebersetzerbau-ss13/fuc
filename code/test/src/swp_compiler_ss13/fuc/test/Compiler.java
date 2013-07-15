package swp_compiler_ss13.fuc.test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.semantic_analyser.SemanticAnalyser;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Map;

/**
 * Compiler
 */
public class Compiler {
	private LexerImpl lexer;
	private ParserImpl parser;
	private SemanticAnalyser analyser;
	private IntermediateCodeGeneratorImpl irgen;
	private LLVMBackend backend;
	private ReportLogImpl errlog;
	protected ReportLogImpl errlogAfterParser;
	protected ReportLogImpl errlogAfterAnalyzer;

	/**
	 * Initializes a compiler
	 */
	public Compiler() {
		this.lexer = new LexerImpl();
		this.parser = new ParserImpl();
		this.analyser = new SemanticAnalyser();
		this.irgen = new IntermediateCodeGeneratorImpl();
		this.backend = new LLVMBackend();
	}

	/**
	 * Compiles a programme.
	 *
	 * @param prog the programme to compile
	 * @return the compilation results (target language code)
	 * @throws java.io.UnsupportedEncodingException if not utf-8
	 * @throws IntermediateCodeGeneratorException if an error occurs in the Intermediate Code Generator
	 * @throws BackendException if an error occurs in the Backend
	 */
	protected InputStream compile(String prog) throws BackendException,
			IntermediateCodeGeneratorException, InterruptedException, UnsupportedEncodingException {
		errlog = new ReportLogImpl();

		lexer.setSourceStream(new ByteArrayInputStream(prog.getBytes("UTF-8")));

		parser.setLexer(lexer);
		parser.setReportLog(errlog);
		AST ast = parser.getParsedAST();
		errlogAfterParser = errlog.clone();
		if (errlog.hasErrors())
			return null;

		analyser.setReportLog(errlog);
		AST ast2 = analyser.analyse(ast);
		errlogAfterAnalyzer = errlog.clone();
		if (errlog.hasErrors())
			return null;

		List<Quadruple> tac = irgen.generateIntermediateCode(ast2);

		Map<String, InputStream> targets = backend.generateTargetCode("prog", tac);

		return targets.get(targets.keySet().iterator().next());
	}

	/**
	 * Get the ReportLog
	 * @return the ReportLog
	 */
	public ReportLogImpl getReportLog() {
		return errlog;
	}

	/**
	 * Get a clone of the ReportLog representing the state of the ReportLog after the parser.
	 * @return the ReportLog after the parser
	 */
	public ReportLogImpl getReportLogAfterParser() {
		return errlogAfterParser;
	}

	/**
	 * Get a clone of the ReportLog representing the state of the ReportLog after the semantic analyzer.
	 * @return the ReportLog after the semantic analyzer
	 */
	public ReportLogImpl getReportLogAfterAnalyzer() {
		return errlogAfterAnalyzer;
	}

}
