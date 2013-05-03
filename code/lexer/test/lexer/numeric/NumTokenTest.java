package lexer.numeric;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import lexer.LexerImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of num types (double)
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
	 * Test for tokenizing of num types
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfNumTypesTest()
			throws UnsupportedEncodingException {
		String simpleKeywordString = Constants.LONGSTRING1 + " "
				+ Constants.LONGSTRING2 + " " + Constants.LONGSTRING3 + " "
				+ Constants.LONGSTRING4 + " " + Constants.LONGSTRING5;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleKeywordString
				.getBytes("UTF-8")));

		NumToken token = (NumToken) this.lexer.getNextToken();
		assertEquals(Constants.LONGSTRING1, token.getValue());
		assertEquals(TokenType.NUM, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.LONGSTRING1));
		assertTrue(token.getLongValue() == Long.valueOf(Constants.LONGSTRING1));

		// TODO implement the other strings

	}
}
