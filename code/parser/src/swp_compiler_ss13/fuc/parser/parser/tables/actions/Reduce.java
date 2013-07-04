package swp_compiler_ss13.fuc.parser.parser.tables.actions;

import swp_compiler_ss13.fuc.parser.grammar.Production;

/**
 * The Reduce-action of a LR-parser. It consists 
 */
public class Reduce extends ALRAction {
	private final Production production;
	
	/**
	 * @see Reduce
	 * @param production
	 */
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
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((production == null) ? 0 : production.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Reduce other = (Reduce) obj;
		if (production == null) {
			if (other.production != null)
				return false;
		} else if (!production.equals(other.production))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "[reduce: '" + production.toString() + "']";
	}
}
