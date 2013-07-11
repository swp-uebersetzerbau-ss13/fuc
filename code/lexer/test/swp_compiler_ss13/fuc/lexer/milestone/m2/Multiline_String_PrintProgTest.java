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
 * @author "Thomas Benndorf" (refactoring)
 */
public class Multiline_String_PrintProgTest {
	private String prog =
		"# return false\n" +
		"# if\n" +
		"# true\n" +
		"# then\n" +
		"# System.out.println(\"Hello World\")\n" +
		"# else\n" +
		"# System.out.println(\"Hello Hell\")\n" +
		"# System.out.println(\"fü-berlin\")\n" +
		"\n" +
		"string s;\n" +
		"\n" +
		"s = \"fü-\n" +
		"berlin\n" +
		"\\n\";  # c-like escaping in multiline string\n" +
		"\n" +
		"print s;\n" +
		"\n" +
		"return;                    # equivalent to return EXIT_SUCCESS";
	private InputStream stream;
	private LexerImpl lexer;
	private ArrayList<Token> list;

	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream(prog.getBytes());
		this.lexer = new swp_compiler_ss13.fuc.lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(
			new TokenImpl("# return false", TokenType.COMMENT, 1, 1),
			new TokenImpl("# if", TokenType.COMMENT, 2, 1),
			new TokenImpl("# true", TokenType.COMMENT, 3, 1),
			new TokenImpl("# then", TokenType.COMMENT, 4, 1),
			new TokenImpl("# System.out.println(\"Hello World\")", TokenType.COMMENT, 5, 1),
			new TokenImpl("# else", TokenType.COMMENT, 6, 1),
			new TokenImpl("# System.out.println(\"Hello Hell\")", TokenType.COMMENT, 7, 1),
			new TokenImpl("# System.out.println(\"fü-berlin\")", TokenType.COMMENT, 8, 1),
			new TokenImpl("string", TokenType.STRING_SYMBOL, 10, 1),
			new TokenImpl("s", TokenType.ID, 10, 8),
			new TokenImpl(";", TokenType.SEMICOLON, 10, 9),
			new TokenImpl("s", TokenType.ID, 12, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 12, 3),
			new TokenImpl("\"fü", TokenType.NOT_A_TOKEN, 12, 5),
			new TokenImpl("-", TokenType.MINUS, 15, 5),
			new TokenImpl("berlin", TokenType.ID, 13, 1),
			new TokenImpl("\\n\"", TokenType.NOT_A_TOKEN, 14, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 14, 4),
			new TokenImpl("# c-like escaping in multiline string", TokenType.COMMENT, 14, 7),
			new TokenImpl("print", TokenType.PRINT, 16, 1),
			new TokenImpl("s", TokenType.ID, 16, 7),
			new TokenImpl(";", TokenType.SEMICOLON, 16, 8),
			new TokenImpl("return", TokenType.RETURN, 18, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 18, 7),
			new TokenImpl("# equivalent to return EXIT_SUCCESS", TokenType.COMMENT, 18, 28),
			new TokenImpl(null, TokenType.EOF, 18, 0)

		));
	}

	@Test
	public void testgetNextToken() {
		Token token = null;
		Token comparisonToken = null;

		do {
			comparisonToken = list.remove(0);
			token = this.lexer.getNextToken();

			assertEquals(comparisonToken.getValue(), token.getValue());		
			assertEquals(comparisonToken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
