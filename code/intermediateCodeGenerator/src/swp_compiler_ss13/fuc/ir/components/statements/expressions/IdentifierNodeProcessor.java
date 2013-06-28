package swp_compiler_ss13.fuc.ir.components.statements.expressions;

import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;
import swp_compiler_ss13.fuc.ir.components.NodeProcessor;

/**
 * Processor for a block node
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class IdentifierNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the block node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public IdentifierNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the basic identifier node.
	 * 
	 * @param node
	 *            the basic identifier node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             The given identifier is undeclared
	 */

	public void processBasicIdentifier(BasicIdentifierNode node)
			throws IntermediateCodeGeneratorException {
		// A basic identifier can be pushed to the stack of results immediately
		String identifier = node.getIdentifier();
		Type identifierType = this.state.getCurrentSymbolTable().lookupType(identifier);

		// if the identifier was renamed due to single static assignment
		// get the renamed identifier name
		String actualIdentifier = this.state.loadIdentifier(identifier);
		this.state.pushIntermediateResult(identifierType, actualIdentifier);
	}
}
