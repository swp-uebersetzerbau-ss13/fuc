package lexer.other;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;
import lexer.LexerImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Testclass for tokenizing of operation symbols
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class OperationTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of operation symbols
	 */
	@Test
	public void matchingOperationSymbolsTest() {
		TokenType tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.ASSIGNOPSTRING);
		assertEquals(TokenType.ASSIGNOP, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.ANDSTRING);
		assertEquals(TokenType.AND, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.ORSTRING);
		assertEquals(TokenType.OR, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.EQUALSSTRING);
		assertEquals(TokenType.EQUALS, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.NOTEQUALSSTRING);
		assertEquals(TokenType.NOT_EQUALS, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LESSSTRING);
		assertEquals(TokenType.LESS, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.LESS_OR_EQUALSTRING);
		assertEquals(TokenType.LESS_OR_EQUAL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.GREATERSTRING);
		assertEquals(TokenType.GREATER, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.GREATER_EQUALSTRING);
		assertEquals(TokenType.GREATER_EQUAL, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.PLUSSTRING);
		assertEquals(TokenType.PLUS, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.MINUSSTRING);
		assertEquals(TokenType.MINUS, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.TIMESSTRING);
		assertEquals(TokenType.TIMES, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.DIVIDESTRING);
		assertEquals(TokenType.DIVIDE, tokenType);

		tokenType = (TokenType) PA.invokeMethod(this.lexer,
				"matchToken(java.lang.String)", Constants.NOTSTRING);
		assertEquals(TokenType.NOT, tokenType);
	}

	/**
	 * Test for tokenizing of operation symbols
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingOfOperationSymbolsTest()
			throws UnsupportedEncodingException {
		String operationString = Constants.ASSIGNOPSTRING + " "
				+ Constants.ANDSTRING + " " + Constants.ORSTRING + " "
				+ Constants.EQUALSSTRING + " " + Constants.NOTEQUALSSTRING
				+ " " + Constants.LESSSTRING + " "
				+ Constants.LESS_OR_EQUALSTRING + " " + Constants.GREATERSTRING
				+ " " + Constants.GREATER_EQUALSTRING + " "
				+ Constants.PLUSSTRING + " " + Constants.MINUSSTRING + " "
				+ Constants.TIMESSTRING + " " + Constants.DIVIDESTRING + " "
				+ Constants.NOTSTRING;

		this.lexer.setSourceStream(new ByteArrayInputStream(operationString
				.getBytes("UTF-8")));

		Token token = this.lexer.getNextToken();
		assertEquals(Constants.ASSIGNOPSTRING, token.getValue());
		assertEquals(TokenType.ASSIGNOP, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.ANDSTRING, token.getValue());
		assertEquals(TokenType.AND, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.ORSTRING, token.getValue());
		assertEquals(TokenType.OR, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.EQUALSSTRING, token.getValue());
		assertEquals(TokenType.EQUALS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.NOTEQUALSSTRING, token.getValue());
		assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.LESSSTRING, token.getValue());
		assertEquals(TokenType.LESS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.LESS_OR_EQUALSTRING, token.getValue());
		assertEquals(TokenType.LESS_OR_EQUAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.GREATERSTRING, token.getValue());
		assertEquals(TokenType.GREATER, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.GREATER_EQUALSTRING, token.getValue());
		assertEquals(TokenType.GREATER_EQUAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.PLUSSTRING, token.getValue());
		assertEquals(TokenType.PLUS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.MINUSSTRING, token.getValue());
		assertEquals(TokenType.MINUS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.TIMESSTRING, token.getValue());
		assertEquals(TokenType.TIMES, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.DIVIDESTRING, token.getValue());
		assertEquals(TokenType.DIVIDE, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.NOTSTRING, token.getValue());
		assertEquals(TokenType.NOT, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(1, token.getColumn().intValue());
	}

}
