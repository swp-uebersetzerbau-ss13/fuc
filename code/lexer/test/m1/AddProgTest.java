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
public class AddProgTest {
	private String prog = 
		"# return 27\n" +
		"long l;\n" +
		"l = 10 +\n" +
		"        23 # - 23\n" +
		"- 23\n" +
		"+ 100 /\n" +
		"\n" +
		"2\n" +
		"-       30 \n" +
		"      - 9 / 3;\n" +
		"return l;";
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
			new TokenImpl("# return 27", TokenType.COMMENT, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("l", TokenType.ID, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("l", TokenType.ID, 1, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 1, 1),
			new TokenImpl("10", TokenType.NUM, 1, 1),
			new TokenImpl("+", TokenType.PLUS, 1, 1),
			new TokenImpl("23", TokenType.NUM, 1, 1),
			new TokenImpl("# - 23", TokenType.COMMENT, 1, 1),
			new TokenImpl("-", TokenType.MINUS, 1, 1),
			new TokenImpl("23", TokenType.NUM, 1, 1),
			new TokenImpl("+", TokenType.PLUS, 1, 1),
			new TokenImpl("100", TokenType.NUM, 1, 1),
			new TokenImpl("/", TokenType.DIVIDE, 1, 1),
			new TokenImpl("2", TokenType.NUM, 1, 1),
			new TokenImpl("-", TokenType.MINUS, 1, 1),
			new TokenImpl("30", TokenType.NUM, 1, 1),
			new TokenImpl("-", TokenType.MINUS, 1, 1),
			new TokenImpl("9", TokenType.NUM, 1, 1),
			new TokenImpl("/", TokenType.DIVIDE, 1, 1),
			new TokenImpl("3", TokenType.NUM, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("return", TokenType.RETURN, 1, 1),
			new TokenImpl("l", TokenType.ID, 1, 1),
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
			System.out.println(token.getValue()+"in : "+token.getTokenType());

			assertTrue(token != null);
			assertEquals(comparisontoken.getValue(), token.getValue());
			assertEquals(comparisontoken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
