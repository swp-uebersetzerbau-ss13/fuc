package swp_compiler_ss13.fuc.ir.visualization;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.common.visualization.TACVisualization;

/**
 * Visualization for Three Address Code
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class TACConsoleVisualizationTriple implements TACVisualization {

	@Override
	public void visualizeTAC(List<Quadruple> tac) {

		System.out.println("Printing the TAC in Triple format.");
		System.out.println("Empty Argument Symbol is \"" + Quadruple.EmptyArgument + "\"");
		System.out.println("");

		String format = "%03d: (%s | %s | %s)";
		Map<String, Integer> indirectAddresses = new HashMap<>();

		int line = 0;

		for (Quadruple quadruple : tac) {
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

			System.out.println(String.format(format, line, operator, argument1, argument2));
			line++;
		}
	}
}
