package swp_compiler_ss13.fuc.parser.table.actions;

import swp_compiler_ss13.fuc.parser.table.ActionEntry;

public class Shift extends ActionEntry {
	
   private final int newState;
   
	public Shift(Integer newState){
	   super(ActionEntryType.SHIFT);
		this.newState = newState;
	}
	
	public int getNewState() {
      return newState;
   }
}