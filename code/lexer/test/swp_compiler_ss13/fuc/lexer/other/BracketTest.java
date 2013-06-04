package swp_compiler_ss13.fuc.lexer.other;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.util.Constants;

/**
 * Testclass for tokenizing of bracket types
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class BracketTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of bracket types
	 */
	@Test
	public void matchingBracketTypesTest() {
		PA.setValue(this.lexer, "actualTokenValue", Constants.LEFT_PARAN);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.LEFT_PARAN,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.RIGHT_PARAN);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.RIGHT_PARAN,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LEFT_BRACE);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.LEFT_BRACE,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.RIGHT_BRACE);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.RIGHT_BRACE,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LEFT_BRACKET);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.LEFT_BRACKET,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.RIGHT_BRACKET);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.RIGHT_BRACKET,
				PA.getValue(this.lexer, "actualTokenType"));
	}

	/**
	 * Test for tokenizing of bracket types
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfBracketTypesTest()
			throws UnsupportedEncodingException {
		String bracketString = Constants.LEFT_PARAN + " "
				+ Constants.RIGHT_PARAN + " " + Constants.LEFT_BRACE + " "
				+ Constants.RIGHT_BRACE + " " + Constants.LEFT_BRACKET + " "
				+ Constants.RIGHT_BRACKET;

		this.lexer.setSourceStream(new ByteArrayInputStream(bracketString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.LEFT_PARAN, token.getValue());
		assertEquals(TokenType.LEFT_PARAN, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.RIGHT_PARAN, token.getValue());
		assertEquals(TokenType.RIGHT_PARAN, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(3, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.LEFT_BRACE, token.getValue());
		assertEquals(TokenType.LEFT_BRACE, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(5, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.RIGHT_BRACE, token.getValue());
		assertEquals(TokenType.RIGHT_BRACE, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(7, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.LEFT_BRACKET, token.getValue());
		assertEquals(TokenType.LEFT_BRACKET, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(9, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.RIGHT_BRACKET, token.getValue());
		assertEquals(TokenType.RIGHT_BRACKET, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(11, token.getColumn().intValue());
	}
}
