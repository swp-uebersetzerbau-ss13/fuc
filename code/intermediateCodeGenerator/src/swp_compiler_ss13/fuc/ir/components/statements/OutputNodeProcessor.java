package swp_compiler_ss13.fuc.ir.components.statements;

import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ir.CastingFactory;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;

/**
 * Processor for an output node
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class OutputNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the output node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public OutputNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the print node.
	 * 
	 * @param node
	 *            the print node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processPrintNode(PrintNode node) throws IntermediateCodeGeneratorException {
		IntermediateResult result = this.executor.process(node.getRightValue());

		String variable = result.getValue();
		result.getType();

		if (result.getType().getKind() != Kind.STRING) {
			// if variable is not of type string cast it to string!
			String tmp = this.state.nextTemporaryIdentifier(new StringType(255L));
			this.state.addIntermediateCode(QuadrupleFactory.declare(tmp, new StringType(255L)));

			this.state.addIntermediateCode(CastingFactory.castToString(result.getType(),
					result.getValue(), tmp));
			variable = tmp;
			new StringType(255L);
		}

		this.state.addIntermediateCode(QuadrupleFactory.print(variable));
	}

	/**
	 * Process the return node.
	 * 
	 * @param node
	 *            the return node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processReturnNode(ReturnNode node) throws IntermediateCodeGeneratorException {
		IdentifierNode right = node.getRightValue();
		if (right != null) {
			IntermediateResult intermediateResult = this.executor.process(right);
			this.state.addIntermediateCode(QuadrupleFactory.exit(intermediateResult.getValue()));
		}
		else {
			this.state.addIntermediateCode(QuadrupleFactory.exit("#0"));
		}
	}
}
