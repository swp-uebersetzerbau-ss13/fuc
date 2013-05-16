package lexer.token;

/**
 * NumToken should always be used for TokenType NUM and only for this.
 */
public interface NumToken extends Token {
	/**
	 * @return value of getValue() converted to Integer
	 */
	Long getLongValue();
}