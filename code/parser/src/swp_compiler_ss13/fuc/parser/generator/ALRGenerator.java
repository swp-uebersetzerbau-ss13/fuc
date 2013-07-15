package swp_compiler_ss13.fuc.parser.generator;

import static swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction.ELRActionType.REDUCE;
import static swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction.ELRActionType.SHIFT;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.generator.automaton.Dfa;
import swp_compiler_ss13.fuc.parser.generator.automaton.DfaEdge;
import swp_compiler_ss13.fuc.parser.generator.items.Item;
import swp_compiler_ss13.fuc.parser.generator.states.ALRState;
import swp_compiler_ss13.fuc.parser.generator.states.AState;
import swp_compiler_ss13.fuc.parser.generator.states.LR0State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.OpAssociativities;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.DoubleEntryException;
import swp_compiler_ss13.fuc.parser.parser.tables.LRActionTable;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Accept;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

/**
 * Base class for LR(0) and LR(1) parser generators. It takes the given
 * {@link Grammar},creates the according {@link Dfa} and finally iterates it to
 * create SHIFT, ACCEPT and REDUCE actions. The latter one is delegated to
 * implementations via
 * {@link #createReduceAction(LRActionTable, Item, LRParserState)}
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
	/**
	 * @see ALRGenerator
	 * @param grammar
	 * @throws GeneratorException
	 */
	public ALRGenerator(Grammar grammar) throws GeneratorException {
		// Compute NULLABLE, FIRST and FOLLOW sets
		grammar = grammar.extendByAuxStartProduction();
		grammarInfo = new GrammarInfo(grammar);

		// Create automaton
		dfa = createDFA();
		S startState = dfa.getStartState();

		// Create a supportive mapping between generator state -> parser state
		Map<S, LRParserState> statesMap = new LinkedHashMap<>();
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

		// Construct parsing table (pass association 'parser state' ->
		// 'generator state' for debugging)
		table = new LRParsingTable(parserStatesMap);

		// Traverse edges and generate shift, goto and accept
		for (DfaEdge<S> edge : dfa.getEdges()) {
			LRParserState src = statesMap.get(edge.getSrc());
			if (edge.getSymbol().isTerminal()) {
				// Terminal
				Terminal terminal = (Terminal) edge.getSymbol();
				if (edge.isDestAccepting()) {
					// ACCEPT
					table.getActionTable().set(new Accept(), src, terminal);
				} else {
					// SHIFT
					LRParserState dstState = statesMap.get(edge.getDst());
					table.getActionTable().set(new Shift(dstState), src,
							terminal);
				}
			} else {
				// NonTerminal
				NonTerminal nonTerminal = (NonTerminal) edge.getSymbol();
				LRParserState dstState = statesMap.get(edge.getDst());
				table.getGotoTable().set(dstState, src, nonTerminal);
			}
		}

		// Find reduction targets
		// Ufff... what to do here? :-o
		for (S kernel : statesMap.keySet()) {
			ALRState<I> state = kernel.closure(grammarInfo); // unpack
			for (I item : state.getItems()) {
				if (item.isComplete()) {
					LRParserState fromState = statesMap.get(kernel);
					createReduceAction(table.getActionTable(), item, fromState);
				}
			}
		}
	}

	/**
	 * The implementing generator has to set its reduce actions here
	 * 
	 * @param table
	 *            The table to set the actions to
	 * @param item
	 *            The item that gets reduced
	 * @param fromState
	 *            The state the parser is currently in
	 * @throws GeneratorException
	 */
	protected abstract void createReduceAction(LRActionTable table, I item,
			LRParserState fromState) throws GeneratorException;

	/**
	 * Tries to set the given {@link Reduce} to the given {@link LRActionTable}.
	 * If there's a conflict, it tries to resolve it by precedence.
	 * 
	 * @param table
	 * @param reduce
	 * @throws DoubleEntryException
	 */
	protected void setReduceAction(LRActionTable table, Reduce reduce,
			LRParserState curState, Terminal curTerminal)
			throws GeneratorException {
		ALRAction action = table.getWithNull(curState, curTerminal);
		if (action == null) {
			// Free cell
			table.set(reduce, curState, curTerminal);
		} else {
			// Try to resolve conflict...
			if (action.getType() == SHIFT) {
				// Shift-Reduce-Conflict. Try to resolve by associativity...
				Shift shift = (Shift) action;
				OpAssociativities associativities = grammarInfo.getGrammar().getAssociativities();
				if (associativities.isRightAssociative(curTerminal)) {
					// curTerminal is RIGHT-associative: Nothing to do here, preserve REDUCE!
				} else {
					throw new ShiftReduceConflict(shift, reduce, curTerminal);
				}
			} else if (action.getType() == REDUCE) {
				// Reduce-Reduce-Conflict.
				Reduce reduce1 = (Reduce) action;
				throw new ReduceReduceConflict(reduce1, reduce);
			}
		}
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Based on the {@link Grammar} of this generator, this method creates
	 * deterministic finite automaton that reflects the states and the possible
	 * states this grammar represents.
	 * 
	 * @return
	 */
	Dfa<I, S> createDFA() {
		// Init
		S startState = createStartState();
		Dfa<I, S> dfa = createLrDFA(startState);

		LinkedList<S> todo = new LinkedList<>();
		todo.add(startState);

		// For all state, find edges to (possibly) new states
		while (!todo.isEmpty()) {
			S kernel = todo.removeFirst();
			ALRState<I> state = kernel.closure(grammarInfo); // unpack!

			for (I item : state.getItems()) {
				if (item.isShiftable()) {
					Symbol symbol = item.getNextSymbol();
					if (symbol.equals(Terminal.EOF)) {
						// $: We accept! Simple AND sufficient! ;-)
						dfa.getEdges().add(
								DfaEdge.createAcceptEdge(kernel, symbol));
					} else {
						@SuppressWarnings("unchecked")
						S nextState = (S) state.goTo(symbol);

						// Is it new?
						S oldState = contains(dfa, nextState);
						if (oldState == null) {
							dfa.getStates().add(nextState);
							todo.add(nextState);
						} else {
							// Re-use old instance and drop temporarely
							// generated 'nextState'..
							nextState = oldState;
						}

						// Add edge to automaton
						DfaEdge<S> edge = new DfaEdge<S>(kernel, symbol,
								nextState, item.getLR0Kernel());
						dfa.getEdges().add(edge);
					}
				}
			}
		}

		return dfa;
	}

	/**
	 * @return The start state of type <S> determined by the implementation
	 */
	protected abstract S createStartState();

	/**
	 * @param startState
	 * @return An instance of {@link Dfa} with the correct types <I, S>
	 */
	protected abstract Dfa<I, S> createLrDFA(S startState);

	/**
	 * @return The equivalent {@link LR0State} if there is one contained in the
	 *         DFA; else <code>null</code>
	 */
	private S contains(Dfa<I, S> dfa, S newState) {
		for (S oldState : dfa.getStates()) {
			if (oldState.equals(newState)) {
				return oldState;
			}
		}
		return null;
	}

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
