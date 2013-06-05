package swp_compiler_ss13.fuc.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.lexer.token.BoolTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.RealTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;

/**
 * Implementation of the interface {@link Lexer}
 * 
 * @author "Thomas Benndorf"
 * 
 */
public class LexerImpl implements Lexer {
	private Token actualToken;
	private ArrayList<String> convertedLines;
	private String actualLineValue;
	private String actualTokenValue;
	private TokenType actualTokenType;
	private Integer actualLine;
	private Integer actualColumn;
	private Integer actualCountOfTokenInLine;
	private boolean isNextLine;
	private boolean isSemicolon;
	private boolean isEOF;

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
		this.isSemicolon = false;
		this.isNextLine = false;
		this.isEOF = false;

	}

	@Override
	public Token getNextToken() {

		/*
		 * check if input is empty, EOF is reached or a semicolon was already
		 * read
		 */
		if (this.isEOF || this.convertedLines.size() == 0) {

			this.actualTokenValue = "$";
			this.actualTokenType = TokenType.EOF;

		} else if (this.isSemicolon) {

			this.actualColumn = this.actualLineValue.indexOf(";",
					this.actualColumn + this.actualTokenValue.length() - 1) + 1;
			this.actualTokenValue = ";";
			this.actualTokenType = TokenType.SEMICOLON;
			this.isSemicolon = false;

		} else {

			this.abstractToken();

			// TODO: skip empty line
			if (this.actualTokenValue.length() == 0) {

				return this.getNextToken();

			}

		}

		switch (this.actualTokenType.name()) {

		case "NUM":
			this.actualToken = new NumTokenImpl(this.actualTokenValue,
					this.actualTokenType, this.actualLine, this.actualColumn);
			break;

		case "REAL":
			this.actualToken = new RealTokenImpl(this.actualTokenValue,
					this.actualTokenType, this.actualLine, this.actualColumn);
			break;

		case "TRUE":
			this.actualToken = new BoolTokenImpl(this.actualTokenValue,
					this.actualTokenType, this.actualLine, this.actualColumn);
			break;

		case "FALSE":
			this.actualToken = new BoolTokenImpl(this.actualTokenValue,
					this.actualTokenType, this.actualLine, this.actualColumn);
			break;

		default:
			this.actualToken = new TokenImpl(this.actualTokenValue,
					this.actualTokenType, this.actualLine, this.actualColumn);
			break;

		}

		return this.actualToken;

	}

	/**
	 * Method to match a a {@link String} into a {@link TokenType}
	 * 
	 */
	private void matchToken() {
		if (this.actualTokenValue
				.matches("(\\+|-)?[0-9]+((e|E)(\\+|-)?[0-9]+)?")) {
			this.actualTokenType = TokenType.NUM;
		} else if (this.actualTokenValue
				.matches("(\\+|-)?[0-9]+\\.[0-9]+((e|E)(\\+|-)?[0-9]+)?")) {
			this.actualTokenType = TokenType.REAL;
		} else if (this.actualTokenValue
				.matches("\\\"(?:[^\\\"\\\\]+|\\\\.)*\\\"")) {
			/*
			 * 
			 */
			this.actualTokenType = TokenType.STRING;
			this.actualTokenValue = this.actualLineValue.substring(
					this.actualColumn - 1,
					this.actualLineValue.indexOf("\"", this.actualColumn) + 1);
		} else if (this.actualTokenValue.matches(";")) {
			this.actualTokenType = TokenType.SEMICOLON;
		} else if (this.actualTokenValue.matches("\\(")) {
			this.actualTokenType = TokenType.LEFT_PARAN;
		} else if (this.actualTokenValue.matches("\\)")) {
			this.actualTokenType = TokenType.RIGHT_PARAN;
		} else if (this.actualTokenValue.matches("\\{")) {
			this.actualTokenType = TokenType.LEFT_BRACE;
		} else if (this.actualTokenValue.matches("\\}")) {
			this.actualTokenType = TokenType.RIGHT_BRACE;
		} else if (this.actualTokenValue.matches("\\[")) {
			this.actualTokenType = TokenType.LEFT_BRACKET;
		} else if (this.actualTokenValue.matches("\\]")) {
			this.actualTokenType = TokenType.RIGHT_BRACKET;
		} else if (this.actualTokenValue.matches("=")) {
			this.actualTokenType = TokenType.ASSIGNOP;
		} else if (this.actualTokenValue.matches("&&")) {
			this.actualTokenType = TokenType.AND;
		} else if (this.actualTokenValue.matches("\\|\\|")) {
			this.actualTokenType = TokenType.OR;
		} else if (this.actualTokenValue.matches("==")) {
			this.actualTokenType = TokenType.EQUALS;
		} else if (this.actualTokenValue.matches("!=")) {
			this.actualTokenType = TokenType.NOT_EQUALS;
		} else if (this.actualTokenValue.matches("<")) {
			this.actualTokenType = TokenType.LESS;
		} else if (this.actualTokenValue.matches("<=")) {
			this.actualTokenType = TokenType.LESS_OR_EQUAL;
		} else if (this.actualTokenValue.matches(">")) {
			this.actualTokenType = TokenType.GREATER;
		} else if (this.actualTokenValue.matches(">=")) {
			this.actualTokenType = TokenType.GREATER_EQUAL;
		} else if (this.actualTokenValue.matches("\\+")) {
			this.actualTokenType = TokenType.PLUS;
		} else if (this.actualTokenValue.matches("\\-")) {
			this.actualTokenType = TokenType.MINUS;
		} else if (this.actualTokenValue.matches("\\*")) {
			this.actualTokenType = TokenType.TIMES;
		} else if (this.actualTokenValue.matches("\\/")) {
			this.actualTokenType = TokenType.DIVIDE;
		} else if (this.actualTokenValue.matches("!")) {
			this.actualTokenType = TokenType.NOT;
		} else if (this.actualTokenValue.matches("true")) {
			this.actualTokenType = TokenType.TRUE;
		} else if (this.actualTokenValue.matches("false")) {
			this.actualTokenType = TokenType.FALSE;
		} else if (this.actualTokenValue.matches("if")) {
			this.actualTokenType = TokenType.IF;
		} else if (this.actualTokenValue.matches("while")) {
			this.actualTokenType = TokenType.WHILE;
		} else if (this.actualTokenValue.matches("do")) {
			this.actualTokenType = TokenType.DO;
		} else if (this.actualTokenValue.matches("break")) {
			this.actualTokenType = TokenType.BREAK;
		} else if (this.actualTokenValue.matches("return")) {
			this.actualTokenType = TokenType.RETURN;
		} else if (this.actualTokenValue.matches("print")) {
			this.actualTokenType = TokenType.PRINT;
		} else if (this.actualTokenValue.matches("long")) {
			this.actualTokenType = TokenType.LONG_SYMBOL;
		} else if (this.actualTokenValue.matches("double")) {
			this.actualTokenType = TokenType.DOUBLE_SYMBOL;
		} else if (this.actualTokenValue.matches("bool")) {
			this.actualTokenType = TokenType.BOOL_SYMBOL;
		} else if (this.actualTokenValue.matches("string")) {
			this.actualTokenType = TokenType.STRING_SYMBOL;
		} else if (this.actualTokenValue.matches("[a-zA-Z]\\w*")) {
			this.actualTokenType = TokenType.ID;
		} else if (this.actualTokenValue.matches("#.*")) {
			/*
			 * when token is a comment, everything in the line after the '#' is
			 * the value
			 */
			this.actualTokenType = TokenType.COMMENT;
			this.actualTokenValue = this.actualLineValue
					.substring(this.actualLineValue.indexOf("#"));
			this.isNextLine = true;
		} else if (this.actualTokenValue == "$") {
			this.actualTokenType = TokenType.EOF;
		} else {
			this.actualTokenType = TokenType.NOT_A_TOKEN;
		}
	}

	/**
	 * Method to get the value, the actual line and the actual column of the
	 * next token
	 * 
	 */
	private void abstractToken() {

		/*
		 * increase actual line if necessary
		 */
		if (this.checkLineOfCode() || this.isNextLine) {

			this.increaseLineOfCode();
			this.isNextLine = false;

		}

		/*
		 * check if all tokens are read
		 */
		if (this.convertedLines.size() < this.actualLine) {

			this.actualTokenValue = "$";
			this.actualTokenType = TokenType.EOF;
			this.isEOF = true;

		} else {

			/*
			 * get the value of the actual line
			 */
			this.actualLineValue = this.convertedLines.get(this.actualLine - 1);

			/*
			 * abstract the next token value of the actual line
			 */
			if (this.actualLineValue.startsWith(" ")) {

				this.actualTokenValue = this.actualLineValue.split("\\s+")[this.actualCountOfTokenInLine + 1];

			} else {

				this.actualTokenValue = this.actualLineValue.split("\\s+")[this.actualCountOfTokenInLine];

			}

			/*
			 * calculate the column for the new token value
			 */
			if (this.actualColumn > 1) {

				this.actualColumn = this.actualLineValue.indexOf(
						this.actualTokenValue, this.actualColumn + 1) + 1;

				// FIXME: set index to the end of the token?
			} else {

				this.actualColumn = this.actualLineValue.indexOf(
						this.actualTokenValue, 0) + 1;

			}

			/*
			 * when actual token value has an semicolon at the end then it must
			 * be abstract of the token value
			 */
			if (this.actualTokenValue.endsWith(";")) {

				this.isSemicolon = true;
				this.actualTokenValue = this.actualTokenValue.substring(0,
						this.actualTokenValue.length() - 1);

			}

			/*
			 * increases the count of read tokens in the actual line and matches
			 * the actual token value
			 */
			this.actualCountOfTokenInLine++;
			this.matchToken();
		}

	}

	/**
	 * Method checks if all tokens in line were read or the actual line is empty
	 * and increases the line of code
	 * 
	 * @return <code>true</code>: line must be increased <br>
	 *         <code>else</code>: otherwise
	 */
	private boolean checkLineOfCode() {
		if (this.actualLineValue != null) {
			int actualTokensInLine = this.actualLineValue.split("\\s+").length;
			if ((this.actualLineValue.startsWith(" ") && actualTokensInLine <= this.actualCountOfTokenInLine + 1)
					|| (!this.actualLineValue.startsWith(" ") && actualTokensInLine <= this.actualCountOfTokenInLine)
					|| this.actualLineValue.isEmpty()
					|| this.actualLineValue.matches("\\s")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}
	}

	/**
	 * Method increases the actual line of code for the lexer and resets the
	 * column and the count of tokens in line
	 */
	private void increaseLineOfCode() {

		this.actualLine++;
		this.actualColumn = 1;
		this.actualCountOfTokenInLine = 0;

	}
}