package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public interface ParseTable {
	
	public ParseTableEntry getEntry(int state, Symbol symbol);

}
