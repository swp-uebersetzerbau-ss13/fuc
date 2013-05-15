package swp_compiler_ss13.fuc.parser.table.actions;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Item;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.table.ActionEntry;

public class Shift extends ActionEntry {
	
   private final int newState;
   private final Item shiftedItem;
   
	public Shift(Integer newState, Item shiftedItem){
	   super(ActionEntryType.SHIFT);
		this.newState = newState;
		this.shiftedItem = shiftedItem;
	}
	
	public int getNewState() {
      return newState;
   }
	
	public Item getShiftedItem() {
      return shiftedItem;
   }
	
	public Production getProduction() {
      return shiftedItem;
   }
	
	public int getPosition() {
      return shiftedItem.getDotPos();
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + newState;
      result = prime * result + ((shiftedItem == null) ? 0 : shiftedItem.hashCode());
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
      Shift other = (Shift) obj;
      if (newState != other.newState)
         return false;
      if (shiftedItem == null) {
         if (other.shiftedItem != null)
            return false;
      } else if (!shiftedItem.equals(other.shiftedItem))
         return false;
      return true;
   }
}