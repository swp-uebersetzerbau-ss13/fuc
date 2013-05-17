package swp_compiler_ss13.fuc.parser.parser.tables.actions;

import swp_compiler_ss13.fuc.parser.grammar.Production;

public class Reduce extends ALRAction {
	private final Production production;
	
	public Reduce(Production production) {
	   super(ELRActionType.REDUCE);
		this.production = production;
	}
	
	public Production getProduction(){
		return production;
	}

   public int getPopCount() {
      return production.getRHSSizeWoEpsilon(); // TODO Correct???
   }
}
