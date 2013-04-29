package swp_compiler_ss13.fuc.ir;

import java.util.EmptyStackException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
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
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.parser.SymbolTable;

/**
 * Create the intermediate code representation for the given AST
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class IntermediateCodeGeneratorImpl implements IntermediateCodeGenerator {

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
	 * Reset the intermediate code generator
	 */
	private void reset() {
		this.irCode = new LinkedList<>();
		this.usedNames = new LinkedList<>();
		this.currentScopeRenames = new Stack<>();
		this.currentSymbolTable = new Stack<>();
	}

	@Override
	public List<Quadruple> generateIntermediateCode(AST ast) throws IntermediateCodeGeneratorException {
		this.reset();
		BlockNode program = ast.getRootNode();
		this.processBlockNode(program);
		return this.irCode;
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
			this.processDeclarationNode(declIterator.next());
		}

		Iterator<StatementNode> statementIterator = node.getStatementIterator();
		while (statementIterator.hasNext()) {
			StatementNode statement = statementIterator.next();
			this.callProcessing(statement);
		}

		// pop the symbol scope and the renaming scope
		this.currentScopeRenames.pop();
		this.currentSymbolTable.pop();
	}

	private void callProcessing(ASTNode node) throws IntermediateCodeGeneratorException {
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
			break;
		}
	}

	private void processWhileNode(WhileNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processStructIdentifierNode(StructIdentifierNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processReturnNode(ReturnNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processRelationExpressionNode(RelationExpressionNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processPrintNode(PrintNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processLogicUnaryExpressionNode(LogicUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processLogicBinaryExpressionNode(LogicBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processLiteralNode(LiteralNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processDoWhileNode(DoWhileNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processDeclarationNode(DeclarationNode node) throws IntermediateCodeGeneratorException {
		String identifierName = node.getIdentifier();
		if (this.usedNames.contains(identifierName)) {
			// a variable of this name has already been declared
			// a renaming is needed. The renaming is only valid inside
			// this block. It is not valid outside this block because
			// outside the meaning of the variable can change (happens
			// when variable shadowing occurs in the source code)
			String newName = this.currentSymbolTable.peek().getNextFreeTemporary();
			this.currentSymbolTable.peek().putTemporary(newName, node.getType());
			this.usedNames.add(newName);
			this.currentScopeRenames.peek().put(identifierName, newName);
		} else {
			// no renaming is needed for this declaration
			this.usedNames.add(identifierName);
			this.currentScopeRenames.peek().put(identifierName, identifierName);
		}

		// create the TAC
		final String actualId = this.getActualIdentifierName(identifierName);
		final Quadruple quadruple = QuadrupleFactory.declaration(actualId, node.getType());
		this.irCode.add(quadruple);
	}

	private void processBreakNode(BreakNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processBranchNode(BranchNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processBasicIdentifierNode(BasicIdentifierNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processAssignmentNode(AssignmentNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processArrayIdentifierNode(ArrayIdentifierNode node) throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processArithmeticUnaryExpressionNode(ArithmeticUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	private void processArithmeticBinaryExpressionNode(ArithmeticBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		// TODO Auto-generated method stub

	}

	/**
	 * Return the identifier name that is used inside the TAC for the given
	 * identifier name. (Resolving identifier renames).
	 * 
	 * @param identifierName
	 *            The identifier to resolve
	 * @return the resolved name
	 * @throws IntermediateCodeGeneratorException
	 *             The variable could not be resolved
	 */
	private String getActualIdentifierName(String identifierName) throws IntermediateCodeGeneratorException {
		@SuppressWarnings("unchecked")
		Stack<Map<String, String>> scopes = (Stack<Map<String, String>>) this.currentScopeRenames.clone();
		try {
			while (true) {
				Map<String, String> renamings = scopes.pop();
				if (renamings.containsKey(identifierName)) {
					return renamings.get(identifierName);
				}
			}
		} catch (EmptyStackException ex) {
			throw new IntermediateCodeGeneratorException("Referenced unknown variable " + identifierName);
		}
	}
}
