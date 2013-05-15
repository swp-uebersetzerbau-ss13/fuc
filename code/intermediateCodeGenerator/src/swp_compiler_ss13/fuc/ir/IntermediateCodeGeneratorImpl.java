package swp_compiler_ss13.fuc.ir;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import org.apache.log4j.Logger;

import sun.reflect.generics.reflectiveObjects.NotImplementedException;
import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
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
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;

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
	private static Logger logger = Logger.getLogger(IntermediateCodeGeneratorImpl.class);

	/**
	 * The generated intermediate code
	 */
	private List<Quadruple> irCode;

	/**
	 * List of used names. This is needed for single static assignment.
	 */
	private List<String> usedNames;

	/**
	 * The stack of identifier renames
	 */
	private Stack<Map<String, String>> currentScopeRenames;

	/**
	 * The stack of symbol tables
	 */
	private Stack<SymbolTable> currentSymbolTable;

	/**
	 * Store for intermediate results
	 */
	private Stack<IntermediateResult> intermediateResults;

	/**
	 * Reset the intermediate code generator. This is called first for every
	 * generateIntermediateCode() to ensure that severeal calls to
	 * generateIntermediateCode do not interfere.
	 */
	private void reset() {

		logger.trace("Resetting the intermediate code generator.");

		this.irCode = new LinkedList<>();
		this.usedNames = new LinkedList<>();
		this.currentScopeRenames = new Stack<>();
		this.currentSymbolTable = new Stack<>();
		this.intermediateResults = new Stack<>();
	}

	@Override
	public List<Quadruple> generateIntermediateCode(AST ast) throws IntermediateCodeGeneratorException {
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
		this.reset();
		this.callProcessing(program);
		return this.irCode;
	}

	/**
	 * call the method that handles the node in the AST
	 * 
	 * @param node
	 *            The node to handle
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred
	 */
	private void callProcessing(ASTNode node) throws IntermediateCodeGeneratorException {
		logger.debug("Processing next node: " + node.getNodeType().toString() + " with "
				+ (node.getNumberOfNodes() - 1) + " subnodes");
		switch (node.getNodeType()) {
		case ArithmeticBinaryExpressionNode:
			this.processArithmeticBinaryExpressionNode((ArithmeticBinaryExpressionNode) node);
			break;
		case ArithmeticUnaryExpressionNode:
			this.processArithmeticUnaryExpressionNode((ArithmeticUnaryExpressionNode) node);
			break;
		case ArrayIdentifierNode:
			this.processArrayIdentifierNode((ArrayIdentifierNode) node);
			break;
		case AssignmentNode:
			this.processAssignmentNode((AssignmentNode) node);
			break;
		case BasicIdentifierNode:
			this.processBasicIdentifierNode((BasicIdentifierNode) node);
			break;
		case BlockNode:
			this.processBlockNode((BlockNode) node);
			break;
		case BranchNode:
			this.processBranchNode((BranchNode) node);
			break;
		case BreakNode:
			this.processBreakNode((BreakNode) node);
			break;
		case DeclarationNode:
			this.processDeclarationNode((DeclarationNode) node);
			break;
		case DoWhileNode:
			this.processDoWhileNode((DoWhileNode) node);
			break;
		case LiteralNode:
			this.processLiteralNode((LiteralNode) node);
			break;
		case LogicBinaryExpressionNode:
			this.processLogicBinaryExpressionNode((LogicBinaryExpressionNode) node);
			break;
		case LogicUnaryExpressionNode:
			this.processLogicUnaryExpressionNode((LogicUnaryExpressionNode) node);
			break;
		case PrintNode:
			this.processPrintNode((PrintNode) node);
			break;
		case RelationExpressionNode:
			this.processRelationExpressionNode((RelationExpressionNode) node);
			break;
		case ReturnNode:
			this.processReturnNode((ReturnNode) node);
			break;
		case StructIdentifierNode:
			this.processStructIdentifierNode((StructIdentifierNode) node);
			break;
		case WhileNode:
			this.processWhileNode((WhileNode) node);
			break;
		default:
			throw new IntermediateCodeGeneratorException("Unknown node type: " + node.getNodeType().toString());
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
	private void processBlockNode(BlockNode node) throws IntermediateCodeGeneratorException {
		// push new renaming scope
		this.currentScopeRenames.push(new HashMap<String, String>());
		// push current symbol table
		this.currentSymbolTable.push(node.getSymbolTable());

		// get declarations
		Iterator<DeclarationNode> declIterator = node.getDeclarationIterator();
		while (declIterator.hasNext()) {
			this.callProcessing(declIterator.next());
		}

		// get statements
		Iterator<StatementNode> statementIterator = node.getStatementIterator();
		while (statementIterator.hasNext()) {
			StatementNode statement = statementIterator.next();
			this.callProcessing(statement);
		}

		// pop the symbol scope and the renaming scope
		this.currentScopeRenames.pop();
		this.currentSymbolTable.pop();
	}

	/**
	 * Process a while node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processWhileNode(WhileNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a structidentifier node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processStructIdentifierNode(StructIdentifierNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a return node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processReturnNode(ReturnNode node) throws IntermediateCodeGeneratorException {
		IdentifierNode right = node.getRightValue();
		this.callProcessing(right);
		IntermediateResult intermediateResult = this.intermediateResults.pop();
		this.irCode.add(QuadrupleFactory.returnNode(intermediateResult.getValue()));
	}

	/**
	 * Process a relationexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processRelationExpressionNode(RelationExpressionNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a print node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processPrintNode(PrintNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
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
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
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
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a literal node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processLiteralNode(LiteralNode node) throws IntermediateCodeGeneratorException {
		String literal = node.getLiteral();
		Type type = node.getLiteralType();
		switch (type.getKind()) {
		case DOUBLE:
		case LONG:
			// Literal of type Long or Double needs to start with a # to mark it
			// as a constant
			this.intermediateResults.push(new IntermediateResult("#" + literal, type));
			break;
		case STRING:
			// Literal of type String needs to be in " and start with a #
			// Replace all " in the string with \"
			this.intermediateResults
					.push(new IntermediateResult("#\"" + literal.replaceAll("\"", "\\\"") + "\"", type));
			break;
		default:
			// Literal of other types are not defined yet.
			throw new IntermediateCodeGeneratorException("Literal node of type " + node.getLiteralType().toString()
					+ " is not supported");
		}
	}

	/**
	 * Process a relationexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processDoWhileNode(DoWhileNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a declaration node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processDeclarationNode(DeclarationNode node) throws IntermediateCodeGeneratorException {
		String identifierName = node.getIdentifier();
		Type identifierType = node.getType();
		// save the new declared variable into our structures
		this.saveIdentifier(identifierName, identifierType);
	}

	/**
	 * Process a break node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processBreakNode(BreakNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a branch node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processBranchNode(BranchNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a basicidentifier node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processBasicIdentifierNode(BasicIdentifierNode node) throws IntermediateCodeGeneratorException {
		// A basic identifier can be pushed to the stack of results immediately
		String identifier = node.getIdentifier();
		Type identifierType = this.currentSymbolTable.peek().lookupType(identifier);
		String actualIdentifier = this.loadIdentifier(identifier);
		this.intermediateResults.push(new IntermediateResult(actualIdentifier, identifierType));
	}

	/**
	 * Process a assignment node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processAssignmentNode(AssignmentNode node) throws IntermediateCodeGeneratorException {
		// the id to assign the value to
		IdentifierNode id = node.getLeftValue();

		this.callProcessing(id);

		IntermediateResult intermediateResult = this.intermediateResults.pop();

		// the id to assign the value to is a basic identifier.
		// no need to resolve an array or struct etc.
		StatementNode value = node.getRightValue();

		// process the right hand expression
		this.callProcessing(value);

		// the result of the right hand expression
		IntermediateResult rightIntermediate = this.intermediateResults.pop();

		// get the name of the id and resolve it from our saved structures
		String idRenamed = intermediateResult.getValue();

		// the type of the id. If necessary the value of the right hand
		// expression
		// needs to be casted to this type.
		Type typeOfId = intermediateResult.getType();

		// check if the cast is needed
		boolean castNeeded = CastingFactory.isCastNeeded(typeOfId, rightIntermediate.getType());

		if (castNeeded) {
			// if a cast is needed cast the right hand expression to the
			// type of the id
			String temporary = this.createAndSaveTemporaryIdentifier(typeOfId);
			Quadruple cast = CastingFactory.createCast(rightIntermediate.getType(), rightIntermediate.getValue(),
					typeOfId, temporary);
			this.irCode.add(cast);
			this.irCode.add(QuadrupleFactory.assign(typeOfId, temporary, idRenamed));
		}
		else {
			// no cast is needed,
			this.irCode.add(QuadrupleFactory.assign(typeOfId, rightIntermediate.getValue(), idRenamed));
		}

	}

	/**
	 * Process a arrayidentifier node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processArrayIdentifierNode(ArrayIdentifierNode node) throws IntermediateCodeGeneratorException {
		throw new IntermediateCodeGeneratorException(new NotImplementedException());
	}

	/**
	 * Process a arithmeticunaryexpression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	private void processArithmeticUnaryExpressionNode(ArithmeticUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		if (node.getOperator() != UnaryOperator.MINUS) {
			// No unary operations except for minus are supported by arithmetic
			// nodes
			throw new IntermediateCodeGeneratorException("Unsupported arithmetic unary operator");
		}
		ExpressionNode rightNode = node.getRightValue();

		// process the right hand value
		this.callProcessing(rightNode);

		IntermediateResult rightIntermediate = this.intermediateResults.pop();

		String temp = this.createAndSaveTemporaryIdentifier(rightIntermediate.getType());
		this.irCode.add(QuadrupleFactory.unaryMinus(rightIntermediate.getType(), rightIntermediate.getValue(), temp));
		this.intermediateResults.push(new IntermediateResult(temp, rightIntermediate.getType()));
	}

	/**
	 * Binary Arithmetic Operation
	 * 
	 * @param node
	 *            the arithmetic binary expression node
	 * @throws IntermediateCodeGeneratorException
	 *             something went wrong
	 */
	private void processArithmeticBinaryExpressionNode(ArithmeticBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {

		// process the left and right value first
		this.callProcessing(node.getLeftValue());
		this.callProcessing(node.getRightValue());

		// get the left and right value
		IntermediateResult right = this.intermediateResults.pop();
		IntermediateResult left = this.intermediateResults.pop();

		// check if a cast is needed
		boolean castNeeded = CastingFactory.isCastNeeded(right, left);

		if (!castNeeded) {
			// no cast is needed
			// either both values are of type LONG or of type DOUBLE
			if (left.getType() instanceof LongType) {
				// both values are of type LONG
				String temp = this.createAndSaveTemporaryIdentifier(new LongType());
				this.irCode.add(QuadrupleFactory.longArithmeticBinaryOperation(node.getOperator(), left.getValue(),
						right.getValue(), temp));

				this.intermediateResults.push(new IntermediateResult(temp, new LongType()));
			}
			else if (left.getType() instanceof DoubleType) {
				// both values are of type DOUBLE
				String temp = this.createAndSaveTemporaryIdentifier(new DoubleType());
				this.irCode.add(QuadrupleFactory.longArithmeticBinaryOperation(node.getOperator(), left.getValue(),
						right.getValue(), temp));

				this.intermediateResults.push(new IntermediateResult(temp, new DoubleType()));
			}
			else {
				// this is an unsupported combination of types
				String err = "Arithmetic Binary Expression with arguments of types %s and %s is not supported";
				String errf = String.format(err, left.getType().toString(), right.getType().toString());
				logger.fatal(errf);
				throw new IntermediateCodeGeneratorException(errf, new NotImplementedException());
			}
		}
		else {
			// A cast is needed.
			// Only LONG and DOUBLE types are valid for arithmetic operations
			// We will always cast to type DOUBLE as it is more precise than
			// LONG
			// and nothing will be lost during the conversion
			if (left.getType() instanceof LongType) {
				// the left value is of type LONG, so it needs to be casted
				String temp = this.createAndSaveTemporaryIdentifier(new DoubleType());
				String temp2 = this.createAndSaveTemporaryIdentifier(new DoubleType());
				this.irCode.add(CastingFactory.createCast(left.getType(), left.getValue(), new DoubleType(), temp));
				this.irCode.add(QuadrupleFactory.doubleArithmeticBinaryOperation(node.getOperator(), temp,
						right.getValue(), temp2));

				this.intermediateResults.push(new IntermediateResult(temp2, new DoubleType()));
			}
			else if (right.getType() instanceof LongType) {
				// the right value is of type LONG, so it needs to be casted
				String temp = this.createAndSaveTemporaryIdentifier(new DoubleType());
				String temp2 = this.createAndSaveTemporaryIdentifier(new DoubleType());
				this.irCode.add(CastingFactory.createCast(right.getType(), right.getValue(), new DoubleType(), temp));
				this.irCode.add(QuadrupleFactory.doubleArithmeticBinaryOperation(node.getOperator(), left.getValue(),
						temp, temp2));

				this.intermediateResults.push(new IntermediateResult(temp2, new DoubleType()));
			}
			else {
				// this combinations of types is not supported
				String err = "Arithmetic Binary Expression with arguments of types %s and %s is not supported";
				String errf = String.format(err, left.getType().toString(), right.getType().toString());
				logger.fatal(errf);
				throw new IntermediateCodeGeneratorException(errf, new NotImplementedException());
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
	private String saveIdentifier(String identifier, Type type) throws IntermediateCodeGeneratorException {
		if (!this.usedNames.contains(identifier)) {
			// an identifier with this name was not yet used.
			// it does not need to be renamed to stick to SSA
			this.usedNames.add(identifier);
			this.currentScopeRenames.peek().put(identifier, identifier);
			this.irCode.add(QuadrupleFactory.declaration(identifier, type));
			return identifier;
		} else {
			// rename is required to keep single static assignment
			String newName = this.currentSymbolTable.peek().getNextFreeTemporary();
			this.currentSymbolTable.peek().putTemporary(newName, type);
			this.usedNames.add(newName);
			this.currentScopeRenames.peek().put(identifier, newName);
			this.irCode.add(QuadrupleFactory.declaration(newName, type));
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
	private String createAndSaveTemporaryIdentifier(Type type) throws IntermediateCodeGeneratorException {
		// Condition: getNextFreeTemporary never returns a name that was already
		// used in the IR until now
		// (globally unique names)
		String id = this.currentSymbolTable.peek().getNextFreeTemporary();
		this.currentSymbolTable.peek().putTemporary(id, type);
		this.usedNames.add(id);
		this.currentScopeRenames.peek().put(id, id);
		this.irCode.add(QuadrupleFactory.declaration(id, type));
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
	private String loadIdentifier(String id) throws IntermediateCodeGeneratorException {
		@SuppressWarnings("unchecked")
		Stack<Map<String, String>> renameScopes = (Stack<Map<String, String>>) this.currentScopeRenames.clone();
		try {
			while (true) {
				// try to find the renaming
				Map<String, String> renamedIds = renameScopes.pop();
				if (renamedIds.containsKey(id)) {
					return renamedIds.get(id);
				}
			}
		} catch (EmptyStackException e) {
			// renaming was not found, therefore the id is undeclared
			logger.fatal("Undeclared variable found: " + id);
			throw new IntermediateCodeGeneratorException("Undeclared variable found: " + id);
		}
	}
}
