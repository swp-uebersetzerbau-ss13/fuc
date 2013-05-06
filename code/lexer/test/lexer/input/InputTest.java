package lexer.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import junit.extensions.PA;
import lexer.LexerImpl;
import lexer.token.TokenImpl;
import lexer.util.Constants;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Token;

/**
 * Testclass for setting the input of the lexer correct
 * 
 * @author "Thomas Benndorf", "Tay Phuong Ho"
 * 
 */
public class InputTest {
	private LexerImpl lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
		this.lexer.setSourceStream(new ByteArrayInputStream(
				" test1 test2 \ntest3".getBytes("UTF-8")));

	}

	/**
	 * Test for reading an {@link InputStream} and convert it into a
	 * {@link ArrayList} of {@link String} for each line
	 */
	@SuppressWarnings("unchecked")
	@Test
	public void convertInputStreamInStringArrayForLinesTest() {
		ArrayList<String> expectedInputStream = new ArrayList<>();
		expectedInputStream.add(" test1 test2 ");
		expectedInputStream.add("test3");

		ArrayList<String> convertedInputStream = (ArrayList<String>) PA
				.getValue(this.lexer, "convertedLines");

		for (int i = 0; i < convertedInputStream.size(); i++) {
			assertEquals(expectedInputStream.get(i),
					convertedInputStream.get(i));
		}
	}

	/**
	 * Test for reading a Token
	 */
	@Test
	public void getTokenTest() {
		Token token = this.lexer.getNextToken();
		assertTrue(token != null);
		assertEquals("test1", token.getValue());
		assertTrue(token.getLine() == 1);
		assertTrue(token.getColumn() == 2);
	}

	/**
	 * Test for getting the value of the first token using the {@link ArrayList}
	 * including the code lines
	 */
	@Test
	public void getStringsFromInputStreamArrayTest() {
		String tokenString = (String) PA.invokeMethod(this.lexer,
				"abstractToken()");
		assertTrue(PA.getValue(this.lexer, "convertedLines") != null);
		assertEquals("test1", tokenString);
	}

	/**
	 * Test for getting the correct column value of the tokens
	 */
	@Test
	public void getActualColumnOfTokenStringTest() {
		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(2, PA.getValue(this.lexer, "actualColumn"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(8, PA.getValue(this.lexer, "actualColumn"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(1, PA.getValue(this.lexer, "actualColumn"));
	}

	/**
	 * Test for getting the correct line value of the tokens
	 */
	@Test
	public void getActualLineOfTokenStringTest() {
		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(1, PA.getValue(this.lexer, "actualLine"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(1, PA.getValue(this.lexer, "actualLine"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(2, PA.getValue(this.lexer, "actualLine"));
	}

	/**
	 * Test for getting the correct kind of class for a token TODO: add
	 * additional kind of tokens
	 * 
	 * @throws UnsupportedEncodingException
	 */
	@Test
	public void getCorrectClassForTokenTest()
			throws UnsupportedEncodingException {

		Token token;

		/* test keyword values */

		String keywordString = Constants.IFSTRING + " " + Constants.WHILESTRING
				+ " " + Constants.DOSTRING + " " + Constants.BREAKSTRING + " "
				+ Constants.RETURNSTRING + " " + Constants.PRINTSTRING;
		this.lexer.setSourceStream(new ByteArrayInputStream(keywordString
				.getBytes("UTF-8")));

		for (int i = 1; i <= 6; i++) {
			token = this.lexer.getNextToken();
			assertEquals("Error for the " + i + ". token", TokenImpl.class,
					token.getClass());
		}
	}
}
