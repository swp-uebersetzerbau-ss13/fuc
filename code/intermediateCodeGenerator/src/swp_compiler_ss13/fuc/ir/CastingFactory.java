package swp_compiler_ss13.fuc.ir;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.derived.DerivedType;
import swp_compiler_ss13.fuc.ir.data.IntermediateResult;
import swp_compiler_ss13.fuc.ir.data.QuadrupleImpl;

/**
 * Factory for all casting tasks
 * 
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class CastingFactory {

	/**
	 * the logger
	 */
	private static Logger logger = Logger.getLogger(CastingFactory.class);

	/**
	 * Check if a cast is needed between a and b
	 * 
	 * @param type1
	 *            IntermediateResult a
	 * @param type2
	 *            IntermediateResult b
	 * @return true, if a and b are not of the same type
	 * @throws IntermediateCodeGeneratorException
	 *             a and b could not be checked
	 */
	public static boolean isCastNeeded(IntermediateResult type1, IntermediateResult type2)
			throws IntermediateCodeGeneratorException {
		return CastingFactory.isCastNeeded(type1.getType(), type2.getType());
	}

	/**
	 * Check if a cast is needed between a and b
	 * 
	 * @param type1
	 *            Type a
	 * @param type2
	 *            Type b
	 * @return true, if a and b are not of the same type
	 * @throws IntermediateCodeGeneratorException
	 *             a and b could not be checked
	 */
	public static boolean isCastNeeded(Type type1, Type type2)
			throws IntermediateCodeGeneratorException {

		if (isPrimitive(type1) && isPrimitive(type2)) {
			return type1.getKind() != type2.getKind();
		}
		return !type1.toString().equals(type2.toString());
	}

	/**
	 * Check if the given type is a simple type (not derived).
	 * 
	 * @param type
	 *            The type to check
	 * @return true if the type is simple, false if the type is derived
	 */
	public static boolean isPrimitive(Type type) {
		return !(type instanceof DerivedType);
	}

	/**
	 * Create a cast from long to double
	 * 
	 * @param leftValue
	 *            The long value to cast to double
	 * @param castedLeft
	 *            The target double identifier
	 * @return The Quadruple for the cast
	 */
	public static Quadruple longToDouble(String leftValue, String castedLeft) {
		if (leftValue.startsWith("#")) {
			leftValue = leftValue + ".0";
			return new QuadrupleImpl(Operator.ASSIGN_DOUBLE, leftValue, castedLeft);
		}
		return new QuadrupleImpl(Operator.LONG_TO_DOUBLE, leftValue, castedLeft);
	}

	/**
	 * Create a cast from double to long
	 * 
	 * @param leftValue
	 *            The double value to cast to long
	 * @param castedLeft
	 *            The target long identifier
	 * @return The Quadruple for the cast
	 */
	public static Quadruple doubleToLong(String leftValue, String castedLeft) {
		if (leftValue.startsWith("#")) {
			int decimalpoint = leftValue.lastIndexOf('.');
			if (decimalpoint >= 0) {
				leftValue = leftValue.substring(0, decimalpoint);
			}
			return new QuadrupleImpl(Operator.ASSIGN_LONG, leftValue, castedLeft);
		}
		return new QuadrupleImpl(Operator.DOUBLE_TO_LONG, leftValue, castedLeft);
	}

	/**
	 * Check if the value is numeric (long or double)
	 * 
	 * @param value
	 *            The value to check
	 * @return true if the type is numeric
	 */
	public static boolean isNumeric(IntermediateResult value) {
		return CastingFactory.isNumeric(value.getType());
	}

	/**
	 * Check if the type is numeric (long or double)
	 * 
	 * @param type
	 *            The type to check
	 * @return true if the type is numeric
	 */
	public static boolean isNumeric(Type type) {
		if ((type.getKind() == Kind.LONG) || (type.getKind() == Kind.DOUBLE)) {
			return true;
		}
		return false;
	}

	/**
	 * Cast the given identifier to string
	 * 
	 * @param type
	 *            The type to cast from
	 * @param value
	 *            The value to cast
	 * @param tmp
	 *            The destination value
	 * @return The Quadruple representing the cast
	 * @throws IntermediateCodeGeneratorException
	 *             Cast not possible
	 */
	public static Quadruple castToString(Type type, String value, String tmp)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case BOOLEAN:
			if (value.equals("#TRUE")) {
				return new QuadrupleImpl(Operator.ASSIGN_STRING, "#\"true\"", tmp);
			}
			if (value.equals("#FALSE")) {
				return new QuadrupleImpl(Operator.ASSIGN_STRING, "#\"false\"", tmp);
			}
			return new QuadrupleImpl(Operator.BOOLEAN_TO_STRING, value, tmp);
		case DOUBLE:
			if (value.startsWith("#")) {
				return new QuadrupleImpl(Operator.ASSIGN_STRING, "#\"" + value.substring(1) + "\"", tmp);
			}
			return new QuadrupleImpl(Operator.DOUBLE_TO_STRING, value, tmp);
		case LONG:
			if (value.startsWith("#")) {
				return new QuadrupleImpl(Operator.ASSIGN_STRING, "#\"" + value.substring(1) + "\"", tmp);
			}
			return new QuadrupleImpl(Operator.LONG_TO_STRING, value, tmp);
		default:
			break;
		}
		String error = "can not cast from " + type.toString() + " to string";
		CastingFactory.logger.fatal(error);
		throw new IntermediateCodeGeneratorException(error);
	}

}
