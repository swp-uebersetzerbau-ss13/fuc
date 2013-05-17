package swp_compiler_ss13.fuc.parser.grammar;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;

/**
 * Extends {@link Token} by a {@link Terminal}
 * 
 * @author Gero
 */
public class TokenEx implements Token {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Token token;
   private final Terminal terminal;
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public TokenEx(Token token, Terminal terminal) {
      this.token = token;
      this.terminal = terminal;
   }
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
   public Token getToken() {
      return token;
   }
   
   public Terminal getTerminal() {
      return terminal;
   }

   @Override
   public String getValue() {
      return token.getValue();
   }

   @Override
   public TokenType getTokenType() {
      return token.getTokenType();
   }

   @Override
   public Integer getLine() {
      return token.getLine();
   }

   @Override
   public Integer getColumn() {
      return token.getColumn();
   }
}
