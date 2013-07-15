package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.*;
import swp_compiler_ss13.common.types.*;
import swp_compiler_ss13.common.types.primitive.*;
import swp_compiler_ss13.common.types.derived.*;

import java.util.*;

/**
 * Corresponds to an array in three address code
 * and extends the normal <code>ArrayType</code>
 * with convenience functions for the LLVMBackend.
 *
 */
public class LLVMBackendArrayType extends ArrayType
{
	/**
	 * Creates a new backend array type.
	 *
	 * @param dimensions the dimensions of this array type
	 * @param type the type of the array type's elements
	 */
	public LLVMBackendArrayType(List<Integer> dimensions, Type type) {
		super(type, dimensions.get(0));
		assert((type instanceof PrimitiveType) ||
		       (type instanceof StructType));

		dimensions.remove(0);

		if(dimensions.size() > 0) {
			this.type = new LLVMBackendArrayType(dimensions, type);
			this.width = this.type.getWidth() * this.length;
		}
	}

	/**
	 * Get the dimensions.
	 *
	 * @return the dimensions
	 */
	public List<Integer> getDimensions() {
		List<Integer> dimensions = new LinkedList<Integer>();
		dimensions.add(this.length);

		if(this.type instanceof LLVMBackendArrayType) {
			dimensions.addAll(((LLVMBackendArrayType) this.type).getDimensions());
		}

		return dimensions;
	}

	/**
	 * Get the type of the elements.
	 *
	 * @return the elements' type
	 */
	public Type getStorageType() {
		if(this.type instanceof LLVMBackendArrayType) {
			return ((LLVMBackendArrayType) this.type).getStorageType();
		}
		else {
			return this.type;
		}
	}
}