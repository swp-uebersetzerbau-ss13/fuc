package swp_compiler_ss13.fuc.parser.generator;

import java.util.LinkedList;

import swp_compiler_ss13.fuc.parser.generator.automaton.Dfa;
import swp_compiler_ss13.fuc.parser.generator.automaton.DfaEdge;
import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR0State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;



public class LR0Generator extends ALRGenerator<LR0Item, LR0State> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LR0Generator(Grammar grammar) {
      super(grammar);
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   @Override
   public Dfa<LR0Item, LR0State> createDFA() {
      Grammar grammar = grammarInfo.getGrammar();
      
      // Init
      LR0State startState = new LR0State(new LR0Item(grammar.getStartProduction(), 0));
      Dfa<LR0Item, LR0State> dfa = new Dfa<LR0Item, LR0State>(startState);
      
      LinkedList<LR0State> todo = new LinkedList<>();
      todo.add(startState);
      
      // For all state, find edges to (possibly) new states
      while (!todo.isEmpty()) {
         LR0State kernel = todo.removeFirst();
         LR0State state = kernel.closure(grammarInfo);   // unpack!
         
         for (LR0Item item : state.getItems()) {
            if (item.isShiftable()) {
               Symbol symbol = item.getNextSymbol();
               if (symbol.equals(Terminal.EOF)) {
                  // $: We accept...? TODO Is the simplest solution, but... sufficient?
                  dfa.getEdges().add(DfaEdge.createAcceptEdge(kernel, symbol));
               } else {
                  LR0State nextState = state.goTo(symbol);
                  
                  // Is it new?
                  if (!dfa.getStates().contains(nextState)) {
                     dfa.getStates().add(nextState);
                     todo.add(nextState);
                  }
                  
                  // Add edge to automaton
                  dfa.getEdges().add(new DfaEdge<LR0State>(kernel, symbol, nextState, item));
               }
            }
         }
      }
      
      return dfa;
   }
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
}
