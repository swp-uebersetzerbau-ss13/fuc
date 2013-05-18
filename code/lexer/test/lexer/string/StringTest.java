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
 * Testclass for tokenizing of strings
 * TODO: implement
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class StringTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of strings
	 */
	@Test
	public void matchingStringsTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.IFSTRING);
		//assertEquals(TokenType.IF, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.WHILESTRING);
		//assertEquals(TokenType.WHILE, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOSTRING);
		//assertEquals(TokenType.DO, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.BREAKSTRING);
		//assertEquals(TokenType.BREAK, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.RETURNSTRING);
		//assertEquals(TokenType.RETURN, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.PRINTSTRING);
		//assertEquals(TokenType.PRINT, tokenType);
	}

	/**
	 * Test for tokenizing of strings
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingStringsTest()
			throws UnsupportedEncodingException {
		String simpleStringString = Constants.NOID1 + " " + Constants.NOID2;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleStringString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		//assertEquals(Constants.STRING1, token.getValue());
		//assertEquals(TokenType.STRING, token.getTokenType());
		//assertEquals(1, token.getLine().intValue());
		//assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		//assertEquals(Constants.STRING2, token.getValue());
		//assertEquals(TokenType.STRING, token.getTokenType());
		//assertEquals(1, token.getLine().intValue());
		//assertEquals(5, token.getColumn().intValue());
	}
}
