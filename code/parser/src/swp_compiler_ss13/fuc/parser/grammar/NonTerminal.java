package swp_compiler_ss13.fuc.parser.grammar;

import java.util.LinkedList;
import java.util.List;

/**
 * Like all {@link Symbol}s {@link NonTerminal}s are identified by their
 * String id, except {@link SpecialNonTerminal}! They are identified by identity
 * in memory and thus must be referenced from this class, like
 * {@link NonTerminal#StartLHS}.
 * 
 * @author Gero
 */
public class NonTerminal extends Symbol {
	public static final NonTerminal StartLHS = new SpecialNonTerminal("S'");

	public static class SpecialNonTerminal extends NonTerminal {
		private static final List<SpecialNonTerminal> values = new LinkedList<>();

		public static List<SpecialNonTerminal> values() {
			return values;
		}

		SpecialNonTerminal(String repr) {
			super(repr);
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

	public NonTerminal(String name) {
		super(SymbolType.NONTERMINAL, name);
	}
}
