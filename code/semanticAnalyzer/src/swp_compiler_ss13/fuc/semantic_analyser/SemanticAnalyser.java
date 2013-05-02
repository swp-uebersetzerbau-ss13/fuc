package swp_compiler_ss13.fuc.semantic_analyser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
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
		 * <code>"1"</code> - identifier is initialized,<br/>
		 * <code>"0"</code> - identifier is not initialized
		 * 
		 * @see ExpressionNode
		 */
		INITIALIZATION_STATUS,
		/**
		 * name of identifier
		 * 
		 * @see IdentifierNode
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

	protected void traverseAstNode(ASTNode node, SymbolTable table) {
		switch (node.getNodeType()) {
		case BasicIdentifierNode:
			handleNode((BasicIdentifierNode) node, table);
			break;
		case BreakNode:
			break;
		case LiteralNode:
			handleNode((LiteralNode) node, table);
			break;
		case ArithmeticUnaryExpressionNode:
			handleNode((ArithmeticUnaryExpressionNode) node, table);
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
			handleNode((ArithmeticBinaryExpressionNode) node, table);
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
			handleNode((BlockNode) node);
			break;

		default:
			_errorLog.reportError("", -1, -1, "unknown ASTNodeType");
			break;
		}
	}

	protected void handleNode(LiteralNode node, SymbolTable table) {
		setAttribute(node, Attribute.INITIALIZATION_STATUS, IS_INITIALIZED);
	}

	protected void handleNode(ArithmeticBinaryExpressionNode node, SymbolTable table) {
		ExpressionNode expression = node.getLeftValue();
		traverseAstNode(expression, table);
		checkInitialization(expression);
		expression = node.getRightValue();
		traverseAstNode(expression, table);
		checkInitialization(expression);
		setAttribute(node, Attribute.INITIALIZATION_STATUS, IS_INITIALIZED);
	}

	protected void handleNode(ArithmeticUnaryExpressionNode node, SymbolTable table) {
		ExpressionNode expression = node.getRightValue();
		traverseAstNode(expression, table);
		checkInitialization(expression);
		setAttribute(node, Attribute.INITIALIZATION_STATUS, IS_INITIALIZED);
	}

	protected void handleNode(BlockNode node) {
		SymbolTable newTable = node.getSymbolTable();
		for (StatementNode child : node.getStatementList()) {
			traverseAstNode(child, newTable);
		}
	}

	protected void handleNode(AssignmentNode node, SymbolTable table) {
		traverseAstNode(node.getLeftValue(), table);
		traverseAstNode(node.getRightValue(), table);
		addIdentifier(table, getAttribute(node.getLeftValue(), Attribute.IDENTIFIER));
	}

	protected void handleNode(BasicIdentifierNode node, SymbolTable table) {
		setAttribute(node, Attribute.IDENTIFIER, node.getIdentifier());
		setAttribute(
				node,
				Attribute.INITIALIZATION_STATUS,
				isInitialized(getIdentifierSymboltable(table, node.getIdentifier()), node.getIdentifier()) ? IS_INITIALIZED
						: IS_NOT_INITIALIZED);
	}

	protected void handleNode(ReturnNode node, SymbolTable table) {
		IdentifierNode identifier = node.getRightValue();
		traverseAstNode(identifier, table);
		checkInitialization(identifier);
	}

	private void checkInitialization(ExpressionNode identifier) {
		switch (getAttribute(identifier, Attribute.INITIALIZATION_STATUS)) {
		case IS_NOT_INITIALIZED:
			_errorLog.reportError("", -1, -1, "Variable" + getAttribute(identifier, Attribute.IDENTIFIER)
					+ " is not initialized");
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

	protected SymbolTable getIdentifierSymboltable(SymbolTable childTable, String identifier) {
		SymbolTable parentTable = childTable;
		while (!parentTable.isDeclared(identifier)) {
			parentTable = parentTable.getParentSymbolTable();
		}
		return parentTable;
	}

	protected void addIdentifier(SymbolTable table, String identifier) {
		SymbolTable declarationTable = getIdentifierSymboltable(table, identifier);
		if (initializations.get(declarationTable) == null) {
			initializations.put(declarationTable, new HashSet<String>());
		}
		initializations.get(declarationTable).add(identifier);
	}

	protected String getAttribute(ASTNode node, Attribute attribute) {
		Map<Attribute, String> nodeMap = attributes.get(node);
		if (nodeMap == null) {
			return NO_ATTRIBUTE_VALUE;
		}
		String value = nodeMap.get(attribute);
		return value == null ? NO_ATTRIBUTE_VALUE : value;
	}

	protected void setAttribute(ASTNode node, Attribute attribute, String value) {
		if (attributes.get(node) == null) {
			attributes.put(node, new HashMap<Attribute, String>());
		}
		attributes.get(node).put(attribute, value);
	}
}
