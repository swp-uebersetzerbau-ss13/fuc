package lexer.token;

import lexer.token.BoolToken;
import lexer.token.TokenType;

/**
 * Implementation of the interface {@link BoolToken}
 * 
 * @author "Ho, Tay Phuong", "Thomas Benndorf"
 * 
 */
public class BoolTokenImpl implements BoolToken {

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
	public BoolTokenImpl(String value, TokenType type, Integer line,
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
	 * @return converted {@link Boolean} of value read by lexer for this token
	 */
	@Override
	public Boolean getBooleanValue() {
		return Boolean.parseBoolean(this.value);
	}

}
