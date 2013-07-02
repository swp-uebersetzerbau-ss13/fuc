package swp_compiler_ss13.fuc.ir;

import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
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
import swp_compiler_ss13.fuc.ir.components.DeclarationNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.BlockNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.BranchNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.LoopNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.OutputNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.expressions.AssignmentNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.expressions.BinaryExpressionNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.expressions.IdentifierNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.expressions.LiteralNodeProcessor;
import swp_compiler_ss13.fuc.ir.components.statements.expressions.UnaryExpressionNodeProcessor;

/**
 * Intermediate Code Generator. Generates Intermediate Code (TAC - Three Address
 * Code) from a given AST (Abstract Syntax Tree). The given AST is expected to
 * be correct. If an incorrect AST is given the behaviour of the
 * IntermediateCodeGenerator is indeterminate and
 * IntermediateCodeGeneratorExceptions may be thrown.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class IntermediateCodeGeneratorImpl implements IntermediateCodeGenerator {

	/**
	 * The logger instance
	 */
	private static Logger logger = Logger.getLogger(IntermediateCodeGeneratorImpl.class);

	/**
	 * The state object to store the current state of the generator.
	 */
	private final GeneratorState state;

	/**
	 * The executor helper for this generator;
	 */
	private final GeneratorExecutor executor;

	/**
	 * The block node processor
	 */
	private final BlockNodeProcessor blockNodeProcessor;

	/**
	 * The literal node processor
	 */
	private final LiteralNodeProcessor literalNodeProcessor;

	/**
	 * The declaration node processor
	 */
	private final DeclarationNodeProcessor declarationNodeProcessor;

	/**
	 * The loop node processor
	 */
	private final LoopNodeProcessor loopNodeProcessor;

	/**
	 * The branch node processor
	 */
	private final BranchNodeProcessor branchNodeProcessor;

	/**
	 * The identifier node processor
	 */
	private final IdentifierNodeProcessor identifierNodeProcessor;

	/**
	 * The binary expression node processor
	 */
	private final BinaryExpressionNodeProcessor binaryExpressionNodeProcessor;

	/**
	 * The unary expression node processor
	 */
	private final UnaryExpressionNodeProcessor unaryExpressionNodeProcessor;

	/**
	 * The output node processor
	 */
	private final OutputNodeProcessor outputNodeProcessor;

	/**
	 * The assignment node processor
	 */
	private final AssignmentNodeProcessor assignmentNodeProcessor;

	/**
	 * Creates a new IntermediateCodeGenerator instance
	 */
	public IntermediateCodeGeneratorImpl() {
		this.state = new GeneratorState();
		this.executor = new GeneratorExecutor(this);
		this.blockNodeProcessor = new BlockNodeProcessor(this.state, this.executor);
		this.literalNodeProcessor = new LiteralNodeProcessor(this.state, this.executor);
		this.declarationNodeProcessor = new DeclarationNodeProcessor(this.state, this.executor);
		this.loopNodeProcessor = new LoopNodeProcessor(this.state, this.executor);
		this.branchNodeProcessor = new BranchNodeProcessor(this.state, this.executor);
		this.identifierNodeProcessor = new IdentifierNodeProcessor(this.state, this.executor);
		this.binaryExpressionNodeProcessor = new BinaryExpressionNodeProcessor(this.state,
				this.executor);
		this.unaryExpressionNodeProcessor = new UnaryExpressionNodeProcessor(this.state,
				this.executor);
		this.outputNodeProcessor = new OutputNodeProcessor(this.state, this.executor);
		this.assignmentNodeProcessor = new AssignmentNodeProcessor(this.state, this.executor);
	}

	/**
	 * Reset the IntermediateCodeGenerator for the next run.
	 */
	private void reset() {
		this.state.reset();
	}

	@Override
	public List<Quadruple> generateIntermediateCode(AST ast)
			throws IntermediateCodeGeneratorException {
		if (ast == null) {
			String err = "The given ast can not be null.";
			IntermediateCodeGeneratorImpl.logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}
		if (ast.getRootNode() == null) {
			String err = "The root node of the ast can not be null.";
			IntermediateCodeGeneratorImpl.logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		this.reset();
		this.process(ast.getRootNode());
		return this.state.getIntermediateCode();
	}

	/**
	 * Process the given node and create the corresponding TAC. Processed nodes
	 * will push intermediate results onto the stack to be used by other nodes
	 * if needed.
	 * 
	 * @param node
	 *            The node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while executing the
	 *             IntermediateCodeGenerator.
	 */
	void process(ASTNode node) throws IntermediateCodeGeneratorException {
		if (node == null) {
			String err = "The given node is null. A null node can not be processed.";
			IntermediateCodeGeneratorImpl.logger.error(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String msg = String.format("Processing the %s with %d subnodes.", node.toString(),
				node.getNumberOfNodes() - 1);
		IntermediateCodeGeneratorImpl.logger.debug(msg);

		switch (node.getNodeType()) {
		case ArithmeticBinaryExpressionNode:
			this.binaryExpressionNodeProcessor
					.processArithmeticBinaryNode((ArithmeticBinaryExpressionNode) node);
			break;
		case ArithmeticUnaryExpressionNode:
			this.unaryExpressionNodeProcessor
					.processArithmeticUnaryNode((ArithmeticUnaryExpressionNode) node);
			break;
		case ArrayIdentifierNode:
			this.identifierNodeProcessor.processArrayIdentifier((ArrayIdentifierNode) node);
			break;
		case AssignmentNode:
			this.assignmentNodeProcessor.processAssignment((AssignmentNode) node);
			break;
		case BasicIdentifierNode:
			this.identifierNodeProcessor.processBasicIdentifier((BasicIdentifierNode) node);
			break;
		case BlockNode:
			this.blockNodeProcessor.processBlockNode((BlockNode) node);
			break;
		case BranchNode:
			this.branchNodeProcessor.processBranchNode((BranchNode) node);
			break;
		case BreakNode:
			this.loopNodeProcessor.processBreakNode((BreakNode) node);
			break;
		case DeclarationNode:
			this.declarationNodeProcessor.processDeclarationNode((DeclarationNode) node);
			break;
		case DoWhileNode:
			this.loopNodeProcessor.processDoWhileNode((DoWhileNode) node);
			break;
		case LiteralNode:
			this.literalNodeProcessor.processLiteralNode((LiteralNode) node);
			break;
		case LogicBinaryExpressionNode:
			this.binaryExpressionNodeProcessor
					.processLogicBinaryNode((LogicBinaryExpressionNode) node);
			break;
		case LogicUnaryExpressionNode:
			this.unaryExpressionNodeProcessor
					.processLogicUnaryNode((LogicUnaryExpressionNode) node);
			break;
		case PrintNode:
			this.outputNodeProcessor.processPrintNode((PrintNode) node);
			break;
		case RelationExpressionNode:
			this.binaryExpressionNodeProcessor
					.processRelationExpressionNode((RelationExpressionNode) node);
			break;
		case ReturnNode:
			this.outputNodeProcessor.processReturnNode((ReturnNode) node);
			break;
		case StructIdentifierNode:
			this.identifierNodeProcessor.processStructIdentifier((StructIdentifierNode) node);
			break;
		case WhileNode:
			this.loopNodeProcessor.processWhileNode((WhileNode) node);
			break;
		default:
			break;

		}
	}

	/**
	 * Get the state of the intermediate code generator.
	 * 
	 * @return the state of the intermediate code generator.
	 */
	GeneratorState getState() {
		return this.state;
	}

}
