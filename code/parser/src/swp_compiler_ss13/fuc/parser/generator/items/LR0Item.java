package swp_compiler_ss13.fuc.parser.generator.items;

import java.util.List;

import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

/**
 * An LR(0)-Item is a {@link Production} combined with a position in its right
 * hand side. E.g.: "E → (.B)", where the dot denotes the position.
 */
public class LR0Item implements Item {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	protected final Production production;
	protected final int position;

	// cache
	protected final Symbol nextSymbol;
	protected final int hashCode;
	private final boolean shiftable;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public LR0Item(Production production, int position) {
		this.production = production;

		// correct position if epsilons are following
		List<Symbol> rhs = production.getRHS();
		int correctedPosition = position;
		if (position == 0 && rhs.get(0) == Terminal.Epsilon) {
			correctedPosition = 1; // immediately shift over epsilon
		}
		this.position = correctedPosition;

		// shiftable
		this.shiftable = (this.position < rhs.size());

		// next symbol
		if (this.shiftable)
			this.nextSymbol = rhs.get(this.position);
		else
			this.nextSymbol = null;

		// hash code
		this.hashCode = production.hashCode() + this.position;
	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public Production getProduction() {
		return production;
	}

	@Override
	public int getPosition() {
		return position;
	}

	@Override
	public Symbol getNextSymbol() {
		return nextSymbol;
	}

	@Override
	public boolean isShiftable() {
		return shiftable;
	}

	@Override
	public boolean isComplete() {
		return !shiftable;
	}

	@Override
	public LR0Item shift() {
		if (!isShiftable())
			throw new RuntimeException(
					"Shifting not possible: Item already closed: " + this);

		// return shifted item
		return new LR0Item(production, position + 1);
	}

	/**
	 * @return A new {@link LR0Item} with the same production but shifted until
	 *         it is complete
	 */
	public LR0Item complete() {
		if (isComplete()) {
			return this;
		} else {
			return new LR0Item(production, production.getRHS().size());
		}
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public String toString() {
		return production.toString(position);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof LR0Item) {
			LR0Item l = (LR0Item) obj;
			if (!production.equals(l.production))
				return false;
			if (position != l.position)
				return false;
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		return hashCode;
	}
}
