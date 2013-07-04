package swp_compiler_ss13.fuc.parser.generator.states;

import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashSet;

import swp_compiler_ss13.fuc.parser.generator.items.Item;
import swp_compiler_ss13.fuc.parser.util.It;

/**
 * This is the base class for generator-created parser states. It simply
 * consists of a fixed set of {@link Item}s
 * 
 * @author Gero
 * 
 * @param <I>
 */
public class AState<I> {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	protected final LinkedHashSet<I> items;

	// cache
	private final int hashCode;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @see AState
	 * @param items
	 *            Added by the ordering of the collections {@link Iterator}!
	 */
	public AState(Collection<I> items) {
		this.items = new LinkedHashSet<>(items);
		this.hashCode = calcHashCode();
	}

	@SafeVarargs
	public AState(I... items) {
		this.items = new LinkedHashSet<>();
		for (I i : items) {
			this.items.add(i);
		}
		this.hashCode = calcHashCode();
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	/**
	 * @return An {@link It}erator over the items of this state. Order of the
	 *         given items is maintained!
	 */
	public It<I> getItems() {
		return new It<>(items);
	}

	/**
	 * @return The first of the items
	 */
	public I getFirstItem() {
		return items.iterator().next();
	}

	/**
	 * @return The number of items defining this state
	 */
	public int getItemsCount() {
		return items.size();
	}

	private int calcHashCode() {
		return items.hashCode();
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof AState))
			return false;
		AState<?> other = (AState<?>) obj;
		if (items == null) {
			if (other.items != null)
				return false;
		} else if (!items.equals(other.items))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return items.toString();
	}
}
