package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.*;
import swp_compiler_ss13.common.types.*;
import swp_compiler_ss13.common.types.primitive.*;
import swp_compiler_ss13.common.types.derived.*;

import java.util.*;

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
		super("", members.toArray(new Member[members.size()]));
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