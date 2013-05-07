package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Terminal extends Symbol {
	public Type getType() {
		return Type.TERMINAL;
	}
	public Terminal(String stringRep) {
		super(stringRep);
	}
}
