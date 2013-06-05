package swp_compiler_ss13.fuc.lexer.string;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.fuc.lexer.LexerImpl;
import swp_compiler_ss13.fuc.lexer.util.Constants;

/**
 * Testclass for tokenizing of strings TODO: implement
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class StringTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
	}

	/**
	 * Test for matching of strings
	 */
	@Test
	public void matchingStringsTest() {
		/*
		 * FIXME: PA.invokeMethode() throws IllegalArgumentException
		 * 
		 * PA.setValue(this.lexer, "actualTokenValue", Constants.STRING1);
		 * PA.invokeMethod(this.lexer, "matchToken()");
		 * assertEquals(TokenType.STRING, PA.getValue(this.lexer,
		 * "actualTokenType"));
		 * 
		 * PA.setValue(this.lexer, "actualTokenValue", Constants.STRING2);
		 * PA.invokeMethod(this.lexer, "matchToken()");
		 * assertEquals(TokenType.STRING, PA.getValue(this.lexer,
		 * "actualTokenType"));
		 */
	}

	/**
	 * Test for tokenizing of strings
	 * 
	 * @throws UnsupportedEncodingException
	 *             : UTF-8 encoding not supported
	 */
	@Test
	public void simpleTokenizingStringsTest()
			throws UnsupportedEncodingException {
		String simpleStringString = Constants.STRING1 + " " + Constants.STRING2;
		this.lexer.setSourceStream(new ByteArrayInputStream(simpleStringString
				.getBytes("UTF-8")));

		this.lexer.getNextToken();

		this.lexer.getNextToken();
	}
}
