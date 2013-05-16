package lexer;

import java.io.InputStream;
import lexer.token.Token;

/**
 * Proposed interface for lexer
 * 
 * @author "Damla Durmaz", "Ferhat Beyaz", "Sebastian Barthel", "Florian Freudenberg"
 * @version 2
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/issues/3</a>
 */
public interface Lexer {

	/**
	 * Defines the stream for program source. The stream has to be ready for
	 * reading. Normally the lexer will read the complete stream till it ends,
	 * but this is not guaranteed.
	 * 
	 * After every call to this method, the lexer behaves as if after the first
	 * call.
	 * 
	 * @param stream
	 */
	public void setSourceStream(InputStream stream);

	/** 
	 * As long as not all characters are tokenized, it returns a token with
	 * token.getTokenType() != TokenType.EOF
	 * 
	 * If there are no characters left for tokenization it always returns a
	 * EOF-token.
	 * 
	 * For all minimal sequences of characters which are not matched by a
	 * token definition, the lexer returns these as NOT_A_TOKEN-tokens, eg:
	 * 3 + $$$$ 4 2w
	 * would result in:
	 * <NUM, '3'> <PLUS, '+'> <NOT_A_TOKEN, '$$$$'> <NUM, '4'> <NOT_A_TOKEN, '2w'>
	 * 
	 * Note that whitespaces are ignored but are essentially for token split. 
	 * This means '2w' may not be parsed as <NUM, '2'> <ID, 'w'>.
	 * 
	 * @return a token instance
	 */
	public Token getNextToken();
}