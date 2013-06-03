package swp_compiler_ss13.fuc.parser.parser.tables;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRErrorState;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;


public class LRGotoTable {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Map<LRTableKey, LRParserState> table = new HashMap<>();
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRGotoTable() {
      
   }
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRParserState get(LRParserState curState, NonTerminal curNonTerminal) {
      LRParserState result = table.get(new LRTableKey(curState, curNonTerminal));
      if (result == null) {
         return new LRErrorState("An Error occurred: No transition from " + curState + " with NonTerminal " + curNonTerminal);
      } else {
         return result;
      }
   }
   
   
   public void set(LRParserState newState, LRParserState curState, NonTerminal curNonTerminal) throws DoubleEntryException {
      LRTableKey key = new LRTableKey(curState, curNonTerminal);
      if (table.containsKey(key)) {
         LRParserState oldState = table.get(key);
         throw new DoubleEntryException("There already is a state " + oldState + " for the key " + key + "! (new state: " + newState + ")");
      }
      table.put(key, newState);
   }
}
