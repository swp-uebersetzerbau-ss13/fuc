package swp_compiler_ss13.fuc.parser.table;

public class GotoEntry {
   public static final GotoEntry ERROR_ENTRY = new GotoEntry(-1);
   
   private final int newState;
   
   public GotoEntry(int newState) {
      this.newState = newState;
   }
   
   public int getNewState() {
      return newState;
   }
   
   
   
   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + newState;
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
      GotoEntry other = (GotoEntry) obj;
      if (newState != other.newState)
         return false;
      return true;
   }

   @Override
   public String toString() {
      return "goto state: " + newState;
   }
}
