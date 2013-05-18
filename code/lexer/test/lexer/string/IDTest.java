package lexer.string;

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

public class IDTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of IDs
	 */
	@Test
	public void matchingIDsTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.ID1);
		assertEquals(TokenType.ID, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.ID2);
		assertEquals(TokenType.ID, tokenType);
	}

	/**
	 * Test for tokenizing of IDs
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingIDsTest() throws UnsupportedEncodingException {
		String simpleIDString = Constants.ID1 + " " + Constants.ID2;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleIDString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.ID1, token.getValue());
		assertEquals(TokenType.ID, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.ID2, token.getValue());
		assertEquals(TokenType.ID, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(4, token.getColumn().intValue());
	}

	/**
	 * Test for tokenizing of wrong IDs
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void tokenizingOfWrongIDsTest() throws UnsupportedEncodingException {
		String simpleIDString = Constants.NOID1 + " " + Constants.NOID2;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleIDString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.NOID1, token.getValue());
		assertTrue(token.getTokenType().name() != TokenType.ID.name());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.NOID2, token.getValue());
		assertTrue(token.getTokenType().name() != TokenType.ID.name());
		assertEquals(1, token.getLine().intValue());
		assertEquals(5, token.getColumn().intValue());
	}
}
