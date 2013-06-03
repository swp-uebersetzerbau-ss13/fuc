package swp_compiler_ss13.fuc.lexer.string;

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
 * Testclass for tokenizing of type symbols
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class TypeSymbolTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of type symbols
	 */
	@Test
	public void matchingTypeSymbolsTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LONGSYMBOL);
		assertEquals(TokenType.LONG_SYMBOL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOUBLESYMBOL);
		assertEquals(TokenType.DOUBLE_SYMBOL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.BOOLSYMBOL);
		assertEquals(TokenType.BOOL_SYMBOL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.STRINGSYMBOL);
		assertEquals(TokenType.STRING_SYMBOL, tokenType);

	}

	/**
	 * Test for tokenizing of type symbols
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingTypeSymbolsTest()
			throws UnsupportedEncodingException {

		String simpleTypeSymbolString = Constants.LONGSYMBOL + " "
				+ Constants.DOUBLESYMBOL + " " + Constants.BOOLSYMBOL + " "
				+ Constants.STRINGSYMBOL;

		this.lexer.setSourceStream(new ByteArrayInputStream(
				simpleTypeSymbolString.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.LONGSYMBOL, token.getValue());
		assertEquals(TokenType.LONG_SYMBOL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.DOUBLESYMBOL, token.getValue());
		assertEquals(TokenType.DOUBLE_SYMBOL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(6, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.BOOLSYMBOL, token.getValue());
		assertEquals(TokenType.BOOL_SYMBOL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(13, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.STRINGSYMBOL, token.getValue());
		assertEquals(TokenType.STRING_SYMBOL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(18, token.getColumn().intValue());
	}
}
