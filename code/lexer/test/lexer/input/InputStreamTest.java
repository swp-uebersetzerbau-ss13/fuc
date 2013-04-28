package lexer.input;

import java.io.FileInputStream;
import java.io.InputStream;

import lexer.LexerTestSuite;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 * @author Thomas Benndorf
 * @since 28.04.2013
 * 
 */
public class InputStreamTest {
	private InputStream stream;
	private FucLexer lexer;

	@Before
	public void setUp() throws Exception {
		this.stream = new FileInputStream(LexerTestSuite.fileLocation1);
		this.lexer = new FucLexer();
		this.lexer.setSourceStream(this.stream);

	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void setInputStreamCorrect() {
		assertTrue(this.lexer.stream != null);

		assertEquals(this.stream.read() != -1, this.lexer.stream.read() != -1);
	}

	@Test
	public void readTokenCorrect() {
		FucToken token = this.lexer.getNextToken();
		assertTrue(token != null);
	}
}
