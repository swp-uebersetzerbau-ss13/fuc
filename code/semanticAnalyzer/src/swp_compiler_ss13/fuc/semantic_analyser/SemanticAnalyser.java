package swp_compiler_ss13.fuc.semantic_analyser;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.parser.ReportLog;

public class SemanticAnalyser {

	private ReportLog _errorLog;

	public SemanticAnalyser(ReportLog errorLog) {
		_errorLog = errorLog;
	}

	public AST analyse(AST ast) {
		return ast;
	}
}
