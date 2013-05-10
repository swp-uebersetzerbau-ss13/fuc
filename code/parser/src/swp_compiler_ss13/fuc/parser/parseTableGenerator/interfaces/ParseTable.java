package swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry;

public interface ParseTable {
	public ParseTableEntry getEntry(int state, Token symbol);

}
