package swp_compiler_ss13.fuc.ir.data;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.types.Type;

/**
 * An intermediate result. Intermediate results are created when nodes of the AST are processed.
 * Intermediate results are immutable.
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class IntermediateResult {

	/**
	 * The logger for this class
	 */
	private static Logger logger = Logger.getLogger(IntermediateResult.class);

	/**
	 * The type of the intermediate result.
	 */
	private final Type type;

	/**
	 * The value of the intermediate result.
	 */
	private final String value;

	/**
	 * Create a new instance of IntermediateResult class with the given initial values.
	 * 
	 * @param type
	 *            The type of the intermediate result.
	 * @param value
	 *            The value of the intermediate result.
	 */
	public IntermediateResult(Type type, String value) {
		this.type = type;
		this.value = value;

		if (this.type == null) {
			IntermediateResult.logger
					.warn("The type of an intermediate result should not be null!");
		}
	}

	/**
	 * Get the type of this intermediate result.
	 * 
	 * @return the type.
	 */
	public Type getType() {
		return this.type;
	}

	/**
	 * Get the value of this intermediate result.
	 * 
	 * @return the value.
	 */
	public String getValue() {
		return this.value;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		// make sure to avoid null pointer exception
		if (this.type != null) {
			return "IntermediateResult: " + this.type.toString() + " " + this.value;
		}
		return "IntermediateResult: (unknown type) " + this.value;
	}
}
