package swp_compiler_ss13.fuc.parser.table.actions;

import swp_compiler_ss13.fuc.parser.table.ActionEntry;

public class Error extends ActionEntry {

   private final String msg;
   
   public Error(String msg) {
      super(ActionEntryType.ERROR);
      this.msg = msg;
   }
   
   public String getMsg() {
      return msg;
   }
}
