package swp_compiler_ss13.fuc.parser.table.actions;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.table.ActionEntry;

public class Reduce extends ActionEntry {
	private final Production production;
	
	public Reduce(Production production) {
	   super(ActionEntryType.REDUCE);
		this.production = production;
	}
	
	public Production getProduction(){
		return production;
	}

   public int getPopCount() {
      return production.getNrOFSymbolsWOEpsilon();
   }
}
