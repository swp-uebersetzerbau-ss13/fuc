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
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class Compiler {
	private LexerImpl lexer;
	private ParserImpl parser;
	private SemanticAnalyser analyser;
	private IntermediateCodeGeneratorImpl irgen;
	private LLVMBackend backend;
	private ReportLogImpl errlog;
	protected ReportLogImpl errlogAfterParser;
	protected ReportLogImpl errlogAfterAnalyzer;

	public Compiler() {
		this.lexer = new LexerImpl();
		this.parser = new ParserImpl();
		this.analyser = new SemanticAnalyser();
		this.irgen = new IntermediateCodeGeneratorImpl();
		this.backend = new LLVMBackend();
	}

	public Compiler(LexerImpl lexer, ParserImpl parser, SemanticAnalyser analyser, IntermediateCodeGeneratorImpl irgen, LLVMBackend backend) {
		this.lexer = lexer;
		this.parser = parser;
		this.analyser = analyser;
		this.irgen = irgen;
		this.backend = backend;
	}

	protected InputStream compile(String prog) throws BackendException,
			IntermediateCodeGeneratorException, IOException, InterruptedException {
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

	public ReportLogImpl getErrlog() {
		return errlog;
	}

	public ReportLogImpl getErrlogAfterParser() {
		return errlogAfterParser;
	}

	public ReportLogImpl getErrlogAfterAnalyzer() {
		return errlogAfterAnalyzer;
	}

}
