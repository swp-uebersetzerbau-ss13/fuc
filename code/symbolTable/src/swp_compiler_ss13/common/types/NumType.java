package swp_compiler_ss13.common.types;

/**
 * Num type definition.
 * 
 * @author "Frank Zechert", "Danny Maasch"
 * @version 1
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/wiki/Types">Types Wiki</a>
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/issues/8">Types Issue Tracker</a>
 */
public class NumType extends BasicType {
	
	/**
	 * Create a new num type
	 */
	public NumType() {
		this.width = 4L;
	}
}
