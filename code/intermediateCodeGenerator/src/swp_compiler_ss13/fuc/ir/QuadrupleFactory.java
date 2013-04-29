package swp_compiler_ss13.fuc.ir;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;

/**
 * This factory generates quadruples for the given instructions
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class QuadrupleFactory {

	/**
	 * create a quadruple to represent the variable declaration
	 * 
	 * @param id
	 *            The name of the declared variable
	 * @param type
	 *            The type of the declared variable
	 * @return The quadruple representing the declaration
	 * @throws IntermediateCodeGeneratorException
	 *             Thrown if an unknown type is declared
	 */
	public static Quadruple declaration(String id, Type type) throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, id);
		case LONG:
			return new QuadrupleImpl(Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, id);
		default:
			throw new IntermediateCodeGeneratorException("Unsupported type " + type.toString());
		}
	}

}
