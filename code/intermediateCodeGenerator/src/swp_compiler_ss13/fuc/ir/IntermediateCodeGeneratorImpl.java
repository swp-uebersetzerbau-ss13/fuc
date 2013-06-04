package swp_compiler_ss13.fuc.ir;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.StructIdentifierNode;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;

/**
 * Create the intermediate code representation for the given AST
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class IntermediateCodeGeneratorImpl implements IntermediateCodeGenerator {

	/**
	 * The Log4J Logger
	 */
	private static Logger logger = Logger
			.getLogger(IntermediateCodeGeneratorImpl.class);

	/**
	 * Number of the next free label
	 */
	private static long labelNum = 0L;

	/**
	 * The generated intermediate code
	 */
	private List<Quadruple> irCode;

	/**
	 * List of used names. This is needed for single static assignment.
	 */
	private List<String> usedNames;

	/**
	 * The stack of symbol tables
	 */
	private Stack<SymbolTable> currentSymbolTable;

	/**
	 * Store for intermediate results
	 */
	private Stack<IntermediateResult> intermediateResults;

	/**
	 * is used to store the level of the outermost array index
	 */
	Integer arrayLevel = null;

	/**
	 * when processing an arrayIdentifier it needs to be known if it is used in
	 * an assignment or referenced
	 */
	Boolean arrayAssignment = false;

	/**
	 * Reset the intermediate code generator. This is called first for every
	 * generateIntermediateCode() to ensure that severeal calls to
	 * generateIntermediateCode do not interfere.
	 */
	private void reset() {

		logger.trace("Resetting the intermediate code generator.");

		irCode = new LinkedList<>();
		usedNames = new LinkedList<>();
		currentSymbolTable = new Stack<>();
		intermediateResults = new Stack<>();
	}

	@Override
	public List<Quadruple> generateIntermediateCode(AST ast)
			throws IntermediateCodeGeneratorException {
		if (ast == null) {
			logger.fatal("The argument ast in generateIntermediateCode(AST ast) can not be null!");
			throw new IntermediateCodeGeneratorException(
					"The argument ast in generateIntermediateCode(AST ast) can not be null!");
		}

		ASTNode program = ast.getRootNode();
		if (program == null) {
			logger.fatal("The ast given in generateIntermediateCode(AST ast) must have a root node!");
			throw new IntermediateCodeGeneratorException(
					"The ast given in generateIntermediateCode(AST ast) must have a root node");
		}
		reset();
		callProcessing(program);
		return irCode;
	}

	/**
	 * call the method that handles the node in the AST
	 * 
	 * @param node
	 *            The node to handle
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred
	 */
	private void callProcessing(ASTNode node)
			throws IntermediateCodeGeneratorException {
		logger.debug("Processing next node: " + node.getNodeType().toString()
				+ " with " + (node.getNumberOfNodes() - 1) + " subnodes");
		switch (node.getNodeType()) {
		case ArithmeticBinaryExpressionNode:
			processArithmeticBinaryExpressionNode((ArithmeticBinaryExpressionNode) node);
			break;
		case ArithmeticUnaryExpressionNode:
			processArithmeticUnaryExpressionNode((ArithmeticUnaryExpressionNode) node);
			break;
		case ArrayIdentifierNode:
			processArrayIdentifierNode((ArrayIdentifierNode) node);
			break;
		case AssignmentNode:
			processAssignmentNode((AssignmentNode) node);
			break;
		case BasicIdentifierNode:
			processBasicIdentifierNode((BasicIdentifierNode) node);
			break;
		case BlockNode:
			processBlockNode((BlockNode) node);
			break;
		case BranchNode:
			processBranchNode((BranchNode) node);
			break;
		case BreakNode:
			processBreakNode((BreakNode) node);
			break;
		case DeclarationNode:
			processDeclarationNode((DeclarationNode) node);
			break;
		case DoWhileNode:
			processDoWhileNode((DoWhileNode) node);
			break;
		case LiteralNode:
			processLiteralNode((LiteralNode) node);
			break;
		case LogicBinaryExpressionNode:
			processLogicBinaryExpressionNode((LogicBinaryExpressionNode) node);
			break;
		case LogicUnaryExpressionNode:
			processLogicUnaryExpressionNode((LogicUnaryExpressionNode) node);
			break;
		case PrintNode:
			processPrintNode((PrintNode) node);
			break;
		case ReturnNode:
			processReturnNode((ReturnNode) node);
			break;
		case StructIdentifierNode:
			processStructIdentifierNode((StructIdentifierNode) node);
			break;
		case WhileNode:
			processWhileNode((WhileNode) node);
			break;
		default:
			throw new IntermediateCodeGeneratorException("Unknown node type: "
					+ node.getNodeType().toString());
		}
	}

	/**
	 * Process a block node and generate the needed IR Code
	 * 
	 * @param node
	 *            The block node to process
	 * @throws IntermediateCodeGeneratorException
	 *             an error occurred while process the node
	 */
	private void processBlockNode(BlockNode node)
			throws IntermediateCodeGeneratorException {
		// push current symbol table
		currentSymbolTable.push(node.getSymbolTable());

		// get declarations
		Iterator<DeclarationNode> declIterator = node.getDeclarationIterator();
		while (declIterator.hasNext()) {
			callProcessing(declIterator.next());
		}

		// get statements
		Iterator<StatementNode> statementIterator = node.getStatementIterator();
		while (statementIterator.hasNext()) {
			StatementNode statement = statementIterator.next();
			callProcessing(statement);
		}

		// pop the symbol scope
		currentSymbolTable.pop();
	}

	/**
	 * Process a while node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processWhileNode(WhileNode node)
			throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(
				new UnsupportedOperationException());
	}

	/**
	 * Process a structidentifier node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processStructIdentifierNode(StructIdentifierNode node)
			throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(
				new UnsupportedOperationException());
	}

	/**
	 * Process a return node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processReturnNode(ReturnNode node)
			throws IntermediateCodeGeneratorException {
		IdentifierNode right = node.getRightValue();
		if (right != null) {
			callProcessing(right);
			IntermediateResult intermediateResult = intermediateResults.pop();
			irCode.add(QuadrupleFactory.returnNode(intermediateResult
					.getValue()));
		} else {
			irCode.add(QuadrupleFactory.returnNode("0"));
		}

	}

	/**
	 * Process a print node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processPrintNode(PrintNode node)
			throws IntermediateCodeGeneratorException {
		callProcessing(node.getRightValue());
		IntermediateResult result = intermediateResults.pop();
		irCode.add(QuadrupleFactory.print(result.getValue(), result.getType()));
	}

	/**
	 * Process a logicunaryexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processLogicUnaryExpressionNode(LogicUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		callProcessing(node.getRightValue());
		IntermediateResult result = intermediateResults.pop();

		if (result.getType().getKind() != Kind.BOOLEAN) {
			String err = "Unary logic operation NOT is not supported for type "
					+ result.getType();
			logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String temp = createAndSaveTemporaryIdentifier(new BooleanType());
		irCode.add(QuadrupleFactory.unaryNot(result.getValue(), temp));
		intermediateResults
				.push(new IntermediateResult(temp, new BooleanType()));
	}

	/**
	 * Process a logicbinaryexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processLogicBinaryExpressionNode(LogicBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		callProcessing(node.getLeftValue());
		callProcessing(node.getRightValue());

		IntermediateResult rightResult = intermediateResults.pop();
		IntermediateResult leftResult = intermediateResults.pop();

		if (leftResult.getType().getKind() != Kind.BOOLEAN
				|| rightResult.getType().getKind() != Kind.BOOLEAN) {
			String err = "Binary logic operation is not supported for "
					+ leftResult.getType() + " and " + rightResult.getType();
			logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String temp = createAndSaveTemporaryIdentifier(new BooleanType());
		irCode.add(QuadrupleFactory.booleanArithmetic(node.getOperator(),
				leftResult.getValue(), rightResult.getValue(), temp));
		intermediateResults
				.push(new IntermediateResult(temp, new BooleanType()));
	}

	/**
	 * Process a literal node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processLiteralNode(LiteralNode node)
			throws IntermediateCodeGeneratorException {
		String literal = node.getLiteral();
		Type type = node.getLiteralType();
		switch (type.getKind()) {
		case DOUBLE:
		case LONG:
		case BOOLEAN:
			// Literal of type Long or Double needs to start with a # to mark it
			// as a constant
			intermediateResults.push(new IntermediateResult("#"
					+ literal.toUpperCase(), type));
			break;
		case STRING:
			// Literal of type String needs to be in " and start with a #
			// Replace all " in the string with \"
			literal = escapeString(literal, "\"", "\\\"");
			literal = escapeString(literal, "\n", "\\n");
			literal = escapeString(literal, "\r", "\\r");
			literal = escapeString(literal, "\t", "\\t");
			literal = escapeString(literal, "\0", "\\0");
			intermediateResults.push(new IntermediateResult("#\"" + literal
					+ "\"", type));
			break;
		default:
			// Literal of other types are not defined yet.
			throw new IntermediateCodeGeneratorException(
					"Literal node of type " + node.getLiteralType().toString()
							+ " is not supported");
		}
	}

	/**
	 * Escape special char sequences but ignore them if they are already escaped
	 * 
	 * @param literal
	 *            The string to escape
	 * @param search
	 *            The char sequence to replace
	 * @param replace
	 *            The char sequence to use as replacement
	 * @return The escaped string
	 */
	private String escapeString(String literal, String search, String replace) {
		int fromIndex = 0;
		int pos = 0;
		while ((pos = literal.indexOf(search, fromIndex)) >= 0) {
			if (pos > 0 && literal.charAt(pos - 1) == '\\') {
				fromIndex = pos;
				continue;
			}
			fromIndex = pos + replace.length();
			if (pos > 0) {
				literal = literal.substring(0, pos) + replace
						+ literal.substring(pos + 1);
			} else {
				literal = replace + literal.substring(pos + 1);
			}
		}
		return literal;
	}

	/**
	 * Process a relationexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processDoWhileNode(DoWhileNode node)
			throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(
				new UnsupportedOperationException());
	}

	/**
	 * Process a declaration node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processDeclarationNode(DeclarationNode node)
			throws IntermediateCodeGeneratorException {
		String identifierName = node.getIdentifier();
		Type identifierType = node.getType();
		// save the new declared variable into our structures
		saveIdentifier(identifierName, identifierType);
	}

	/**
	 * Process a break node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processBreakNode(BreakNode node)
			throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(
				new UnsupportedOperationException());
	}

	/**
	 * Process a branch node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processBranchNode(BranchNode node)
			throws IntermediateCodeGeneratorException {
		ExpressionNode condition = node.getCondition();
		StatementNode onTrue = node.getStatementNodeOnTrue();
		StatementNode onFalse = node.getStatementNodeOnFalse();

		callProcessing(condition);
		IntermediateResult conditionResult = intermediateResults.pop();
		if (conditionResult.getType().getKind() != Kind.BOOLEAN) {
			String err = "A condition is not of type boolean but of unsupported type "
					+ conditionResult.getType();
			logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String trueLabel = createNewLabel();
		String falseLabel = createNewLabel();
		String endLabel = createNewLabel();

		irCode.add(QuadrupleFactory.branch(conditionResult.getValue(),
				trueLabel, falseLabel));
		irCode.add(QuadrupleFactory.label(trueLabel));
		callProcessing(onTrue);
		irCode.add(QuadrupleFactory.jump(endLabel));
		irCode.add(QuadrupleFactory.label(falseLabel));

		if (onFalse != null) {
			callProcessing(onFalse);
		}

		irCode.add(QuadrupleFactory.label(endLabel));

	}

	/**
	 * Process a basicidentifier node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processBasicIdentifierNode(BasicIdentifierNode node)
			throws IntermediateCodeGeneratorException {
		// A basic identifier can be pushed to the stack of results immediately
		String identifier = node.getIdentifier();
		Type identifierType = currentSymbolTable.peek().lookupType(identifier);

		// arrays get assigned with their full type but we want the base type in
		// the center of it
		while (identifierType instanceof ArrayType) {
			identifierType = ((ArrayType) identifierType).getInnerType();
		}

		String actualIdentifier = loadIdentifier(identifier);
		intermediateResults.push(new IntermediateResult(actualIdentifier,
				identifierType));
	}

	/**
	 * Process a assignment node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processAssignmentNode(AssignmentNode node)
			throws IntermediateCodeGeneratorException {
		// the id to assign the value to
		IdentifierNode id = node.getLeftValue();

		arrayAssignment = true;
		arrayLevel = null;
		callProcessing(id);
		Integer toArrayIndex = arrayLevel;
		arrayLevel = null;

		IntermediateResult toIntermediate = intermediateResults.pop();

		// the id to assign the value to is a basic identifier.
		// no need to resolve an array or struct etc.
		StatementNode value = node.getRightValue();

		// process the right hand expression
		callProcessing(value);
		Integer fromArrayIndex = arrayLevel;
		arrayLevel = null;
		arrayAssignment = false;

		// the result of the right hand expression
		IntermediateResult fromIntermediate = intermediateResults.pop();

		// get the name of the id and resolve it from our saved structures
		String toId = toIntermediate.getValue();

		// the type of the id. If necessary the value of the right hand
		// expression
		// needs to be casted to this type.
		Type toType = toIntermediate.getType();

		// check if the cast is needed
		boolean castNeeded = CastingFactory.isCastNeeded(toType,
				fromIntermediate.getType());

		// get the name and id of the source
		String fromId = fromIntermediate.getValue();
		Type fromType = fromIntermediate.getType();

		if (fromArrayIndex != null && toArrayIndex != null) {
			// assign array to array
			String temporary = createAndSaveTemporaryIdentifier(fromType);
			irCode.addAll(QuadrupleFactory.assign(fromType, fromId, temporary,
					fromArrayIndex, null));
			if (castNeeded) {
				String temporary2 = createAndSaveTemporaryIdentifier(toType);
				irCode.add(CastingFactory.createCast(fromType, temporary,
						toType, temporary2));
				irCode.addAll(QuadrupleFactory.assign(toType, temporary2, toId,
						null, toArrayIndex));
				toId = temporary2;
			} else {
				irCode.addAll(QuadrupleFactory.assign(toType, temporary, toId,
						null, toArrayIndex));
				toId = temporary;
			}
		} else {
			if (castNeeded) {
				if (toArrayIndex == null) {
					// assign array to variable
					String temporary = createAndSaveTemporaryIdentifier(fromType);
					irCode.addAll(QuadrupleFactory.assign(fromType, fromId,
							temporary, fromArrayIndex, toArrayIndex));
					irCode.add(CastingFactory.createCast(fromType, temporary,
							toType, toId));
					toId = temporary;
				} else {
					// assign variable to array
					String temporary = createAndSaveTemporaryIdentifier(toType);
					irCode.add(CastingFactory.createCast(fromType, fromId,
							toType, temporary));
					irCode.addAll(QuadrupleFactory.assign(toType, temporary,
							toId, fromArrayIndex, toArrayIndex));
					toId = temporary;
				}
			} else {
				irCode.addAll(QuadrupleFactory.assign(toType, fromId, toId,
						fromArrayIndex, toArrayIndex));
			}
		}
		intermediateResults.push(new IntermediateResult(toId, toType));
	}

	/**
	 * Process a arrayidentifier node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processArrayIdentifierNode(ArrayIdentifierNode node)
			throws IntermediateCodeGeneratorException {

		IdentifierNode innerNode = node.getIdentifierNode();

		boolean outerArray = false;
		if (arrayLevel == null) {
			arrayLevel = node.getIndex();
			outerArray = true;
		}

		callProcessing(innerNode);

		// array assignments are special so don't push the reference of the
		// outermost array
		if (!arrayAssignment || !outerArray) {
			IntermediateResult innerResult = intermediateResults.pop();
			Type nodeType = innerResult.getType();
			String tmpIdentifier;

			if (outerArray) {
				tmpIdentifier = createAndSaveTemporaryIdentifier(nodeType);
			} else {
				tmpIdentifier = createAndSaveTemporaryIdentifierReference(nodeType);
			}
			currentSymbolTable.peek().lookupType(tmpIdentifier);

			// FIXME just looks at the outermost type so this works
			Type arrayType = new ArrayType(new StringType(0L), 0);
			if (outerArray) {
				arrayType = nodeType;
			}
			irCode.add(QuadrupleFactory.arrayGet(arrayType,
					innerResult.getValue(), "#" + node.getIndex(),
					tmpIdentifier));
			intermediateResults.push(new IntermediateResult(tmpIdentifier,
					innerResult.getType()));
		}
	}

	/**
	 * Process a arithmeticunaryexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processArithmeticUnaryExpressionNode(
			ArithmeticUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		ExpressionNode rightNode = node.getRightValue();

		// process the right hand value
		callProcessing(rightNode);

		IntermediateResult rightIntermediate = intermediateResults.pop();

		String temp = createAndSaveTemporaryIdentifier(rightIntermediate
				.getType());
		irCode.add(QuadrupleFactory.unaryMinus(rightIntermediate.getType(),
				rightIntermediate.getValue(), temp));
		intermediateResults.push(new IntermediateResult(temp, rightIntermediate
				.getType()));
	}

	/**
	 * Binary Arithmetic Operation
	 * 
	 * @param node
	 *            the arithmetic binary expression node
	 * @throws IntermediateCodeGeneratorException
	 *             something went wrong
	 */
	private void processArithmeticBinaryExpressionNode(
			ArithmeticBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {

		// process the left and right value first
		callProcessing(node.getLeftValue());
		callProcessing(node.getRightValue());

		// get the left and right value
		IntermediateResult right = intermediateResults.pop();
		IntermediateResult left = intermediateResults.pop();

		// check if a cast is needed
		boolean castNeeded = CastingFactory.isCastNeeded(right, left);

		if (!castNeeded) {
			// no cast is needed
			// either both values are of type LONG or of type DOUBLE
			if (left.getType() instanceof LongType) {
				// both values are of type LONG
				String temp = createAndSaveTemporaryIdentifier(new LongType());
				irCode.add(QuadrupleFactory.longArithmeticBinaryOperation(
						node.getOperator(), left.getValue(), right.getValue(),
						temp));

				intermediateResults.push(new IntermediateResult(temp,
						new LongType()));
			} else if (left.getType() instanceof DoubleType) {
				// both values are of type DOUBLE
				String temp = createAndSaveTemporaryIdentifier(new DoubleType());
				irCode.add(QuadrupleFactory.doubleArithmeticBinaryOperation(
						node.getOperator(), left.getValue(), right.getValue(),
						temp));

				intermediateResults.push(new IntermediateResult(temp,
						new DoubleType()));
			} else {
				// this is an unsupported combination of types
				String err = "Arithmetic Binary Expression with arguments of types %s and %s is not supported";
				String errf = String.format(err, left.getType().toString(),
						right.getType().toString());
				logger.fatal(errf);
				throw new IntermediateCodeGeneratorException(errf,
						new UnsupportedOperationException());
			}
		} else {
			// A cast is needed.
			// Only LONG and DOUBLE types are valid for arithmetic operations
			// We will always cast to type DOUBLE as it is more precise than
			// LONG
			// and nothing will be lost during the conversion
			if (left.getType() instanceof LongType) {
				// the left value is of type LONG, so it needs to be casted
				String temp = createAndSaveTemporaryIdentifier(new DoubleType());
				String temp2 = createAndSaveTemporaryIdentifier(new DoubleType());
				irCode.add(CastingFactory.createCast(left.getType(),
						left.getValue(), new DoubleType(), temp));
				irCode.add(QuadrupleFactory.doubleArithmeticBinaryOperation(
						node.getOperator(), temp, right.getValue(), temp2));

				intermediateResults.push(new IntermediateResult(temp2,
						new DoubleType()));
			} else if (right.getType() instanceof LongType) {
				// the right value is of type LONG, so it needs to be casted
				String temp = createAndSaveTemporaryIdentifier(new DoubleType());
				String temp2 = createAndSaveTemporaryIdentifier(new DoubleType());
				irCode.add(CastingFactory.createCast(right.getType(),
						right.getValue(), new DoubleType(), temp));
				irCode.add(QuadrupleFactory.doubleArithmeticBinaryOperation(
						node.getOperator(), left.getValue(), temp, temp2));

				intermediateResults.push(new IntermediateResult(temp2,
						new DoubleType()));
			} else {
				// this combinations of types is not supported
				String err = "Arithmetic Binary Expression with arguments of types %s and %s is not supported";
				String errf = String.format(err, left.getType().toString(),
						right.getType().toString());
				logger.fatal(errf);
				throw new IntermediateCodeGeneratorException(errf,
						new UnsupportedOperationException());
			}
		}

	}

	/**
	 * Save the given identifier to the interal store of variables
	 * 
	 * @param identifier
	 *            The name of the variable
	 * @param type
	 *            the type of the variable
	 * @return The renamed name of the variable that was stored
	 * @throws IntermediateCodeGeneratorException
	 *             an exception occurred
	 */
	private String saveIdentifier(String identifier, Type type)
			throws IntermediateCodeGeneratorException {
		if (!usedNames.contains(identifier)) {
			// an identifier with this name was not yet used.
			// it does not need to be renamed to stick to SSA
			usedNames.add(identifier);
			irCode.addAll(QuadrupleFactory.declaration(identifier, type));
			return identifier;
		} else {
			// rename is required to keep single static assignment
			String newName = currentSymbolTable.peek().getNextFreeTemporary();
			currentSymbolTable.peek().putTemporary(newName, type);
			usedNames.add(newName);
			currentSymbolTable.peek().setIdentifierAlias(identifier, newName);
			irCode.addAll(QuadrupleFactory.declaration(newName, type));
			return newName;
		}
	}

	/**
	 * Create a new temporary value and save it to the internal store of
	 * variables
	 * 
	 * @param type
	 *            The type of the new variable
	 * @return The name of the new variable
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred
	 */
	private String createAndSaveTemporaryIdentifier(Type type)
			throws IntermediateCodeGeneratorException {
		// Condition: getNextFreeTemporary never returns a name that was already
		// used in the IR until now
		// (globally unique names)
		String id = currentSymbolTable.peek().getNextFreeTemporary();
		currentSymbolTable.peek().putTemporary(id, type);
		usedNames.add(id);
		irCode.addAll(QuadrupleFactory.declaration(id, type));
		return id;
	}

	/**
	 * Create a new temporary variable reference and save it to the internal
	 * store of variables
	 * 
	 * @param type
	 *            The type of the new variable reference
	 * @return The name of the new variable
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred
	 */
	private String createAndSaveTemporaryIdentifierReference(Type type)
			throws IntermediateCodeGeneratorException {
		String id = currentSymbolTable.peek().getNextFreeTemporary();
		currentSymbolTable.peek().putTemporary(id, type);
		usedNames.add(id);
		irCode.add(new QuadrupleImpl(Operator.DECLARE_REFERENCE,
				Quadruple.EmptyArgument, Quadruple.EmptyArgument, id));
		return id;
	}

	/**
	 * Load the given identifier and return its actual name (if renaming was
	 * done)
	 * 
	 * @param id
	 *            The identifier name to load
	 * @return The actual name of the identifier
	 * @throws IntermediateCodeGeneratorException
	 *             Identifier was not found
	 */
	private String loadIdentifier(String id)
			throws IntermediateCodeGeneratorException {
		String name = currentSymbolTable.peek().getIdentifierAlias(id);
		if (name == null) {
			logger.fatal("Undeclared variable found: " + id);
			throw new IntermediateCodeGeneratorException(
					"Undeclared variable found: " + id);
		}
		return name;
	}

	/**
	 * Get a new free label name
	 * 
	 * @return The label name
	 */
	private String createNewLabel() {
		return "label" + (labelNum++);
	}
}
