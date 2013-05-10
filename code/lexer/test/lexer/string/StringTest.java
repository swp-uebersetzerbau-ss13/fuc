package lexer.string;

import static org.junit.Assert.assertEquals;
import junit.extensions.PA;
import lexer.LexerImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of strings
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
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.IFSTRING);
		assertEquals(TokenType.IF, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.WHILESTRING);
		assertEquals(TokenType.WHILE, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DOSTRING);
		assertEquals(TokenType.DO, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.BREAKSTRING);
		assertEquals(TokenType.BREAK, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.RETURNSTRING);
		assertEquals(TokenType.RETURN, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.PRINTSTRING);
		assertEquals(TokenType.PRINT, tokenType);
	}

}
