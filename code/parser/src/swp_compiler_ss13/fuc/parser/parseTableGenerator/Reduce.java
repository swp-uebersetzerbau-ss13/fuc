package swp_compiler_ss13.fuc.parser.parseTableGenerator;

public class Reduce extends ParseTableEntry {
	
	private Integer count;
	private Production production;
	
	public Reduce(Integer count, Integer newState, Production production) {
		this.count = count;
		super.newState = newState;
	}
	
	public Integer getCount() {
		return count;
	}
	
	public ParseTableEntryType getType() {
		return ParseTableEntryType.REDUCE;
	}
	
	
	public Production getProduction(){
		return production;
	}
	


}
