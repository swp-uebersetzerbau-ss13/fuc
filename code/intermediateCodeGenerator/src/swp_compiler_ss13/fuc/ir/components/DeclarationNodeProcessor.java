package swp_compiler_ss13.fuc.ir.components;

import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.QuadrupleFactory;

/**
 * Processor for a declaration node
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class DeclarationNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the declaration node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public DeclarationNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the declaration node.
	 * 
	 * @param declaration
	 *            the declaration node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processDeclarationNode(DeclarationNode declaration)
			throws IntermediateCodeGeneratorException {
		String declaredName = declaration.getIdentifier();
		Type declaredType = declaration.getType();

		// Ensure ssa (single static assignment) by avoiding
		// all identifier names that have been used already
		String ssaName = this.state.saveIdentifier(declaredName, declaredType);

		this.state.addIntermediateCode(QuadrupleFactory.declare(ssaName, declaredType));
	}
}
