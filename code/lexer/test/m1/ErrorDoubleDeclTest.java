package m1;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import lexer.LexerImpl;
import lexer.token.TokenImpl;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

public class ErrorDoubleDeclTest {
	private final String prog = "# error: two decls for same id i\nlong i;\nlong i;";
	private InputStream stream;
	private LexerImpl lexer;
	private ArrayList<Token> list;

	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream(this.prog.getBytes());
		this.lexer = new lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(new TokenImpl(
				"# error: two decls for same id i", TokenType.COMMENT, 1, 1),
				new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1),
				new TokenImpl("i", TokenType.ID, 1, 1), new TokenImpl(";",
						TokenType.SEMICOLON, 1, 1), new TokenImpl("long",
						TokenType.LONG_SYMBOL, 1, 1), new TokenImpl("i",
						TokenType.ID, 1, 1), new TokenImpl(";",
						TokenType.SEMICOLON, 1, 1), new TokenImpl("$",
						TokenType.EOF, 1, 1)));
	}

	@Test
	public void testgetNextToken() {
		Token token = null;
		Token comparisontoken = null;

		do {
			comparisontoken = this.list.remove(0);
			token = this.lexer.getNextToken();

			assertTrue(token != null);
			assertEquals(comparisontoken.getValue(), token.getValue());
			assertEquals(comparisontoken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
