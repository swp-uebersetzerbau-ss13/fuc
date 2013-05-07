package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public abstract class Symbol implements Comparable<Symbol> {
	public enum Type {
		TERMINAL,
		VARIABLE
	}
	public String getString() {
		return _stringRep;
	}
	public abstract Type getType();
	public Symbol(String stringRep) { _stringRep = stringRep; };
	public int compareTo(Symbol other) {
		if(
				getType() == other.getType()
				&& getString() == other.getString()
		)
			return -1;
		return 0;
	}
	private String _stringRep;
}
