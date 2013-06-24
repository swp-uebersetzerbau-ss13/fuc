package swp_compiler_ss13.fuc.parser.generator;

import swp_compiler_ss13.fuc.parser.generator.automaton.Dfa;
import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR0State;
import swp_compiler_ss13.fuc.parser.generator.terminals.ITerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.LRActionTable;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;

/**
 * Implements {@link #createDFA()} for the creation of LR0 grammars
 * 
 * @author Gero
 */
public class LR0Generator extends ALRGenerator<LR0Item, LR0State> {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LR0Generator(Grammar grammar) throws GeneratorException {
		super(grammar);
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	protected Dfa<LR0Item,LR0State> createLrDFA(LR0State startState) {
		return new Dfa<LR0Item, LR0State>(startState);
	}
	
	@Override
	protected LR0State createStartState() {
		Grammar grammar = grammarInfo.getGrammar();
		return new LR0State(new LR0Item(grammar.getStartProduction(), 0));
	}
	
	@Override
	protected void createReduceAction(LRActionTable table, LR0Item item,
			LRParserState fromState) throws GeneratorException {
		// Aw! I need to add this Reduce for all elements of the
		// FOLLOW-set of this item...
		ITerminalSet terminalSet = grammarInfo.getFollowSets().get(
				item.getProduction().getLHS());
		for (Terminal terminal : terminalSet.getTerminals()) {
//			table.set(new Reduce(item.getProduction()), fromState, terminal);
			setReduceAction(table, new Reduce(item.getProduction()), fromState, terminal);
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
}
