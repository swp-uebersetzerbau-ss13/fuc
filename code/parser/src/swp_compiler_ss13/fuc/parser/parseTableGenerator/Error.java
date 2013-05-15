package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Error extends ParseTableEntry{

	@Override
	public ParseTableEntryType getType() {
	
		return ParseTableEntryType.ERROR;
	}
	
}
