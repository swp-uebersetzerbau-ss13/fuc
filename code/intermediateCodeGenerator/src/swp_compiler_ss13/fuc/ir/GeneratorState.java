package swp_compiler_ss13.fuc.ir;

import java.util.List;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.fuc.ir.data.LoggingQuadrupleLinkedList;

/**
 * This objects holds the current state of the IntermediateCodeGenerator.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class GeneratorState {

	/**
	 * A list to store the generated intermediate code.
	 */
	private List<Quadruple> intermediateCode;

	/**
	 * Create a new instance of the GeneratorState class.
	 */
	public GeneratorState() {
		this.intermediateCode = new LoggingQuadrupleLinkedList();
	}

	/**
	 * Reset the generator state object to the state of initializing.
	 */
	public void reset() {
		this.intermediateCode.clear();
	}

	/**
	 * Return the intermediate code that was created up to the current state.
	 * 
	 * @return The intermediate code (three address code - tac).
	 */
	public List<Quadruple> getIntermediateCode() {
		return this.intermediateCode;
	}

}
