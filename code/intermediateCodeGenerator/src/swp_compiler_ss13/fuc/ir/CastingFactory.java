package swp_compiler_ss13.fuc.ir;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.primitive.PrimitiveType;

/**
 * This factory adds castings where needed to the IR Code. Castings can be
 * disabled by setting the property -Dfuc.irgen.castings=false
 * 
 * @author "Frank Zechert"
 * @version 2
 */
public class CastingFactory {

	/**
	 * Indicates whether casting should be enabled or disabled. By default
	 * casting is enabled. To disable casting set -Dfuc.irgen.castings=false
	 */
	static final boolean castingEnabled = Boolean.parseBoolean(System.getProperty("fuc.irgen.castings", "true"));

	/**
	 * The logger
	 */
	static final Logger logger = Logger.getLogger(CastingFactory.class);

	/**
	 * Check if a cast is needed between type1 and type2
	 * 
	 * @param type1
	 *            Type 1 to check
	 * @param type2
	 *            Type 2 to check
	 * @return true if a cast is needed between type1 and type2
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred
	 */
	public static boolean isCastNeeded(Type type1, Type type2) throws IntermediateCodeGeneratorException {
		if (!castingEnabled) {
			return false;
		}
		if (isPrimitive(type1) && isPrimitive(type2)) {
			return type1.getKind() != type2.getKind();
		}

		String err = "CastingFactory.isCastNeeded is not implemented for non-primitive types. Types given: %s and %s";
		String errf = String.format(err, type1.toString(), type2.toString());
		logger.fatal(errf);
		throw new IntermediateCodeGeneratorException(errf, new UnsupportedOperationException());
	}

	/**
	 * Check if a cast is needed between the types of result1 and result2
	 * 
	 * @param result1
	 *            Result 1 to check
	 * @param result2
	 *            Result 2 to check
	 * @return true if a cast is needed between the types of result1 and result2
	 * @throws IntermediateCodeGeneratorException
	 *             An error occurred
	 */
	public static boolean isCastNeeded(IntermediateResult result1, IntermediateResult result2)
			throws IntermediateCodeGeneratorException {
		return isCastNeeded(result1.getType(), result2.getType());
	}

	/**
	 * Checks whether the given type is a primitive type.
	 * 
	 * @param type
	 *            The type to check
	 * @return True if the given type is a primitive type (Boolean, Double,
	 *         Long, String).
	 */
	public static boolean isPrimitive(Type type) {
		return (type instanceof PrimitiveType);
	}

	/**
	 * Create and return a quadruple to cast fromId of type fromType to toId of
	 * type toType
	 * 
	 * @param fromType
	 *            The type to cast from
	 * @param fromId
	 *            The id to cast from
	 * @param toType
	 *            The type to cast to
	 * @param toId
	 *            The id to cast to
	 * @return The Quadruple that represents the cast.
	 * @throws IntermediateCodeGeneratorException
	 *             Something went wrong
	 */
	public static Quadruple createCast(Type fromType, String fromId, Type toType, String toId)
			throws IntermediateCodeGeneratorException {
		switch (fromType.getKind()) {
		case DOUBLE:
			switch (toType.getKind()) {
			case LONG:
				return new QuadrupleImpl(Quadruple.Operator.DOUBLE_TO_LONG, fromId, Quadruple.EmptyArgument, toId);
			default:
				break;
			}
		case LONG:
			switch (toType.getKind()) {
			case DOUBLE:
				return new QuadrupleImpl(Quadruple.Operator.LONG_TO_DOUBLE, fromId, Quadruple.EmptyArgument, toId);
			default:
				break;
			}
		default:
			break;
		}

		String err = "Can not create a cast from %s of type %s to %s of type %s.";
		String errf = String.format(err, fromId, fromType, toId, toType);
		throw new IntermediateCodeGeneratorException(errf, new UnsupportedOperationException());
	}
}
