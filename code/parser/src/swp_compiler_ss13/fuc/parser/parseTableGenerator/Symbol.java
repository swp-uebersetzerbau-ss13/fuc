package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public abstract class Symbol implements Comparable<Symbol> {
	
	private String stringRep;
	
	public enum SymbolTable {
		TERMINAL,
		VARIABLE
	}
	
	public String getString() {
		
		return this.stringRep;
	}
	
	public abstract SymbolTable getType();

	public Symbol(String stringRep) { 
		
		this.stringRep = stringRep; 
	}
	
	public int compareTo(Symbol other) {
		
		if(getType() == other.getType() && getString() == other.getString()){
			return -1;
		}
		return 0;
	}
}
