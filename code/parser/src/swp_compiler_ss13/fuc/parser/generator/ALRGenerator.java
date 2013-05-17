package swp_compiler_ss13.fuc.parser.generator;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.generator.automaton.Dfa;
import swp_compiler_ss13.fuc.parser.generator.automaton.DfaEdge;
import swp_compiler_ss13.fuc.parser.generator.items.Item;
import swp_compiler_ss13.fuc.parser.generator.states.ALRState;
import swp_compiler_ss13.fuc.parser.generator.states.AState;
import swp_compiler_ss13.fuc.parser.generator.terminals.ITerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.DoubleEntryException;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Accept;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

/**
 * Base class for LR(0) and LR(1) parser generators.
 * 
 * @author Gero
 */
public abstract class ALRGenerator<I extends Item, S extends ALRState<I>> {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	protected final GrammarInfo grammarInfo;
	private final Dfa<I, S> dfa;
	private final LRParsingTable table;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ALRGenerator(Grammar grammar) throws RuntimeException {
		// Compute NULLABLE, FIRST and FOLLOW sets
		grammar = grammar.extendByAuxStartProduction();
		grammarInfo = new GrammarInfo(grammar);

		// Create automaton
		dfa = createDFA();
		S startState = dfa.getStartState();

		// Create a supportive mapping between generator state -> parser state
		Map<S, LRParserState> statesMap = new HashMap<>();
		// Gets stored for debug/testing in the table!
		LinkedHashMap<LRParserState, AState<?>> parserStatesMap = new LinkedHashMap<>();
		LRParserState parserStartState = new LRParserState(0);
		statesMap.put(startState, parserStartState);
		parserStatesMap.put(parserStartState, startState);
		int i = 1;
		for (S dfaState : dfa.getStates()) {
			if (!dfaState.equals(startState)) {
				LRParserState parserState = new LRParserState(i);
				statesMap.put(dfaState, parserState);
				parserStatesMap.put(parserState, dfaState);
				i++;
			}
		}

		// Construct parsing table
		table = new LRParsingTable(parserStatesMap);

		// Traverse edge and generate shift, goto and accept
		for (DfaEdge<S> edge : dfa.getEdges()) {
			LRParserState src = statesMap.get(edge.getSrc());
			if (edge.getSymbol().isTerminal()) {
				// Terminal
				Terminal terminal = (Terminal) edge.getSymbol();
				if (edge.isDestAccepting()) {
					// ACCEPT
					try {
						table.getActionTable().set(new Accept(), src, terminal);
					} catch (DoubleEntryException err) {
						throw new RuntimeException(err); // TODO Really the
															// right way...?
					}
				} else {
					// SHIFT
					LRParserState dstState = statesMap.get(edge.getDst());
					try {
						table.getActionTable().set(new Shift(dstState), src,
								terminal);
					} catch (DoubleEntryException err) {
						throw new RuntimeException(err); // TODO Really the
															// right way...?
					}
				}
			} else {
				// NonTerminal
				NonTerminal nonTerminal = (NonTerminal) edge.getSymbol();
				LRParserState dstState = statesMap.get(edge.getDst());
				try {
					table.getGotoTable().set(dstState, src, nonTerminal);
				} catch (DoubleEntryException err) {
					throw new RuntimeException(err); // TODO Really the right
														// way...?
				}
			}
		}

		// Find reduction targets
		// Ufff... what to do here? :-o
		for (S kernel : statesMap.keySet()) {
			ALRState<I> state = kernel.closure(grammarInfo); // unpack
			for (I item : state.getItems()) {
				if (item.isComplete()) {
					LRParserState fromState = statesMap.get(kernel);

					// Aw! I need to add this Reduce for all elements of the
					// FOLLOW-set of this item...
					ITerminalSet terminalSet = grammarInfo.getFollowSets().get(
							item.getProduction().getLHS());
					for (Terminal terminal : terminalSet.getTerminals()) {
						try {
							table.getActionTable().set(
									new Reduce(item.getProduction()),
									fromState, terminal);
						} catch (DoubleEntryException err) {
							throw new RuntimeException(err); // TODO Really the
																// right way...?
						}
					}
				}
			}
		}
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	public abstract Dfa<I, S> createDFA();

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	public GrammarInfo getGrammarInfo() {
		return grammarInfo;
	}

	public LRParsingTable getParsingTable() {
		return table;
	}
}
