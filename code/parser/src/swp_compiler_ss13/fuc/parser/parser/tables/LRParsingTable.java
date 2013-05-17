package swp_compiler_ss13.fuc.parser.parser.tables;

import java.util.LinkedList;

import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.util.It;


/**
 * Consists of a {@link LRActionTable} and a {@link LRGotoTable}.
 * First state is the start state!!!
 * 
 * @author Gero
 */
public class LRParsingTable {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final LinkedList<LRParserState> states;
   
   private final LRActionTable actionTable;
   private final LRGotoTable gotoTable;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRParsingTable(int statesCount) {
      // Initialize bare states TODO Take LRParsingStates here
      states = new LinkedList<>();
      for (int i = 0; i < statesCount; i++) {
         states.add(new LRParserState(i));
      }
      
      // Create empty tables
      actionTable = new LRActionTable();
      gotoTable = new LRGotoTable();
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRParserState getStartState() {
      return states.getFirst();
   }
   
   public int getStatesCount() {
      return states.size();
   }
   
   public It<LRParserState>getStates() {
      return new It<LRParserState>(states);
   }
   
   public LRActionTable getActionTable() {
      return actionTable;
   }
   
   public LRGotoTable getGotoTable() {
      return gotoTable;
   }
}
