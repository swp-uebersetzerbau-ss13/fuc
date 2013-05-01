package swp_compiler_ss13.fuc.semantic_analyser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.common.parser.SymbolTable;

public class SemanticAnalyser {

	enum Attribute {
		/**
		 * num, basic, array,...
		 */
		TYPE,
		/**
		 * <code>"1"</code> - identifier is initialized, <br/>
		 * <code>"0"</code> - identifier is not initialized
		 */
		HAS_VALUE,
		/**
		 * name of identifier
		 */
		IDENTIFIER
	}

	private static final String IS_NOT_INITIALIZED = "0";
	private static final String IS_INITIALIZED = "1";
	private static final String NO_ATTRIBUTE_VALUE = "no Value";

	private final ReportLog _errorLog;
	private final Map<ASTNode, Map<Attribute, String>> attributes;
	private final Map<SymbolTable, Set<String>> initializations;

	public SemanticAnalyser(ReportLog errorLog) {
		attributes = new HashMap<>();
		initializations = new HashMap<>();
		_errorLog = errorLog;
	}

	public AST analyse(AST ast) {
		traverseAstNode(ast.getRootNode(), ast.getRootSymbolTable());
		return ast;
	}

	private void traverseAstNode(ASTNode node, SymbolTable table) {
		switch (node.getNodeType()) {
		case BasicIdentifierNode:
			handleNode((BasicIdentifierNode) node, table);
			break;
		case BreakNode:
			break;
		case LiteralNode:
			break;
		case ArithmeticUnaryExpressionNode:
			break;
		case ArrayIdentifierNode:
			break;
		case DeclarationNode:
			break;
		case LogicUnaryExpressionNode:
			break;
		case PrintNode:
			break;
		case ReturnNode:
			handleNode((ReturnNode) node, table);
			break;
		case StructIdentifierNode:
			break;
		case ArithmeticBinaryExpressionNode:
			break;
		case AssignmentNode:
			handleNode((AssignmentNode) node, table);
			break;
		case DoWhileNode:
			break;
		case LogicBinaryExpressionNode:
			break;
		case RelationExpressionNode:
			break;
		case WhileNode:
			break;
		case BranchNode:
			break;
		case BlockNode:
			break;

		default:
			_errorLog.reportError("", -1, -1, "unknown ASTNodeType");
			break;
		}
	}

	private void handleNode(AssignmentNode node, SymbolTable table) {
		traverseAstNode(node.getLeftValue(), table);
		traverseAstNode(node.getRightValue(), table);
		addIdentifier(table, getAttribute(node.getLeftValue(), Attribute.IDENTIFIER));
	}

	private void handleNode(BasicIdentifierNode node, SymbolTable table) {
		setAttribute(node, Attribute.IDENTIFIER, node.getIdentifier());
		setAttribute(node, Attribute.HAS_VALUE, isInitialized(getIdentifierSymboltable(table, node
				.getIdentifier()), node.getIdentifier()) ? IS_INITIALIZED : IS_NOT_INITIALIZED);
	}

	private void handleNode(ReturnNode node, SymbolTable table) {
		IdentifierNode identifier = node.getRightValue();
		traverseAstNode(identifier, table);
		switch (getAttribute(identifier, Attribute.HAS_VALUE)) {
		case IS_NOT_INITIALIZED:
			_errorLog.reportError("", -1, -1, "Variable"
					+ getAttribute(identifier, Attribute.IDENTIFIER) + " is not initialized");
			break;
		case IS_INITIALIZED:
			break;
		default:
			throw new IllegalStateException("child node has no initialization information");
		}
	}

	private boolean isInitialized(SymbolTable table, String identifier) {
		SymbolTable declarationTable = getIdentifierSymboltable(table, identifier);
		Set<String> identifiers = initializations.get(declarationTable);
		if (identifiers == null) {
			return false;
		}
		return identifiers.contains(identifier);
	}

	private SymbolTable getIdentifierSymboltable(SymbolTable leafTable, String identifier) {
		SymbolTable result = leafTable;
		// TODO check if isDeclared is already recursive
		while (result != null && !result.isDeclared(identifier)) {
			result = result.getParentSymbolTable();
		}
		return result;
	}

	private void addIdentifier(SymbolTable table, String identifier) {
		SymbolTable declarationTable = getIdentifierSymboltable(table, identifier);
		if (initializations.get(declarationTable) == null) {
			initializations.put(declarationTable, new HashSet<String>());
		}
		initializations.get(declarationTable).add(identifier);
	}

	private String getAttribute(ASTNode node, Attribute attribute) {
		Map<Attribute, String> nodeMap = attributes.get(node);
		if (nodeMap == null) {
			return NO_ATTRIBUTE_VALUE;
		}
		String value = nodeMap.get(attribute);
		return value == null ? NO_ATTRIBUTE_VALUE : value;
	}

	private void setAttribute(ASTNode node, Attribute attribute, String value) {
		if (attributes.get(node) == null) {
			attributes.put(node, new HashMap<Attribute, String>());
		}
		attributes.get(node).put(attribute, value);
	}
}
