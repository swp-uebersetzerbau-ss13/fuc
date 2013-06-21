package swp_compiler_ss13.fuc.parser.generator;

import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;

public class ReduceReduceConflict extends GeneratorException {
	private static final long serialVersionUID = 3039517190930222215L;
	
	private final Reduce reduce1;
	private final Reduce reduce2;
	
	public ReduceReduceConflict(Reduce reduce1, Reduce reduce2) {
		super();
		this.reduce1 = reduce1;
		this.reduce2 = reduce2;
	}
	
	public Reduce getReduce1() {
		return reduce1;
	}
	
	public Reduce getReduce2() {
		return reduce2;
	}
}
