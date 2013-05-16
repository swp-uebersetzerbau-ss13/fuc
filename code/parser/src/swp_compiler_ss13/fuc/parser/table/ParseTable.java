package swp_compiler_ss13.fuc.parser.table;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;

public interface ParseTable {
	
	public ActionEntry getActionEntry(int state, Token symbol);
	public void setActionEntry(int state, Terminal terminal, ActionEntry action) throws DoubleEntryException;

   public GotoEntry getGotoEntry(int state, Token symbol);
   public void setGotoEntry(int state, Variable variable, GotoEntry action) throws DoubleEntryException;
   
   public class DoubleEntryException extends Exception {
      private static final long serialVersionUID = 5218371583653601763L;
      
      public DoubleEntryException(String msg) {
         super(msg);
      }
   }
}
