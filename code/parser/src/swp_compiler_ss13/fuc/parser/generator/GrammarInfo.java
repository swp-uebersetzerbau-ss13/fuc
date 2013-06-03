package swp_compiler_ss13.fuc.parser.generator;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.generator.terminals.EfficientTerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;



/**
 * This class contains a {@link Grammar} extended by more information,
 * like the set of nullable nonterminals and the FIRST sets.
 * 
 * These values are cached for performance reasons.
 */
public class GrammarInfo {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Grammar grammar;
   
   private final NullableSet nullableSet;
   private final FirstSets firstSets;
   private final FollowSets followSets;
   private final HashMap<NonTerminal, List<Production>> productionsStartingWith;
   private final Map<NonTerminal, List<Production>> constProductionsStartingWith;
   
   private final EfficientTerminalSet emptyTerminalsSet;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   /**
    * Creates a {@link GrammarInfo} from the given {@link Grammar}.
    */
   public GrammarInfo(Grammar grammar) {
      this.grammar = grammar;
      
      // Compute additional information about the grammar
      this.nullableSet = new NullableSet(grammar.getProductions());
      this.firstSets = new FirstSets(grammar, this.nullableSet);
      this.followSets = new FollowSets(grammar, firstSets, nullableSet);
      this.productionsStartingWith = new HashMap<NonTerminal, List<Production>>();
      for (NonTerminal nonTerminal : grammar.getNonTerminals()) {
         List<Production> list = new LinkedList<Production>();
         for (Production p : grammar.getProductions()) {
            if (p.getLHS() == nonTerminal)
               list.add(p);
         }
         this.productionsStartingWith.put(nonTerminal, Collections.unmodifiableList(list));
      }
      this.constProductionsStartingWith = Collections.unmodifiableMap(this.productionsStartingWith);
      
      // Empty set of terminals
      this.emptyTerminalsSet = new EfficientTerminalSet(grammar.getTerminals());
   }
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public Grammar getGrammar() {
      return grammar;
   }
   
   
   /**
    * @return all productions beginning with the given {@link NonTerminal}
    */
   public List<Production> getProductionsFrom(NonTerminal lhs) {
      return productionsStartingWith.get(lhs);
   }
   
   
   /**
    * @return all productions beginning with the given {@link NonTerminal}
    */
   public Map<NonTerminal, List<Production>> getProductionsFrom() {
      return constProductionsStartingWith;
   }
   
   
   /**
    * Gets the set of all nullable non-terminals, that means the
    * non-terminals that can derive the empty string.
    */
   public NullableSet getNullableSet() {
      return nullableSet;
   }
   
   
   public FirstSets getFirstSets() {
      return firstSets;
   }
   
   public FollowSets getFollowSets() {
      return followSets;
   }
   
   
   public EfficientTerminalSet getEmptyTerminalSet() {
      return emptyTerminalsSet;
   }
   
   
   public EfficientTerminalSet getTerminalSet(Terminal terminal) {
      return emptyTerminalsSet.plus(terminal);
   }
}
