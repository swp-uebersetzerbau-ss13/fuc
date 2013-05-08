package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import swp_compiler_ss13.common.lexer.TokenType;

public interface ParseTable {
	
	public ParseTableEntry getEntry(int state, TokenType symbol);

}
