/**
 * 
 */
package swp_compiler_ss13.fuc.lexer.milestone.m1;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;

/**
 * @author Tay, Ho Phuong
 * @author "Thomas Benndorf" (refactoring)
 */
public class EmptyProgTest {
	private final String prog = "";
	private InputStream stream;
	private LexerImpl lexer;
	private ArrayList<Token> list;

	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream(this.prog.getBytes());
		this.lexer = new swp_compiler_ss13.fuc.lexer.LexerImpl();
		this.lexer.setSourceStream(this.stream);
		this.list = new ArrayList<Token>(Arrays.asList(new TokenImpl(null,
				TokenType.EOF, 1, 1)));
	}

	@Test
	public void testgetNextToken() {
		Token token = null;
		Token comparisonToken = null;

		do {

			comparisonToken = this.list.remove(0);
			token = this.lexer.getNextToken();

			assertEquals(comparisonToken.getValue(), token.getValue());
			assertEquals(comparisonToken.getTokenType(), token.getTokenType());

		} while (token.getTokenType() != TokenType.EOF);
	}

}
