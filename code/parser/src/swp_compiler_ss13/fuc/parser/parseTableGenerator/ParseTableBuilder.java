package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol.SymbolType;
import swp_compiler_ss13.fuc.parser.table.GotoEntry;
import swp_compiler_ss13.fuc.parser.table.ParseTable;
import swp_compiler_ss13.fuc.parser.table.ParseTable.DoubleEntryException;
import swp_compiler_ss13.fuc.parser.table.ParseTableImpl;
import swp_compiler_ss13.fuc.parser.table.actions.Reduce;
import swp_compiler_ss13.fuc.parser.table.actions.Shift;

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
	public ParseTable getTable(Grammar grammar) throws ParseTableBuildException {
		ParseTableImpl table = new ParseTableImpl();
		// these two objects are a collection of the already discovered states:
		Map<Integer, ItemSet> stateToItemSet = new HashMap<Integer, ItemSet>();
		Map<ItemSet, Integer> itemSetToState = new HashMap<ItemSet, Integer>();
		if(grammar.getProductions().size() == 0)
			return table;
		
		// calculate state0 (theoritically: CLOSURE ( { S' -> .S } )      ):
		ItemSet state0 = new ItemSet(new Item(grammar.getProductions().get(0),0)); 
		state0.CLOSURE(grammar);
		
		/* the following algorithm searches for all ItemSets (/States) and fills the parseTable
		 * with the transition function between them.
		 */
		Set<ItemSet> lastDiscovered = new HashSet<ItemSet>();
		Set<ItemSet> nextToBeDiscovered = new HashSet<ItemSet>();
		// algorithm starts with state0:
		stateToItemSet.put( 0, state0 );
		itemSetToState.put( state0, 0 );
		lastDiscovered.add( state0 );
		
		do {
			for( ItemSet currentState : lastDiscovered ) {
				int indexStateCurrent = itemSetToState.get( currentState );
				Map<Symbol,ItemSet> GOTO = GOTO(currentState, itemSetToState.keySet());
				// possibly add a reduce:
				{
					// check, wheather there are reducable items ( ones ending with a dot ):
					Item itemToReduce = currentState.getReducableItem();
					if( itemToReduce != null) {
						Reduce reduce = new Reduce( itemToReduce );
						try {
							for(Terminal t : itemToReduce.getFOLLOW()) {
								table.setActionEntry(indexStateCurrent, t, reduce);
							}
						}
						catch (DoubleEntryException e) {
							throw new ParseTableBuildException("Something went horribly wrong: " + e.getMessage());
						}
					}
				}
				// discover new states via GOTO( currentState ):
				for( Map.Entry<Symbol,ItemSet> arrow : GOTO.entrySet()) {
					Symbol sym = arrow.getKey();
					ItemSet stateDest = arrow.getValue();
					// 1. add the state (if it has not yet been discovered!):
					int indexStateDest = -1 ;
					if( itemSetToState.containsKey( stateDest) ) {
						indexStateDest = itemSetToState.get( stateDest );
					}
					else {
						indexStateDest = stateToItemSet.size();
						stateToItemSet.put( indexStateDest, stateDest );
						itemSetToState.put(stateDest, indexStateDest ); 
						
						nextToBeDiscovered.add( stateDest );
					}
					
					// 2. add the arrow to the parseTable:
					if( sym.getType() == SymbolType.TERMINAL ) {
						Terminal t = (Terminal )sym;
						Shift shift = new Shift( indexStateDest );
						try {
							table.setActionEntry( indexStateCurrent, t, shift);
						}
						catch (DoubleEntryException e) {
							throw new ParseTableBuildException("Something went horribly wrong: " + e.getMessage());
						}
					}
					else { //( sym.getType() == SymbolType.TERMINAL )
						Variable v = (Variable )sym;
						GotoEntry goto_ = new GotoEntry( indexStateDest );
						try {
							table.setGotoEntry( indexStateCurrent, v, goto_);
						}
						catch (DoubleEntryException e) {
							throw new ParseTableBuildException("Something went horribly wrong: " + e.getMessage());
						}
					}
				}
			}
			lastDiscovered = nextToBeDiscovered;
		}
		while ( ! nextToBeDiscovered.isEmpty() ); // as long as new states are discovered
		
		return table;
	}
	
	/*ItemSet getReduceDest(ItemSet state) {
		Item i = state.getReducableItem();
		if( i != null ) {
			Variable left = i.getLeft();
			List<Symbol> right = i.getRight();
			ItemSet currentSet = state;
			// first go "back" via the right side of the rule:
			for(int iSymRight= right.size()-1; iSymRight>=0; iSymRight++)
			{
				Symbol symRight = right.get(iSymRight);
				currentSet = currentSet.getPrev().get(symRight);
			}
			// secondly go once into the direction of the left side of the rule:
			currentSet = currentSet.getGOTO().get(left);
			return currentSet;
		}
		return null;
	}*/
	
	/**
	 * this method calculates all possible transitions to a following state.
	 * If one of the states that can be reached from the origin is already present in
	 * the Set of states that have already been discovered, a reference to it is used
	 * (this is absolutely necessary, to enable loops!)
	 */
	private Map<Symbol,ItemSet> GOTO(ItemSet origin, Set<ItemSet> itemSetAlreadyDiscovered) {
		Map<Symbol,ItemSet> GOTO = new HashMap<Symbol,ItemSet>();
		// go through every item:
		for( Item item : origin ) {
			// <Var> -> x1 ... xi . symAfterDot ...
			Symbol symAfterDot = item.getSymbolAfterDot();
			if( symAfterDot != null ) {
				ItemSet itemSetDest = null;
				if( ! GOTO.containsKey(symAfterDot)) {
					itemSetDest = new ItemSet();
					GOTO.put(symAfterDot, itemSetDest);
				}
				else {
					itemSetDest = GOTO.get( symAfterDot );
				}
				itemSetDest.add(new Item( item, item.getDotPos()+1 ));
			}
		}
		// replace states in the GOTO-Set by references to existing ones, if possible:
		for( Map.Entry<Symbol,ItemSet> gotoEntry : GOTO.entrySet()) {
			if( itemSetAlreadyDiscovered.contains(gotoEntry.getValue()) )
			{
				// get a reference to the already existing state:
				for( ItemSet alreadyExistingState : itemSetAlreadyDiscovered) {
					if( alreadyExistingState.equals(gotoEntry.getValue()))
					{
						gotoEntry.setValue( alreadyExistingState );
						break;
					}
				}
			}
		}
		return GOTO;
	}
	
	private Set<Item> CLOSURE(Set<Item> item) {
		return null;
	}
	
	public class StateTuple implements Comparable<StateTuple> {
		public Integer stateIndex;
		public ItemSet stateSet;
		@Override
		public int compareTo(StateTuple other) {
			return stateIndex.compareTo(other.stateIndex);
		}
		@Override
		public boolean equals(Object other) {
			if( other instanceof StateTuple ) {
				return (compareTo((StateTuple )other) == 0);
			}
			return false;
		}
		@Override
		public int hashCode() {
			return stateIndex;
		}
	}
	
	public class ParseTableBuildException extends Exception {
		public ParseTableBuildException() {
			super();
		}
		public ParseTableBuildException(String message) {
			super(message);
		}
		private static final long serialVersionUID = 1L;
	}
}