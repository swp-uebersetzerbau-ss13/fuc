package swp_compiler_ss13.fuc.token.test;

import java.io.ByteArrayInputStream;

import lexer.LexerImpl;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.fuc.token.visualization.TokenStreamVisualizationImpl;

public class TokenStreamVisualizationTest {
	private Lexer lexer;

	@Before
	public void setUp() throws Exception {
		this.lexer = new LexerImpl();
		String addPorg = "# return 27\n" + "long l;\n" + "l = 10 +\n"
				+ "        23 # - 23\n" + "- 23\n" + "+ 100 /\n" + "\n" + "2\n"
				+ "-       30 \n" + "      - 9 / 3;\n" + "return l;";
		this.lexer.setSourceStream(new ByteArrayInputStream(addPorg
				.getBytes("UTF-8")));
	}

	@Test
	public void testTokenVisualization() {
		new TokenStreamVisualizationImpl().visualizeTokenStream(this.lexer);
	}

}
