package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.List;


public class Grammar {
	public List<Production> getProductions() {
		return productions;
	}
	Grammar(List<Production> productions) {
		this.productions = productions;
	}
	private List<Production> productions;
}
