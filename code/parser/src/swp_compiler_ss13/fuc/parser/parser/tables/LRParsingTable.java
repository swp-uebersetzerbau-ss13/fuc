package swp_compiler_ss13.fuc.parser.parser.tables;

import java.util.LinkedHashMap;
import java.util.Map.Entry;

import swp_compiler_ss13.fuc.parser.generator.states.AState;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.util.It;

/**
 * Consists of a {@link LRActionTable} and a {@link LRGotoTable}. First state is
 * the start state!!!
 * 
 * @author Gero
 */
public class LRParsingTable {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private final LRActionTable actionTable;
	private final LRGotoTable gotoTable;

	private final LinkedHashMap<LRParserState, AState<?>> states;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LRParsingTable(LinkedHashMap<LRParserState, AState<?>> states) {
		this.states = states;

		// Create empty tables
		actionTable = new LRActionTable();
		gotoTable = new LRGotoTable();
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	public LRParserState getStartState() {
		// LinkedHashMap maintains order of values!
		return states.entrySet().iterator().next().getKey();
	}

	public int getStatesCount() {
		return states.size();
	}

	public It<LRParserState> getGenStates() {
		return new It<LRParserState>(states.keySet());
	}
	
	public It<Entry<LRParserState, AState<?>>> getGenStateEnries() {
		return new It<>(states.entrySet());
	}
	
	public AState<?> getGenState(LRParserState parserState) {
		return states.get(parserState);
	}

	public LRActionTable getActionTable() {
		return actionTable;
	}

	public LRGotoTable getGotoTable() {
		return gotoTable;
	}
}
