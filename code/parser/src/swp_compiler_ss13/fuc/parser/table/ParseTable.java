package swp_compiler_ss13.fuc.parser.table;

import swp_compiler_ss13.common.lexer.Token;

public interface ParseTable {
	
	public ActionEntry getActionEntry(int state, Token symbol);
	public void setActionEntry(int state, Token symbol, ActionEntry action) throws DoubleEntryException;

   public GotoEntry getGotoEntry(int state, Token symbol);
   public void setGotoEntry(int state, Token symbol, GotoEntry action) throws DoubleEntryException;
   
   public class DoubleEntryException extends Exception {
      private static final long serialVersionUID = 5218371583653601763L;
      
      public DoubleEntryException(String msg) {
         super(msg);
      }
   }
}
