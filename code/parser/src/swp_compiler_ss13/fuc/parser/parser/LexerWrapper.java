package swp_compiler_ss13.fuc.parser.parser;

import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.TokenEx;

/**
 * Used to wrap the stream of {@link Token}s from the {@link Lexer} with our
 * {@link TokenEx} class, so the {@link LRParser} receives {@link TokenEx}
 * instead.
 * 
 * @author Gero
 */
public class LexerWrapper {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private final Lexer lexer;
	private final Grammar grammar;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LexerWrapper(Lexer lexer, Grammar grammar) {
		this.lexer = lexer;
		this.grammar = grammar;
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public TokenEx getNextToken() {
		Token nextToken = lexer.getNextToken();
		// System.out.println(TokenEx.tokenToString(nextToken) + "\n");
		return TokenEx.createFromToken(nextToken, grammar);
	}
}
