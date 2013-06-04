package swp_compiler_ss13.fuc.lexer.other;

import static org.junit.Assert.assertEquals;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.util.Constants;

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
		PA.setValue(this.lexer, "actualTokenValue", Constants.ASSIGNOPSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.ASSIGNOP,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.ANDSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.AND, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.ORSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.OR, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.EQUALSSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.EQUALS,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.NOTEQUALSSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NOT_EQUALS,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.LESSSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.LESS, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue",
				Constants.LESS_OR_EQUALSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.LESS_OR_EQUAL,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.GREATERSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.GREATER,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue",
				Constants.GREATER_EQUALSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.GREATER_EQUAL,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.PLUSSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.PLUS, PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.MINUSSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.MINUS,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.TIMESSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.TIMES,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.DIVIDESTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.DIVIDE,
				PA.getValue(this.lexer, "actualTokenType"));

		PA.setValue(this.lexer, "actualTokenValue", Constants.NOTSTRING);
		PA.invokeMethod(this.lexer, "matchToken()");
		assertEquals(TokenType.NOT, PA.getValue(this.lexer, "actualTokenType"));
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
		assertEquals(3, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.ORSTRING, token.getValue());
		assertEquals(TokenType.OR, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(6, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.EQUALSSTRING, token.getValue());
		assertEquals(TokenType.EQUALS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(9, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.NOTEQUALSSTRING, token.getValue());
		assertEquals(TokenType.NOT_EQUALS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(12, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.LESSSTRING, token.getValue());
		assertEquals(TokenType.LESS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(15, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.LESS_OR_EQUALSTRING, token.getValue());
		assertEquals(TokenType.LESS_OR_EQUAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(17, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.GREATERSTRING, token.getValue());
		assertEquals(TokenType.GREATER, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(20, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.GREATER_EQUALSTRING, token.getValue());
		assertEquals(TokenType.GREATER_EQUAL, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(22, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.PLUSSTRING, token.getValue());
		assertEquals(TokenType.PLUS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(25, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.MINUSSTRING, token.getValue());
		assertEquals(TokenType.MINUS, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(27, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.TIMESSTRING, token.getValue());
		assertEquals(TokenType.TIMES, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(29, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.DIVIDESTRING, token.getValue());
		assertEquals(TokenType.DIVIDE, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(31, token.getColumn().intValue());

		token = this.lexer.getNextToken();
		assertEquals(Constants.NOTSTRING, token.getValue());
		assertEquals(TokenType.NOT, token.getTokenType());
		assertEquals(1, token.getLine().intValue());
		assertEquals(33, token.getColumn().intValue());
	}

}
