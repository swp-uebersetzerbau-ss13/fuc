package swp_compiler_ss13.fuc.parser.grammar;

import java.util.Arrays;
import java.util.List;

import swp_compiler_ss13.fuc.parser.util.It;



/**
 * A production consists of a left hand side {@link NonTerminal} and a list of right hand side {@link Symbol}s.
 * 
 * If there are productions with more than one right hand sides
 * (in parser books separated by "|"), each of these sides
 * must be represented by its own {@link Production} instance.
 */
public final class Production {
   private int id;
   private NonTerminal lhs;
   private List<Symbol> rhs;
   
   // cache
   private Terminal lastTerminal;
   private Terminal precTerminal; // save the Terminal with the precedence of this Production
   private int rhsSizeWoEpsilon;
   private int hashCode;
   
   
   /**
    * Creates a new {@link Production}.
    */
   public Production(int id, NonTerminal lhs, List<Symbol> rhs) {
      // check rhs
      if (rhs.size() == 0) {
         // empty RHS means epsilon
         rhs = Arrays.asList((Symbol) Terminal.Epsilon);
         this.rhsSizeWoEpsilon = 0;
      } else if (rhs.size() == 1) {
         this.rhsSizeWoEpsilon = (rhs.get(0) == Terminal.Epsilon ? 0 : 1);
      } else if (rhs.size() > 1) {
         // check, that there is no epsilon in a RHS with at least 2 symbols
         for (Symbol symbol : rhs) {
            if (symbol == Terminal.Epsilon) {
               throw new IllegalArgumentException("Epsilon is only allowed as the single symbol of a RHS!");
            }
         }
         this.rhsSizeWoEpsilon = rhs.size();
      }
      this.id = id;
      this.lhs = lhs;
      this.rhs = rhs;
      
      // cache
      // find last terminal
      Terminal lastTerminal = null;
      for (int i = rhs.size() - 1; i >= 0; i--) {
         if (rhs.get(i) instanceof Terminal) {
            lastTerminal = (Terminal) rhs.get(i);
            break;
         }
      }
      this.lastTerminal = lastTerminal;
      this.precTerminal = (precTerminal != null) ? precTerminal : lastTerminal;
      
      // hash code
      computeHashcode();
   }
   
   
   public Production(int id, NonTerminal lhs, Symbol... rhs) {
      this(id, lhs, Arrays.asList(rhs));
   }
   
   
   public int getID() {
      return id;
   }
   
   
   public NonTerminal getLHS() {
      return lhs;
   }
   
   
   public List<Symbol> getRHS() {
      return rhs;
   }
   
   
   /**
    * Returns the number of right hand side symbols,
    * without epsilons.
    */
   public int getRHSSizeWoEpsilon() {
      return rhsSizeWoEpsilon;
   }
   
   
   /**
    * @return Last terminal of RHS or <code>null</code>
    */
   public Terminal getLastTerminal() {
      return lastTerminal;
   }
   
   
   @Override
   public boolean equals(Object obj) {
      if (obj instanceof Production) {
         Production p = (Production) obj;
         if (lhs != p.lhs)
            return false;
         if (!rhs.equals(p.rhs))
            return false;
         return true;
      }
      return false;
   }
   
   
   private int computeHashcode() {
      int sum = lhs.hashCode(); // default hashCode is the object address
      for (Symbol s : rhs) {
         sum = (sum + s.hashCode()) % ((1 << 31) - 1);
      }
      this.hashCode = sum;
      return this.hashCode;
   }
   
   @Override
   public int hashCode() {
      return hashCode;
   }
   
   
   public String getStringRep() {
      String result = lhs.toString() + " ->";
      for (Symbol s : rhs) {
         result += " " + s;
      }
      return result;
   }
   
   
   @Override
   public String toString() {
      StringBuilder rhsString = new StringBuilder();
      for (Symbol s : rhs)
         rhsString.append(s + " ");
      return lhs + " → " + rhsString;
   }
   
   
   public String toString(int dotPosition) {
      StringBuilder rhsString = new StringBuilder();
      It<Symbol> rhsSymbols = new It<Symbol>(rhs);
      for (Symbol s : rhsSymbols) {
         if (dotPosition == rhsSymbols.getIndex())
            rhsString.append(" .");
         rhsString.append(" " + s);
      }
      if (dotPosition == rhs.size())
         rhsString.append(" .");
      return lhs + " →" + rhsString;
   }
}
