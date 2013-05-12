package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.HashSet;
import java.util.Set;


public class Variable extends Symbol {
	public SymbolType getType() {
		return SymbolType.VARIABLE;
	}
	public Variable(String stringRep) {
		super(stringRep);
		_FIRST = new HashSet<Terminal>();
		_FOLLOW = new HashSet<Terminal>();
	}
	public Variable(String stringRep, Set<Terminal> FIRST, Set<Terminal> FOLLOW) {
		super(stringRep);
		_FIRST = FIRST;
		_FOLLOW = FOLLOW;
	}
	
	public Set<Terminal> getFIRST() {
		return _FIRST;
	}
	public Set<Terminal> getFOLLOW() {
		return _FOLLOW;
	}
	
	private Set<Terminal> _FIRST;
	private Set<Terminal> _FOLLOW;
}
