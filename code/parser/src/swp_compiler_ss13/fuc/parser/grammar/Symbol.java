package swp_compiler_ss13.fuc.parser.grammar;

/**
 * The base class for both {@link Terminal} and {@link NonTerminal}, which are
 * identified by their {@link SymbolType} and {@link Symbol#id}.
 * 
 * @author Gero
 */
public abstract class Symbol {
	public enum SymbolType {
		TERMINAL, NONTERMINAL;
	}

	private final SymbolType type;
	private final String id;

	Symbol(SymbolType type, String id) {
		this.type = type;
		this.id = id;
	}

	public boolean isTerminal() {
		return type == SymbolType.TERMINAL;
	}

	public boolean isNonTerminal() {
		return type == SymbolType.NONTERMINAL;
	}

	public SymbolType getType() {
		return type;
	}

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
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
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
