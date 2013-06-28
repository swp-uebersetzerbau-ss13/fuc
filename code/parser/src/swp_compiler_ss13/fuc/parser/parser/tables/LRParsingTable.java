package swp_compiler_ss13.fuc.parser.parser.tables;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import swp_compiler_ss13.fuc.parser.generator.states.AState;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.util.It;

/**
 * Consists of a {@link LRActionTable} and a {@link LRGotoTable}. First state is
 * the start state!!! Also holds the association (abstract)
 * {@link LRParserState} -> (generator) {@link AState} for debugging and testing
 * purposes ({@link #states}).
 */
public class LRParsingTable {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private final LRActionTable actionTable;
	private final LRGotoTable gotoTable;

	private final LinkedHashMap<LRParserState, AState<?>> states;

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @see LRParsingTable
	 * @param states
	 */
	public LRParsingTable(LinkedHashMap<LRParserState, AState<?>> states) {
		this.states = states;

		// Create empty tables
		actionTable = new LRActionTable();
		gotoTable = new LRGotoTable();
	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return The start state of this parsing table
	 */
	public LRParserState getStartState() {
		// LinkedHashMap maintains order of values!
		return states.entrySet().iterator().next().getKey();
	}

	/**
	 * @return The number of generator states that were used to create this table
	 */
	public int getStatesCount() {
		return states.size();
	}

	/**
	 * @return An {@link It} over all states that were used to create this table
	 */
	public It<LRParserState> getGenStates() {
		return new It<LRParserState>(states.keySet());
	}
	
	/**
	 * @return A {@link It} over all pairs of {@link LRParserState} and generator
	 * states that were used to create this table
	 */
	public It<Entry<LRParserState, AState<?>>> getGenStateEnries() {
		return new It<>(states.entrySet());
	}
	
	/**
	 * @param parserState
	 * @return The generator state that is represented by the given
	 * {@link LRParserState}
	 */
	public AState<?> getGenState(LRParserState parserState) {
		return states.get(parserState);
	}

	/**
	 * @return The action part of this parsing table
	 */
	public LRActionTable getActionTable() {
		return actionTable;
	}

	/**
	 * @return The goto part of this parsing table
	 */
	public LRGotoTable getGotoTable() {
		return gotoTable;
	}
}
