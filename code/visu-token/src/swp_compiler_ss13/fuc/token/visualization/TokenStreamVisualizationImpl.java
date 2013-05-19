package swp_compiler_ss13.fuc.token.visualization;

import java.io.PrintStream;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.visualization.TokenStreamVisualization;

/**
 * Implementation of the Interface {@link TokenStreamVisualization}
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class TokenStreamVisualizationImpl implements TokenStreamVisualization {

	@Override
	public void visualizeTokenStream(Lexer lexer) {
		PrintStream outStream = System.out;
		outStream.println("Print tokenstream of current inputfile...\n");

		Token token;
		while ((token = lexer.getNextToken()).getTokenType() != TokenType.EOF) {
			outStream.println("< " + token.getTokenType() + ", "
					+ token.getValue() + " >");
		}

		outStream.println("< " + token.getTokenType() + ", " + token.getValue()
				+ " >");
	}
}
