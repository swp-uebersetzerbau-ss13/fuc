package lexer.bool;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import lexer.LexerImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.BoolToken;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of boolean types
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class BoolTokenTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for tokenizing of boolean types
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfBooleanTypesTest()
			throws UnsupportedEncodingException {
		String simpleKeywordString = Constants.TRUESTRING + " "
				+ Constants.FALSESTRING;

		this.lexer.setSourceStream(new ByteArrayInputStream(simpleKeywordString
				.getBytes("UTF-8")));

		BoolToken token = (BoolToken) this.lexer.getNextToken();
		assertEquals(Constants.TRUESTRING, token.getValue());
		assertEquals(TokenType.TRUE, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.TRUESTRING));
		assertTrue(token.getBooleanValue());

		token = (BoolToken) this.lexer.getNextToken();
		assertEquals(Constants.FALSESTRING, token.getValue());
		assertEquals(TokenType.FALSE, token.getTokenType());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == simpleKeywordString
				.indexOf(Constants.FALSESTRING));
		assertFalse(token.getBooleanValue());

	}

}
