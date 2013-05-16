package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;

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
			if( item.getNextSymbol() == null )
				return item;
		}
		return null;
	}
	

	/**
	 * @param grammar
	 * @return new {@link ItemSet} which contains the closure of <code>this</code>
	 */
	public ItemSet CLOSURE(Grammar grammar) {
		ItemSet result = new ItemSet(this);	// all items of origin..
		
		LinkedList<Item> todo = new LinkedList<>();
		todo.addAll(this);
		// ...plus all following ones (for any item "A → α.Xβ" and any "X → γ" add "X → .γ")
		
		while (!todo.isEmpty()) {
			Item item = todo.removeFirst();
			Symbol nextSymbol = item.getNextSymbol();
			if (nextSymbol != null && nextSymbol.getType() == SymbolType.VARIABLE) {
				Variable nonTerminal = (Variable) nextSymbol;
				for (Production prod : grammar.getProductions()) {
					if (prod.getLeft().equals(nonTerminal)) {
						// Productions starting from nonTerminal..
						Item newItem = new Item(prod, 0);
						if (!result.contains(newItem)) {
							result.add(newItem);
							todo.addLast(newItem);
						}
					}
				}
			}
		}
		
		return result;
//		int cardOld;
//		do {
//			cardOld = this.size();
//			ItemSet newSet = new ItemSet(this);
//			for( Item item : this ) {
//				Symbol nextSymbol = item.getNextSymbol();
//				//System.out.println( "current Item: " + item.getString());
//				if( nextSymbol != null ) {
//					if( nextSymbol.getType() == SymbolType.VARIABLE ) {
//						Variable nextNonTerminal = (Variable) nextSymbol;
//						for( Production prod : grammar.getProductions()) {
//							if( prod.getLeft().equals(nextNonTerminal) ) {
//								//System.out.println("adding: " + prod.getString() );
//								newSet.add( new Item( prod, 0 ));
//							}
//						}
//					}
//				}
//			}
//			//this.clear();
//			this.addAll(newSet);
//			//System.out.println("difference: " + (this.size() - cardOld));
//		} while ( this.size() > cardOld );
	}
	
	public String getString() {
		String ret = "{";
		boolean isFirst = true;
		for ( Item item : this) {
			if( isFirst)
				ret += ( " " + item.getStringItem());
			else
				ret += ( "\n  " + item.getStringItem());
			isFirst = false;
		}
		ret += " }";
		return ret;
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
