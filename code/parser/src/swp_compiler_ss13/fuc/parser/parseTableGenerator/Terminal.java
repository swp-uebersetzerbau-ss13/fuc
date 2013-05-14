package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import swp_compiler_ss13.common.lexer.Token;

public class Terminal extends Symbol {
	public SymbolType getType() {
		return SymbolType.TERMINAL;
	}
	
	public boolean equalsToken(Token token) {
		return this.getString().equals(token.getValue());
	}
	
	public Terminal(String stringRep) {
		super(stringRep);
	}
	public Terminal(Token token) {
		super(token.getValue());
	}
}
