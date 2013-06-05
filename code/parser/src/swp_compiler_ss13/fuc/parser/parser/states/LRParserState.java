package swp_compiler_ss13.fuc.parser.parser.states;


/**
 * Simple class that represents a parser-state by an id
 * 
 * @author Gero
 */
public class LRParserState {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final int id;
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRParserState(int id) {
      this.id = id;
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public boolean isErrorState() {
      return false;
   }
   
   public int getId() {
      return id;
   }


   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + id;
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
      LRParserState other = (LRParserState) obj;
      if (id != other.id)
         return false;
      return true;
   }
   
   @Override
   public String toString() {
      return "LRParserState (" + id + ")";
   }
}
