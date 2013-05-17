package swp_compiler_ss13.fuc.parser.parser.tables.actions;



public class Accept extends ALRAction {
   
   public Accept() {
      super(ELRActionType.ACCEPT);
   }
   
   @Override
   public boolean equals(Object obj) {
      if (obj instanceof Accept) {
         return true;
      }
      return false;
   }
   
   @Override
   public String toString() {
      return "accept";
   }
}
