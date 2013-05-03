package lexer;

import java.io.InputStream;

import lexer.token.BoolTokenImpl;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;

public class LexerImpl implements Lexer {
	private InputStream inputStream;
	private Token token;

	@Override
	public void setSourceStream(InputStream stream) {
		this.inputStream = stream;
	}

	@Override
	public Token getNextToken() {
		this.token = new BoolTokenImpl(null, null, null, null);
		return this.token;
	}

}
