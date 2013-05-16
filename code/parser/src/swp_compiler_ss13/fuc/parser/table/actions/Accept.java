package swp_compiler_ss13.fuc.parser.table.actions;

import swp_compiler_ss13.fuc.parser.table.ActionEntry;


public class Accept extends ActionEntry {
   
   public Accept() {
      super(ActionEntryType.ACCEPT);
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
