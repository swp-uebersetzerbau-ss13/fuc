package swp_compiler_ss13.fuc.parser;

import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedList;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;

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
}
