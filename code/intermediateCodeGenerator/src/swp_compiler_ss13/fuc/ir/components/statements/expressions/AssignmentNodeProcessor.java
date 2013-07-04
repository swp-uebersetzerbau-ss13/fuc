package swp_compiler_ss13.fuc.ir.components.statements.expressions;

import java.util.EmptyStackException;

import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.fuc.ir.ArrayHelper;
import swp_compiler_ss13.fuc.ir.CastingFactory;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;
import swp_compiler_ss13.fuc.ir.StructHelper;
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

		String targetIndex = null;

		IntermediateResult left = this.executor.process(assignment.getLeftValue());

		// if the left side of the assignment is an array or struct, we need to
		// know the
		// index
		if (left.getType() instanceof ArrayType || left.getType() instanceof StructType) {
			try {
				targetIndex = this.state.popIntermediateResult().getValue();
			} catch (EmptyStackException e) {
				String err = "Deep copy of complex structures is not supported";
				logger.fatal(err);
				throw new IntermediateCodeGeneratorException(err, e);
			}
		}

		IntermediateResult right = this.executor.process(assignment.getRightValue());

		String leftValue = left.getValue();
		Type leftType = left.getType();
		String rightValue = right.getValue();
		Type rightType = right.getType();

		Type typeForCastCheckLeft = leftType;
		Type typeForCastCheckRight = rightType;
		boolean castSupported = true;

		if (leftType instanceof ArrayType) {
			Type baseType = ArrayHelper.getBaseType(leftType, assignment.getLeftValue());
			typeForCastCheckLeft = baseType;
		}

		if (leftType instanceof StructType) {
			Type baseType = StructHelper.getBaseType(leftType, assignment.getLeftValue());
			typeForCastCheckLeft = baseType;
		}

		boolean castNeeded = CastingFactory.isCastNeeded(typeForCastCheckLeft, typeForCastCheckRight);
		if (castNeeded) {
			// Casting is only supported for long and double
			if (!CastingFactory.isNumeric(typeForCastCheckLeft) || !CastingFactory.isNumeric(typeForCastCheckRight)) {
				String err = "Assignment from type " + rightType + " to type " + leftType
						+ " is unsupported.";
				NodeProcessor.logger.fatal(err);
				throw new IntermediateCodeGeneratorException(err);
			}

			String tmp = this.state.nextTemporaryIdentifier(typeForCastCheckLeft);
			this.state.addIntermediateCode(QuadrupleFactory.declare(tmp, typeForCastCheckLeft));

			if (typeForCastCheckLeft.getKind() == Kind.LONG) {
				this.state.addIntermediateCode(CastingFactory.doubleToLong(rightValue, tmp));
			}
			else {
				this.state.addIntermediateCode(CastingFactory.longToDouble(rightValue, tmp));
			}

			rightType = leftType;
			rightValue = tmp;
		}

		if (leftType instanceof ArrayType) {
			// assignment to an array needs special care
			// we can not use the assignment operator (=) in this case
			// but we need to use ARRAY_SET_{Type} Operator instead.
			Type baseType = ArrayHelper.getBaseType(leftType, assignment.getLeftValue());
			this.state.addIntermediateCode(QuadrupleFactory.arraySetType(baseType, leftValue, targetIndex, rightValue));
		}
		else if (leftType instanceof StructType) {
			// assignment to an structs needs special care
			// we can not use the assignment operator (=) in this case
			// but we need to use STRUCT_SET_{Type} Operator instead.
			Type baseType = StructHelper.getBaseType(leftType, assignment.getLeftValue());
			this.state
					.addIntermediateCode(QuadrupleFactory.structSetType(baseType, leftValue, targetIndex, rightValue));
		}
		else {
			// this is a normal assignment
			this.state.addIntermediateCode(QuadrupleFactory.assignment(leftType, leftValue, rightValue));
		}
		// this is needed for multiple assignments only
		// multiple assignments are assignments of this form: a = b = c = d = 5
		this.state.pushIntermediateResult(leftType, leftValue);
	}
}
