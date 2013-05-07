package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public abstract class ParseTableEntry {
	public abstract ParseTableEntryType getType();
	public enum ParseTableEntryType {
		SHIFT,
		REDUCE,
		ACCEPT
	};
}
