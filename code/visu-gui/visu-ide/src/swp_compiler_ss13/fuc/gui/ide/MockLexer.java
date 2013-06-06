package swp_compiler_ss13.fuc.gui.ide;

import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;

public class MockLexer implements Lexer {

	private Queue<Token> tokens;

	public MockLexer(List<Token> tokens) {
		this.tokens = new LinkedList<Token>(tokens);
	}

	@Override
	public void setSourceStream(InputStream stream) {
		// ignored;
	}

	@Override
	public Token getNextToken() {
		return this.tokens.poll();
	}
}
