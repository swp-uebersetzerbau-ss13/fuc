package swp_compiler_ss13.fuc.parser.parser.tables.actions;

/**
 * Used to represent an error state when the parsers asks the parsing table for
 * a state-symbol combination it cannot serve
 */
public class Error extends ALRAction {

	private final String msg;

	public Error(String msg) {
		super(ELRActionType.ERROR);
		this.msg = msg;
	}

	public String getMsg() {
		return msg;
	}
}
