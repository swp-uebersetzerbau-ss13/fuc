package swp_compiler_ss13.fuc.parser.grammar;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.lexer.BoolToken;
import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.RealToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * This class wraps a {@link Token} and adds a {@link Terminal} as specified in
 * the {@link Grammar}, as the parser internally needs the association
 * {@link Token} -> {@link Terminal}.
 * 
 * @author Gero
 */
public class TokenEx implements Token {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Logger log = Logger.getLogger(TokenEx.class);

	protected final Token token;
	private final Terminal terminal;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @see TokenEx
	 * @param token
	 *            The {@link Token} to wrap
	 * @param terminal
	 *            The {@link Terminal} the token should by extended with
	 */
	public TokenEx(Token token, Terminal terminal) {
		this.token = token;
		this.terminal = terminal;
	}
	
	/**
	 * @see TokenEx
	 * @param value
	 * @param type
	 * @param line
	 * @param col
	 * @param terminal The {@link Terminal} the token should by extended with
	 */
	public TokenEx(String value, TokenType type, int line, int col, Terminal terminal) {
		this.token = new BaseToken(value, type, line, col);
		this.terminal = terminal;
	}

	/**
	 * Associates the given {@link Token} with a {@link Terminal} as specified
	 * in the {@link Grammar}
	 * 
	 * @param token
	 * @param grammar
	 * @return
	 */
	public static TokenEx createFromToken(Token token, Grammar grammar) {
		// Handle SpecialTerminals first!
		if (token.getTokenType() == TokenType.EOF) {
			return new TokenEx(token, Terminal.EOF);
		} else if (token.getTokenType() == TokenType.COMMENT) {
			return new TokenEx(token, null);
		} else {
			Terminal terminal = null;
			TokenType newType = token.getTokenType();
			for (Terminal t : grammar.getTerminals()) {
				if (t.isTerminalFor(newType)) {
					terminal = t;
					break;
				}
			}

			if (terminal == null) {
				log.warn("Unable to find a terminal for token: "
						+ token.toString());
			}

			switch (token.getTokenType()) {
			case NUM:
				return new NumTokenEx((NumToken) token, terminal);
			case TRUE:
			case FALSE:
				return new BoolTokenEx((BoolToken) token, terminal);
			case REAL:
				return new RealTokenEx((RealToken) token, terminal);
			default:
				return new TokenEx(token, terminal);
			}
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return The base token
	 */
	public Token getToken() {
		return token;
	}

	/**
	 * @return The {@link Terminal} this token represents
	 */
	public Terminal getTerminal() {
		return terminal;
	}

	@Override
	public String getValue() {
		return token.getValue();
	}

	@Override
	public TokenType getTokenType() {
		return token.getTokenType();
	}

	@Override
	public Integer getLine() {
		return token.getLine();
	}

	@Override
	public Integer getColumn() {
		return token.getColumn();
	}

	@Override
	public String toString() {
		return "[Token: '" + token.getValue() + "' |type: '"
				+ token.getTokenType() + "' |terminal: '"
				+ (terminal != null ? terminal.toString() : "<null>")
				+ "' |at: line " + token.getLine() + ", col "
				+ token.getColumn() + "]";
	}
	
	private static class BaseToken implements Token {
		
		private final String value;
		private final TokenType type;
		private final int line;
		private final int column;
		
		private BaseToken(String value, TokenType type, int line, int column) {
			this.value = value;
			this.type = type;
			this.line = line;
			this.column = column;
		}

		@Override
		public String getValue() {
			return value;
		}

		@Override
		public TokenType getTokenType() {
			return type;
		}

		@Override
		public Integer getLine() {
			return line;
		}

		@Override
		public Integer getColumn() {
			return column;
		}
	}

	/**
	 * {@link NumToken} implementation of {@link TokenEx}
	 */
	public static class NumTokenEx extends TokenEx implements NumToken {
		public NumTokenEx(NumToken token, Terminal terminal) {
			super(token, terminal);
		}

		@Override
		public Long getLongValue() {
			return ((NumToken) token).getLongValue();
		}
	}

	/**
	 * {@link RealToken} implementation of {@link TokenEx}
	 */
	public static class RealTokenEx extends TokenEx implements RealToken {
		public RealTokenEx(RealToken token, Terminal terminal) {
			super(token, terminal);
		}

		@Override
		public Double getDoubleValue() {
			return ((RealToken) token).getDoubleValue();
		}
	}

	/**
	 * {@link BoolToken} implementation of {@link TokenEx}
	 */
	public static class BoolTokenEx extends TokenEx implements BoolToken {
		public BoolTokenEx(BoolToken token, Terminal terminal) {
			super(token, terminal);
		}

		@Override
		public Boolean getBooleanValue() {
			return ((BoolToken) token).getBooleanValue();
		}
	}
}
