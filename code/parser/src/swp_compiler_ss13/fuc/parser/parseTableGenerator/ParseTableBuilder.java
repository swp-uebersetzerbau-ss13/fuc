package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.Set;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable;

/**
 * @author EsGeh
 *
 */
public class ParseTableBuilder {
	
	/**
	 * creates a ParseTable from a Grammar
	 * 
	 * @param a
	 * @return The LR(1) parsetable for "grammar"
	 */
	public ParseTable getTable(Grammar grammar) {
		ParseTable table = new ParseTableImpl(0, grammar.getTerminals());
		//return null;
	}
	
	private Set<Item> CLOSURE(Set<Item> item) {
		return null;
	}
}