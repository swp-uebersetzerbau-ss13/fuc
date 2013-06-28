package swp_compiler_ss13.fuc.ir;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.fuc.ir.data.QuadrupleImpl;

/**
 * This factory creates quadruples according to the Three Address Code (TAC) specifiaction given in
 * the common Wiki.
 * 
 * @see "https://github.com/swp-uebersetzerbau-ss13/common/wiki/Three-Address-Code-Specification"
 * @author "Frank Zechert"
 * @author "Danny Maasch"
 * @author kaworu
 * @version 3
 */
public class QuadrupleFactory {

	/**
	 * The logger instance
	 */
	private static Logger logger = Logger.getLogger(QuadrupleFactory.class);

	/**
	 * Create one or more quadruples to represent the declaration.
	 * 
	 * @param name
	 *            The name of the variable to be declared.
	 * @param type
	 *            Type of the variable to be declared.
	 * @return A List of Quadruples that represent the declaration
	 * @throws IntermediateCodeGeneratorException
	 *             Declaration of the given type is unsupported
	 */
	public static List<Quadruple> declare(String name, Type type)
			throws IntermediateCodeGeneratorException {
		List<Quadruple> declaration = new LinkedList<>();
		switch (type.getKind()) {
			case ARRAY:
				declaration.addAll(QuadrupleFactory.declareArray(name, (ArrayType) type));
				break;
			case BOOLEAN:
				declaration.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, name));
				break;
			case DOUBLE:
				declaration.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, name));
				break;
			case LONG:
				declaration.add(new QuadrupleImpl(Operator.DECLARE_LONG, name));
				break;
			case STRING:
				declaration.add(new QuadrupleImpl(Operator.DECLARE_STRING, name));
				break;
			case STRUCT:
				declaration.addAll(QuadrupleFactory.declareStruct(name, (StructType) type));
				break;
			default:
				String error = String.format(
						"Variable declaration for a variable of type %s is not supported!",
						type.toString());
				QuadrupleFactory.logger.fatal(error);
				throw new IntermediateCodeGeneratorException(error);
		}

		return declaration;
	}

	/**
	 * Declare a struct
	 * 
	 * @param name
	 *            the name of the struct
	 * @param type
	 *            the type of the struct
	 * @return Quadruples to declare the given struct
	 * @throws IntermediateCodeGeneratorException
	 *             Error in struct declaration
	 */
	private static Collection<? extends Quadruple> declareStruct(String name, StructType type)
			throws IntermediateCodeGeneratorException {

		List<Quadruple> decl = new LinkedList<>();
		Member[] members = type.members();
		String memberCount = "#" + members.length;
		decl.add(new QuadrupleImpl(Operator.DECLARE_STRUCT, memberCount, name));
		for (Member member : members) {
			decl.addAll(QuadrupleFactory.declare(member.getName(), member.getType()));
		}
		return decl;

	}

	/**
	 * Declare an array
	 * 
	 * @param name
	 *            the name of the array
	 * @param type
	 *            the type of the array
	 * @return the Quadruples to declare the given array
	 * @throws IntermediateCodeGeneratorException
	 *             Error in array declaration
	 */
	private static List<Quadruple> declareArray(String name, ArrayType type)
			throws IntermediateCodeGeneratorException {

		List<Quadruple> decl = new LinkedList<>();
		String length = "#" + type.getLength();
		Quadruple level = new QuadrupleImpl(Operator.DECLARE_ARRAY, length, name);
		decl.add(level);
		decl.addAll(QuadrupleFactory.declare("!", type.getInnerType()));
		return decl;

	}

	/**
	 * Create a label
	 * 
	 * @param label
	 *            the name of the label
	 * @return The label as Quadruple
	 */
	public static Quadruple label(String label) {
		return new QuadrupleImpl(Operator.LABEL, label, Quadruple.EmptyArgument);
	}

	/**
	 * Create a conditional jump
	 * 
	 * @param condition
	 *            The condition
	 * @param trueLbl
	 *            The label to jump to if the condition is true
	 * @param falseLbl
	 *            The label to jump to if the condition is false
	 * @return The quadruple representing the conditional jump
	 */
	public static Quadruple branch(String condition, String trueLbl, String falseLbl) {
		return new QuadrupleImpl(Operator.BRANCH, trueLbl, falseLbl, condition);
	}

	/**
	 * Create an unconditional jump
	 * 
	 * @param label
	 *            The label to jump to
	 * @return The unconditional jump as Quadruple
	 */
	public static Quadruple jump(String label) {
		return new QuadrupleImpl(Operator.BRANCH, label, Quadruple.EmptyArgument);
	}

	/**
	 * Create a quadruple for an arithmetic binary operation
	 * 
	 * @param op
	 *            The operator for the operation
	 * @param type
	 *            The type of the left and right values
	 * @param left
	 *            the left value
	 * @param right
	 *            the right value
	 * @param result
	 *            the target value
	 * @return the quadruple for the given operation
	 * @throws IntermediateCodeGeneratorException
	 *             Unsupported operator or type
	 */
	public static Quadruple arithmeticBinary(BinaryOperator op, Type type, String left,
			String right, String result) throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
			case DOUBLE:
				switch (op) {
					case ADDITION:
						return new QuadrupleImpl(Operator.ADD_DOUBLE, left, right, result);
					case DIVISION:
						return new QuadrupleImpl(Operator.DIV_DOUBLE, left, right, result);
					case MULTIPLICATION:
						return new QuadrupleImpl(Operator.MUL_DOUBLE, left, right, result);
					case SUBSTRACTION:
						return new QuadrupleImpl(Operator.SUB_DOUBLE, left, right, result);
					default:
						break;
				}
				break;
			case LONG:
				switch (op) {
					case ADDITION:
						return new QuadrupleImpl(Operator.ADD_LONG, left, right, result);
					case DIVISION:
						return new QuadrupleImpl(Operator.DIV_LONG, left, right, result);
					case MULTIPLICATION:
						return new QuadrupleImpl(Operator.MUL_LONG, left, right, result);
					case SUBSTRACTION:
						return new QuadrupleImpl(Operator.SUB_LONG, left, right, result);
					default:
						break;
				}
				break;
			default:
				break;

		}
		throw new IntermediateCodeGeneratorException("The type " + type.toString()
				+ " and Operator " + op.toString()
				+ " is not supported for arithmetic binary expressions");
	}

	/**
	 * Create a logical binary expression
	 * 
	 * @param operator
	 *            The operator
	 * @param value1
	 *            left value
	 * @param value2
	 *            right value
	 * @param result
	 *            the result address
	 * @return The Quadruple for the binary logical expression
	 * @throws IntermediateCodeGeneratorException
	 *             unsupoorted operator
	 */
	public static Quadruple logicBinary(BinaryOperator operator, String value1, String value2,
			String result) throws IntermediateCodeGeneratorException {
		switch (operator) {
			case LOGICAL_AND:
				return new QuadrupleImpl(Operator.AND_BOOLEAN, value1, value2, result);
			case LOGICAL_OR:
				return new QuadrupleImpl(Operator.OR_BOOLEAN, value1, value2, result);
			default:
				break;
		}
		throw new IntermediateCodeGeneratorException("The operator " + operator.toString()
				+ " is not supported for logic binary expressions");
	}

	/**
	 * Create a Quadruple for a relation expresion
	 * 
	 * @param operator
	 *            The operator
	 * @param v1
	 *            the left value
	 * @param v2
	 *            the right value
	 * @param res
	 *            the result target
	 * @param type
	 *            the type
	 * @return the Quadruple for the operation
	 * @throws IntermediateCodeGeneratorException
	 *             Unsupported type or operator
	 */
	public static Quadruple relation(BinaryOperator operator, String v1, String v2, String res,
			Type type) throws IntermediateCodeGeneratorException {
		switch (operator) {
			case EQUAL:
			case INEQUAL:
				switch (type.getKind()) {
					case DOUBLE:
						return new QuadrupleImpl(Operator.COMPARE_DOUBLE_E, v1, v2, res);
					case LONG:
						return new QuadrupleImpl(Operator.COMPARE_LONG_E, v1, v2, res);
					default:
						break;
				}
				break;
			case GREATERTHAN:
				switch (type.getKind()) {
					case DOUBLE:
						return new QuadrupleImpl(Operator.COMPARE_DOUBLE_G, v1, v2, res);
					case LONG:
						return new QuadrupleImpl(Operator.COMPARE_LONG_G, v1, v2, res);
					default:
						break;
				}
				break;
			case GREATERTHANEQUAL:
				switch (type.getKind()) {
					case DOUBLE:
						return new QuadrupleImpl(Operator.COMPARE_DOUBLE_GE, v1, v2, res);
					case LONG:
						return new QuadrupleImpl(Operator.COMPARE_LONG_GE, v1, v2, res);
					default:
						break;
				}
				break;
			case LESSTHAN:
				switch (type.getKind()) {
					case DOUBLE:
						return new QuadrupleImpl(Operator.COMPARE_DOUBLE_L, v1, v2, res);
					case LONG:
						return new QuadrupleImpl(Operator.COMPARE_LONG_L, v1, v2, res);
					default:
						break;
				}
				break;
			case LESSTHANEQUAL:
				switch (type.getKind()) {
					case DOUBLE:
						return new QuadrupleImpl(Operator.COMPARE_DOUBLE_LE, v1, v2, res);
					case LONG:
						return new QuadrupleImpl(Operator.COMPARE_LONG_LE, v1, v2, res);
					default:
						break;
				}
				break;
			default:
				break;
		}
		throw new IntermediateCodeGeneratorException("The type " + type.toString()
				+ " and operator " + operator.toString()
				+ " is not supported for relation expressions");
	}

	/**
	 * Create a unary logic operation
	 * 
	 * @param op
	 *            the operator
	 * @param from
	 *            the value
	 * @param to
	 *            the target
	 * @return the Quadruple for the operation
	 * @throws IntermediateCodeGeneratorException
	 *             unsupported operator
	 */
	public static Quadruple logicUnary(UnaryOperator op, String from, String to)
			throws IntermediateCodeGeneratorException {
		switch (op) {
			case LOGICAL_NEGATE:
				return new QuadrupleImpl(Operator.NOT_BOOLEAN, from, to);
			default:
				break;
		}
		throw new IntermediateCodeGeneratorException("The operator " + op.toString()
				+ " is not supported for unary logic expressions");
	}

	/**
	 * Create a unary arithmetic operation
	 * 
	 * @param op
	 *            the operator
	 * @param from
	 *            the value
	 * @param to
	 *            the target
	 * @param t
	 *            The type
	 * @return the Quadruple for the operation
	 * @throws IntermediateCodeGeneratorException
	 *             unsupported operator
	 */
	public static Quadruple arithmeticUnary(UnaryOperator op, String from, String to, Type t)
			throws IntermediateCodeGeneratorException {
		switch (op) {
			case MINUS:
				switch (t.getKind()) {
					case DOUBLE:
						return new QuadrupleImpl(Operator.SUB_DOUBLE, "#0.0", from, to);
					case LONG:
						return new QuadrupleImpl(Operator.SUB_LONG, "#0", from, to);
					default:
						break;

				}
			default:
				break;
		}
		throw new IntermediateCodeGeneratorException("The operator " + op.toString()
				+ " is not supported for unary arithmetic expressions");
	}

	/**
	 * Create a print statement
	 * 
	 * @param variable
	 *            The variable to print
	 * @return The Quadruple to print the variable
	 */
	public static Quadruple print(String variable) {
		return new QuadrupleImpl(Operator.PRINT_STRING, variable, Quadruple.EmptyArgument);
	}

	/**
	 * Return an exit value
	 * 
	 * @param value
	 *            the exit value
	 * @return The quadruple for a return statement
	 */
	public static Quadruple exit(String value) {
		return new QuadrupleImpl(Operator.RETURN, value, Quadruple.EmptyArgument);
	}

	/**
	 * Create an assignment
	 * 
	 * @param type
	 *            type of the left hand side variable
	 * @param to
	 *            the destination of the assignment
	 * @param from
	 *            the source of the assignment
	 * @return the quadruple for the assignment
	 * @throws IntermediateCodeGeneratorException
	 *             unsupported type
	 */
	public static Quadruple assignment(Type type, String to, String from)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
			case BOOLEAN:
				return new QuadrupleImpl(Operator.ASSIGN_BOOLEAN, from, to);
			case DOUBLE:
				return new QuadrupleImpl(Operator.ASSIGN_DOUBLE, from, to);
			case LONG:
				return new QuadrupleImpl(Operator.ASSIGN_LONG, from, to);
			case STRING:
				return new QuadrupleImpl(Operator.ASSIGN_STRING, from, to);
			default:
				break;
		}
		String err = "assignment for type " + type.toString() + " is not supported.";
		QuadrupleFactory.logger.fatal(err);
		throw new IntermediateCodeGeneratorException(err);
	}
}
