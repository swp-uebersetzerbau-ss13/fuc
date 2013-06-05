package swp_compiler_ss13.fuc.parser.generator;

import swp_compiler_ss13.fuc.parser.generator.automaton.Dfa;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.generator.terminals.ITerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.DoubleEntryException;
import swp_compiler_ss13.fuc.parser.parser.tables.LRActionTable;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;

public class LR1Generator extends ALRGenerator<LR1Item, LR1State> {

	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LR1Generator(Grammar grammar) throws RuntimeException {
		super(grammar);
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	protected Dfa<LR1Item, LR1State> createLrDFA(LR1State startState) {
		return new Dfa<LR1Item, LR1State>(startState);
	}
	
	@Override
	protected LR1State createStartState() {
		Grammar grammar = grammarInfo.getGrammar();
		ITerminalSet empty = grammarInfo.getEmptyTerminalSet();
		return new LR1State(new LR1Item(grammar.getStartProduction(), 0, empty));
	}
	
	@Override
	protected void addReduceAction(LRActionTable table, LR1Item item,
			LRParserState fromState) throws DoubleEntryException {
		// LR (1): Set action not for all FOLLOW symbols but all lookaheads
		Reduce reduce = new Reduce(item.getProduction());
		for (Terminal lookahead : item.getLookaheads().getTerminals()) {
			table.set(reduce, fromState, lookahead);	// TODO Feels right, but...? :-P
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
}
