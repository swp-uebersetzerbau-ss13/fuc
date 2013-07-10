package swp_compiler_ss13.fuc.backend;

import java.util.Arrays;
import java.util.List;

import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;

/**
 * Corresponds to a struct in three address code
 * and extends the normal <code>StructType</code>
 * with convenience functions for the LLVMBackend.
 *
 */
public class LLVMBackendStructType extends StructType
{
	/**
	 * Creates a new backend struct type.
	 *
	 * @param members the members of the struct type
	 */
	public LLVMBackendStructType(List<Member> members) {
		super(members.toArray(new Member[members.size()]));
	}

	/**
	 * Get the members.
	 *
	 * @return the members.
	 */
	public List<Member> getMembers() {
		return Arrays.asList(this.members);
	}
}