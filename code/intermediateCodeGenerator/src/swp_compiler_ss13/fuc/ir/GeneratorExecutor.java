package swp_compiler_ss13.fuc.ir;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;

/**
 * The GeneratorExecutor is a helper class to execute the process method of the intermediate code
 * generator.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class GeneratorExecutor {

	/**
	 * Instance of the intermediate code generator
	 */
	private final IntermediateCodeGeneratorImpl generator;

	/**
	 * Create a new instance of the generatorExecutor.
	 * 
	 * @param generator
	 *            The IntermediateCodeGenerator instance.
	 */
	GeneratorExecutor(IntermediateCodeGeneratorImpl generator) {
		this.generator = generator;
	}

	/**
	 * Process a node of the AST and pop the intermediate result from the stack of intermediate
	 * results.
	 * 
	 * @param node
	 *            The node to process.
	 * @return The intermediate result on top of the stack.
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while executing the IntermediateCodeGenerator.
	 */
	public IntermediateResult process(ASTNode node) throws IntermediateCodeGeneratorException {
		this.generator.process(node);
		return this.generator.getState().popIntermediateResult();
	}

	/**
	 * Process a node of the AST but do not pop the intermediate result from the stack of
	 * intermediate results.
	 * 
	 * @param node
	 *            The node to process
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred while executing the IntermediateCodeGenerator.
	 */
	public void processWithoutResult(ASTNode node) throws IntermediateCodeGeneratorException {
		this.generator.process(node);
	}
}
