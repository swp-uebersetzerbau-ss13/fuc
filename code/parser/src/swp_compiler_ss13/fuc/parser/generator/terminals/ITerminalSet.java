package swp_compiler_ss13.fuc.parser.generator.terminals;

import java.util.Set;

import swp_compiler_ss13.fuc.parser.grammar.Terminal;

/**
 * The interface for all implementations of sets of terminals
 * 
 * @author Gero
 */
public interface ITerminalSet {
	/**
	 * @return New, empty {@link ITerminalSet} with indexes for the same
	 * grammar
	 */
	public ITerminalSet empty();

	/**
	 * @param terminal
	 * @return New instance of {@link ITerminalSet} with added terminal
	 */
	public ITerminalSet plus(Terminal terminal);

	/**
	 * @param terminals
	 * @return New instance of {@link ITerminalSet} plus the given terminal set.
	 */
	public ITerminalSet plusAll(ITerminalSet terminals);

	/**
	 * @param terminals
	 * @return New instance of {@link ITerminalSet} plus all given terminal -
	 *         except {@link Terminal#Epsilon}.
	 */
	public ITerminalSet plusAllExceptEpsilon(ITerminalSet terminals);

	public boolean contains(Terminal terminal);

	public Set<Terminal> getTerminals();
}
