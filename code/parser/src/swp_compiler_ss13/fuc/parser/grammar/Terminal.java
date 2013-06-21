package swp_compiler_ss13.fuc.parser.grammar;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.util.It;

/**
 * Like all {@link Symbol}s {@link Terminal}s are identified by their String
 * id, except {@link SpecialTerminal}! They are identified by identity in memory
 * and thus must be referenced from this class, like {@link Terminal#Epsilon} or
 * {@link Terminal#EOF}.
 * 
 * @author Gero
 */
public class Terminal extends Symbol {
	public static final Terminal Epsilon = new SpecialTerminal("ε");
	public static final Terminal EOF = new SpecialTerminal("$");

	public static class SpecialTerminal extends Terminal {
		private static final List<SpecialTerminal> values = new LinkedList<>();

		public static List<SpecialTerminal> values() {
			return values;
		}

		SpecialTerminal(String repr) {
			super(repr, new TokenType[0]);
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

	private final List<TokenType> tokenTypes;

	public Terminal(String id, TokenType... tokenTypes) {
		super(SymbolType.TERMINAL, id);
		this.tokenTypes = Arrays.asList(tokenTypes);
	}

	public It<TokenType> getTokenTypes() {
		return new It<>(tokenTypes);
	}
}
