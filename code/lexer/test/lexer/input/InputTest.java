package lexer.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;

import junit.extensions.PA;
import lexer.LexerImpl;

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
	 * Test for reading the first Token with correct loc and coc
	 */
	@Test
	public void readFirstTokenTest() {
		Token token = this.lexer.getNextToken();
		assertTrue(token != null);
	}

	@Test
	public void getStringsFromInputStreamArrayTest() {
		String tokenString = (String) PA.invokeMethod(this.lexer,
				"abstractToken()");
		assertTrue(PA.getValue(this.lexer, "convertedLines") != null);
		assertEquals("test1", tokenString);
	}

	@Test
	public void getActualColumnOfTokenStringTest() {
		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(2, PA.getValue(this.lexer, "actualColumn"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(8, PA.getValue(this.lexer, "actualColumn"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(1, PA.getValue(this.lexer, "actualColumn"));
	}

	@Test
	public void getActualLineOfTokenStringTest() {
		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(1, PA.getValue(this.lexer, "actualLine"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(1, PA.getValue(this.lexer, "actualLine"));

		PA.invokeMethod(this.lexer, "abstractToken()");
		assertEquals(2, PA.getValue(this.lexer, "actualLine"));

	}
}
