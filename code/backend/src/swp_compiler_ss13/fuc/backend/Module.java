package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import swp_compiler_ss13.common.types.*;
import swp_compiler_ss13.common.types.primitive.*;
import swp_compiler_ss13.common.types.derived.*;

import java.io.PrintWriter;
import java.util.*;
import java.io.UnsupportedEncodingException;
import java.util.regex.Pattern;
import java.util.regex.Matcher;

import static swp_compiler_ss13.common.types.Type.*;
import static swp_compiler_ss13.common.types.Type.Kind.BOOLEAN;

/**
 * This class allows for the generation of an LLVM IR module.
 * Each methode that begins with "add" generates LLVM IR code
 * and writes it to the <code>PrintWriter</code> <code>out</code>.
 *
 */
public class Module
{
	/**
	 * Get a string literal's identifier by its
	 * id (i.e. its position in the list of string literals).
	 *
	 * @param id the string literal's id
	 * @return the string literal's identifier
	 */
	public static String getStringLiteralIdentifier(int id)
	{
		return "%.string_" + String.valueOf(id);
	}
	/**
	 * Get the LLVM IR type corresponding to
	 * a type from the three address code.
	 *
	 * @param type the type from the three address code
	 * @return the corresponding LLVM IR type
	 */
	public static String getIRType(Kind type) {
		return Module.getIRType(type,false);
	}
	public static String getIRType(Kind type, boolean as_ptr)
	{
		String irType = "";
		switch(type)
		{
			case LONG:
				/* Longs are 64bit signed integers */
				irType = "i64";
				break;
			case DOUBLE:
				/* Doubles are 64bit IEEE floats */
				irType = "double";
				break;
			case BOOLEAN:
				/* Booleans are 8bit signed integers */
				irType = "i1";
				break;
			case STRING:
				/* Strings are each a pointer to a string literal
				   (which itself is a pointer to a memory segment
				   of signed integers).*/
				irType = "i8*";
				break;
		}

		return irType + (as_ptr ? "*" : "");
	}

	/**
	 * Get the LLVM IR type corresponding to
	 * an *array* type from the three address code.
	 *
	 * @param type the final type of the array, eg: double[][] --> double
	 * @param sizes a list of sizes of the array dimensions, eg:
	 *          double x[5][10][7] --> [5,10,7]
	 * @return the corresponding LLVM IR type for the array
	 */
	public static String getIRAType(Type type, List<Integer> dimensions) {
		return Module.getIRAType(type,dimensions,false);
	}
	public static String getIRAType(Type type, List<Integer> dimensions, boolean as_ptr)
	{
		String irType = "";
		// write dimensions
		for (int d : dimensions)
			irType = irType + "[" + d + " x ";
		// write actual type
		if(type instanceof PrimitiveType) {
			irType = irType + Module.getIRType(type.getKind());
		}
		else if(type instanceof LLVMBackendStructType) {
			irType += Module.getIRSType(((LLVMBackendStructType) type).getMembers());
		}
		// write closing brakets
		for (int d : dimensions)
			irType += "]";
		irType.trim();
		return irType + (as_ptr ? "*" : "");
	}

	public static String getIRSType(List<Member> members) {
		String irType = "{ ";

		for(Member m: members) {
			Type type = m.getType();

			if(type instanceof PrimitiveType) {
				irType += Module.getIRType(type.getKind());
			}
			else if(type instanceof LLVMBackendArrayType) {
				LLVMBackendArrayType array = (LLVMBackendArrayType) type;
				irType += Module.getIRAType(array.getStorageType(), array.getDimensions());
			}
			else if(type instanceof LLVMBackendStructType) {
				LLVMBackendStructType struct = (LLVMBackendStructType) type;
				irType += Module.getIRSType(struct.getMembers());
			}

			irType += ", ";
		}

		irType = irType.substring(0, irType.length() - 2);

		return irType + " }";
	}

