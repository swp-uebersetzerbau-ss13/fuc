package swp_compiler_ss13.common.types;

/**
 * Boolean type definition.
 * 
 * @author "Frank Zechert", "Danny Maasch"
 * @version 1
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/wiki/Types">Types Wiki</a>
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/issues/8">Types Issue Tracker</a>
 */
public class BooleanType extends BasicType {
	
	/**
	 * Create a new boolean type
	 */
	public BooleanType() {
		this.width = 1L;
	}
}
