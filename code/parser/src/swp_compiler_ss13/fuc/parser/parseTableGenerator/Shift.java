package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Shift extends ParseTableEntry {
	
	public Shift(Integer newState){
		super.newState = newState;
	}
	
	public ParseTableEntryType getType() {
		return ParseTableEntryType.SHIFT;
	}

}