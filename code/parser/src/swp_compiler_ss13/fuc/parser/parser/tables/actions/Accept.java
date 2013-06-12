package swp_compiler_ss13.fuc.parser.parser.tables.actions;

/**
 * Represents the most important action of the parser - to accept a given input
 * and finish the parsing process!
 */
public class Accept extends ALRAction {

	public Accept() {
		super(ELRActionType.ACCEPT);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof Accept) {
			return true;
		}
		return false;
	}

	@Override
	public String toString() {
		return "accept";
	}
}
