package lexer.milestones;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;

import junit.extensions.PA;
import lexer.LexerImpl;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * TODO: to implement
 * 
 * @author "Ho, Tay Phuong", "Thomas Benndorf"
 * 
 */
@RunWith(value = Parameterized.class)
public class MilestoneTest {
	private final InputStream stream;
	private LexerImpl lexer;

	@Parameters
	public static Collection<Object[]> data() {
		Object[][] data = new Object[][] {
				{ "~/common/examples/m1/add.prog" },
				{ "~/common/examples/m1/error_double_decl.prog" },
				{ "~/common/examples/m1/error_invalid_ids.prog" },
				{ "~/common/examples/m1/error_multiple_minus_e_notation.prog" },
				{ "~/common/examples/m1/error_multiple_pluses_in_exp.prog" },
				{ "~/common/examples/m1/error_undef_return.prog" },
				{ "~/common/examples/m1/paratheses.prog" },
				{ "~/common/examples/m1/simple_add.prog" },
				{ "~/common/examples/m1/simple_mul.prog" } };
		return Arrays.asList(data);
	}

	public MilestoneTest(String fileLocation) throws Exception {
		this.stream = new FileInputStream(fileLocation);
	}

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
		this.lexer.setSourceStream(this.stream);
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testsetSourceStream() throws IOException {
		assertTrue((InputStream) PA.getValue(this.lexer, "inputStream") != null);

		assertEquals(
				this.stream.read() != -1,
				((InputStream) PA.getValue(this.lexer, "inputStream")).read() != -1);
	}

	@Test
	public void testgetNextToken() {
		Token token = this.lexer.getNextToken();

		while (token.getTokenType() != TokenType.EOF) {
			assertTrue(token != null);

			token = this.lexer.getNextToken();
		}
	}

}
