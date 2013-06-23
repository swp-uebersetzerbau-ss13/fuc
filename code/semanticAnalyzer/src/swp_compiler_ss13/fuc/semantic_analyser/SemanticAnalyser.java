package swp_compiler_ss13.fuc.semantic_analyser;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.LoopNode;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;

public class SemanticAnalyser implements swp_compiler_ss13.common.semanticAnalysis.SemanticAnalyser {

	private static Logger logger = Logger.getLogger(SemanticAnalyser.class);

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
		IDENTIFIER,
		CAN_BREAK,
		TYPE_CHECK,
		CODE_STATE
	}
	private static final String IS_INITIALIZED = "1";
	private static final String NO_ATTRIBUTE_VALUE = "undefined";
	private static final String CAN_BREAK = "true";
	private static final String TYPE_MISMATCH = "type mismatch";
	private static final String DEAD_CODE = "dead";
	private ReportLog errorLog;
	private Map<ASTNode, Map<Attribute, String>> attributes;
	/**
	 * Contains all initialized identifiers. As soon it has assigned it will be
	 * added.
	 */
	private Map<SymbolTable, Set<String>> initializedIdentifiers;

	public SemanticAnalyser() {
		this.attributes = new HashMap<>();
		this.initializedIdentifiers = new HashMap<>();
	}

	public SemanticAnalyser(ReportLog log) {
		this.attributes = new HashMap<>();
		this.initializedIdentifiers = new HashMap<>();
		this.errorLog = log;
	}

	protected Map<SymbolTable, Set<String>> copy(Map<SymbolTable, Set<String>> m) {
		Map<SymbolTable, Set<String>> c = new HashMap<>(m);

		for (SymbolTable x : m.keySet()) {
			Set<String> i = new HashSet<>();

			for (String y : m.get(x)) {
				i.add(y);
			}

			c.put(x, i);
		}

		return c;
	}

	@Override
	public void setReportLog(ReportLog log) {
		errorLog = log;
	}

	@Override
	public AST analyse(AST ast) {
		assert (errorLog != null);

		attributes.clear();
		initializedIdentifiers.clear();

		logger.debug("Analyzing ... please stand by!");
		traverse(ast.getRootNode(), ast.getRootSymbolTable());

		return ast;
	}

	protected void traverse(ASTNode node, SymbolTable table) {
		logger.debug("traverse: " + node);

		switch (node.getNodeType()) {
			case BasicIdentifierNode:
				handleNode((BasicIdentifierNode) node, table);
				break;
			case BreakNode:
				handleNode((BreakNode) node, table);
				break;
			case LiteralNode:
				handleNode((LiteralNode) node, table);
				break;
			case ArithmeticUnaryExpressionNode:
				handleNode((ArithmeticUnaryExpressionNode) node, table);
				break;
			case ArrayIdentifierNode:
				handleNode((ArrayIdentifierNode) node, table);
				break;
			case DeclarationNode:
				break;
			case LogicUnaryExpressionNode:
				handleNode((LogicUnaryExpressionNode) node, table);
				break;
			case PrintNode:
				handleNode((PrintNode) node, table);
				break;
			case ReturnNode:
				this.handleNode((ReturnNode) node, table);
				break;
			case StructIdentifierNode:
				break;
			case ArithmeticBinaryExpressionNode:
				this.handleNode((ArithmeticBinaryExpressionNode) node, table);
				break;
			case AssignmentNode:
				this.handleNode((AssignmentNode) node, table);
				break;
			case DoWhileNode:
				handleNode((DoWhileNode) node, table);
				break;
			case LogicBinaryExpressionNode:
				handleNode((LogicBinaryExpressionNode) node, table);
				break;
			case RelationExpressionNode:
				handleNode((RelationExpressionNode) node, table);
				break;
			case WhileNode:
				handleNode((WhileNode) node, table);
				break;
			case BranchNode:
				handleNode((BranchNode) node, table);
				break;
			case BlockNode:
				this.handleNode((BlockNode) node, table);
				break;
			default:
				throw new IllegalArgumentException("unknown ASTNodeType");
		}
	}

	protected Type.Kind getType(ASTNode node) {
		String attr = getAttribute(node, Attribute.TYPE);
		if (!attr.equals(NO_ATTRIBUTE_VALUE)) {
			return Type.Kind.valueOf(attr);
		} else {
			return null;
		}
	}

	protected Type.Kind leastUpperBoundType(ASTNode left, ASTNode right) {
		TreeSet<Type.Kind> types = new TreeSet<>();
		Type.Kind leftType = getType(left);
		Type.Kind rightType = getType(right);
		
		if (leftType == null || rightType == null) {
			return null;
		}
		
		types.add(leftType);
		types.add(rightType);

		if (types.size() == 1) {
			return types.first();
		}

		if (types.contains(Type.Kind.LONG) && types.contains(Type.Kind.DOUBLE)) {
			return Type.Kind.DOUBLE;
		}

		return null;
	}

	/**
	 * Check if k is in (LONG, DOUBLE).
	 *
	 * @param k
	 * @return
	 */
	protected boolean isNumeric(Type.Kind k) {
		return k == Type.Kind.DOUBLE || k == Type.Kind.LONG;
	}

	protected boolean isBool(Type.Kind k) {
		return k == Type.Kind.BOOLEAN;
	}

	/*
	 * Loops
	 */
	protected void checkLoopNode(LoopNode node, SymbolTable table) {
		if (!hasAttribute(node.getCondition(), Attribute.TYPE, Type.Kind.BOOLEAN.name())) {
			errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(), "The condition must be of type bool.");
		}
	}

	protected void handleNode(DoWhileNode node, SymbolTable table) {
		setAttribute(node, Attribute.CAN_BREAK, CAN_BREAK);
		traverse(node.getLoopBody(), table);
		traverse(node.getCondition(), table);

		checkLoopNode(node, table);
	}

	protected void handleNode(WhileNode node, SymbolTable table) {
		setAttribute(node, Attribute.CAN_BREAK, CAN_BREAK);
		traverse(node.getCondition(), table);

		/*
		 * Ignore all initialization after leaving the body's scope,
		 * because the body may not be entered. But inside the body
		 * the initializations are valid.
		 * TODO: Better checking!
		 */
		Map<SymbolTable, Set<String>> before = copy(initializedIdentifiers);
		traverse(node.getLoopBody(), table);
		initializedIdentifiers = before;

		checkLoopNode(node, table);
	}

	protected void handleNode(BreakNode node, SymbolTable table) {
		if (!hasAttribute(node.getParentNode(), Attribute.CAN_BREAK, CAN_BREAK)) {
			errorLog.reportError(ReportType.UNDEFINED, node.coverage(), "Break can only be used in a loop.");
		}
	}

	/*
	 * Branch node
	 */
	protected List<SymbolTable> getSymbolTableChain(SymbolTable table) {
		List<SymbolTable> chain = new LinkedList<>();

		do {
			chain.add(table);
			table = table.getParentSymbolTable();
		} while (table != null);

		return chain;
	}

	protected void handleNode(BranchNode node, SymbolTable table) {
		traverse(node.getCondition(), table);

		Map<SymbolTable, Set<String>> beforeTrue = copy(initializedIdentifiers);
		List<SymbolTable> beforeTrueTableChain = getSymbolTableChain(table);
		traverse(node.getStatementNodeOnTrue(), table);

		if (node.getStatementNodeOnFalse() != null) {
			Map<SymbolTable, Set<String>> beforeFalse = copy(initializedIdentifiers);
			initializedIdentifiers = beforeTrue;
			traverse(node.getStatementNodeOnFalse(), table);

			/*
			 * Remove all initializations that do not occur in both branches.
			 */
			for (SymbolTable s : beforeTrueTableChain) {
				if (initializedIdentifiers.containsKey(s)) {
					/*
					 * initializedIdentifiers contains the state after the
					 * else branch, beforeFalse contains the state after
					 * the true branch.
					 * 
					 * Remove all from else, that were not also assigent
					 * in the true branch.
					 */
					logger.debug(s + ": f:" + initializedIdentifiers.get(s) + ",  t:" + beforeFalse.get(s));
					initializedIdentifiers.get(s).retainAll(beforeFalse.get(s));
				}
			}
		} else {
			logger.debug("No else branch, reset initialization.");
			initializedIdentifiers = beforeTrue;
		}

		if (!hasAttribute(node.getCondition(), Attribute.TYPE, Type.Kind.BOOLEAN.name())) {
			errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(), "The condition must be of type bool.");
		}
	}

	/*
	 * Unary and binary expressions.
	 */
	protected void binaryExpression(BinaryExpressionNode node, SymbolTable table) {
		/*
		 * Left sub tree
		 */
		ExpressionNode left = node.getLeftValue();
		traverse(left, table);

		/*
		 * right sub tree
		 */
		ExpressionNode right = node.getRightValue();
		traverse(right, table);

		Type.Kind type = leastUpperBoundType(left, right);

		if (hasAttribute(left, Attribute.TYPE_CHECK, TYPE_MISMATCH)
			|| hasAttribute(right, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
		} else {
			if (type == null) {
				setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"No implicit cast (no upper bound) defined for " + getType(left) + " and " + getType(right) + ".");
			} else {
				setAttribute(node, Attribute.TYPE, type.name());
			}
		}
	}

	protected void unaryExpression(UnaryExpressionNode node, SymbolTable table) {
		ExpressionNode expression = node.getRightValue();
		traverse(expression, table);

		if (hasAttribute(expression, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
		} else {
			setAttribute(node, Attribute.TYPE, getAttribute(expression, Attribute.TYPE));
		}
	}

	/*
	 * Arithmetic
	 */
	protected void handleNode(ArithmeticBinaryExpressionNode node, SymbolTable table) {
		binaryExpression(node, table);

		if (!hasAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			Type.Kind type = getType(node);

			if (!isNumeric(type) /*|| (type == Type.Kind.STRING && node.getOperator() == BinaryExpressionNode.BinaryOperator.ADDITION)*/) {
				setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"Operator " + node.getOperator().name() + " is not defined for " + getType(node) + ".");
			}
		}
	}

	protected void handleNode(ArithmeticUnaryExpressionNode node, SymbolTable table) {
		unaryExpression(node, table);

		if (hasAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			if (!isNumeric(getType(node))) {
				setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"Operation " + node.getOperator() + " is not defined for " + getType(node) + ".");
			}
		}
	}

	protected void handleNode(RelationExpressionNode node, SymbolTable table) {
		binaryExpression(node, table);

		if (!hasAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			Type.Kind type = getType(node);

			BinaryExpressionNode.BinaryOperator op = node.getOperator();

			if (!isNumeric(type) && (op != BinaryExpressionNode.BinaryOperator.EQUAL || op != BinaryExpressionNode.BinaryOperator.INEQUAL)) {
				setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"Operator " + node.getOperator() + " expects numeric operands.");
			} else {
				/*
				 * A ReleationExpression results in a boolean.
				 */
				setAttribute(node, Attribute.TYPE, Type.Kind.BOOLEAN.name());
			}
		}
	}

	/*
	 * Bool
	 */
	protected void handleNode(LogicBinaryExpressionNode node, SymbolTable table) {
		binaryExpression(node, table);

		if (!hasAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			if (!isBool(getType(node))) {
				setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"Operator " + node.getOperator() + " expects boolean operands");
			}
		}
	}

	protected void handleNode(LogicUnaryExpressionNode node, SymbolTable table) {
		unaryExpression(node, table);

		if (!hasAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			if (!isBool(getType(node))) {
				setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"Operator " + node.getOperator() + " expects boolean operand.");
			}
		}
	}

	/*
	 * Block
	 */
	protected void handleNode(BlockNode node, SymbolTable table) {
		if (hasAttribute(node.getParentNode(), Attribute.CAN_BREAK, CAN_BREAK)) {
			setAttribute(node, Attribute.CAN_BREAK, CAN_BREAK);
		}

		SymbolTable blockScope = node.getSymbolTable();

		for (StatementNode child : node.getStatementList()) {
			if (hasAttribute(node, Attribute.CODE_STATE, DEAD_CODE)) {
				errorLog.reportError(ReportType.UNDEFINED, child.coverage(),
					"Unreachable statement, see previous “return” in block.");
			}

			this.traverse(child, blockScope);
		}
	}

	protected void handleNode(AssignmentNode node, SymbolTable table) {
		traverse(node.getLeftValue(), table);
		traverse(node.getRightValue(), table);

		if (hasAttribute(node.getLeftValue(), Attribute.TYPE_CHECK, TYPE_MISMATCH)
			|| hasAttribute(node.getRightValue(), Attribute.TYPE_CHECK, TYPE_MISMATCH)) {
			setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
		} else if (getType(node.getLeftValue())
			!= leastUpperBoundType(node.getLeftValue(), node.getRightValue())) {

			setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
			errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
				"Expected " + getType(node.getLeftValue())
				+ " found " + getType(node.getRightValue()));
		} else {
			markIdentifierAsInitialized(table, getAttribute(node.getLeftValue(), Attribute.IDENTIFIER));
			setAttribute(node, Attribute.TYPE, getAttribute(node.getLeftValue(), Attribute.TYPE));

			logger.debug("Assignment: " + getAttribute(node.getLeftValue(), Attribute.IDENTIFIER)
				+ ":" + getType(node.getLeftValue()) + " := " + getType(node.getRightValue()));
		}
	}

	/*
	 * Leaf nodes
	 */
	protected void handleNode(LiteralNode node, SymbolTable table) {
		setAttribute(node, Attribute.INITIALIZATION_STATUS, IS_INITIALIZED);
		setAttribute(node, Attribute.TYPE, node.getLiteralType().getKind().name());
	}

	protected void handleNode(BasicIdentifierNode node, SymbolTable table) {
		String identifier = node.getIdentifier();
		Type t = table.lookupType(node.getIdentifier());
		boolean initialzed = isInitialized(table, identifier);

		if (t == null) {
			setAttribute(node, Attribute.TYPE_CHECK, TYPE_MISMATCH);
			errorLog.reportError(ReportType.UNDECLARED_VARIABLE_USAGE, node.coverage(),
				"Identifier “" + identifier + "” has not been declared.");

			return;
		}
		
		boolean array = t.getKind() == Type.Kind.ARRAY;
		
		if (array) {
			t = ((ArrayType)t).getInnerType();
		}
		
		logger.debug("BasicIdentifierNode: identifier=" + identifier + ", initialized=" + initialzed + ", array=" + array + ", type=" + t);
		
		

		setAttribute(node, Attribute.IDENTIFIER, identifier);
		setAttribute(node, Attribute.TYPE, t.getKind().name());

		/*
		 * checks
		 */
		boolean reportInitialization = false;

		if (node.getParentNode() instanceof AssignmentNode) {
			AssignmentNode p = (AssignmentNode) node.getParentNode();
			reportInitialization = p.getLeftValue() != node;
		} else if (node.getParentNode().getNodeType() != ASTNode.ASTNodeType.AssignmentNode && !array) {
			reportInitialization = true;
		}

		if (reportInitialization && !initialzed) {
			errorLog.reportWarning(ReportType.UNDEFINED, node.coverage(),
				"Variable “" + identifier + "” may be used without initialization.");
		}
	}
	
	protected void handleNode(ArrayIdentifierNode node, SymbolTable table) {
		traverse(node.getIdentifierNode(), table);
		setAttribute(node, Attribute.TYPE, getAttribute(node.getIdentifierNode(), Attribute.TYPE));
	}

	protected void handleNode(ReturnNode node, SymbolTable table) {
		IdentifierNode identifier = node.getRightValue();

		if (identifier != null) {
			traverse(identifier, table);

			if (!getAttribute(identifier, Attribute.TYPE).equals(Type.Kind.LONG.name())) {
				errorLog.reportError(ReportType.TYPE_MISMATCH, node.coverage(),
					"Only variables of type long can be returned.");
			}
		}

		setAttribute(node.getParentNode(), Attribute.CODE_STATE, DEAD_CODE);
	}
	
	protected void handleNode(PrintNode node, SymbolTable table) {
		IdentifierNode identifier = node.getRightValue();
		traverse(identifier, table);
	}

	private boolean isInitialized(SymbolTable table, String identifier) {
		SymbolTable declarationTable = table.getDeclaringSymbolTable(identifier);
		Set<String> identifiers = this.initializedIdentifiers.get(declarationTable);

		return identifiers != null && identifiers.contains(identifier);
	}

	protected void markIdentifierAsInitialized(SymbolTable table, String identifier) {
		SymbolTable declarationTable = table.getDeclaringSymbolTable(identifier);

		if (!initializedIdentifiers.containsKey(declarationTable)) {
			initializedIdentifiers.put(declarationTable, new HashSet<String>());
		}

		initializedIdentifiers.get(declarationTable).add(identifier);
	}

	protected String getAttribute(ASTNode node, Attribute attribute) {
		Map<Attribute, String> nodeMap = this.attributes.get(node);
		if (nodeMap == null) {
			return NO_ATTRIBUTE_VALUE;
		}
		String value = nodeMap.get(attribute);
		return value == null ? NO_ATTRIBUTE_VALUE : value;
	}

	protected void setAttribute(ASTNode node, Attribute attribute, String value) {
		if (!this.attributes.containsKey(node)) {
			this.attributes.put(node, new HashMap<Attribute, String>());
		}
		this.attributes.get(node).put(attribute, value);
	}

	protected boolean hasAttribute(ASTNode node, Attribute attribute, String value) {
		return getAttribute(node, attribute).equals(value);
	}
}
