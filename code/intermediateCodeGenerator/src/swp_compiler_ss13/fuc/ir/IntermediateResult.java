package swp_compiler_ss13.fuc.ir;

import swp_compiler_ss13.common.types.Type;

/**
 * Container class for intermediate results.
 * 
 * @author "Frank Zechert"
 * @version 1
 */
class IntermediateResult {
	/**
	 * The value of this intermediate result
	 */
	private final String value;

	/**
	 * The type of this intermediate result
	 */
	private final Type type;

	/**
	 * Create a new IntermediateResult
	 * 
	 * @param value
	 *            The value of the intermediate result
	 * @param type
	 *            The type of the intermediate result
	 */
	IntermediateResult(String value, Type type) {
		this.value = value;
		this.type = type;
	}

	/**
	 * Get the value of this intermediate result.
	 * 
	 * @return the value.
	 */
	String getValue() {
		return this.value;
	}

	/**
	 * Get the type of this intermediate result.
	 * 
	 * @return the type.
	 */
	Type getType() {
		return this.type;
	}
}
