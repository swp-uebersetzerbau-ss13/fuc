package swp_compiler_ss13.fuc.parser.grammar;

import java.util.LinkedList;
import java.util.List;

import swp_compiler_ss13.common.lexer.TokenType;



public class Terminal extends Symbol {
   public static final Terminal Epsilon = new SpecialTerminal("ε");
   public static final Terminal EOF = new SpecialTerminal("$");

   
   public static class SpecialTerminal extends Terminal {
      private static final List<SpecialTerminal> values = new LinkedList<>();
      public static List<SpecialTerminal> values() {
         return values;
      }
      
      SpecialTerminal(String repr) {
         super(repr, null);
         values.add(this);
      }
      
      @Override
      public int hashCode() {
         return 1;
      }
      
      @Override
      public boolean equals(Object obj) {
         return this == obj;
      }
   }
   
   
   /** TODO This is more a hack then anything else - find better solution! */
   private final TokenType tokenType;
   
   public Terminal(String id, TokenType tokenType) {
      super(SymbolType.TERMINAL, id);
      this.tokenType = tokenType;
   }
   
   public TokenType getTokenType() {
      return tokenType;
   }
}
