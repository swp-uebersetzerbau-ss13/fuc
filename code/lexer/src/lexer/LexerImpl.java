package lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import lexer.token.BoolTokenImpl;
import lexer.token.NumTokenImpl;
import lexer.token.RealTokenImpl;
import lexer.token.TokenImpl;
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
	private Integer actualLine;
	private Integer actualColumn;
	private Integer actualCountOfTokenInLine;

	@Override
	public void setSourceStream(InputStream stream) {
		this.init();
		this.convertedLines = new ArrayList<>();
		Scanner scanner = new Scanner(stream, "UTF-8");
		while (scanner.hasNext()) {
			this.convertedLines.add(scanner.useDelimiter("\\n").next());
		}
		scanner.close();
	}

	/**
	 * Method to initialize class variables when getting a new
	 * {@link InputStream}
	 */
	private void init() {
		this.actualLine = 1;
		this.actualColumn = 1;
		this.actualCountOfTokenInLine = 0;
		this.actualToken = null;
	}

	@Override
	public Token getNextToken() {
		String actualTokenValue = this.abstractToken();
		TokenType actualTokenType = this.matchToken(actualTokenValue);
		switch (actualTokenType.name()) {
		case "NUM":
			this.actualToken = new NumTokenImpl(actualTokenValue,
					actualTokenType, this.actualLine, this.actualColumn);
			break;
		case "REAL":
			this.actualToken = new RealTokenImpl(actualTokenValue,
					actualTokenType, this.actualLine, this.actualColumn);
			break;
		case "TRUE":
			this.actualToken = new BoolTokenImpl(actualTokenValue,
					actualTokenType, this.actualLine, this.actualColumn);
		case "FALSE":
			this.actualToken = new BoolTokenImpl(actualTokenValue,
					actualTokenType, this.actualLine, this.actualColumn);
		default:
			this.actualToken = new TokenImpl(actualTokenValue, actualTokenType,
					this.actualLine, this.actualColumn);
			break;
		}

		return this.actualToken;
	}

	/**
	 * Method to match a a {@link String} into a {@link TokenType}
	 * 
	 * @param {@link String} to match
	 * @return {@link TokenType} of the input {@link String}
	 */
	private TokenType matchToken(String nextToken) {
		if (nextToken.matches("[0-9]+((e|E)-?[0-9]+)?")) {
			return TokenType.NUM;
		} else if (nextToken.matches("[0-9]+\\.[0-9]+((e|E)-?[0-9]+)?")) {
			return TokenType.REAL;
		} else if (nextToken.matches(";")) {
			return TokenType.SEMICOLON;
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
		} else if (nextToken.matches("=")) {
			return TokenType.ASSIGNOP;
		} else if (nextToken.matches("&&")) {
			return TokenType.AND;
		} else if (nextToken.matches("\\|\\|")) {
			return TokenType.OR;
		} else if (nextToken.matches("==")) {
			return TokenType.EQUALS;
		} else if (nextToken.matches("!=")) {
			return TokenType.NOT_EQUALS;
		} else if (nextToken.matches("<")) {
			return TokenType.LESS;
		} else if (nextToken.matches("<=")) {
			return TokenType.LESS_OR_EQUAL;
		} else if (nextToken.matches(">")) {
			return TokenType.GREATER;
		} else if (nextToken.matches(">=")) {
			return TokenType.GREATER_EQUAL;
		} else if (nextToken.matches("\\+")) {
			return TokenType.PLUS;
		} else if (nextToken.matches("\\-")) {
			return TokenType.MINUS;
		} else if (nextToken.matches("\\*")) {
			return TokenType.TIMES;
		} else if (nextToken.matches("\\/")) {
			return TokenType.DIVIDE;
		} else if (nextToken.matches("!")) {
			return TokenType.NOT;
		} else if (nextToken.matches("true")) {
			return TokenType.TRUE;
		} else if (nextToken.matches("false")) {
			return TokenType.FALSE;
		} else if (nextToken.matches("if")) {
			return TokenType.IF;
		} else if (nextToken.matches("while")) {
			return TokenType.WHILE;
		} else if (nextToken.matches("do")) {
			return TokenType.DO;
		} else if (nextToken.matches("break")) {
			return TokenType.BREAK;
		} else if (nextToken.matches("return")) {
			return TokenType.RETURN;
		} else if (nextToken.matches("print")) {
			return TokenType.PRINT;
		} else if (nextToken.matches("#")) {
			return TokenType.COMMENT;
		} else {
			return TokenType.NOT_A_TOKEN;
		}
	}

	/**
	 * Method to get the value, the actual line and the actual column of the
	 * next token
	 * 
	 * @return abstracted token value of current read token
	 */
	private String abstractToken() {
		if ((this.convertedLines.get(this.actualLine - 1).startsWith(" ") ? (this.convertedLines
				.get(this.actualLine - 1).split("\\s+").length == this.actualCountOfTokenInLine + 1)
				: (this.convertedLines.get(this.actualLine - 1).split("\\s+").length == this.actualCountOfTokenInLine))) {
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
				+ this.actualLine + ", Read token: "
				+ this.actualCountOfTokenInLine);

		return actualTokenValue;
	}
}
