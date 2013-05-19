/**
 * 
 */
package m1;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import lexer.LexerImpl;
import lexer.token.TokenImpl;
import static org.junit.Assert.*;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import org.junit.Before;
import org.junit.Test;

/**
 * @author Tay, Ho Phuong
 * 
 */
public class Error_Invalid_IdsProgTest {
	private String prog = 
		"# error: invalid ids\n" +
		"long foo$bar;\n" +
		"long spam_ham;\n" +
		"long 2fooly;\n" +
		"long return;\n" +
		"long string;\n" +
		"long bool;\n" +
		"long fü_berlin;";
	private InputStream stream;
	private LexerImpl lexer;
	private ArrayList<Token> list;

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream(prog.getBytes());
		this.lexer = new lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(
			new TokenImpl("# error: invalid ids", TokenType.COMMENT, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("foo$bar", TokenType.NOT_A_TOKEN, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("spam_ham", TokenType.ID, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("2fooly", TokenType.NOT_A_TOKEN, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("return", TokenType.RETURN, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("string", TokenType.STRING_SYMBOL, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("bool", TokenType.BOOL_SYMBOL, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("fü_berlin", TokenType.NOT_A_TOKEN, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("$", TokenType.EOF, 1, 1)
		));
	}

	@Test
	public void testgetNextToken() {
		Token token = null;
		Token comparisontoken = null;

		do {
			comparisontoken = list.remove(0);
			token = this.lexer.getNextToken();

			assertTrue(token != null);
			assertEquals(comparisontoken.getValue().length(), token.getValue()
					.length());
			assertEquals(comparisontoken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
