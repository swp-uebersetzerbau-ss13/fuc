package swp_compiler_ss13.fuc.parser.table;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;
import swp_compiler_ss13.fuc.parser.table.actions.Error;


public class ParseTableImpl implements ParseTable {
   
   private final Map<Key, ActionEntry> actions = new HashMap<>();
   private final Map<Key, GotoEntry> gotos = new HashMap<>();
   
   public ParseTableImpl() {
      
   }
   
   @Override
   public void setActionEntry(int state, Terminal terminal, ActionEntry newEntry) throws DoubleEntryException {
      Key key = new Key(state, terminal);
      if (actions.containsKey(key)) {
         ActionEntry oldEntry = actions.get(key);
         throw new DoubleEntryException("There already is an ActionEntry for key " + key + ": " + oldEntry + " (old) | " + newEntry + " (new)");
      }
      actions.put(key, newEntry);
   }
   
   @Override
   public ActionEntry getActionEntry(int state, Terminal symbol) {
      Key key = new Key(state, symbol);
      ActionEntry action = actions.get(key);
      if (action == null) {
         return new Error("There is no entry for key " + key + " in the ACTION table!!!");
      }
      return action;
   }
   
   @Override
   public void setGotoEntry(int state, Variable nonTerminal, GotoEntry newEntry) throws DoubleEntryException {
      Key key = new Key(state, nonTerminal);
      if (gotos.containsKey(key)) {
         GotoEntry oldEntry = gotos.get(key);
         throw new DoubleEntryException("There already is an GotoEntry for key " + key + ": " + oldEntry + " (old) | " + newEntry + " (new)");
      }
      gotos.put(key, newEntry);
   }
   
   @Override
   public GotoEntry getGotoEntry(int state, Variable nonTerminal) {
      Key key = new Key(state, nonTerminal);
      GotoEntry targetEntry = gotos.get(key);
      if (targetEntry == null) {
         return GotoEntry.ERROR_ENTRY;
      }
      return targetEntry;
   }
   
   
   private static class Key {
      private final Integer state;
      private final Symbol symbol;
      
      private Key(Integer state, Symbol symbol) {
         this.state = state;
         this.symbol = symbol;
      }
      
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((state == null) ? 0 : state.hashCode());
         result = prime * result + ((symbol == null) ? 0 : symbol.getString().hashCode()); // Use tokens value instead of
                                                                                          // tokenImpl, as equals is
                                                                                          // implementation
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
         Key other = (Key) obj;
         if (state == null) {
            if (other.state != null)
               return false;
         } else if (!state.equals(other.state))
            return false;
         if (symbol == null) {
            if (other.symbol != null)
               return false;
         } else if (!symbol.getString().equals(other.symbol.getString()))
            return false;
         return true;
      }
      
      @Override
      public String toString() {
         return state + "|" + symbol.getString();
      }
   }
}
