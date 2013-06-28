package swp_compiler_ss13.fuc.ir.components.statements.expressions;

import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.fuc.ir.CastingFactory;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;

/**
 * Processor for an assignment node
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class AssignmentNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the assignment node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public AssignmentNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the assignment node.
	 * 
	 * @param assignment
	 *            the assignment node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processAssignment(AssignmentNode assignment)
			throws IntermediateCodeGeneratorException {
		assignment.getLeftValue();

		IntermediateResult left = this.executor.process(assignment.getLeftValue());
		IntermediateResult right = this.executor.process(assignment.getRightValue());

		String leftValue = left.getValue();
		Type leftType = left.getType();
		String rightValue = right.getValue();
		Type rightType = right.getType();

		boolean castNeeded = CastingFactory.isCastNeeded(leftType, rightType);
		if (castNeeded) {
			// Casting is only supported for long and double
			if (!CastingFactory.isNumeric(leftType) || !CastingFactory.isNumeric(rightType)) {
				String err = "Assignment from type " + rightType + " to type " + leftType
						+ " is unsupported.";
				NodeProcessor.logger.fatal(err);
				throw new IntermediateCodeGeneratorException(err);
			}

			String tmp = this.state.nextTemporaryIdentifier(leftType);
			this.state.addIntermediateCode(QuadrupleFactory.declare(tmp, leftType));

			if (leftType.getKind() == Kind.LONG) {
				this.state.addIntermediateCode(CastingFactory.doubleToLong(rightValue, tmp));
			}
			else {
				this.state.addIntermediateCode(CastingFactory.longToDouble(rightValue, tmp));
			}

			rightType = leftType;
			rightValue = tmp;
		}

		this.state
				.addIntermediateCode(QuadrupleFactory.assignment(leftType, leftValue, rightValue));
		this.state.pushIntermediateResult(leftType, leftValue);
	}
}
