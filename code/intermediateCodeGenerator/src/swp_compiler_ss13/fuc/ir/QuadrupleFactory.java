package swp_compiler_ss13.fuc.ir;

import java.util.LinkedList;
import java.util.List;

import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;

/**
 * This factory generates quadruples for the given instructions
 * 
 * @author "Frank Zechert,Danny Maasch"
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
	public static List<Quadruple> declaration(String id, Type type) throws IntermediateCodeGeneratorException {
		List<Quadruple> quadruples = new LinkedList<>();
		switch (type.getKind()) {
		case DOUBLE:
			quadruples.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument,
					id));
			break;
		case LONG:
			quadruples.add(new QuadrupleImpl(Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument,
					id));
			break;
		case STRING:
			quadruples.add(new QuadrupleImpl(Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument,
					id));
			break;
		case BOOLEAN:
			quadruples.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument,
					Quadruple.EmptyArgument, id));
			break;
		case ARRAY:
			ArrayType arrayType = (ArrayType) type;
			quadruples.add(new QuadrupleImpl(Operator.DECLARE_ARRAY, "#" + arrayType.getLength().toString(),
					Quadruple.EmptyArgument, id));
			quadruples.addAll(declaration("!", arrayType.getInnerType()));
			break;
		default:
			throw new IntermediateCodeGeneratorException("Unsupported type " + type.toString());
		}
		return quadruples;
	}

	/**
	 * create a quadruple to represent array_get
	 * 
	 * @param type
	 *            the type of the variable to get
	 * @param source
	 *            the source to get
	 * @param index
	 *            the index to get
	 * @param target
	 *            the target where the reference is stored
	 * @return the quadruple
	 * @throws IntermediateCodeGeneratorException
	 *             something went wring
	 */
	public static Quadruple arrayGet(Type type, String source, String index, String target)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case BOOLEAN:
			return new QuadrupleImpl(Operator.ARRAY_GET_BOOLEAN, source, index, target);
		case DOUBLE:
			return new QuadrupleImpl(Operator.ARRAY_GET_DOUBLE, source, index, target);
		case LONG:
			return new QuadrupleImpl(Operator.ARRAY_GET_LONG, source, index, target);
		case STRING:
			return new QuadrupleImpl(Operator.ARRAY_GET_STRING, source, index, target);
		case ARRAY:
			return new QuadrupleImpl(Operator.ARRAY_GET_REFERENCE, source, index, target);
		case STRUCT:
		default:
			throw new IntermediateCodeGeneratorException(new UnsupportedOperationException());
		}
	}

	/**
	 * Create a new binaryOperator Quadruple for Arguments of Type long
	 * 
	 * @param operator
	 *            The binary operator
	 * @param left
	 *            argument 1
	 * @param right
	 *            argument 2
	 * @param result
	 *            result
	 * @return the quadruple
	 * @throws IntermediateCodeGeneratorException
	 *             unsupported operator
	 */
	public static Quadruple longArithmeticBinaryOperation(BinaryOperator operator, String left, String right,
			String result) throws IntermediateCodeGeneratorException {
		switch (operator) {
		case ADDITION:
			return new QuadrupleImpl(Operator.ADD_LONG, left, right, result);
		case DIVISION:
			return new QuadrupleImpl(Operator.DIV_LONG, left, right, result);
		case MULTIPLICATION:
			return new QuadrupleImpl(Operator.MUL_LONG, left, right, result);
		case SUBSTRACTION:
			return new QuadrupleImpl(Operator.SUB_LONG, left, right, result);
		default:
			throw new IntermediateCodeGeneratorException("Unsupported binary operator " + operator.toString());
		}
	}

	/**
	 * Create a new binaryOperator Quadruple for Arguments of Type double
	 * 
	 * @param operator
	 *            The binary operator
	 * @param left
	 *            argument 1
	 * @param right
	 *            argument 2
	 * @param result
	 *            result
	 * @return the quadruple
	 * @throws IntermediateCodeGeneratorException
	 *             unsupported operator
	 */
	public static Quadruple doubleArithmeticBinaryOperation(BinaryOperator operator, String left, String right,
			String result) throws IntermediateCodeGeneratorException {
		switch (operator) {
		case ADDITION:
			return new QuadrupleImpl(Operator.ADD_DOUBLE, left, right, result);
		case DIVISION:
			return new QuadrupleImpl(Operator.DIV_DOUBLE, left, right, result);
		case MULTIPLICATION:
			return new QuadrupleImpl(Operator.MUL_DOUBLE, left, right, result);
		case SUBSTRACTION:
			return new QuadrupleImpl(Operator.SUB_DOUBLE, left, right, result);
		default:
			throw new IntermediateCodeGeneratorException("Unsupported binary operator " + operator.toString());
		}
	}

	/**
	 * Create a new cast from long to double
	 * 
	 * @param from
	 *            The long to cast from
	 * @param to
	 *            The double to cast to
	 * @return The tac quadruple
	 */
	public static Quadruple castLongToDouble(String from, String to) {
		return new QuadrupleImpl(Operator.LONG_TO_DOUBLE, from, Quadruple.EmptyArgument, to);
	}

	/**
	 * Create a new cast from double to long
	 * 
	 * @param from
	 *            The double to cast from
	 * @param to
	 *            The long to cast to
	 * @return The tac quadurple
	 */
	public static Quadruple castDoubleToLong(String from, String to) {
		return new QuadrupleImpl(Operator.DOUBLE_TO_LONG, from, Quadruple.EmptyArgument, to);
	}

	/**
	 * Create a new assignment in TAC
	 * 
	 * @param typeOfid
	 *            The type of the variable to assign the value to
	 * @param from
	 *            The value to assign to
	 * @param to
	 *            The variable to hold the assigned value
	 * @param fromIndex
	 *            The index of the array to assign from. null for no array
	 * @param toIndex
	 *            The index of the array to assign to. null for no array
	 * @return The tac quadruple
	 * @throws IntermediateCodeGeneratorException
	 *             something went wrong.
	 */
	public static List<Quadruple> assign(Type typeOfid, String from, String to, Integer fromIndex, Integer toIndex)
			throws IntermediateCodeGeneratorException {

		List<Quadruple> quadruples = new LinkedList<>();
		if (toIndex != null && fromIndex != null) {
			throw new IntermediateCodeGeneratorException("Unsupport assignment type");
		} else if (toIndex != null) {
			switch (typeOfid.getKind()) {
			case DOUBLE:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_SET_DOUBLE, to, "#" + toIndex, from));
				break;
			case LONG:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_SET_LONG, to, "#" + toIndex, from));
				break;
			case BOOLEAN:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_SET_BOOLEAN, to, "#" + toIndex, from));
				break;
			case STRING:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_SET_STRING, to, "#" + toIndex, from));
				break;
			default:
				throw new IntermediateCodeGeneratorException("Unsupport assignment type");
			}
		} else if (fromIndex != null) {
			switch (typeOfid.getKind()) {
			case DOUBLE:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_GET_DOUBLE, from, "#" + fromIndex, to));
				break;
			case LONG:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_GET_LONG, from, "#" + fromIndex, to));
				break;
			case BOOLEAN:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_GET_BOOLEAN, from, "#" + fromIndex, to));
				break;
			case STRING:
				quadruples.add(new QuadrupleImpl(Operator.ARRAY_GET_STRING, from, "#" + fromIndex, to));
				break;
			default:
				throw new IntermediateCodeGeneratorException("Unsupport assignment type");
			}
		} else {
			switch (typeOfid.getKind()) {
			case DOUBLE:
				quadruples.add(new QuadrupleImpl(Operator.ASSIGN_DOUBLE, from, Quadruple.EmptyArgument, to));
				break;
			case LONG:
				quadruples.add(new QuadrupleImpl(Operator.ASSIGN_LONG, from, Quadruple.EmptyArgument, to));
				break;
			case STRING:
				quadruples.add(new QuadrupleImpl(Operator.ASSIGN_STRING, from, Quadruple.EmptyArgument, to));
				break;
			case BOOLEAN:
				quadruples.add(new QuadrupleImpl(Operator.ASSIGN_BOOLEAN, from, Quadruple.EmptyArgument, to));
				break;
			default:
				throw new IntermediateCodeGeneratorException("Unsupport assignment type");
			}
		}
		return quadruples;
	}

	/**
	 * Generate unary minus quadruples for long or double
	 * 
	 * @param typeOfId
	 *            The type of the identifier (long or double)
	 * @param from
	 *            The variable to calculate minus from
	 * @param to
	 *            The result variable
	 * @return The quadruple for unary minus
	 * @throws IntermediateCodeGeneratorException
	 *             something went wrong
	 */
	public static Quadruple unaryMinus(Type typeOfId, String from, String to) throws IntermediateCodeGeneratorException {
		switch (typeOfId.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.SUB_DOUBLE, "#0.0", from, to);
		case LONG:
			return new QuadrupleImpl(Operator.SUB_LONG, "#0", from, to);
		default:
			throw new IntermediateCodeGeneratorException("Unsupport assignment type");
		}
	}

	/**
	 * create a new return Node Quadruple
	 * 
	 * @param identifier
	 *            The identifier to return
	 * @return The quadruple representing the return node
	 */
	public static Quadruple returnNode(String identifier) {
		return new QuadrupleImpl(Quadruple.Operator.RETURN, identifier, Quadruple.EmptyArgument,
				Quadruple.EmptyArgument);
	}

	/**
	 * Create a Quadruple for relation equals
	 * 
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param result
	 *            the result
	 * @param type
	 *            type of values
	 * @return The quadruple representing the equals relation
	 * @throws IntermediateCodeGeneratorException
	 *             illegal quadruple
	 */
	public static Quadruple relationEqual(String left, String right, String result, Type type)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.COMPARE_DOUBLE_E, left, right, result);
		case LONG:
			return new QuadrupleImpl(Operator.COMPARE_LONG_E, left, right, result);
		default:
			String err = "Illegal Relation Equals for Type " + type;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a Quadruple for relation greater
	 * 
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param result
	 *            the result
	 * @param type
	 *            type of values
	 * @return The quadruple representing the greater relation
	 * @throws IntermediateCodeGeneratorException
	 *             illegal quadruple
	 */
	public static Quadruple relationGreater(String left, String right, String result, Type type)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.COMPARE_DOUBLE_G, left, right, result);
		case LONG:
			return new QuadrupleImpl(Operator.COMPARE_LONG_G, left, right, result);
		default:
			String err = "Illegal Relation Greater for Type " + type;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a Quadruple for relation greater equals
	 * 
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param result
	 *            the result
	 * @param type
	 *            type of values
	 * @return The quadruple representing the greater equals relation
	 * @throws IntermediateCodeGeneratorException
	 *             illegal quadruple
	 */
	public static Quadruple relationGreaterEqual(String left, String right, String result, Type type)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.COMPARE_DOUBLE_GE, left, right, result);
		case LONG:
			return new QuadrupleImpl(Operator.COMPARE_LONG_GE, left, right, result);
		default:
			String err = "Illegal Relation GreaterEquals for Type " + type;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a Quadruple for relation less
	 * 
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param result
	 *            the result
	 * @param type
	 *            type of values
	 * @return The quadruple representing the less relation
	 * @throws IntermediateCodeGeneratorException
	 *             illegal quadruple
	 */
	public static Quadruple relationLess(String left, String right, String result, Type type)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.COMPARE_DOUBLE_L, left, right, result);
		case LONG:
			return new QuadrupleImpl(Operator.COMPARE_LONG_L, left, right, result);
		default:
			String err = "Illegal Relation Less for Type " + type;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a Quadruple for relation less equals
	 * 
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param result
	 *            the result
	 * @param type
	 *            type of values
	 * @return The quadruple representing the less equals relation
	 * @throws IntermediateCodeGeneratorException
	 *             illegal quadruple
	 */
	public static Quadruple relationLessEqual(String left, String right, String result, Type type)
			throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case DOUBLE:
			return new QuadrupleImpl(Operator.COMPARE_DOUBLE_LE, left, right, result);
		case LONG:
			return new QuadrupleImpl(Operator.COMPARE_LONG_LE, left, right, result);
		default:
			String err = "Illegal Relation Less Equals for Type " + type;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a Quadruple for the print statement
	 * 
	 * @param value
	 *            The variable to print
	 * @param type
	 *            The type of the variable
	 * @return The quadruple for the print statement
	 * @throws IntermediateCodeGeneratorException
	 *             illegal print
	 */
	public static Quadruple print(String value, Type type) throws IntermediateCodeGeneratorException {
		switch (type.getKind()) {
		case BOOLEAN:
			return new QuadrupleImpl(Operator.PRINT_BOOLEAN, value, Quadruple.EmptyArgument, Quadruple.EmptyArgument);
		case DOUBLE:
			return new QuadrupleImpl(Operator.PRINT_DOUBLE, value, Quadruple.EmptyArgument, Quadruple.EmptyArgument);
		case LONG:
			return new QuadrupleImpl(Operator.PRINT_LONG, value, Quadruple.EmptyArgument, Quadruple.EmptyArgument);
		case STRING:
			return new QuadrupleImpl(Operator.PRINT_STRING, value, Quadruple.EmptyArgument, Quadruple.EmptyArgument);
		default:
			String err = "Cannot print array or struct " + type;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a quadruple for unary NOT operation
	 * 
	 * @param value
	 *            The value to negate
	 * @param result
	 *            The result variable
	 * @return The quadruple
	 */
	public static Quadruple unaryNot(String value, String result) {
		return new QuadrupleImpl(Operator.NOT_BOOLEAN, value, Quadruple.EmptyArgument, result);
	}

	/**
	 * Create a quadruple for boolean arithmetic
	 * 
	 * @param operator
	 *            The boolean operator
	 * @param left
	 *            left value
	 * @param right
	 *            right value
	 * @param result
	 *            result value
	 * @return The quadruple
	 * @throws IntermediateCodeGeneratorException
	 *             Illegal operator given
	 */
	public static Quadruple booleanArithmetic(BinaryOperator operator, String left, String right, String result)
			throws IntermediateCodeGeneratorException {
		switch (operator) {
		case LOGICAL_AND:
			return new QuadrupleImpl(Operator.AND_BOOLEAN, left, right, result);
		case LOGICAL_OR:
			return new QuadrupleImpl(Operator.OR_BOOLEAN, left, right, result);
		default:
			String err = "unsupported logical operator " + operator;
			throw new IntermediateCodeGeneratorException(err);
		}
	}

	/**
	 * Create a quadruple for boolean arithmetic with unary operator
	 * 
	 * @param operator
	 *            the operator
	 * @param from
	 *            the source value
	 * @param to
	 *            the target value
	 * @return the quadruple
	 * @throws IntermediateCodeGeneratorException
	 *             something went wrong
	 */
	public static Quadruple booleanArithmetic(UnaryOperator operator, String from, String to)
			throws IntermediateCodeGeneratorException {
		switch (operator) {
		case LOGICAL_NEGATE:
			return new QuadrupleImpl(Operator.NOT_BOOLEAN, from, Quadruple.EmptyArgument, to);
		default:
			throw new IntermediateCodeGeneratorException("Unsupported binary operator " + operator.toString());
		}
	}

	/**
	 * Create a conditional jump
	 * 
	 * @param condition
	 *            The condition to evaluate
	 * @param trueLabel
	 *            the label to jump to if the condition is true
	 * @param falseLabel
	 *            the label to jump to if the condition is false
	 * @return the quadruple
	 */
	public static Quadruple branch(String condition, String trueLabel, String falseLabel) {
		return new QuadrupleImpl(Operator.BRANCH, trueLabel, falseLabel, condition);
	}

	/**
	 * Add a new label into the TAC
	 * 
	 * @param label
	 *            The label to add
	 * @return The Quadruple
	 */
	public static Quadruple label(String label) {
		return new QuadrupleImpl(Operator.LABEL, label, Quadruple.EmptyArgument, Quadruple.EmptyArgument);
	}

	/**
	 * Add an unconditional jump to the label
	 * 
	 * @param label
	 *            The label to jump to
	 * @return The Quadruple
	 */
	public static Quadruple jump(String label) {
		return new QuadrupleImpl(Operator.BRANCH, label, Quadruple.EmptyArgument, Quadruple.EmptyArgument);
	}
}
