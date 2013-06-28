package swp_compiler_ss13.fuc.ir.components.statements.expressions;

import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.RelationExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.fuc.ir.CastingFactory;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;

/**
 * Processor for a binary expression nodes
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class BinaryExpressionNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the binary expression node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public BinaryExpressionNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the binary arithmetic expression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             Processing failed
	 */
	public void processArithmeticBinaryNode(ArithmeticBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {
		ExpressionNode left = node.getLeftValue();
		ExpressionNode right = node.getRightValue();
		BinaryOperator operator = node.getOperator();

		IntermediateResult leftResult = this.executor.process(left);
		IntermediateResult rightResult = this.executor.process(right);

		String leftValue = leftResult.getValue();
		String rightValue = rightResult.getValue();

		boolean needsCast = CastingFactory.isCastNeeded(leftResult, rightResult);

		if (!needsCast) {
			// no cast is needed
			Type type = leftResult.getType();
			String target = this.state.nextTemporaryIdentifier(type);
			this.state.addIntermediateCode(QuadrupleFactory.declare(target, type));
			this.state.addIntermediateCode(QuadrupleFactory.arithmeticBinary(operator, type,
					leftValue, rightValue, target));
			this.state.pushIntermediateResult(type, target);
		}
		else {
			// cast is needed
			Type type = new DoubleType();
			if (leftResult.getType().getKind() == Kind.LONG) {
				// left side needs long_to_double cast
				String castedLeft = this.state.nextTemporaryIdentifier(type);
				this.state.addIntermediateCode(QuadrupleFactory.declare(castedLeft, type));
				this.state.addIntermediateCode(CastingFactory.longToDouble(leftValue, castedLeft));
				String target = this.state.nextTemporaryIdentifier(type);
				this.state.addIntermediateCode(QuadrupleFactory.declare(target, type));
				this.state.addIntermediateCode(QuadrupleFactory.arithmeticBinary(operator, type,
						castedLeft, rightValue, target));
				this.state.pushIntermediateResult(type, target);
			}
			else {
				// right side needs long_to_double cast
				String castedRight = this.state.nextTemporaryIdentifier(type);
				this.state.addIntermediateCode(QuadrupleFactory.declare(castedRight, type));
				this.state
						.addIntermediateCode(CastingFactory.longToDouble(rightValue, castedRight));
				String target = this.state.nextTemporaryIdentifier(type);
				this.state.addIntermediateCode(QuadrupleFactory.declare(target, type));
				this.state.addIntermediateCode(QuadrupleFactory.arithmeticBinary(operator, type,
						leftValue, castedRight, target));
				this.state.pushIntermediateResult(type, target);
			}
		}
	}

	/**
	 * process a logic binary expression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             processing failed
	 */
	public void processLogicBinaryNode(LogicBinaryExpressionNode node)
			throws IntermediateCodeGeneratorException {

		BinaryOperator operator = node.getOperator();
		IntermediateResult leftResult = this.executor.process(node.getLeftValue());
		IntermediateResult rightResult = this.executor.process(node.getRightValue());

		Kind leftKind = leftResult.getType().getKind();
		Kind rightKind = rightResult.getType().getKind();

		if ((leftKind != Kind.BOOLEAN) || (rightKind != Kind.BOOLEAN)) {
			String err = "Binary logic operation is not supported for " + leftResult.getType()
					+ " and " + rightResult.getType();
			NodeProcessor.logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String temp = this.state.nextTemporaryIdentifier(new BooleanType());
		this.state.addIntermediateCode(QuadrupleFactory.declare(temp, new BooleanType()));
		this.state.addIntermediateCode(QuadrupleFactory.logicBinary(operator,
				leftResult.getValue(), rightResult.getValue(), temp));

		this.state.pushIntermediateResult(new IntermediateResult(new BooleanType(), temp));
	}

	/**
	 * Process a relation expression node
	 * 
	 * @param node
	 *            the node to process
	 * @throws IntermediateCodeGeneratorException
	 *             processing failed
	 */
	public void processRelationExpressionNode(RelationExpressionNode node)
			throws IntermediateCodeGeneratorException {
		BinaryOperator operator = node.getOperator();

		ExpressionNode leftNode = node.getLeftValue();
		ExpressionNode rightNode = node.getRightValue();

		IntermediateResult leftResult = this.executor.process(leftNode);
		IntermediateResult rightResult = this.executor.process(rightNode);

		boolean castNeeded = CastingFactory.isCastNeeded(leftResult, rightResult);
		String castedleft = leftResult.getValue();
		String castedright = rightResult.getValue();
		Type type = leftResult.getType();

		if (castNeeded) {
			type = new DoubleType();
			if (!CastingFactory.isNumeric(leftResult) || !CastingFactory.isNumeric(rightResult)) {
				// casts are only supported for long and double
				// so this is not supported :-(
				String err = "Relation between the types " + leftResult.getType().toString()
						+ " and " + rightResult.getType().toString() + " is not supported!";
				NodeProcessor.logger.fatal(err);
				throw new IntermediateCodeGeneratorException(err);
			}

			// cast one side to double
			// cast the left side
			if (leftResult.getType().getKind() == Kind.LONG) {
				castedleft = this.state.nextTemporaryIdentifier(type);
				this.state.addIntermediateCode(QuadrupleFactory.declare(castedleft, type));
				this.state.addIntermediateCode(CastingFactory.longToDouble(leftResult.getValue(),
						castedleft));
			}
			// cast the right side
			else {
				castedright = this.state.nextTemporaryIdentifier(type);
				this.state.addIntermediateCode(QuadrupleFactory.declare(castedright, type));
				this.state.addIntermediateCode(CastingFactory.longToDouble(rightResult.getValue(),
						castedright));
			}
		}

		String result = this.state.nextTemporaryIdentifier(new BooleanType());
		this.state.addIntermediateCode(QuadrupleFactory.declare(result, new BooleanType()));
		this.state.addIntermediateCode(QuadrupleFactory.relation(operator, castedleft, castedright,
				result, type));

		if (operator == BinaryOperator.INEQUAL) {
			// we need to negate the result
			String tmp = this.state.nextTemporaryIdentifier(new BooleanType());
			this.state.addIntermediateCode(QuadrupleFactory.declare(tmp, new BooleanType()));
			this.state.addIntermediateCode(QuadrupleFactory.logicUnary(
					UnaryOperator.LOGICAL_NEGATE, result, tmp));
			result = tmp;
		}

		this.state.pushIntermediateResult(new IntermediateResult(new BooleanType(), result));
	}
}
