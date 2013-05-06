package lexer.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;
import lexer.LexerImpl;
import lexer.token.NumTokenImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of num types (long)
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class NumTokenTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of num types
	 */
	@Test
	public void matchingNumTypesTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSTRING1);
		assertEquals(TokenType.NUM, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSTRING2);
		assertEquals(TokenType.NUM, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSTRING3);
		assertEquals(TokenType.NUM, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSTRING4);
		assertEquals(TokenType.NUM, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSTRING5);
		assertEquals(TokenType.NUM, tokenType);
	}

	/**
	 * Test for getting the correct kind of class for a num token
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getCorrectClassForTokenTest()
			throws UnsupportedEncodingException {
		String numString = Constants.LONGSTRING1 + " " + Constants.LONGSTRING2
				+ " " + Constants.LONGSTRING3 + " " + Constants.LONGSTRING4
				+ " " + Constants.LONGSTRING5;
		this.lexer.setSourceStream(new ByteArrayInputStream(numString
				.getBytes("UTF-8")));

		Token token;
		for (int i = 1; i <= 5; i++) {
			token = this.lexer.getNextToken();
			assertEquals("Error for the " + i + ". token", NumTokenImpl.class,
					token.getClass());
		}
	}

	/**
	 * Test for tokenizing of num types
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfNumTypesTest()
			throws UnsupportedEncodingException {
		String simpleNumString = Constants.LONGSTRING1 + " "
				+ Constants.LONGSTRING2 + " " + Constants.LONGSTRING3 + " "
				+ Constants.LONGSTRING4 + " " + Constants.LONGSTRING5;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleNumString
				.getBytes("UTF-8")));

		NumToken token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING1, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf(Constants.LONGSTRING1)
				.longValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING2, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(5, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf(Constants.LONGSTRING2)
				.longValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING3, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(11, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf(Constants.LONGSTRING3)
				.longValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING4, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(17, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf(Constants.LONGSTRING4)
				.longValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING5, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(24, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf(Constants.LONGSTRING5)
				.longValue());
	}
}
