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
	public TokenEx(Token token, Terminal terminal) {
		this.token = token;
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
				for (TokenType type : t.getTokenTypes()) {
					if (type == newType) {
						terminal = t;
						break;
					}
				}
			}

			if (terminal == null) {
				log.warn("Unable to find a terminal for token: "
						+ tokenToStringFull(token));
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
	public Token getToken() {
		return token;
	}

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

	public static String tokenToString(Token t) {
		return t.getValue();
	}

	public static String tokenToStringFull(Token t) {
		return "[Token: '" + t.getValue() + "|Type: '" + t.getTokenType()
				+ "'|At: line " + t.getLine() + ", col " + t.getColumn() + "]";
	}

	@Override
	public String toString() {
		return "Token: " + tokenToString(token) + "|"
				+ (terminal != null ? terminal.toString() : "");
	}

	public static class NumTokenEx extends TokenEx implements NumToken {
		public NumTokenEx(NumToken token, Terminal terminal) {
			super(token, terminal);
		}

		@Override
		public Long getLongValue() {
			return ((NumToken) token).getLongValue();
		}
	}

	public static class RealTokenEx extends TokenEx implements RealToken {
		public RealTokenEx(RealToken token, Terminal terminal) {
			super(token, terminal);
		}

		@Override
		public Double getDoubleValue() {
			return ((RealToken) token).getDoubleValue();
		}
	}

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