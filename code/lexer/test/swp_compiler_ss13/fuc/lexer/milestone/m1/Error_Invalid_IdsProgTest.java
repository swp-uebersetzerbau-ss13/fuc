/**
 * 
 */
package swp_compiler_ss13.fuc.lexer.milestone.m1;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;
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
		this.lexer = new swp_compiler_ss13.fuc.lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(
			new TokenImpl("# error: invalid ids", TokenType.COMMENT, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 2, 1),
			new TokenImpl("foo$bar", TokenType.NOT_A_TOKEN, 2, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 2, 13),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 3, 1),
			new TokenImpl("spam_ham", TokenType.ID, 3, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 3, 14),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 4, 1),
			new TokenImpl("2fooly", TokenType.NOT_A_TOKEN, 4, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 4, 12),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 5, 1),
			new TokenImpl("return", TokenType.RETURN, 5, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 5, 12),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 6, 1),
			new TokenImpl("string", TokenType.STRING_SYMBOL, 6, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 6, 12),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 7, 1),
			new TokenImpl("bool", TokenType.BOOL_SYMBOL, 7, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 7, 10),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 8, 1),
			new TokenImpl("fü_berlin", TokenType.NOT_A_TOKEN, 8, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 8, 15),
			new TokenImpl("$", TokenType.EOF, 9, 1)
		));
	}

	@Test
	public void testgetNextToken() {
		Token token = null;
		Token comparisontoken = null;

		do {
			comparisontoken = list.remove(0);
			token = this.lexer.getNextToken();
			//System.out.println("new TokenImpl(\""+token.getValue()+"\", TokenType."+token.getTokenType()+", "+token.getLine()+", "+token.getColumn()+"),");
			assertTrue(token != null);
			assertEquals(comparisontoken.getValue(), token.getValue());
			assertEquals(comparisontoken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
