package swp_compiler_ss13.fuc.ir;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;

/**
 * Helper to get optimizde branches
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class BranchHelper {

	/**
	 * Optimize a branch. If the condition is true or false literal use an
	 * unconditional jump. Otherwise use a conditional jump
	 * 
	 * @param conditionValue
	 *            The condition to decide the jump on
	 * @param trueLabel
	 *            The label to jump to if the condition is true
	 * @param falseLabel
	 *            The label to jump to if the condition is false
	 * @return the quadruple for the optimized jump
	 * @throws IntermediateCodeGeneratorException
	 *             The condition is a literal but not #true and not #false.
	 */
	public static Quadruple optimizedBranch(String conditionValue, String trueLabel, String falseLabel)
			throws IntermediateCodeGeneratorException {
		if (conditionValue.startsWith("#")) {
			if (conditionValue.toUpperCase() == "#TRUE") {
				return QuadrupleFactory.jump(trueLabel);
			}
			else if (conditionValue.toUpperCase() == "#FALSE") {
				return QuadrupleFactory.jump(falseLabel);
			}
			else {
				throw new IntermediateCodeGeneratorException("literal " + conditionValue
						+ " can not be used as condition for jump.");
			}
		}
		else {
			return QuadrupleFactory.branch(conditionValue, trueLabel, falseLabel);
		}
	}

}
