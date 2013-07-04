package swp_compiler_ss13.fuc.parser.parser.tables.actions;

/**
 * The base class for all LR parser actions
 */
public abstract class ALRAction {
	private final ELRActionType type;

	public ALRAction(ELRActionType type) {
		this.type = type;
	}

	public ELRActionType getType() {
		return type;
	}

	public enum ELRActionType {
		SHIFT, REDUCE, ACCEPT, ERROR;
	}

	@Override
	public String toString() {
		return "[" + type.toString().toLowerCase() + "]";
	}
}
