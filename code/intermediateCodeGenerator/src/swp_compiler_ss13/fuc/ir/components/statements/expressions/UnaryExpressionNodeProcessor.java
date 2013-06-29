package swp_compiler_ss13.fuc.ir.components.statements.expressions;

import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;

/**
 * Processor for a unary expression nodes
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class UnaryExpressionNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the unary expression node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public UnaryExpressionNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * process an arithmetic unary node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             processing failed
	 */
	public void processArithmeticUnaryNode(ArithmeticUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		node.getRightValue();

		// process the right hand value
		IntermediateResult rightIntermediate = this.executor.process(node.getRightValue());

		String temp = this.state.nextTemporaryIdentifier(rightIntermediate.getType());
		this.state.addIntermediateCode(QuadrupleFactory.declare(temp, rightIntermediate.getType()));
		this.state.addIntermediateCode(QuadrupleFactory.arithmeticUnary(node.getOperator(),
				rightIntermediate.getValue(), temp, rightIntermediate.getType()));

		this.state
				.pushIntermediateResult(new IntermediateResult(rightIntermediate.getType(), temp));
	}

	/**
	 * process a logic unary node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             processing failed
	 */
	public void processLogicUnaryNode(LogicUnaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		IntermediateResult result = this.executor.process(node.getRightValue());

		if (result.getType().getKind() != Kind.BOOLEAN) {
			String err = "Unary logic operation NOT is not supported for type " + result.getType();
			NodeProcessor.logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String temp = this.state.nextTemporaryIdentifier(new BooleanType());
		this.state.addIntermediateCode(QuadrupleFactory.declare(temp, new BooleanType()));
		this.state.addIntermediateCode(QuadrupleFactory.logicUnary(node.getOperator(),
				result.getValue(), temp));
		this.state.pushIntermediateResult(new BooleanType(), temp);
	}
}
