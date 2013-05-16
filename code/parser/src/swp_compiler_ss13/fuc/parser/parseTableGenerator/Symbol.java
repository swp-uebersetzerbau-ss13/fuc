package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public abstract class Symbol implements Comparable<Symbol> {
	
	private String stringRep;
	
	public enum SymbolType {
		TERMINAL,
		VARIABLE  // TODO rename to NonTerminal!
	}
	
	public String getString() {
		
		return this.stringRep;
	}
	
	public abstract SymbolType getType();

	public Symbol(String stringRep) { 
		
		this.stringRep = stringRep;
	}
	
	/**
	 * compare two Symbols
	 * 
	 * Two symbols are equal, if their types and their string representation are equal.
	 * Terminals are considered smaller than Variables.
	 */
	public int compareTo(Symbol other) {
		if(getType() != other.getType())
			if(getType() == SymbolType.TERMINAL)
				return -1;
			else
				return 1;
		else
			return getString().compareTo(other.getString());
	}
	
	/**
	 * this is important! if this method is not be overwritten, it would not work as expected!
	 * @param other Symbol
	 * @return calls {@link compareTo} to check for equality
	 */
	@Override
	public boolean equals(Object other) {
		System.out.println("Symbol.equals called!");
		if( other instanceof Symbol)
			return (compareTo((Symbol )other) == 0);
		return false;
	}
	
	@Override
	public int hashCode() {
		//System.out.println(getString() + ".hashCode() == " + stringRep.hashCode());
		return stringRep.hashCode();
	}
}
