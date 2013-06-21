package swp_compiler_ss13.fuc.parser.generator;

import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

public class ShiftReduceConflict extends GeneratorException {
	private static final long serialVersionUID = -2998010003068711943L;

	private final Shift shift;
	private final Reduce reduce;
	private final Terminal terminal;

	public ShiftReduceConflict(Shift shift, Reduce reduce, Terminal terminal) {
		super();
		this.shift = shift;
		this.reduce = reduce;
		this.terminal = terminal;
	}

	public Reduce getReduce() {
		return reduce;
	}

	public Shift getShift() {
		return shift;
	}

	public Terminal getTerminal() {
		return terminal;
	}

	@Override
	public String toString() {
		return "Shift-Reduce conflict with terminal '" + terminal
				+ "' between: \n" + shift.toString() + "\n and \n"
				+ reduce.toString();
	}
}
