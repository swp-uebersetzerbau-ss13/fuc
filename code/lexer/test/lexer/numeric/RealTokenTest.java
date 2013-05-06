package lexer.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;
import lexer.LexerImpl;
import lexer.token.RealTokenImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.RealToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of real types (double)
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class RealTokenTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of real types
	 */
	@Test
	public void matchingRealTypesTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESTRING1);
		assertEquals(TokenType.REAL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESTRING2);
		assertEquals(TokenType.REAL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESTRING3);
		assertEquals(TokenType.REAL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESTRING4);
		assertEquals(TokenType.REAL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESTRING5);
		assertEquals(TokenType.REAL, tokenType);
	}

	/**
	 * Test for getting the correct kind of class for real token
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getCorrectClassForTokenTest()
			throws UnsupportedEncodingException {
		Token token;
		String realString = Constants.DOUBLESTRING1 + " "
				+ Constants.DOUBLESTRING2 + " " + Constants.DOUBLESTRING3 + " "
				+ Constants.DOUBLESTRING4 + " " + Constants.DOUBLESTRING5;
		this.lexer.setSourceStream(new ByteArrayInputStream(realString
				.getBytes("UTF-8")));

		for (int i = 1; i <= 5; i++) {
			token = this.lexer.getNextToken();
			assertEquals("Error for the " + i + ". token", RealTokenImpl.class,
					token.getClass());
		}
	}

	/**
	 * Test for tokenizing of real types
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfRealTypesTest()
			throws UnsupportedEncodingException {
		String simpleNumString = Constants.DOUBLESTRING1 + " "
				+ Constants.DOUBLESTRING2 + " " + Constants.DOUBLESTRING3 + " "
				+ Constants.DOUBLESTRING4 + " " + Constants.DOUBLESTRING5;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleNumString
				.getBytes("UTF-8")));

		RealToken token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING1, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());
		assertTrue(
				"wrong cast",
				token.getDoubleValue() == Double.valueOf(
						Constants.DOUBLESTRING1).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING2, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(9, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING2).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING3, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(19, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING3).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING4, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(29, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING4).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING5, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(40, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING5).doubleValue());

	}
}