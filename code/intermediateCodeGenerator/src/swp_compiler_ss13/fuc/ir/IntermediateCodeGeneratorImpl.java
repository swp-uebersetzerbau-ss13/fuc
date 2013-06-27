package swp_compiler_ss13.fuc.ir;

import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;

/**
 * Intermediate Code Generator. Generates Intermediate Code (TAC - Three Address
 * Code) from a given AST (Abstract Syntax Tree). The given AST is expected to
 * be correct. If an incorrect AST is given the behaviour of the
 * IntermediateCodeGenerator is indeterminate and
 * IntermediateCodeGeneratorExceptions may be thrown.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class IntermediateCodeGeneratorImpl implements IntermediateCodeGenerator {

	/**
	 * The logger instance
	 */
	private static Logger logger = Logger.getLogger(IntermediateCodeGeneratorImpl.class);

	/**
	 * The state object to store the current state of the generator.
	 */
	private GeneratorState state;

	/**
	 * Creates a new IntermediateCodeGenerator instance
	 */
	public IntermediateCodeGeneratorImpl() {
		this.state = new GeneratorState();
	}

	/**
	 * Reset the IntermediateCodeGenerator for the next run.
	 */
	private void reset() {
		this.state.reset();
	}

	@Override
	public List<Quadruple> generateIntermediateCode(AST ast) throws IntermediateCodeGeneratorException {
		if (ast == null) {
			String errf = "The given ast can not be null.";
			logger.fatal(errf);
			throw new IntermediateCodeGeneratorException(errf);
		}
		if (ast.getRootNode() == null) {
			String errf = "The root node of the ast can not be null.";
			logger.fatal(errf);
			throw new IntermediateCodeGeneratorException(errf);
		}

		this.reset();
		this.process(ast.getRootNode());
		return this.state.getIntermediateCode();
	}

	/**
	 * Process the given node and create the corresponding TAC. Processed nodes
	 * will push intermediate results onto the stack to be used by other nodes
	 * if needed.
	 * 
	 * @param node
	 *            The node to process
	 */
	void process(ASTNode node) {
	}

}
