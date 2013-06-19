package swp_compiler_ss13.fuc.parser.parser.tables;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRErrorState;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;

/**
 * Stores the association [(current {@link LRParserState}, current
 * {@link NonTerminal}) -> next {@link LRParserState}]
 */
public class LRGotoTable {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private final Map<LRTableKey, LRParserState> table = new HashMap<>();

	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @see LRGotoTable
	 */
	public LRGotoTable() {

	}

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @param curState
	 * @param curNonTerminal
	 * @return The new {@link LRParserState} which is identified by the given {@link LRParserState} and
	 * {@link NonTerminal}
	 */
	public LRParserState get(LRParserState curState, NonTerminal curNonTerminal) {
		LRParserState result = table.get(new LRTableKey(curState,
				curNonTerminal));
		if (result == null) {
			return new LRErrorState("An Error occurred: No transition from "
					+ curState + " with NonTerminal " + curNonTerminal);
		} else {
			return result;
		}
	}

	/**
	 * Sets the given new state for the combination of given (curState, curNonTerminal)
	 * 
	 * @param newState
	 * @param curState
	 * @param curNonTerminal
	 * @throws DoubleEntryException
	 */
	public void set(LRParserState newState, LRParserState curState,
			NonTerminal curNonTerminal) throws DoubleEntryException {
		LRTableKey key = new LRTableKey(curState, curNonTerminal);
		if (table.containsKey(key)) {
			LRParserState oldState = table.get(key);
			throw new DoubleEntryException("There already is a state "
					+ oldState + " for the key " + key + "! (new state: "
					+ newState + ")");
		}
		table.put(key, newState);
	}
}
