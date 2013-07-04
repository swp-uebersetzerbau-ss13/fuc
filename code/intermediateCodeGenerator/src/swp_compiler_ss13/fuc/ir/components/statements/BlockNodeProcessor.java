package swp_compiler_ss13.fuc.ir.components.statements;

import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
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
public class BlockNodeProcessor extends NodeProcessor {

	/**
	 * Create a new instance of the block node processor.
	 * 
	 * @param state
	 *            the state of the intermediate code generator
	 * @param executor
	 *            the intermediate code generator executor
	 */
	public BlockNodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		super(state, executor);
	}

	/**
	 * Process the block node.
	 * 
	 * @param block
	 *            the block node to process.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while processing the node.
	 */
	public void processBlockNode(BlockNode block) throws IntermediateCodeGeneratorException {
		// set the new active symbol table
		this.state.pushSymbolTable(block.getSymbolTable());

		// process all declaration nodes
		for (DeclarationNode declarationNode : block.getDeclarationList()) {
			this.executor.processWithoutResult(declarationNode);
		}

		// process all statement nodes
		for (StatementNode statementNode : block.getStatementList()) {
			this.executor.processWithoutResult(statementNode);
		}

		// restore the symbol table scope
		this.state.popSymbolTable();
	}
}
