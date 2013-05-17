package swp_compiler_ss13.fuc.parser.parser.tables.actions;

import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;


public class Shift extends ALRAction {
	
   private final LRParserState newState;
   
	public Shift(LRParserState newState){
	   super(ELRActionType.SHIFT);
		this.newState = newState;
	}
	
	public LRParserState getNewState() {
      return newState;
   }
}