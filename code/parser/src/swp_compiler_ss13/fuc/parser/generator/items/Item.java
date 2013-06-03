package swp_compiler_ss13.fuc.parser.generator.items;

import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;

public interface Item {
   public Production getProduction();
   
   public int getPosition();
   
   public Symbol getNextSymbol();
   
   public boolean isShiftable();
   
   public boolean isComplete();
   
   public Item shift();
}
