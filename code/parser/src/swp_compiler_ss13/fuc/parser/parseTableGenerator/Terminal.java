package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Terminal extends Symbol {
	public SymbolType getType() {
		return SymbolType.TERMINAL;
	}
	public Terminal(String stringRep) {
		super(stringRep);
	}
}
