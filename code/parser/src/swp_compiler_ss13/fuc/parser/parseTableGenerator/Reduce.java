package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Reduce extends ParseTableEntry {
	Reduce(Integer count, Integer newState) {
		this.count = count;
		this.newState = newState;
	}
	public Integer getCount() {
		return count;
	}
	public Integer getNewState() {
		return newState;
	}
	public Type getType() {
		return Type.REDUCE;
	}
	
	private Integer count;
	private Integer newState;
}
