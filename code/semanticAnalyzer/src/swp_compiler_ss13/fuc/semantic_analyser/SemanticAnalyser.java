package swp_compiler_ss13.fuc.semantic_analyser;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.parser.ReportLog;

public class SemanticAnalyser {

	enum Attribute {
		/**
		 * num, basic, array,...
		 */
		TYPE
	}

	private ReportLog _errorLog;
	private Map<ASTNode, Map<Attribute, String>> attributes;

	public SemanticAnalyser(ReportLog errorLog) {
		attributes = new HashMap<>();
		_errorLog = errorLog;
	}

	public AST analyse(AST ast) {
		return ast;
	}

	private String getAttribute(ASTNode node, Attribute attribute) {
		return attributes.get(node).get(attribute);
	}

	private void setAttribute(ASTNode node, Attribute attribute, String value) {
		attributes.get(node).put(attribute, value);
	}
}
