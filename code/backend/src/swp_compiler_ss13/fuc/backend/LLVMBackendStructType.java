package swp_compiler_ss13.fuc.backend;

import java.util.Arrays;
import java.util.List;

import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;

public class LLVMBackendStructType extends StructType
{
	public LLVMBackendStructType(List<Member> members) {
		super(members.toArray(new Member[members.size()]));
	}

	public List<Member> getMembers() {
		return Arrays.asList(this.members);
	}
}