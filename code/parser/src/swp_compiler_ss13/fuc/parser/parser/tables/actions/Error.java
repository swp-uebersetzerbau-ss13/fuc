package swp_compiler_ss13.fuc.parser.parser.tables.actions;


public class Error extends ALRAction {

   private final String msg;
   
   public Error(String msg) {
      super(ELRActionType.ERROR);
      this.msg = msg;
   }
   
   public String getMsg() {
      return msg;
   }
}
