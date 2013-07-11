/**
 * 
 */
package swp_compiler_ss13.fuc.lexer.milestone.m1;

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
public class Error_Multiple_Pluses_in_expProgTest {
	private String prog = 
		"# error: too many pluses in an expression\n" +
		"long foo;\n" +
		"long bar;\n" +
		"foo = 3;\n" +
		"bar = foo ++ 1;";
	private InputStream stream;
	private LexerImpl lexer;
	private ArrayList<Token> list;

	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream(prog.getBytes());
		this.lexer = new swp_compiler_ss13.fuc.lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(
			new TokenImpl("# error: too many pluses in an expression", TokenType.COMMENT, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("foo", TokenType.ID, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
			new TokenImpl("bar", TokenType.ID, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("foo", TokenType.ID, 1, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 1, 1),
			new TokenImpl("3", TokenType.NUM, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl("bar", TokenType.ID, 1, 1),
			new TokenImpl("=", TokenType.ASSIGNOP, 1, 1),
			new TokenImpl("foo", TokenType.ID, 1, 1),
			new TokenImpl("+", TokenType.PLUS, 1, 1),
			new TokenImpl("+", TokenType.PLUS, 1, 1),
			new TokenImpl("1", TokenType.NUM, 1, 1),
			new TokenImpl(";", TokenType.SEMICOLON, 1, 1),
			new TokenImpl(null, TokenType.EOF, 1, 1)
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
