package swp_compiler_ss13.common.parser;

import swp_compiler_ss13.common.optimization.Liveliness;
import swp_compiler_ss13.common.types.Type;

/**
 * Proposed interface for symbol table
 * 
 * @author "Frank Zechert", "Danny Maasch"
 * @version 1
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/wiki/Symbol Table">Symbol Table Wiki</a>
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/issues/6">Symbol Table Issue Tracker</a>
 */
public interface SymbolTable {
	/**
	 * Get the symbol table of the previous scope. If there is no previous scope returns null.
	 * @return The symbol table of the previous scope or null.
	 */
	public SymbolTable getParentSymbolTable();
	
	/**
	 * Checks if the given identifier is already declared.
	 * @param identifier The identifier to check.
	 * @return true if the identifier is declared, false otherwise
	 */
	public Boolean isDeclared(String identifier);
	
	/**
	 * Returns the Type of the given identifier. If the identifier is not defined returns null.
	 * @param identifier The identifier to lookup.
	 * @return The Type of the identifier.
	 */
	public Type lookupType(String identifier);
	
	/**
	 * Insert the identifier <code>identifier</code> of type <code>type</code> into the symbol table.
	 * @param identifier The identifier to insert into the symbol table.
	 * @param type The Type of the identifier.
	 */
	public void insert(String identifier, Type type);
	
	/**
	 * Remove the <code>identifier</code> from the symbol table.
	 * @param identifier The identifier to remove.
	 * @return true if the identifier was removed, false if it was not in the symbol table and therefore not removed.
	 */
	public Boolean remove(String identifier);
	
	/**
	 * Set the liveliness information of the identifier.
	 * @param identifier The identifier.
	 * @param liveliness New liveliness information.
	 */
	public void setLivelinessInformation(String identifier, Liveliness liveliness);
	
	/**
	 * Get the liveliness information of the identifier.
	 * @param identifier The identifier to get the liveliniess informations for.
	 * @return The liveliness information or null if none.
	 */
	public Liveliness getLivelinessInformation(String identifier);
	
    /**
     * Get the next free temporary name.
     * @return the next free temporary name
     */
    public String getNextFreeTemporary();
}
