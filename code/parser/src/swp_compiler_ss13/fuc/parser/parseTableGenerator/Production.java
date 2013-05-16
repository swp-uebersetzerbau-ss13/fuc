package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;
import java.util.List;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol.SymbolType;


public class Production implements Comparable<Production> {
   
	private final Variable left;
	private final List<Symbol> right;
   
   public Production(Variable left, List<Symbol> right) {
      this.left = left;
      this.right = new ArrayList<Symbol>(right);
   }
   public Production(Variable left, Symbol... right) {
      this.left = left;
      this.right = new ArrayList<Symbol>();
      for( Symbol rightSym : right) {
    	  this.right.add(rightSym);
      }
   }
   
   /*
    * public Production(String leftStr,String rightStr[])
    * {
    * left = new Variable(leftStr);
    * right = new ArrayList<Symbol>();
    * List<Symbol> right = new ArrayList<Symbol>();
    * for(int i=0; i<rightStr.length; i++)
    * right.add(new Variable(rightStr[i]));
    * }
    */
   
   
   public Variable getLeft() {
      return left;
   }
   
   public List<Symbol> getRight() {
      return right;
   }
   
   public Set<Terminal> getFIRST() {
      return left.getFIRST();
   }
   
   public Set<Terminal> getFOLLOW() {
      return left.getFOLLOW();
   }
   
   public int getNrOfNonTerminals() {
      int result = 0;
      for (Symbol s : right) {
         if (s.getType() == SymbolType.VARIABLE) {
            result++;
         }
      }
      return result;
   }
   
   public int getNrOFSymbolsWOEpsilon() {
      int result = 0;
      for (Symbol s : right) {
         if (!s.equals(Terminal.EPSILON)) {
            result++;
         }
      }
      return result;
   }
   
   
   /**
    * compare two productions
    * 
    * the order is defined as follows:
    * if the productions are not equal, the return type equals the comparison of the first
    * two Symbols, that differ (no symbol is considered smaller than an existing one).
    * 
    */
   public int compareTo(Production arg0) {
      int ret = -1;
      ret = getLeft().compareTo(arg0.getLeft());
      if (ret != 0)
         return ret;
      Iterator<Symbol> iL = getRight().iterator();
      Iterator<Symbol> iR = arg0.getRight().iterator();
      while (iL.hasNext()) {
         Symbol l = iL.next();
         if (!iR.hasNext())
            return 1;
         Symbol r = iR.next();
         ret = l.compareTo(r);
         if (ret != 0)
            return ret;
      }
      if (iR.hasNext())
         return -1;
      return 0;
   }
   
   /**
    * this is important! if this method is not be overwritten, it would not work as expected!
    * @param other Symbol
    * @return calls {@link compareTo} to check for equality
    */
   public boolean equals(Production other) {
      return (compareTo(other) == 0);
   }
   
   public String getString() {
      String ret = getLeft().getString();
      ret += " ->";
      for (Symbol s : getRight()) {
         ret += (" " + s.getString());
      }
      return ret;
   }

   	@Override
   	public int hashCode() {
   		return getString().hashCode();
   }
   	
   	@Override
   	public String toString() {
   		return getString();
   	}
}
