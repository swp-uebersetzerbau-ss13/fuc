package swp_compiler_ss13.fuc.parser.generator;

import static swp_compiler_ss13.fuc.parser.grammar.Terminal.Epsilon;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.util.It;



/**
 * Set of non-terminals that can derive the empty string.
 */
public class NullableSet implements Iterable<NonTerminal> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Set<NonTerminal> set = new HashSet<>();
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   /**
    * Computes the non-terminals that can derive the empty string.
    */
   public NullableSet(List<Production> productions) {
      // iterate as long as we get new nullable productions.
      // in the first step, we only get the trivial ones (X → ɛɛɛ...),
      // then the ones using X (e.g. Y -> XɛX), and so on
      boolean changed;
      do {
         changed = false;
         for (Production production : productions) {
            NonTerminal lhs = production.getLHS();
            // only check non-terminals which are not nullable already
            if (!set.contains(lhs)) {
               // all rhs symbols nullable?
               boolean nullable = true;
               for (Symbol symbol : production.getRHS()) {
                  if (!(symbol == Epsilon || set.contains(symbol))) {
                     nullable = false;
                     break;
                  }
               }
               // then remember it as a nullable terminal
               if (nullable) {
                  set.add(lhs);
                  changed = true;
               }
            }
         }
      } while (changed);
   }
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   @Override
   public Iterator<NonTerminal> iterator() {
      return new It<>(set);
   }
   
   
   public boolean contains(NonTerminal nonTerminal) {
      return set.contains(nonTerminal);
   }
   
   /**
    * Convenience method
    */
   public boolean contains(Symbol symbol) {
      if (!symbol.isNonTerminal())
         return false;
      return contains((NonTerminal) symbol);
   }
   
   @Override
	public String toString() {
		return "NullableSet: " + set.toString();
	}
}
