package swp_compiler_ss13.fuc.parser;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

public class TestToken implements Token {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private final String value;
	private final TokenType type;
	
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public TestToken(String value, TokenType type) {
		this.value = value;
		this.type = type;
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
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
		return -1;
	}

	@Override
	public Integer getColumn() {
		return -1;
	}
	
	@Override
	public String toString() {
		return value;
	}
}
