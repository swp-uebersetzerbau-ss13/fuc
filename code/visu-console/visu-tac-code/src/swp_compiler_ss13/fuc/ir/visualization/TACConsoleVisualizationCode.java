package swp_compiler_ss13.fuc.ir.visualization;

import java.util.List;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.visualization.TACVisualization;

/**
 * Visualization for Three Address Code
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class TACConsoleVisualizationCode implements TACVisualization {

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

	@Override
	public void visualizeTAC(List<Quadruple> tac) {
		System.out.println("Printing the TAC in Code format.");
		System.out.println("Empty Argument Symbol is \"" + Quadruple.EmptyArgument + "\"");
		System.out.println("");

		String format = "%03d: %s";

		int line = 0;

		for (Quadruple quadruple : tac) {
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

			System.out.println(String.format(format, line++, code));
		}
	}
}
