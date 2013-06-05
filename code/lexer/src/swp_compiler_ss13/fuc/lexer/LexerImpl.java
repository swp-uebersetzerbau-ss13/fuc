package swp_compiler_ss13.fuc.lexer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Scanner;

import swp_compiler_ss13.common.lexer.BoolToken;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.RealToken;
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

	/**
	 * Method sets an {@link InputStream} for the lexer and splits it line by
	 * line into an {@link ArrayList}
	 */
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
	 * Method to initialize variables when getting new input
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

	/**
	 * Method read the next token from the input and returns it with his value,
	 * type, line of code and column of code
	 * 
	 * @return {@link NumToken}: token has the {@link TokenType}
	 *         <code>NUM</code><br>
	 *         {@link RealToken}: token has the {@link TokenType}
	 *         <code>REAL</code><br>
	 *         {@link BoolToken}: token has the {@link TokenType}
	 *         <code>TRUE</code> or <code>FALSE</code><br>
	 *         {@link Token}: otherwise<br>
	 */
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

			/*
			 * check if actual line is empty
			 */
			if (this.actualLineValue.isEmpty()
					|| this.actualLineValue.matches("\\s")) {
				return this.getNextToken();

			}

		}

		/*
		 * analyse the actual type of token
		 */
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
	 * Method to get the value, the actual line and the actual column of the
	 * next token
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

			} else {

				this.actualColumn = this.actualLineValue.indexOf(
						this.actualTokenValue, 0) + 1;

			}

			/*
			 * when actual token value starts with an apostrophe everything till
			 * the next apostrophe is the value
			 */
			if (this.actualTokenValue.startsWith("\"")) {

				int indexOfNextApostrophe = this.actualLineValue.indexOf("\"",
						this.actualColumn);

				/*
				 * check if string is finished in this line
				 */
				if (indexOfNextApostrophe == -1) {

					// FIXME: try to find the apostrophe in the next line
					System.err.println("not yet implemented");

				} else {

					/*
					 * loop as long as the next unescaped apostrophe is found
					 */
					while (!this.checkEscapeStatus(indexOfNextApostrophe)) {

						indexOfNextApostrophe = this.actualLineValue.indexOf(
								"\"", indexOfNextApostrophe + 1);

					}

					/*
					 * set the correct value for the string token
					 */
					this.actualTokenValue = this.actualLineValue.substring(
							this.actualColumn - 1, indexOfNextApostrophe + 1);

					/*
					 * check if the next character after the string is a
					 * semicolon
					 */
					if (this.actualLineValue.substring(
							indexOfNextApostrophe + 1).equals(";")) {

						this.isSemicolon = true;
					}

					/*
					 * calculate the new count of token FIXME: work without
					 * count of token or start subsequence from behind
					 */
					if (this.actualTokenValue.contains(" ")) {

						for (int i = 0; i < this.actualLineValue.split("\\s+").length; i++) {

							if (this.actualLineValue.split("\\s+")[i]
									.contains(this.actualTokenValue
											.subSequence(this.actualTokenValue
													.indexOf(" ") + 1,
													indexOfNextApostrophe + 1))) {

								this.actualCountOfTokenInLine = i;
							}

						}

					}

				}

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
	 * Method checks if in the actual line a character is escaped or not
	 * 
	 * @param index
	 *            of the character which is probably escaped
	 * @return <code>true</code>: character is escaped<br>
	 *         <code>false</code>: character is not escaped
	 */
	private boolean checkEscapeStatus(int index) {
		return !String.valueOf(this.actualLineValue.charAt(index - 1)).equals(
				"\\");
	}

	/**
	 * Method checks if the actual line is empty or all tokens in line were read
	 * 
	 * @return <code>true</code>: all tokens were read, line must be increased <br>
	 *         <code>false</code>: otherwise
	 */
	private boolean checkLineOfCode() {

		boolean increaseLine = false;

		if (this.actualLineValue != null) {

			int actualTokensInLine = this.actualLineValue.split("\\s+").length;

			if ((this.actualLineValue.startsWith(" ") && actualTokensInLine <= this.actualCountOfTokenInLine + 1)
					|| (!this.actualLineValue.startsWith(" ") && actualTokensInLine <= this.actualCountOfTokenInLine)) {

				increaseLine = true;

			}

		}

		return increaseLine;
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

	/**
	 * Method matches the actual token value and find the type of the token
	 * 
	 * @see TokenType
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

			this.actualTokenType = TokenType.STRING;

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
			 * part of the value
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

}