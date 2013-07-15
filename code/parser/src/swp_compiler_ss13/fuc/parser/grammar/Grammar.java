package swp_compiler_ss13.fuc.parser.grammar;

import static swp_compiler_ss13.fuc.parser.grammar.NonTerminal.StartLHS;
import static swp_compiler_ss13.fuc.parser.grammar.Terminal.EOF;

import java.util.LinkedList;
import java.util.List;


/**
 * This class represents a context free grammar consisting of
 * {@link Terminal}s, {@link Production}s, {@link NonTerminal}s and {@link OpAssociativities}.
 * 
 * @author Gero
 */
public class Grammar {
   private final List<Terminal> terminals;
   private final List<NonTerminal> nonTerminals;
   private final List<Production> productions;
   private final OpAssociativities associativities;
   
   
   public Grammar(List<Terminal> terminals, List<NonTerminal> nonTerminals, List<Production> productions, OpAssociativities associativities) {
      this.terminals = terminals;
      this.nonTerminals = nonTerminals;
      this.productions = productions;
      this.associativities = associativities;
   }
   
   
   /**
    * Adds auxiliary start production "S' → S$" (Using
    * {@link NonTerminal#StartLHS}). If the grammar already contains a $
    * terminal, it is returned unmodified.
    */
   public Grammar extendByAuxStartProduction() {
      // Already augmented?
      if (terminals.contains(EOF)) {
         // yes. return unmodified grammar
         return this;
      } else {
         // No. Add StartLHS (S') and $ (EndOfInputStream)
         LinkedList<Terminal> terminals = new LinkedList<Terminal>();
         terminals.addAll(this.terminals);
         terminals.add(EOF);
         
         // Add StartLHS to the non-terminals list
         LinkedList<NonTerminal> nonTerminals = new LinkedList<NonTerminal>();
         nonTerminals.addAll(this.nonTerminals);
         nonTerminals.add(StartLHS);
         
         // Add start production to productions list
         Production startProduction = new Production(0, StartLHS, productions.get(0).getLHS(), EOF);
         LinkedList<Production> productions = new LinkedList<Production>();
         productions.add(startProduction);
         productions.addAll(this.productions);
         
         // Create and return grammar
         return new Grammar(terminals, nonTerminals, productions, associativities);
      }
   }
   
   
   public List<Terminal> getTerminals() {
      return terminals;
   }
   
   
   public List<NonTerminal> getNonTerminals() {
      return nonTerminals;
   }
   
   
   public Production getStartProduction() {
      return productions.get(0);
   }
   
   
   public List<Production> getProductions() {
      return productions;
   }
   
   public OpAssociativities getAssociativities() {
	return associativities;
}
}
