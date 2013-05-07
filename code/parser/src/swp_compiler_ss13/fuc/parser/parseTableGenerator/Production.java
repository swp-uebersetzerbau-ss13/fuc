package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.Set;
import java.util.List;


public class Production {
	public Variable getLeft() {
		return left;
	}
	public List<Symbol> getRight() {
		return right;
	}
	public Set<Terminal> getFIRST() {
		return left.getFIRST();
	}
	public Set<Terminal> getFOLLOW() {
		return left.getFOLLOW();
	}
	
	public Production(Variable left, List<Symbol> right) {
		this.left = left;
		this.right = right;
	}
	
	private Variable left;
	private List<Symbol> right;
}
