package swp_compiler_ss13.fuc.lexer.token;

import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Implementation of the interface {@link NumToken}
 * 
 * @author "Ho, Tay Phuong", "Thomas Benndorf"
 * 
 */
public class NumTokenImpl implements NumToken {

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
	public NumTokenImpl(String value, TokenType type, Integer line,
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
	 * Method parses the {@link String} value of the token into a {@link Long}
	 * value
	 * 
	 * @return converted {@link Long} of value read by lexer for this token or
	 *         NULL if a {@linkNumberFormatException} occurs
	 */
	@Override
	public Long getLongValue() {
		Long longValue;
		try {
			if (this.value.contains("e") || this.value.contains("E")) {
				String[] longValueParts = this.value.split("(e|E)");
				longValue = Math.round(Double.parseDouble(longValueParts[0])
						* Math.pow(10, Double.parseDouble(longValueParts[1])));
			} else {
				longValue = Long.parseLong(this.value);
			}
		} catch (NumberFormatException nfe) {
			longValue = null;
		}
		return longValue;
	}
}