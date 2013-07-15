package swp_compiler_ss13.fuc.ir.components.statements;

import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.fuc.ir.BranchHelper;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;

/**
 * Processor for a branch node
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class BranchNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the branch node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public BranchNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the branch node.
	 * 
	 * @param branch
	 *            the branch node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processBranchNode(BranchNode branch) throws IntermediateCodeGeneratorException {
		ExpressionNode condition = branch.getCondition();
		StatementNode onTrue = branch.getStatementNodeOnTrue();
		StatementNode onFalse = branch.getStatementNodeOnFalse();

		IntermediateResult conditionResult = this.executor.process(condition);
		if (conditionResult.getType().getKind() != Kind.BOOLEAN) {
			String err = "A condition is not of type boolean but of unsupported type "
					+ conditionResult.getType();
			NodeProcessor.logger.fatal(err);
			throw new IntermediateCodeGeneratorException(err);
		}

		String trueLabel = this.state.createNewLabel();
		String falseLabel = this.state.createNewLabel();
		String endLabel = this.state.createNewLabel();

		String conditionValue = conditionResult.getValue();

		Quadruple branchQuadruple = BranchHelper.optimizedBranch(conditionValue, trueLabel, falseLabel);

		this.state.addIntermediateCode(branchQuadruple);
		this.state.addIntermediateCode(QuadrupleFactory.label(trueLabel));
		this.executor.processWithoutResult(onTrue);
		this.state.addIntermediateCode(QuadrupleFactory.jump(endLabel));
		this.state.addIntermediateCode(QuadrupleFactory.label(falseLabel));

		if (onFalse != null) {
			this.executor.processWithoutResult(onFalse);
		}

		this.state.addIntermediateCode(QuadrupleFactory.label(endLabel));
	}
}
