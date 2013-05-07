package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Terminal extends Symbol {
	public SymbolTable getType() {
		return SymbolTable.TERMINAL;
	}
	public Terminal(String stringRep) {
		super(stringRep);
	}
}
