package lexer.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import junit.extensions.PA;
import lexer.LexerImpl;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Token;

/**
 * 
 * @author "Thomas Benndorf", "Tay Phuong Ho"
 * 
 */
public class InputStreamTest {
	private InputStream stream;
	private LexerImpl lexer;

	@Before
	public void setUp() throws Exception {
		this.stream = new ByteArrayInputStream("test".getBytes("UTF-8"));
		this.lexer = new LexerImpl();
		this.lexer.setSourceStream(this.stream);

	}

	/**
	 * Test for set the {@link InputStream} in Class {@link LexerImpl} correct
	 * 
	 * @throws IOException
	 *             : Can't read from {@link InputStream}
	 */
	@Test
	public void setInputStreamTest() throws IOException {
		assertTrue((InputStream) PA.getValue(this.lexer, "inputStream") != null);

		assertEquals(
				this.stream.read() != -1,
				((InputStream) PA.getValue(this.lexer, "inputStream")).read() != -1);
	}

	/**
	 * Test for reading the first Token
	 */
	@Test
	public void readFirstTokenTest() {
		Token token = this.lexer.getNextToken();

		assertTrue(token != null);
	}

}
