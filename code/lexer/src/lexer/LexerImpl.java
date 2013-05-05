package lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import lexer.token.BoolTokenImpl;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Implementation of the interface {@link Lexer}
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class LexerImpl implements Lexer {
	private Token actualToken;
	private ArrayList<String> convertedLines;
	// private String actualTokenValue;
	private Integer actualLine = 1;
	private Integer actualColumn = 1;
	private Integer actualCountOfTokenInLine = 0;

	@Override
	public void setSourceStream(InputStream stream) {
		this.convertedLines = new ArrayList<>();
		Scanner scanner = new Scanner(stream, "UTF-8");
		while (scanner.hasNext()) {
			this.convertedLines.add(scanner.useDelimiter("\\n").next());
		}
		scanner.close();
	}

	@Override
	public Token getNextToken() {
		this.matchToken(this.abstractToken());
		this.actualToken = new BoolTokenImpl(null, null, null, null);
		return this.actualToken;
	}

	private TokenType matchToken(String nextToken) {
		if (nextToken.matches("[0-9]+((e|E)-?[0-9]+)?")) {
			return TokenType.NUM;
		} else if (nextToken.matches("[0-9]+\\.[0-9]+((e|E)-?[0-9]+)?")) {
			return TokenType.REAL;
			// } else if (nextToken.matches(";")) {
			// return TokenType.SEMICOLON;
		} else if (nextToken.matches("\\)")) {
			return TokenType.LEFT_PARAN;
		} else if (nextToken.matches("\\(")) {
			return TokenType.RIGHT_PARAN;
		} else if (nextToken.matches("\\}")) {
			return TokenType.LEFT_BRACE;
		} else if (nextToken.matches("\\{")) {
			return TokenType.RIGHT_BRACE;
		} else if (nextToken.matches("\\]")) {
			return TokenType.LEFT_BRACKET;
		} else if (nextToken.matches("\\[")) {
			return TokenType.RIGHT_BRACKET;
		} else if (nextToken.matches("#")) {
			return null; // line comment, ignore the rest of the token in this
							// line
			// FIXME: EOF
		} else {
			return TokenType.NOT_A_TOKEN;
		}
	}

	private String abstractToken() {

		if (this.convertedLines.get(this.actualLine - 1).split("\\s+").length == this.actualCountOfTokenInLine + 1) {
			System.err.println("Line: " + this.actualLine + ", Read token: "
					+ this.actualCountOfTokenInLine);
			this.actualLine++;
			this.actualColumn = 1;
			this.actualCountOfTokenInLine = 0;
		}

		String actualTokenValue;
		System.err.println("Actual tokenline: "
				+ this.convertedLines.get(this.actualLine - 1));

		if (!this.convertedLines.get(this.actualLine - 1).startsWith(" ")) {
			actualTokenValue = this.convertedLines.get(this.actualLine - 1)
					.split("\\s+")[this.actualCountOfTokenInLine];
			this.actualColumn = this.convertedLines
					.get(this.actualLine - 1)
					.indexOf(
							actualTokenValue,
							(this.actualColumn == 1 ? 0 : this.actualColumn + 1)) + 1;
		} else {
			actualTokenValue = this.convertedLines.get(this.actualLine - 1)
					.split("\\s+")[this.actualCountOfTokenInLine + 1];
			this.actualColumn = this.convertedLines
					.get(this.actualLine - 1)
					.indexOf(
							actualTokenValue,
							(this.actualColumn == 1 ? 0 : this.actualColumn + 1)) + 1;
		}

		this.actualCountOfTokenInLine++;
		System.err.println("Column: " + this.actualColumn + ", Line: "
				+ this.actualLine);

		return actualTokenValue;
	}
}
