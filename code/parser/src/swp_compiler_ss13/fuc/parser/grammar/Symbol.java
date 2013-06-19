package swp_compiler_ss13.fuc.parser.grammar;

/**
 * The base class for both {@link Terminal} and {@link NonTerminal}, which are
 * identified by their {@link SymbolType} and {@link Symbol#id}.
 * 
 * @author Gero
 */
public abstract class Symbol {
	/**
	 * The different types a symbol may have
	 */
	public enum SymbolType {
		TERMINAL, NONTERMINAL;
	}

	private final SymbolType type;
	private final String id;

	/**
	 * Creates a {@link Symbol} of the given type identified by the given id
	 * 
	 * @param type
	 * @param id
	 */
	Symbol(SymbolType type, String id) {
		if (type == null || id == null) {
			throw new NullPointerException("SymbolType and id must not be null!");
		}
		this.type = type;
		this.id = id;
	}

	/**
	 * @return <code>true</code> iff this {@link Symbol} is of type {@link SymbolType#TERMINAL}
	 */
	public boolean isTerminal() {
		return type == SymbolType.TERMINAL;
	}


	/**
	 * @return <code>true</code> iff this {@link Symbol} is of type {@link SymbolType#NONTERMINAL}
	 */
	public boolean isNonTerminal() {
		return type == SymbolType.NONTERMINAL;
	}

	/**
	 * @return The {@link SymbolType} of this {@link Symbol}
	 */
	public SymbolType getType() {
		return type;
	}

	/**
	 * @return The unique identifier of this {@link Symbol}
	 */
	public String getId() {
		return id;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Symbol))
			return false;
		Symbol other = (Symbol) obj;
		if (!id.equals(other.id))
			return false;
		if (type != other.type)
			return false;
		return true;
	}

	@Override
	public String toString() {
		return id;
	}
}
