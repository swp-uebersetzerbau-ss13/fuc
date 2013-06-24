package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.*;
import swp_compiler_ss13.common.types.*;
import swp_compiler_ss13.common.types.primitive.*;
import swp_compiler_ss13.common.types.derived.*;

import java.util.*;

public class LLVMBackendStructType extends StructType
{
	public LLVMBackendStructType(List<Member> members) {
		super("", members.toArray(new Member[members.size()]));
	}
}