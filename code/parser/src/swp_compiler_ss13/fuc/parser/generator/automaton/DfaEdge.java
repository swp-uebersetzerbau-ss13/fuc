package swp_compiler_ss13.fuc.parser.generator.automaton;

import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;

/**
 * A part of the {@link Dfa} connecting two states of type <S> with a
 * {@link Symbol}.
 * 
 * @author Gero
 * 
 * @param <S> The type of the states this edge connects with each other
 */
public class DfaEdge<S> {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final S src;
   private final Symbol symbol;
   private final S dst;
   private final LR0Item srcItem;
   
   // cache
   private final int hashCode;
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public DfaEdge(S src, Symbol symbol, S dest, LR0Item srcItem) {
      this.src = src;
      this.symbol = symbol;
      this.dst = dest;
      this.srcItem = srcItem;
      
      // Identity by src x symbol
      int srcHash = src == null ? 0 : src.hashCode();
      int symbolHash = symbol == null ? 0 : symbol.hashCode();
      this.hashCode = srcHash * 100 + symbolHash;
   }
   
   public static <S> DfaEdge<S> createAcceptEdge(S src, Symbol symbol)
   {
      return new DfaEdge<S>(src, symbol, null, null);
   }
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public S getSrc() {
      return src;
   }

   public Symbol getSymbol() {
      return symbol;
   }

   public S getDst() {
      return dst;
   }
   
   public LR0Item getSrcItem() {
      return srcItem;
   }
   
   public boolean isDestAccepting() {
      return dst == null;
   }
   
   
   @Override
   public int hashCode() {
      return hashCode;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      DfaEdge<?> other = (DfaEdge<?>) obj;
      if (src == null) {
         if (other.src != null)
            return false;
      } else if (!src.equals(other.src))
         return false;
      if (symbol == null) {
         if (other.symbol != null)
            return false;
      } else if (!symbol.equals(other.symbol))
         return false;
      return true;
   }

   @Override
   public String toString() {
      return src + " " + symbol;
   }
}
