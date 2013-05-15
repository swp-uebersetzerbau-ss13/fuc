package swp_compiler_ss13.fuc.parser.table;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.parser.table.actions.Error;


public class ParseTableImpl implements ParseTable {
   
   private final Map<Key, ActionEntry> actions = new HashMap<>();
   private final Map<Key, GotoEntry> gotos = new HashMap<>();
   
   public ParseTableImpl() {
      
   }
   
   @Override
   public void setActionEntry(int state, Token symbol, ActionEntry newEntry) throws DoubleEntryException {
      Key key = new Key(state, symbol);
      if (actions.containsKey(key)) {
         ActionEntry oldEntry = actions.get(key);
         throw new DoubleEntryException("There already is an ActionEntry for key " + key + ": " + oldEntry + " (old) | " + newEntry + " (new)");
      }
      actions.put(key, newEntry);
   }
   
   @Override
   public ActionEntry getActionEntry(int state, Token symbol) {
      Key key = new Key(state, symbol);
      ActionEntry action = actions.get(key);
      if (action == null) {
         return new Error("There is no entry for key " + key + "!!!");
      }
      return action;
   }
   
   @Override
   public void setGotoEntry(int state, Token symbol, GotoEntry newEntry) throws DoubleEntryException {
      Key key = new Key(state, symbol);
      if (gotos.containsKey(key)) {
         GotoEntry oldEntry = gotos.get(key);
         throw new DoubleEntryException("There already is an GotoEntry for key " + key + ": " + oldEntry + " (old) | " + newEntry + " (new)");
      }
      gotos.put(key, newEntry);
   }
   
   @Override
   public GotoEntry getGotoEntry(int state, Token symbol) {
      Key key = new Key(state, symbol);
      GotoEntry targetEntry = gotos.get(key);
      if (targetEntry == null) {
         return GotoEntry.ERROR_ENTRY;
      }
      return targetEntry;
   }
   
   
   private static class Key {
      private final Integer state;
      private final Token symbol;
      
      private Key(Integer state, Token symbol) {
         this.state = state;
         this.symbol = symbol;
      }
      
      @Override
      public int hashCode() {
         final int prime = 31;
         int result = 1;
         result = prime * result + ((state == null) ? 0 : state.hashCode());
         result = prime * result + ((symbol == null) ? 0 : symbol.getValue().hashCode()); // Use tokens value instead of
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
         } else if (!symbol.getValue().equals(other.symbol.getValue()))
            return false;
         return true;
      }
      
      @Override
      public String toString() {
         return state + "|" + symbol.getValue();
      }
   }
}
