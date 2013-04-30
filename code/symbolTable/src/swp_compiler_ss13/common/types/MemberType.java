package swp_compiler_ss13.common.types;

/**
 * A member of a struct.
 * 
 * @author "Frank Zechert", "Danny Maasch"
 * @version 1
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/wiki/Symbol Table">Symbol Table Wiki</a>
 * @see <a target="_top" href="https://github.com/swp-uebersetzerbau-ss13/common/issues/6">Symbol Table Issue Tracker</a>
 */
public class MemberType {
	
	/**
	 * The identifier (name) of the member
	 */
	protected String identifier;
	
	/**
	 * The type of the member
	 */
	protected Type type;
	
	/**
	 * Create a new member type with the given <code>identifier</code> and <code>type</code>.
	 * @param identifier The identifier of the member
	 * @param type The type of the member
	 */
	public MemberType(String identifier, Type type) {
		this.identifier = identifier;
		this.type = type;
	}
	
	/**
	 * Get the identifier of this member.
	 * @return The identifier name.
	 */
	public String getIdentifier() {
		return identifier;
	}
	
	/**
	 * Get the type of this member.
	 * @return The type of this member.
	 */
	public Type getType() {
		return type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String toString() {
		return String.format("%s: %s", this.identifier, this.type.toString());
	}
}
