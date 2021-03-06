package swp_compiler_ss13.fuc.lexer.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
import swp_compiler_ss13.fuc.lexer.util.Constants;

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
		PA.setValue(this.lexer, "actualTokenValue", Constants.LONGSTRING1);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NUM, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LONGSTRING2);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NUM, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LONGSTRING3);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NUM, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LONGSTRING4);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NUM, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LONGSTRING5);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NUM, PA.getValue(this.lexer, "actualTokenType"));
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
				+ Constants.LONGSTRING4 + " " + Constants.LONGSTRING5 + " "
				+ Constants.LONGSTRING6 + " " + Constants.LONGSTRING7 + " "
				+ Constants.LONGSTRING8 + " " + Constants.LONGSTRING9;

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
		assertTrue(token.getLongValue() == Long.valueOf("12300").longValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING3, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(11, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf("12300").longValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING4, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(17, token.getColumn().intValue());
		assertEquals(Constants.LONGSTRING4, token.getValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING5, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(24, token.getColumn().intValue());
		assertEquals(Constants.LONGSTRING5, token.getValue());

		Token normalToken = this.lexer.getNextToken();
		assertEquals(Constants.MINUSSTRING, normalToken.getValue());
		assertEquals(TokenType.MINUS, normalToken.getTokenType());
		assertEquals(1, normalToken.getLine().intValue());
		assertEquals(31, normalToken.getColumn().intValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING6.substring(1), token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(32, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf(
				Constants.LONGSTRING6.substring(1)).longValue());

		normalToken = this.lexer.getNextToken();
		assertEquals(Constants.MINUSSTRING, normalToken.getValue());
		assertEquals(TokenType.MINUS, normalToken.getTokenType());
		assertEquals(1, normalToken.getLine().intValue());
		assertEquals(36, normalToken.getColumn().intValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING7.substring(1), token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(37, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf("12300").longValue());

		normalToken = this.lexer.getNextToken();
		assertEquals(Constants.MINUSSTRING, normalToken.getValue());
		assertEquals(TokenType.MINUS, normalToken.getTokenType());
		assertEquals(1, normalToken.getLine().intValue());
		assertEquals(43, normalToken.getColumn().intValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING8.substring(1), token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(44, token.getColumn().intValue());
		assertTrue(token.getLongValue() == Long.valueOf("12300").longValue());

		normalToken = this.lexer.getNextToken();
		assertEquals(Constants.MINUSSTRING, normalToken.getValue());
		assertEquals(TokenType.MINUS, normalToken.getTokenType());
		assertEquals(1, normalToken.getLine().intValue());
		assertEquals(50, normalToken.getColumn().intValue());

		token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING9.substring(1), token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(51, token.getColumn().intValue());
		assertEquals(Constants.LONGSTRING9.substring(1), token.getValue());
	}

	/**
	 * Test for getting a long value for input which is out of range of type
	 * {@link Long}
	 */
	@Test
	public void formatNumValuesOutOfRangeTest() {
		NumToken token = new NumTokenImpl(Constants.LONGSTRINGOUTOFRANGE1,
				null, null, null);
		assertTrue(token.getLongValue() == null);

		token = new NumTokenImpl(Constants.LONGSTRINGOUTOFRANGE2, null, null,
				null);
		assertTrue(token.getLongValue() == null);
	}
}
