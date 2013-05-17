package swp_compiler_ss13.fuc.parser.generator.states;

import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

import swp_compiler_ss13.fuc.parser.generator.FirstSets;
import swp_compiler_ss13.fuc.parser.generator.GrammarInfo;
import swp_compiler_ss13.fuc.parser.generator.NullableSet;
import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.terminals.ITerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;


public class LR1State extends ALRState<LR1Item> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final LR0State kernel;
   private final Map<LR0Item, LR1Item> itemsWithKernels;
   private final int hashCode;
   
   // cache
   private LR1State closureCache = null;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LR1State(Set<LR1Item> items) {
      super(items);
      
      // Calc hash once
      int tmpHashCode = 0;
      itemsWithKernels = new HashMap<>();
      for (LR1Item item : items) {
         itemsWithKernels.put(item.getLR0Kernel(), item);
         tmpHashCode += item.hashCode();
      }
      kernel = new LR0State(itemsWithKernels.keySet());
      hashCode = tmpHashCode;
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   @Override
   public LR1State goTo(Symbol symbol) {
      HashSet<LR1Item> ret = new HashSet<>();
      //find all items where the given symbol follows and add them shifted
      for (LR1Item item : items)
      {
         if (item.getNextSymbol() == symbol)
         {
            ret.add(item.shift());
         }
      }
      return new LR1State(ret);
   }

   @Override
   public LR1State closure(GrammarInfo grammarInfo) {
      if (closureCache == null)
         closureCache = new LR1State(closureItems(grammarInfo));
      return closureCache;
   }
   
   /**
    * Returns the closure of the {@link LR1Item}s of this state as set of
    * {@link LR1Item}s. For a description of this algorithm, see Appel's book,
    * page 63.
    * 
    * TODO: optimization possible by using persistent data structures?
    */
   private HashSet<LR1Item> closureItems(GrammarInfo grammarInfo)
   {
      //the closure contains all items of the source...
      //(we use a map with the LR(0)-part as the key for very fast access,
      //since we will probably often add new lookahead symbols to existing LR1Items)
      HashMap<LR0Item, ITerminalSet> items = new HashMap<>();
      for (LR1Item item : this.items)
      {
         items.put(item.getLR0Kernel(), item.getLookaheads());
      }
      // ... and the following ones: for any item "A → α.Xβ with lookahead z",
      // any "X → γ" and any w ∈ FIRST(βz), add "X → .γ with lookahead w"
      LinkedList<LR1Item> queue = new LinkedList<LR1Item>();
      queue.addAll(this.items);
      FirstSets firstSets = grammarInfo.getFirstSets();
      NullableSet nullableSet = grammarInfo.getNullableSet();
      while (!queue.isEmpty())
      {
         LR1Item item = queue.removeFirst(); //item is A → α.Xβ with lookahead z"
         Symbol nextSymbol = item.getNextSymbol();
         if (nextSymbol instanceof NonTerminal) //nextSymbol is "X"
         {
            ITerminalSet firstSet = item.getNextLookaheads(firstSets, nullableSet); //all "w"s
            for (Production p : grammarInfo.getProductionsFrom((NonTerminal) nextSymbol)) //p is each "X → γ"
            {
               LR0Item newItemLR0 = new LR0Item(p, 0);
               //look, if there is already a LR(1) item with that LR(0) kernel. if so, add the
               //new lookahead symbols there. If not, add the LR(1) item to the result set.
               ITerminalSet sameKernelItemLookaheads = items.get(newItemLR0);
               if (sameKernelItemLookaheads != null)
               {
                  //add to existing LR1Item
                  ITerminalSet newLookaheads = sameKernelItemLookaheads.plusAll(firstSet);
                  //if new lookahead was found, add again to queue
                  if (!newLookaheads.equals(sameKernelItemLookaheads))
                  {
                     items.put(newItemLR0, newLookaheads);
                     queue.add(new LR1Item(newItemLR0, newLookaheads));
                  }
               }
               else
               {
                  //new item
                  items.put(newItemLR0, firstSet);
                  queue.add(new LR1Item(newItemLR0, firstSet));
               }
            }
         }
      }
      //collect resulting LR(1) items
      HashSet<LR1Item> ret = new HashSet<LR1Item>();
      for (LR0Item itemLR0 : items.keySet())
      {
         ret.add(new LR1Item(itemLR0.getProduction(), itemLR0.getPosition(), items.get(itemLR0)));
      }
      return ret;
   }
   
   
   /**
    * Gets the item of this state which has the given LR(0) kernel,
    * or <code>null</code>, if there is no such item.
    */
   public LR1Item getItemByLR0Kernel(LR0Item kernel)
   {
      return itemsWithKernels.get(kernel);
   }


   /**
    * Merges this state with the given one and returns the result.
    * Only works, if both states have equal LR(0) kernels.
    */
   public LR1State merge(LR1State state)
   {
      HashSet<LR1Item> items = new HashSet<LR1Item>();
      for (LR1Item item1 : this.items)
      {
         LR1Item newItem = item1;
         
         //find lr0-equal item and merge them
         LR1Item item2 = state.getItemByLR0Kernel(item1.getLR0Kernel());
         newItem = newItem.merge(item2);
         
         items.add(newItem);
      }
      return new LR1State(items);
   }
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   @Override
   public int hashCode() {
      return hashCode;
   }
   
   @Override public boolean equals(Object obj)
   {
      if (obj instanceof LR1State)
      {
         LR1State s = (LR1State) obj;
         if (items.size() != s.items.size())
            return false;
         for (LR1Item item : items)
         {
            if (!s.items.contains(item))
               return false;
         }
         return true;
      }
      return false;
   }
   
   public LR0State getKernel() {
      return kernel;
   }
}
