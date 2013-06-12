package swp_compiler_ss13.fuc.parser.generator.states;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;

import swp_compiler_ss13.fuc.parser.generator.GrammarInfo;
import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;


/**
 * Implements {@link #goTo(Symbol)} and {@link #closure(GrammarInfo)} from
 * {@link ALRState} for LR0 grammar generation.
 * 
 * @author Gero
 */
public class LR0State extends ALRState<LR0Item> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   // cache
   private LR0State closureCache = null;
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LR0State(Collection<LR0Item> items) {
      super(items);
   }
   
   public LR0State(LR0Item... items) {
      super(items);
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   @Override
   public LR0State goTo(Symbol symbol) {
      HashSet<LR0Item> ret = new HashSet<LR0Item>();
      // Find all items where the given symbol follows and add them shifted
      for (LR0Item item : getItems()) {
         if (item.getNextSymbol() == symbol) {
            ret.add(item.shift());
         }
      }
      return new LR0State(ret);
   }
   
   
   @Override
   public LR0State closure(GrammarInfo grammarInfo) {
      if (closureCache == null) {
         closureCache = calcClosure(grammarInfo);
      }
      return closureCache;
   }
   
   private LR0State calcClosure(GrammarInfo grammarInfo) {
      // The closure contains all items of the source...
      HashSet<LR0Item> result = new HashSet<LR0Item>();
      result.addAll(items);
      
      // ... and the following ones (for any item "A → α.Xβ" and any "X → γ" add "X → .γ")
      LinkedList<LR0Item> queue = new LinkedList<LR0Item>();
      queue.addAll(items);
      while (!queue.isEmpty()) {
         LR0Item it = queue.removeFirst();
         Symbol nextSymbol = it.getNextSymbol();
         if (it.isShiftable() && nextSymbol.isNonTerminal()) {
            for (Production p : grammarInfo.getProductionsFrom((NonTerminal) nextSymbol)) {
               LR0Item newItem = new LR0Item(p, 0);
               if (!result.contains(newItem)) {
                  result.add(newItem);
                  queue.addLast(newItem);
               }
            }
         }
      }
      return new LR0State(result);
   }
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
}
