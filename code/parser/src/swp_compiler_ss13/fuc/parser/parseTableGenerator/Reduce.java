package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Reduce extends ParseTableEntry {
	
	private Integer count;
	
	Reduce(Integer count, Integer newState) {
		this.count = count;
		super.newState = newState;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public ParseTableEntryType getType() {
		return ParseTableEntryType.REDUCE;
	}
	


}
