package swp_compiler_ss13.fuc.parser.generator;

import java.util.HashMap;
import java.util.ListIterator;
import java.util.Map;

import swp_compiler_ss13.fuc.parser.generator.terminals.EfficientTerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

/**
 * Calculates and caches the follow sets for the given grammar
 * 
 * @author Gero
 */
public class FollowSets {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private final Grammar grammar;
	private final Map<NonTerminal, EfficientTerminalSet> followSets = new HashMap<>();

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public FollowSets(Grammar grammar, FirstSets firstSets,
			NullableSet nullableSet) {
		this.grammar = grammar;

		// Use fix point iteration to determine all follow sets
		boolean changed = false;
		do {
			changed = false; // reset

			for (Production prod : grammar.getProductions()) {
				NonTerminal lhs = prod.getLHS();

				// now iterate through the right side from right to left:
				ListIterator<Symbol> itRight = prod.getRHS().listIterator(
						prod.getRHS().size());
				EfficientTerminalSet FOLLOWToAdd = getFollowSet(lhs);

				while (itRight.hasPrevious()) { // right (step through right
												// side of the production)
					Symbol right = itRight.previous();
					if (right.isTerminal()) {
						// TERMINAL: Clear FOLLOW, add it as only element
						FOLLOWToAdd = FOLLOWToAdd.empty()
								.plus((Terminal) right);
						// FOLLOWToAdd.clear(); FOLLOWToAdd.add((Terminal
						// )right);
					} else {
						// NONTERMINAL:
						NonTerminal varRight = (NonTerminal) right;

						// 1. add FOLLOWToAdd to varRight.getFOLLOW():
						// varRight.getFOLLOW().addAll(FOLLOWToAdd);
						EfficientTerminalSet rightFollowOld = getFollowSet(varRight);
						EfficientTerminalSet rightFollowNew = addToFollow(
								varRight, FOLLOWToAdd);
						// iCard += (varRight.getFOLLOW().size() - oldSize);
						changed |= (!rightFollowNew.equals(rightFollowOld)); // Changed
																				// anything?

						// 2. prepare new FOLLOWToAdd:
						if (!nullableSet.contains(varRight)) {
							FOLLOWToAdd = FOLLOWToAdd.empty();
						} else {
							// If varRight can produce EPSILON (= isNullable()),
							// we keep our follow-set and try to add it to
							// the next (Non?)Terminal on the left
						}

						// Set<Terminal> currentFIRST = new
						// HashSet<Terminal>(varRight.getFIRST());
						EfficientTerminalSet curFirst = firstSets.get(varRight);
						// currentFIRST.remove(eps);
						curFirst = curFirst.empty().plusAllExceptEpsilon(
								curFirst);
						// FOLLOWToAdd.addAll(currentFIRST);
						FOLLOWToAdd = FOLLOWToAdd.plusAll(curFirst);
						// if (varRight.getFIRST().contains(eps)) {
						// FOLLOWToAdd.addAll(varRight.getFOLLOW());
						// } ==> see above!
					}
				}
			}
		} while (changed);
	}

	private EfficientTerminalSet addToFollow(NonTerminal key,
			EfficientTerminalSet terminalSet) {
		EfficientTerminalSet set = getFollowSet(key);
		set = set.plusAll(terminalSet);
		followSets.put(key, set); // Needed, as TerminalSets are immutable and
									// plus(..) creates new instance
		return set;
	}

	private EfficientTerminalSet getFollowSet(NonTerminal key) {
		EfficientTerminalSet set = followSets.get(key);
		if (set == null) {
			set = new EfficientTerminalSet(grammar.getTerminals());
		}
		return set;
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * Gets the FOLLOW set for the given symbol.
	 */
	public EfficientTerminalSet get(NonTerminal key) {
		return followSets.get(key);
	}
}
