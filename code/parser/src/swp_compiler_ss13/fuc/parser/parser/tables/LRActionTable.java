package swp_compiler_ss13.fuc.parser.parser.tables;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Error;


public class LRActionTable {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Map<LRTableKey, ALRAction> table = new HashMap<>();
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRActionTable() {
      
   }
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public ALRAction get(LRParserState curState, Terminal curTerminal) {
      LRTableKey key = new LRTableKey(curState, curTerminal);
      ALRAction result = table.get(key);
      if (result == null) {
         return new Error("No entry for " + key + "!");
      } else {
         return result;
      }
   }
   
   
   public void set(ALRAction action, LRParserState curState, Terminal curTerminal) throws DoubleEntryException {
      LRTableKey key = new LRTableKey(curState, curTerminal);
      if (table.containsKey(key)) {
         ALRAction oldAction = table.get(key);
         throw new DoubleEntryException("There already is a state " + oldAction + " for the key " + key
               + "! (new state: " + action + ")");
      }
      table.put(key, action);
   }
}
