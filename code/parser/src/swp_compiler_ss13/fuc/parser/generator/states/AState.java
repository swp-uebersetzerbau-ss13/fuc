package swp_compiler_ss13.fuc.parser.generator.states;

import java.util.Collection;
import java.util.HashSet;

import swp_compiler_ss13.fuc.parser.util.It;

public class AState<I> {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	protected final HashSet<I> items;

	// cache
	private final int hashCode;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public AState(Collection<I> items) {
		this.items = new HashSet<>(items);
		this.hashCode = calcHashCode();
	}

	@SafeVarargs
	public AState(I... items) {
		this.items = new HashSet<>();
		for (I i : items) {
			this.items.add(i);
		}
		this.hashCode = calcHashCode();
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	public It<I> getItems() {
		return new It<>(items);
	}

	public I getFirstItem() {
		return items.iterator().next();
	}

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
