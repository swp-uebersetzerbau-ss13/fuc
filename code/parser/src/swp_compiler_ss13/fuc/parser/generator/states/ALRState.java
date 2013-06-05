package swp_compiler_ss13.fuc.parser.generator.states;

import java.util.Collection;

import swp_compiler_ss13.fuc.parser.generator.GrammarInfo;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;

/**
 * The base class for all types of LR-states. Simply extends the {@link AState}
 * with methods for goto- and closure- calculation.
 * 
 * @author Gero
 * 
 * @param <I>
 */
public abstract class ALRState<I> extends AState<I> {

	public ALRState(Collection<I> items) {
		super(items);
	}

	@SafeVarargs
	public ALRState(I... items) {
		super(items);
	}

	/**
	 * Moves the position if the items one step further when the given symbol
	 * follows and returns them as a new state (only the kernel, without
	 * closure).
	 */
	public abstract ALRState<I> goTo(Symbol symbol);

	/**
	 * Returns the closure of this {@link LR0State} as another {@link LR0State}.
	 * See dragon book, p. 271
	 */
	public abstract ALRState<I> closure(GrammarInfo grammarInfo);
}
