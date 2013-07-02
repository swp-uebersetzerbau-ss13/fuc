package swp_compiler_ss13.fuc.ir.components;

import org.apache.log4j.Logger;

import swp_compiler_ss13.fuc.ir.GeneratorExecutor;
import swp_compiler_ss13.fuc.ir.GeneratorState;

/**
 * Base class for all node processors.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class NodeProcessor {
	/**
	 * The logger for this class
	 */
	protected static Logger logger = Logger.getLogger(NodeProcessor.class);

	/**
	 * The state of the intermediate code generator
	 */
	protected GeneratorState state;

	/**
	 * The intermediate code generator executor
	 */
	protected GeneratorExecutor executor;

	/**
	 * Create a new instance of NodeProcessor.
	 * 
	 * @param state
	 *            The state of the intermediate code generator.
	 * @param executor
	 *            The intermediate code generator executor.
	 */
	public NodeProcessor(GeneratorState state, GeneratorExecutor executor) {
		this.state = state;
		this.executor = executor;
	}
}
