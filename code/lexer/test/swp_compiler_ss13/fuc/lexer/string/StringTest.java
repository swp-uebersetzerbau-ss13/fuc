package swp_compiler_ss13.fuc.lexer.string;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.util.Constants;

/**
 * Testclass for tokenizing of strings TODO: implement
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
		/*
		 * FIXME: PA.invokeMethode() throws IllegalArgumentException
		 * 
		 * PA.setValue(this.lexer, "actualTokenValue", Constants.STRING1);
		 * PA.invokeMethod(this.lexer, "matchToken()");
		 * assertEquals(TokenType.STRING, PA.getValue(this.lexer,
		 * "actualTokenType"));
		 * 
		 * PA.setValue(this.lexer, "actualTokenValue", Constants.STRING2);
		 * PA.invokeMethod(this.lexer, "matchToken()");
		 * assertEquals(TokenType.STRING, PA.getValue(this.lexer,
		 * "actualTokenType"));
		 */
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
		String simpleStringString = Constants.STRING1 + " " + Constants.STRING2;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleStringString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.STRING1, token.getValue());
		assertEquals(TokenType.STRING, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.STRING2, token.getValue());
		assertEquals(TokenType.STRING, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(19, token.getColumn().intValue());
	}

	/**
	 * Test for tokenizing of string with semicolons
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void tokenizeStringWithSemicolonsTest()
			throws UnsupportedEncodingException {
		String simpleStringString = Constants.STRING3 + Constants.SEMICOLON;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleStringString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.STRING3, token.getValue());
		assertEquals(TokenType.STRING, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.SEMICOLON, token.getValue());
		assertEquals(TokenType.SEMICOLON, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(12, token.getColumn().intValue());
	}

	/**
	 * Test for tokenizing of string with other characters after the string
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void tokenizeStringWithOtherCharactersAfterTest()
			throws UnsupportedEncodingException {
		String simpleStringString = Constants.STRING4 + Constants.SEMICOLON;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleStringString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals("\"test\"", token.getValue());
		assertEquals(TokenType.STRING, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals("test", token.getValue());
		assertEquals(TokenType.ID, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(7, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.SEMICOLON, token.getValue());
		assertEquals(TokenType.SEMICOLON, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(11, token.getColumn().intValue());
	}

	/**
	 * Test for tokenizing of incorrect strings
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void tokenizeIncorrectStringsTest()
			throws UnsupportedEncodingException {
		String simpleStringString = Constants.NOSTRING1;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleStringString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.NOSTRING1, token.getValue());
		assertEquals(TokenType.NOT_A_TOKEN, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());
	}
}
