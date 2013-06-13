/**
 * 
 */
package swp_compiler_ss13.fuc.lexer.milestone.m2;

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
public class CondProgTest {
	private String prog =
		"# return 5\n" +
		"# prints nothing\n" +
		"\n" +
		"bool b;\n" +
		"bool c;\n" +
		"long l;\n" +
		"\n" +
		"string bla;\n" +
		"bla = \"bla\";\n" +
		"\n" +
		"b = true;\n" +
		"c = false;\n" +
		"\n" +
		"l = 4;\n" +
		"\n" +
		"# dangling-else should be resolved as given by indentation\n" +
		"\n" +
		"if ( b )\n" +
		"  if ( c || ! b )\n" +
		"    print bla;\n" +
		"  else\n" +
		"    l = 5;\n" +
		"\n" +
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
		this.lexer = new swp_compiler_ss13.fuc.lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(
			new TokenImpl("# return 5", TokenType.COMMENT, 1, 1),
			new TokenImpl("# prints nothing", TokenType.COMMENT, 2, 1),
			new TokenImpl("bool", TokenType.BOOL_SYMBOL, 4, 1),
			new TokenImpl("b", TokenType.ID, 4, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 4, 7),
			new TokenImpl("bool", TokenType.BOOL_SYMBOL, 5, 1),
			new TokenImpl("c", TokenType.ID, 5, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 5, 7),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 6, 1),
			new TokenImpl("l", TokenType.ID, 6, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 6, 7),
			new TokenImpl("string", TokenType.STRING_SYMBOL, 8, 1),
			new TokenImpl("bla", TokenType.ID, 8, 8),
			new TokenImpl(";", TokenType.SEMICOLON, 8, 11),
			new TokenImpl("bla", TokenType.ID, 9, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 9, 5),
			new TokenImpl("\"bla\"", TokenType.STRING, 9, 7),
			new TokenImpl(";", TokenType.SEMICOLON, 9, 12),
			new TokenImpl("b", TokenType.ID, 11, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 11, 3),
			new TokenImpl("true", TokenType.TRUE, 11, 5),
			new TokenImpl(";", TokenType.SEMICOLON, 11, 9),
			new TokenImpl("c", TokenType.ID, 12, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 12, 3),
			new TokenImpl("false", TokenType.FALSE, 12, 5),
			new TokenImpl(";", TokenType.SEMICOLON, 12, 10),
			new TokenImpl("l", TokenType.ID, 14, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 14, 3),
			new TokenImpl("4", TokenType.NUM, 14, 5),
			new TokenImpl(";", TokenType.SEMICOLON, 14, 6),
			new TokenImpl("# dangling-else should be resolved as given by indentation", TokenType.COMMENT, 16, 1),
			new TokenImpl("if", TokenType.IF, 18, 1),
			new TokenImpl("(", TokenType.LEFT_PARAN, 18, 4),
			new TokenImpl("b", TokenType.ID, 18, 6),
			new TokenImpl(")", TokenType.RIGHT_PARAN, 18, 8),
			new TokenImpl("if", TokenType.IF, 19, 3),
			new TokenImpl("(", TokenType.LEFT_PARAN, 19, 6),
			new TokenImpl("c", TokenType.ID, 19, 8),
			new TokenImpl("||", TokenType.OR, 19, 10),
			new TokenImpl("!", TokenType.NOT, 19, 13),
			new TokenImpl("b", TokenType.ID, 19, 15),
			new TokenImpl(")", TokenType.RIGHT_PARAN, 19, 17),
			new TokenImpl("print", TokenType.PRINT, 20, 5),
			new TokenImpl("bla", TokenType.ID, 20, 11),
			new TokenImpl(";", TokenType.SEMICOLON, 20, 14),
			new TokenImpl("else", TokenType.ELSE, 21, 3),
			new TokenImpl("l", TokenType.ID, 22, 5),
			new TokenImpl("=", TokenType.ASSIGNOP, 22, 7),
			new TokenImpl("5", TokenType.NUM, 22, 9),
			new TokenImpl(";", TokenType.SEMICOLON, 22, 10),
			new TokenImpl("return", TokenType.RETURN, 24, 1),
			new TokenImpl("l", TokenType.ID, 24, 8),
			new TokenImpl(";", TokenType.SEMICOLON, 24, 9),
			new TokenImpl("$", TokenType.EOF, 25, 1)
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
			assertEquals(comparisontoken.getValue(), token.getValue());
			assertEquals(comparisontoken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
