package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;
import static swp_compiler_ss13.common.types.Type.*;

import java.util.List;

public class ArrayType
{
	ArrayType(List<Integer> dims, Kind type)
	{
		dimensions = dims;
		storage_type = type;
	}

	public List<Integer> dimensions;
	public Kind storage_type;
//	boolean is_reference;
};
