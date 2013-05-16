package lexer.token;

/**
 * RealToken should always be used for TokenType REAL and only for this.
 */
public interface RealToken extends Token {
	/**
	 * @return value of getValue() converted to Double
	 */
	Double getDoubleValue();
}