package swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces;

import swp_compiler_ss13.fuc.parser.table.ParseTable;



public interface ParseTableGenerator {

	/**
	 * creates a ParseTable, hiding the details of its construction
	 * 
	 * @return The ParseTable to be used by the parser
	 */
	public ParseTable getTable();
	
}
