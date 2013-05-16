package swp_compiler_ss13.fuc.ir.visualization;

import java.io.PrintStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;

/**
 * Visualization for Three Address Code
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class TACConsoleVisualization {

	private Quadruple[] tac;

	/**
	 * Create a new Visualization from the tac
	 * 
	 * @param tac
	 *            The tac to visualize
	 */
	public TACConsoleVisualization(Quadruple[] tac) {
		this.tac = tac;
	}

	/**
	 * Create a new Visualization from the tac
	 * 
	 * @param tac
	 *            The tac to visualize
	 */
	public TACConsoleVisualization(List<Quadruple> tac) {
		this(tac.toArray(new Quadruple[0]));
	}

	/**
	 * Print the Three Address Code in Quadruple format to standard out
	 */
	public void printQuadruples() {
		this.printQuadruples(System.out);
	}

	/**
	 * Print the Three Address Code in Quadruple format to the given PrintStream
	 * 
	 * @param out
	 *            The PrintStream to write the output to
	 */
	public void printQuadruples(PrintStream out) {

		out.println("Printing the TAC in Quadruple format.");
		out.println("Empty Argument Symbol is \"" + Quadruple.EmptyArgument + "\"");
		out.println("");

		String format = "%03d: (%s | %s | %s | %s)";
		int line = 0;

		for (Quadruple quadruple : this.tac) {
			String operator = quadruple.getOperator().toString();
			String argument1 = quadruple.getArgument1();
			String argument2 = quadruple.getArgument2();
			String result = quadruple.getResult();
			out.println(String.format(format, line++, operator, argument1, argument2, result));
		}
	}

	/**
	 * Print the Three Address Code in Triples format to standard out
	 */
	public void printTriples() {
		this.printTriples(System.out);
	}

	/**
	 * Print the Three Address Code in Triples format to the given PrintStream
	 * 
	 * @param out
	 *            The PrintStream to write the output to
	 */
	public void printTriples(PrintStream out) {

		out.println("Printing the TAC in Triple format.");
		out.println("Empty Argument Symbol is \"" + Quadruple.EmptyArgument + "\"");
		out.println("");

		String format = "%03d: (%s | %s | %s)";
		Map<String, Integer> indirectAddresses = new HashMap<>();

		int line = 0;

		for (Quadruple quadruple : this.tac) {
			String operator = quadruple.getOperator().toString();
			String argument1 = quadruple.getArgument1();
			String argument2 = quadruple.getArgument2();
			String result = quadruple.getResult();
			Operator openum = quadruple.getOperator();

			if (openum == Operator.DECLARE_BOOLEAN ||
					openum == Operator.DECLARE_DOUBLE ||
					openum == Operator.DECLARE_LONG ||
					openum == Operator.DECLARE_STRING) {
				continue;
			}

			if (openum == Operator.ASSIGN_BOOLEAN ||
					openum == Operator.ASSIGN_DOUBLE ||
					openum == Operator.ASSIGN_LONG ||
					openum == Operator.ASSIGN_STRING) {
				indirectAddresses.put(result, indirectAddresses.get(argument1));
				continue;
			}

			if (!argument1.startsWith("#") && indirectAddresses.get(argument1) != null) {
				argument1 = "(" + indirectAddresses.get(argument1) + ")";
			}
			if (!argument2.startsWith("#") && indirectAddresses.get(argument2) != null) {
				argument2 = "(" + indirectAddresses.get(argument2) + ")";
			}

			indirectAddresses.put(result, line);

			out.println(String.format(format, line, operator, argument1, argument2));
			line++;
		}
	}

	/**
	 * Print the Three Address Code as Code to standard out
	 */
	public void printCode() {
		this.printCode(System.out);
	}

	/**
	 * Print the Three Address Code as Code to the given PrintStream
	 * 
	 * @param out
	 *            The PrintStream to write the output to
	 */
	public void printCode(PrintStream out) {

		out.println("Printing the TAC in Code format.");
		out.println("Empty Argument Symbol is \"" + Quadruple.EmptyArgument + "\"");
		out.println("");

		String format = "%03d: %s";

		int line = 0;

		for (Quadruple quadruple : this.tac) {
			StringBuilder code = new StringBuilder();

			int emptyArgs = 0;
			if (quadruple.getArgument1().equals(Quadruple.EmptyArgument)) {
				emptyArgs++;
			}
			if (quadruple.getArgument2().equals(Quadruple.EmptyArgument)) {
				emptyArgs++;
			}
			if (quadruple.getResult().equals(Quadruple.EmptyArgument)) {
				emptyArgs++;
			}

			if (emptyArgs == 0) {
				// this quadruple uses all 4 addresses in the format
				// <result> = <arg1> <op> <arg2>
				code.append(quadruple.getResult()).append(" = ");
				code.append(quadruple.getArgument1());
				code.append(this.getOperationSign(quadruple.getOperator()));
				code.append(quadruple.getArgument2());
			}

			if (emptyArgs == 1) {
				// One argument is empty (3 addresses used). The format is
				// <result> = <op> <arg1>
				code.append(quadruple.getResult()).append(" =");
				code.append(this.getOperationSign(quadruple.getOperator()));
				code.append(quadruple.getArgument1());
			}

			if (emptyArgs == 2) {
				// One argument and the result are empty (2 addresses used). The
				// format is
				// <op> <arg1>
				// this is only the case for the return statement where
				// <op> = return and <arg1> = identifier
				// and the declare statement where the format is <op> <result>
				code.append(this.getOperationSign(quadruple.getOperator()));
				if (quadruple.getArgument1() != Quadruple.EmptyArgument) {
					code.append(quadruple.getArgument1());
				}
				else {
					code.append(quadruple.getResult());
				}
			}

			out.println(String.format(format, line++, code));
		}
	}

	/**
	 * Convert the given operator into a string that represents the operator.
	 * E.g. converts Operator.ADD_LONG to "+"
	 * 
	 * @param operator
	 *            The operator to convert into a string
	 * @return The String that represents the operator
	 */
	private String getOperationSign(Operator operator) {
		switch (operator) {
		case ADD_DOUBLE:
		case ADD_LONG:
			return " + ";
		case ASSIGN_BOOLEAN:
		case ASSIGN_DOUBLE:
		case ASSIGN_LONG:
		case ASSIGN_STRING:
			return " ";
		case DECLARE_BOOLEAN:
			return "boolean ";
		case DECLARE_DOUBLE:
			return " double ";
		case DECLARE_LONG:
			return "long ";
		case DECLARE_STRING:
			return "string ";
		case DIV_DOUBLE:
		case DIV_LONG:
			return " / ";
		case DOUBLE_TO_LONG:
			return " (d_to_l) ";
		case LONG_TO_DOUBLE:
			return " (l_to_d) ";
		case MUL_DOUBLE:
		case MUL_LONG:
			return " * ";
		case RETURN:
			return "return ";
		case SUB_DOUBLE:
		case SUB_LONG:
			return " - ";
		default:
			return "???";
		}
	}
}
