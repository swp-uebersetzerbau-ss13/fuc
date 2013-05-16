package swp_compiler_ss13.fuc.parser.table;

public abstract class ActionEntry {
   
   private final ActionEntryType type;
   
   public ActionEntry(ActionEntryType type) {
      this.type = type;
   }
   
	public ActionEntryType getType() {
	   return type;
	}
	
	public enum ActionEntryType {
		SHIFT,
		REDUCE,
		ACCEPT,
		ERROR
	}
}
