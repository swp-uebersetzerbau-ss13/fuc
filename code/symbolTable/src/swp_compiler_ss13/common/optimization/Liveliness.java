package swp_compiler_ss13.common.optimization;

/**
 * Proposed interface for liveliness information
 * @author "Frank Zechert", "Danny Maasch"
 * @version 1
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/wiki/Symbol Table">Symbol Table Wiki</a>
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/issues/6">Symbol Table Issue Tracker</a>
 */
public interface Liveliness {
	
	/**
	 * Set whether the identifier is alive or not
	 * @param lives true to set the identifier to be alive
	 */
	public void setLiveliness(Boolean lives);
	
	/**
	 * Checks whether the identifier is alive or not
	 * @return true if the identifier is alive.
	 */
	public Boolean isAlive();
	
	/**
	 * Set the line this identifier is used in next.
	 * @param line The line this identifier is used in next. null for none.
	 */
	public void setNextUse(Integer line);
	
	/**
	 * Gets the next line this identifier is used in next.
	 * @return The line this identifier is used in next or null for none.
	 */
	public Integer getNextUse();
}
