package swp_compiler_ss13.fuc.parser.parser;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.grammar.TokenEx;


/**
 * Used to wrap the not very well defined {@link Token} class with our {@link TokenEx}.
 * 
 * @author Gero
 */
public class LexerWrapper {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private final Lexer lexer;
   private final Grammar grammar;
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LexerWrapper(Lexer lexer, Grammar grammar) {
      this.lexer = lexer;
      this.grammar = grammar;
   }
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   public TokenEx getNextToken() {
      Token token = lexer.getNextToken();
      TokenEx tokenEx;
      if (token.getTokenType() == TokenType.EOF) {
         tokenEx = new TokenEx(token, null);
      } else {
         Terminal terminal = null;
         for (Terminal t : grammar.getTerminals()) {
            if (t.getTokenType().equals(token.getTokenType())) {
               terminal = t;
               break;
            }
         }
         // if (terminal == null) {
         // log.warn("Unable to find a ");
         // }
         tokenEx = new TokenEx(token, terminal);
      }
      return tokenEx;
   }
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
}
