package lexer.input;

import static org.junit.Assert.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import lexer.LexerTestSuite;
import swp_compiler_ss13.common.lexer.LexerClass;
import swp_compiler_ss13.common.lexer.TokenClass;

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
	private LexerClass lexer;

	@Before
	public void setUp() throws Exception {
		this.stream = new FileInputStream(LexerTestSuite.fileLocation1);
		this.lexer = new LexerClass();
		this.lexer.setSourceStream(this.stream);

	}

	@After
	public void tearDown() throws Exception {
		
	}

	@Test
	public void setInputStreamCorrect() throws IOException {
		assertTrue(this.lexer.is != null);

		assertEquals(this.stream.read() != -1, this.lexer.is.read() != -1);
	}

	@Test
	public void readTokenCorrect() {
		
		TokenClass token = this.lexer.getNextToken();
		assertTrue(token != null);
	}
	
}
