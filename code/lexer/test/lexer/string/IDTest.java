package lexer.string;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;
import lexer.LexerImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of IDs
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class IDTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of IDs id -> alpha alphaNumeric*
	 */
	@Test
	public void matchingIDsTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSYMBOL);
		assertEquals(TokenType.LONG_SYMBOL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESYMBOL);
		assertEquals(TokenType.DOUBLE_SYMBOL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.BOOLSYMBOL);
		assertEquals(TokenType.BOOL_SYMBOL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.STRINGSYMBOL);
		assertEquals(TokenType.STRING_SYMBOL, tokenType);
	}

	/**
	 * Test for tokenizing of IDs
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingIDsTest() throws UnsupportedEncodingException {

		String simpleIDString = Constants.LONGSYMBOL + " "
				+ Constants.WHILESTRING + " " + Constants.DOUBLESYMBOL + " "
				+ Constants.BREAKSTRING + " " + Constants.BOOLSYMBOL + " "
				+ Constants.PRINTSTRING + " " + Constants.STRINGSYMBOL + " "
				+ Constants.PRINTSTRING;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleIDString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.IFSTRING, token.getValue());
		assertEquals(TokenType.IF, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.WHILESTRING, token.getValue());
		assertEquals(TokenType.WHILE, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(4, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.DOSTRING, token.getValue());
		assertEquals(TokenType.DO, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(10, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.BREAKSTRING, token.getValue());
		assertEquals(TokenType.BREAK, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(13, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.RETURNSTRING, token.getValue());
		assertEquals(TokenType.RETURN, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(19, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.PRINTSTRING, token.getValue());
		assertEquals(TokenType.PRINT, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(26, token.getColumn().intValue());

	}
}
