package swp_compiler_ss13.fuc.parser.parser.tables;

import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;


/**
 * Helper class that enables us to use a 1-dimensional map as substitution for the 2-dimensional parsing table. Combines
 * {@link LRParserState} and {@link Symbol} as key for the {@link LRParsingTable}-maps.
 * 
 * @author Gero
 */
public class LRTableKey {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final LRParserState state;
   private final Symbol symbol;
   
   // cache
   private final int hashCode;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRTableKey(LRParserState state, Symbol symbol) {
	   if (state == null || symbol == null) {
		   throw new NullPointerException("LRTableKey components must not be null!");
	   }
      this.state = state;
      this.symbol = symbol;
      
      // Precompute hash
      final int prime = 31;
      int result = 1;
      result = prime * result + ((state == null) ? 0 : state.hashCode());
      result = prime * result + ((symbol == null) ? 0 : symbol.hashCode());
      this.hashCode = result;
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRParserState getState() {
      return state;
   }
   
   public Symbol getSymbol() {
      return symbol;
   }
   
   @Override
   public int hashCode() {
      return hashCode;
   }
   
   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      LRTableKey other = (LRTableKey) obj;
      if (state == null) {
         if (other.state != null)
            return false;
      } else if (!state.equals(other.state))
         return false;
      if (symbol == null) {
         if (other.symbol != null)
            return false;
      } else if (!symbol.equals(other.symbol))
         return false;
      return true;
   }
   
   @Override
   public String toString() {
      return "[Key " + state.getId() + "|" + symbol.toString() + "]";
   }
}