	/**
	 * Gets the LLVM IR instruction for a binary TAC operator
	 *
	 * @param operator the binary TAC operator
	 * @return the corresponding LLVM IR instruction
	 */
	public static String getIRBinaryInstruction(Quadruple.Operator operator)
	{
		String irInst = "";

		switch(operator)
		{
			/* Arithmetic */
			case ADD_LONG:
				irInst = "add";
				break;
			case ADD_DOUBLE:
				irInst = "fadd";
				break;
			case SUB_LONG:
				irInst = "sub";
				break;
			case SUB_DOUBLE:
				irInst = "fsub";
				break;
			case MUL_LONG:
				irInst = "mul";
				break;
			case MUL_DOUBLE:
				irInst = "fmul";
				break;
			case DIV_LONG:
				irInst = "sdiv";
				break;
			case DIV_DOUBLE:
				irInst = "fdiv";
				break;

			/* Boolean Arithmetic */
			case OR_BOOLEAN:
				irInst = "or";
				break;
			case AND_BOOLEAN:
				irInst = "and";
				break;

			/* Comparisons */
			case COMPARE_LONG_E:
				irInst = " icmp eq";
				break;
			case COMPARE_LONG_G:
				irInst = " icmp sgt";
			break;
			case COMPARE_LONG_L:
				irInst = " icmp slt";
				break;
			case COMPARE_LONG_GE:
				irInst = " icmp sge";
			break;
			case COMPARE_LONG_LE:
				irInst = " icmp sle";
			break;

			case COMPARE_DOUBLE_E:
				irInst = " fcmp oeq";
			break;
			case COMPARE_DOUBLE_G:
				irInst = " fcmp ogt";
			break;
			case COMPARE_DOUBLE_L:
				irInst = " fcmp olt";
			break;
			case COMPARE_DOUBLE_GE:
				irInst = " fcmp oge";
			break;
			case COMPARE_DOUBLE_LE:
				irInst = " fcmp ole";
			break;
		}

		return irInst;
	}

	/**
	 * Gets the LLVM IR function residing in the preamble
	 * for a binary TAC operator.
	 *
	 * @param operator the binary TAC operator
	 * @return the corresponding LLVM IR function
	 */
	public static String getIRBinaryCall(Quadruple.Operator operator) {
		String irCall = "";

		switch(operator)
		{
			/* Arithmetic */
			case DIV_LONG:
				irCall = "div_long";
				break;
			case DIV_DOUBLE:
				irCall = "div_double";
				break;
			case CONCAT_STRING:
				irCall = "concat_string";
				break;
		}

		return irCall;
	}

	/**
	 * Convert a three adress code boolean
	 * to a LLVM IR boolean.
	 * All other public functions expect booleans
	 * to be in the LLVM IR format.
	 *
	 * @param bool a TAC boolean
	 * @return the converted LLVM IR boolean
	 */
	public static String toIRBoolean(String bool)
	{
		if(bool.equals("#FALSE"))
		{
			return "#0";
		}
		else if(bool.equals("#TRUE"))
		{
			return "#1";
		}
		else
		{
			return bool;
		}
	}

	/**
	 * Convert a three adress code string
	 * to a LLVM IR string.
	 * All other public functions expect strings
	 * to be in the LLVM IR format.
	 *
	 * @param str a TAC string
	 * @return the converted LLVM IR string
	 */
	public static String toIRString(String str)
	{
		String irString = "";

		/* Unescape special characters */
		irString = str.replace("\\\"", "\"").
			replace("\\r", "\r").
			replace("\\n", "\n").
			replace("\\t", "\t").
			replace("\\0", "\0");

		return irString;
	}

	static public Kind QuadTypeToKind(Quadruple.Operator op) throws BackendException {
		switch (op) {
		case DECLARE_DOUBLE:
		case ARRAY_GET_DOUBLE:
		case ARRAY_SET_DOUBLE:
			return Kind.DOUBLE;
		case DECLARE_BOOLEAN:
		case ARRAY_GET_BOOLEAN:
		case ARRAY_SET_BOOLEAN:
			return Kind.BOOLEAN;
		case DECLARE_LONG:
		case ARRAY_GET_LONG:
		case ARRAY_SET_LONG:
			return Kind.LONG;
		}
		throw new BackendException("QuadTypeToKind: unimplemented OP -> unknown type");
	}
}
