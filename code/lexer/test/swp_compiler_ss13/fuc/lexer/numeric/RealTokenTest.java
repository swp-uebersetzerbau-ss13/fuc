package swp_compiler_ss13.fuc.lexer.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.RealToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.RealTokenImpl;
import swp_compiler_ss13.fuc.lexer.util.Constants;

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
		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING1);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING2);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING3);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING4);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING5);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING6);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING7);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING8);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING9);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DOUBLESTRING10);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.REAL, PA.getValue(this.lexer, "actualTokenType"));
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
				+ Constants.DOUBLESTRING4 + " " + Constants.DOUBLESTRING5 + " "
				+ Constants.DOUBLESTRING6 + " " + Constants.DOUBLESTRING7 + " "
				+ Constants.DOUBLESTRING8 + " " + Constants.DOUBLESTRING9 + " "
				+ Constants.DOUBLESTRING10;
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
				+ Constants.DOUBLESTRING4 + " " + Constants.DOUBLESTRING5 + " "
				+ Constants.DOUBLESTRING6 + " " + Constants.DOUBLESTRING7 + " "
				+ Constants.DOUBLESTRING8 + " " + Constants.DOUBLESTRING9 + " "
				+ Constants.DOUBLESTRING10;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleNumString
				.getBytes("UTF-8")));

		RealToken token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING1, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING1).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING2, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(9, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				"12312.300000000001").doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING3, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(19, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				"12312.300000000001").doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING4, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(29, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf("1.23123")
				.doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING5, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(40, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf("1.23123")
				.doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING6, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(51, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING6).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING7, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(60, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				"-12312.300000000001").doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING8, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(71, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				"-12312.300000000001").doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING9, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(82, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				Constants.DOUBLESTRING9).doubleValue());

		token = (RealToken) this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING10, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(94, token.getColumn().intValue());
		assertTrue(token.getDoubleValue() == Double.valueOf(
				"-12312.300000000001").doubleValue());
	}

	/**
	 * Test for getting a double value for input which is out of range of type
	 * {@link Double}
	 */
	@Test
	public void formatRealValuesOutOfRangeTest() {
		RealToken token = new RealTokenImpl(Constants.DOUBLESTRINGOUTOFRANGE1,
				null, null, null);
		assertTrue(token.getDoubleValue() == null);

		token = new RealTokenImpl(Constants.DOUBLESTRINGOUTOFRANGE2, null,
				null, null);
		assertTrue(token.getDoubleValue() == null);
	}

	/**
	 * Test for tokenizing of a real value with semicolon at the end
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void tokenizeRealValueWithSemicolonAtEndTest()
			throws UnsupportedEncodingException {
		String simpleRealString = Constants.DOUBLESTRING1 + Constants.SEMICOLON;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleRealString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESTRING1, token.getValue());
		assertEquals(TokenType.REAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.SEMICOLON, token.getValue());
		assertEquals(TokenType.SEMICOLON, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(8, token.getColumn().intValue());
	}
}