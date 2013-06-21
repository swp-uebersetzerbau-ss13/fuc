package swp_compiler_ss13.fuc.parser.parser.tables.actions;

import swp_compiler_ss13.fuc.parser.grammar.Production;

/**
 * The Reduce-action of a LR-parser. It consists 
 */
public class Reduce extends ALRAction {
	private final Production production;
	
	public Reduce(Production production) {
	   super(ELRActionType.REDUCE);
		this.production = production;
	}
	
	/**
	 * @return The {@link Production} that gets reduced to its LHS by this action
	 */
	public Production getProduction(){
		return production;
	}

   /**
	 * @return The number of items to pop from the stack when reducing this action
	 */
	public int getPopCount() {
      return production.getRHSSize();
   }
}
