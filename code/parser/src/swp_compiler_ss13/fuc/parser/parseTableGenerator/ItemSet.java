package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol.SymbolType;

public class ItemSet extends HashSet<Item> {
	
	/*public Map<Symbol,ItemSet> getGOTO() {
		if(GOTO == null)
			calcGOTO();
		return GOTO;
	}*/
	
	public Item getReducableItem() {		
		// search for an item of the form:
		// A -> x1 x2 ... xn .
		for( Item item : this) {
			if( item.getSymbolAfterDot() == null )
				return item;
		}
		return null;
	}
	

	public void CLOSURE(Grammar grammar) {
		int cardOld;
		do {
			cardOld = this.size();
			ItemSet newSet = new ItemSet(this);
			for( Item item : this ) {
				Symbol symAfterDot = item.getSymbolAfterDot();
				System.out.println( "current Item: " + item.getString());
				if( symAfterDot != null ) {
					if( symAfterDot.getType() == SymbolType.VARIABLE ) {
						Variable varAfterDot = (Variable )symAfterDot;
						for( Production prod : grammar.getProductions()) {
							if( prod.getLeft().equals(varAfterDot) ) {
								System.out.println("adding: " + prod.getString() );
								newSet.add( new Item( prod, 0 ));
							}
						}
					}
				}
			}
			//this.clear();
			this.addAll(newSet);
			System.out.println("difference: " + (this.size() - cardOld));
		} while ( this.size() > cardOld );
	}
	
	Map<Symbol,ItemSet> getGOTO() {
		return GOTO;
	}
	
	public ItemSet(Item ...items) {
		super();
		GOTO = new HashMap<Symbol, ItemSet>();
		for( Item i : items) {
			add(i);
		}
	}
	public ItemSet(ItemSet other) {
		super(other);
	}
	private Map<Symbol,ItemSet> GOTO;
	private static final long serialVersionUID = 1L;

}
