package lexer.other;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
 * Testclass for tokenizing of semicolons, comments and EOF
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class AdditionalSymbolTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of semicolons
	 */
	@Test
	public void matchingSemicolonTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.SEMICOLON);
		assertEquals(TokenType.SEMICOLON, tokenType);
	}

	/**
	 * Test for matching of comments
	 */
	@Test
	public void matchingCommentsTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.COMMENT);
		assertEquals(TokenType.COMMENT, tokenType);
	}

	/**
	 * Test for tokenizing of semicolons, comments and EOF
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfSymbolsTest()
			throws UnsupportedEncodingException {
		String simpleSymbolString = Constants.SEMICOLON + " "
				+ Constants.COMMENT + " " + Constants.COMMENT_EXAMPLE;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleSymbolString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.SEMICOLON, token.getValue());
		assertEquals(TokenType.SEMICOLON, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleSymbolString
				.indexOf(Constants.SEMICOLON));

		token = this.lexer.getNextToken();
		assertEquals(Constants.COMMENT_EXAMPLE, token.getValue());
		assertEquals(TokenType.COMMENT, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleSymbolString
				.indexOf(Constants.COMMENT));

		token = this.lexer.getNextToken();
		assertTrue(token.getValue() == null);
		assertEquals(TokenType.EOF, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleSymbolString
				.indexOf(Constants.SEMICOLON));
	}
}
