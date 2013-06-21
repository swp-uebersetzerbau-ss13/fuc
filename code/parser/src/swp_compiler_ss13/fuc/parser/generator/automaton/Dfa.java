package swp_compiler_ss13.fuc.parser.generator.automaton;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import swp_compiler_ss13.fuc.parser.generator.items.Item;
import swp_compiler_ss13.fuc.parser.generator.states.AState;


/**
 * A deterministic finite automaton (DFA) as a interim step for the generation
 * of parsing tables. Generators create DFAs first from the grammar, which are
 * then converted to actions by moving along their edges.
 * 
 * @author Gero
 * 
 * @param <I> The item type the states consists of 
 * @param <S> The states type of this automaton
 */
public class Dfa<I extends Item, S extends AState<I>> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Set<S> states = new HashSet<>();
   private final Set<DfaEdge<S>> edges = new HashSet<>();
   private final S startState;
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public Dfa(S startState) {
      states.add(startState);
      this.startState = startState;
   }
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   public List<DfaEdge<S>> getEdgesFrom(S state) {
      LinkedList<DfaEdge<S>> result = new LinkedList<>();
      for (DfaEdge<S> edge : edges) {
         if (edge.getSrc().equals(state)) {
            result.add(edge);
         }
      }
      return result;
   }
   
   public List<DfaEdge<S>> getEdgesTo(S state) {
      LinkedList<DfaEdge<S>> result = new LinkedList<>();
      for (DfaEdge<S> edge : edges) {
         if (edge.getDst().equals(state)) {
            result.add(edge);
         }
      }
      return result;
   }
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public Set<S> getStates() {
      return states;
   }
   
   public Set<DfaEdge<S>> getEdges() {
      return edges;
   }
   
   public S getStartState() {
      return startState;
   }
}
