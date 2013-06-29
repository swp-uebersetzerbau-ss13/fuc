package swp_compiler_ss13.fuc.lexer.token;

import swp_compiler_ss13.common.lexer.RealToken;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Implementation of the interface {@link RealToken}
 * 
 * @author "Ho, Tay Phuong", "Thomas Benndorf"
 * 
 */
public class RealTokenImpl implements RealToken {

	private final String value;
	private final TokenType type;
	private final Integer line;
	private final Integer column;

	/**
	 * constructor
	 * 
	 * @param value
	 *            , string read by lexer for this token
	 * @param type
	 *            , type of token
	 * @param line
	 *            , line of code in source file
	 * @param column
	 *            , column of code in source file
	 */
	public RealTokenImpl(String value, TokenType type, Integer line,
			Integer column) {

		this.value = value;
		this.type = type;
		this.line = line;
		this.column = column;
	}

	/**
	 * @return string representation of value read by lexer for this token
	 */
	@Override
	public String getValue() {
		return this.value;
	}

	/**
	 * @return type of token
	 */
	@Override
	public TokenType getTokenType() {
		return this.type;
	}

	/**
	 * @return line of code in source file
	 */
	@Override
	public Integer getLine() {
		return this.line;
	}

	/**
	 * @return column of code in source file
	 */
	@Override
	public Integer getColumn() {
		return this.column;
	}

	/**
	 * @return converted {@link Double} of value read by lexer for this token
	 */
	@Override
	public Double getDoubleValue() {
		Double doubleValue;
		if (this.value.contains("e") || this.value.contains("E")) {
			String[] doubleValueParts = this.value.split("(e|E)");
			doubleValue = Double.parseDouble(doubleValueParts[0])
					* Math.pow(10, Double.parseDouble(doubleValueParts[1]));
		} else {
			doubleValue = Double.parseDouble(this.value);
		}

		if (doubleValue < Double.MIN_VALUE || doubleValue > Double.MAX_VALUE) {
			return null;
		} else {
			return doubleValue;
		}
	}

}