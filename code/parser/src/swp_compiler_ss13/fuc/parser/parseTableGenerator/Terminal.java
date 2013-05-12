package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Terminal extends Symbol {
	public SymbolType getType() {
		return SymbolType.TERMINAL;
	}
	public Terminal(String stringRep) {
		super(stringRep);
	}
	
	/**
	 * this is important! if this method is not be overwritten, it would not work as expected!
	 * @param other Symbol
	 * @return calls {@link compareTo} to check for equality
	 */
	/*public boolean equals(Symbol other) {
		System.out.println("Symbol.equals called!");
		return (compareTo(other) == 0);
	}*/
}
