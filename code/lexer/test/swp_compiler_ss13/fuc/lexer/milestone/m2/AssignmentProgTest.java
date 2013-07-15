package swp_compiler_ss13.fuc.lexer.milestone.m2;

import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
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
public class AssignmentProgTest {
	private String prog =
		"# returns 10\n" +
		"# prints nothing\n" +
		"long a;\n" +
		"long b;\n" +
		"long c;\n" +
		"\n" +
		"a = 4;\n" +
		"b = 3;\n" +
		"c = 2;\n" +
		"\n" +
		"a = b = 4;\n" +
		"c = a + b + c;\n" +
		"\n" +
		"return c;";
	private InputStream stream;
	private LexerImpl lexer;
	private ArrayList<Token> list;

	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream(prog.getBytes());
		this.lexer = new swp_compiler_ss13.fuc.lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(new TokenImpl("# returns 10", TokenType.COMMENT, 1, 1),
			new TokenImpl("# prints nothing", TokenType.COMMENT, 2, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 3, 1),
			new TokenImpl("a", TokenType.ID, 3, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 3, 7),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 4, 1),
			new TokenImpl("b", TokenType.ID, 4, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 4, 7),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 5, 1),
			new TokenImpl("c", TokenType.ID, 5, 6),
			new TokenImpl(";", TokenType.SEMICOLON, 5, 7),
			new TokenImpl("a", TokenType.ID, 7, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 7, 3),
			new TokenImpl("4", TokenType.NUM, 7, 5),
			new TokenImpl(";", TokenType.SEMICOLON, 7, 6),
			new TokenImpl("b", TokenType.ID, 8, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 8, 3),
			new TokenImpl("3", TokenType.NUM, 8, 5),
			new TokenImpl(";", TokenType.SEMICOLON, 8, 6),
			new TokenImpl("c", TokenType.ID, 9, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 9, 3),
			new TokenImpl("2", TokenType.NUM, 9, 5),
			new TokenImpl(";", TokenType.SEMICOLON, 9, 6),
			new TokenImpl("a", TokenType.ID, 11, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 11, 3),
			new TokenImpl("b", TokenType.ID, 11, 5),
			new TokenImpl("=", TokenType.ASSIGNOP, 11, 7),
			new TokenImpl("4", TokenType.NUM, 11, 9),
			new TokenImpl(";", TokenType.SEMICOLON, 11, 10),
			new TokenImpl("c", TokenType.ID, 12, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 12, 3),
			new TokenImpl("a", TokenType.ID, 12, 5),
			new TokenImpl("+", TokenType.PLUS, 12, 7),
			new TokenImpl("b", TokenType.ID, 12, 9),
			new TokenImpl("+", TokenType.PLUS, 12, 11),
			new TokenImpl("c", TokenType.ID, 12, 13),
			new TokenImpl(";", TokenType.SEMICOLON, 12, 14),
			new TokenImpl("return", TokenType.RETURN, 14, 1),
			new TokenImpl("c", TokenType.ID, 14, 8),
			new TokenImpl(";", TokenType.SEMICOLON, 14, 9),
			new TokenImpl(null, TokenType.EOF, 14, 10)
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
			
			if (token.getTokenType().equals(TokenType.NUM)) {

				NumToken comparisonNumToken = new NumTokenImpl(
						comparisonToken.getValue(), null, null, null);
				NumToken numToken = new NumTokenImpl(token.getValue(), null,
						null, null);
				assertEquals(comparisonNumToken.getLongValue(),
						numToken.getLongValue());

			}

		} while (token.getTokenType() != TokenType.EOF);
	}

}
