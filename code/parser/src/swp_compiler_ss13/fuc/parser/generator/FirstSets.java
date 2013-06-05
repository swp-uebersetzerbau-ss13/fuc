package swp_compiler_ss13.fuc.parser.generator;

import static swp_compiler_ss13.fuc.parser.grammar.Terminal.Epsilon;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.generator.terminals.EfficientTerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;




/**
 * Calculates and caches the follow sets for the given grammar
 * 
 * @author Gero
 */
public final class FirstSets {
   
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Map<Symbol, EfficientTerminalSet> data;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   /**
    * Computes the FIRST sets of all symbols of the
    * given grammar, using the precomputed set of nullable non-terminals.
    * This algorithm is inspired by ASU99, page 230 (german edition) (rules).
    */
   public FirstSets(Grammar grammar, NullableSet nullableSet) {
      Map<Symbol, EfficientTerminalSet> data = new HashMap<>();
      
      // initialize map for all symbols
      EfficientTerminalSet emptySet = new EfficientTerminalSet(grammar.getTerminals());
      for (Terminal terminal : grammar.getTerminals()) {
         data.put(terminal, emptySet);
      }
      for (NonTerminal nonTerminal : grammar.getNonTerminals()) {
         data.put(nonTerminal, emptySet);
      }
      
      // rule 1: if X is a terminal, then FIRST(X) = {X}
      for (Terminal terminal : grammar.getTerminals()) {
         data.put(terminal, data.get(terminal).plus(terminal));
      }
      data.put(Epsilon, emptySet.plus(Epsilon));
      
      // rule 2: if X → ɛ is a production, add ɛ to FIRST(X)
      for (NonTerminal nullableNonTerminal : nullableSet) {
         data.put(nullableNonTerminal, data.get(nullableNonTerminal).plus(Epsilon));
      }
      
      // rule 3: if X is a non-terminal and X → Y1 Y2 ... Yk is
      // a production, then add a to FIRST(X), if a is in FIRST(Yi) for
      // any i and FIRST(Y1) ... FIRST(Yi-1) are all nullable. if all
      // FIRST(Y1) ... FIRST(Yk) are nullable, add ɛ to FIRST(X)
      // (Appel ignores the epsilon, but we do not, according to ASU99)
      boolean changed;
      do {
         changed = false;
         for (Production production : grammar.getProductions()) {
            NonTerminal lhs = production.getLHS();
            // find first not nullable rhs symbol. up to this
            // symbol, add all FIRST-symbols of these rhs symbols to
            // the FIRST-set of the current lhs symbol
            Symbol firstNotNullableSymbol = null;
            for (Symbol symbol : production.getRHS()) {
               if (!(symbol == Epsilon || nullableSet.contains(symbol))) {
                  // terminal or not nullable non-terminal, stop
                  firstNotNullableSymbol = symbol;
                  break;
               } else {
                  // epsilon or nullable non-terminal, so go on, but
                  // add their FIRST values
                  EfficientTerminalSet to = data.get(lhs);
                  EfficientTerminalSet from = data.get(symbol);
                  EfficientTerminalSet merged = to.plusAllExceptEpsilon(from);
                  data.put(lhs, merged);
                  changed |= (!merged.equals(to));
               }
            }
            // first not nullable symbol found? than add its FIRST values.
            // otherwise add epsilon
            if (firstNotNullableSymbol != null) {
               EfficientTerminalSet to = data.get(lhs);
               EfficientTerminalSet from = data.get(firstNotNullableSymbol);
               EfficientTerminalSet merged = to.plusAllExceptEpsilon(from);
               data.put(lhs, merged);
               changed |= (!merged.equals(to));
            } else {
               EfficientTerminalSet before = data.get(lhs);
               EfficientTerminalSet after = before.plus(Epsilon);
               data.put(lhs, after);
               changed |= (!before.equals(after));
            }
         }
      } while (changed);
      
      this.data = data;
   }
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   /**
    * Gets the FIRST set for the given symbol.
    */
   public EfficientTerminalSet get(Symbol symbol) {
      return data.get(symbol);
   }
}
