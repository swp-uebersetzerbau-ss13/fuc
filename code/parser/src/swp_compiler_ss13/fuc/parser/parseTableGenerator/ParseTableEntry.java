package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public abstract class ParseTableEntry {
	public abstract Type getType();
	public enum Type {
		SHIFT,
		REDUCE
	};
}
