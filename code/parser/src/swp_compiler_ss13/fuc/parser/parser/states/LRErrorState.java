package swp_compiler_ss13.fuc.parser.parser.states;

import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

/**
 * Returned from {@link LRParsingTable}s to show that there is no valid entry
 * for the given {@link Symbol}-{@link LRParserState} combination.
 * 
 * @author Gero
 */
public class LRErrorState extends LRParserState {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private final String msg;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LRErrorState(String msg) {
		super(-1);
		this.msg = msg;
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public boolean isErrorState() {
		return true;
	}

	@Override
	public String toString() {
		return msg;
	}
}
