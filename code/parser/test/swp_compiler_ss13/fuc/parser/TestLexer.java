package swp_compiler_ss13.fuc.parser;

import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class TestLexer implements Lexer {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private final LinkedList<Token> tokens;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public TestLexer(Token... tokens) {
		this.tokens = new LinkedList<>(Arrays.asList(tokens));
	}
	
	public TestLexer(Collection<Symbol> symbols) {
		this.tokens = new LinkedList<>();
		
		for (Symbol s : symbols) {
			tokens.add(new TestToken(s));
		}
	}
	public TestLexer(Symbol... symbols) {
		this.tokens = new LinkedList<>();
		
		for (Symbol s : symbols) {
			tokens.add(new TestToken(s));
		}
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public void setSourceStream(InputStream stream) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Token getNextToken() {
		return tokens.removeFirst();
	}
	
	
	private static class TestToken implements Token {
		private final Symbol symbol;
		
		
		private TestToken(Symbol symbol) {
			this.symbol = symbol;
		}
		
		@Override
		public String getValue() {
			return symbol.getId();
		}

		@Override
		public TokenType getTokenType() {
			if (!symbol.isTerminal()) {
				return null;
			} else {
				Terminal t = (Terminal) symbol;
				return t.getTokenTypes().next();	// TODO This is arbitrary!
			}
		}

		@Override
		public Integer getLine() {
			return -1;
		}

		@Override
		public Integer getColumn() {
			return -1;
		}
	}
}
