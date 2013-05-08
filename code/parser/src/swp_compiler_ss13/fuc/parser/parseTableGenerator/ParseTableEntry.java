package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public abstract class ParseTableEntry {
	
	protected Integer newState;
	
	public abstract ParseTableEntryType getType();
	
	public enum ParseTableEntryType {
		SHIFT,
		REDUCE,
		ACCEPT,
		ERROR
	};
	
	public Integer getNewState() {
		return newState;
	}
}
