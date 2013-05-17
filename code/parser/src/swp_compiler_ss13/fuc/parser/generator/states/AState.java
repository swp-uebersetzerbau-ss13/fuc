package swp_compiler_ss13.fuc.parser.generator.states;

import java.util.Collection;
import java.util.HashSet;

import swp_compiler_ss13.fuc.parser.util.It;


public class AState<I> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   protected final Collection<I> items;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public AState(Collection<I> items) {
      this.items = items;
   }
   
   @SafeVarargs
   public AState(I... items) {
      this.items = new HashSet<>();
      for (I i : items) {
         this.items.add(i);
      }
   }
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public It<I> getItems() {
      return new It<>(items);
   }
   
   public I getFirstItem() {
      return items.iterator().next();
   }
   
   public int getItemsCount() {
      return items.size();
   }
}
