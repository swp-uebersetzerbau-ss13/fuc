package swp_compiler_ss13.fuc.ir.visualization;

import java.util.List;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.visualization.TACVisualization;

/**
 * Visualization for Three Address Code
 * 
 * @author "Frank Zechert"
 * @version 1
 */
public class TACConsoleVisualizationQuadruple implements TACVisualization {

	@Override
	public void visualizeTAC(List<Quadruple> tac) {
		System.out.println("Printing the TAC in Quadruple format.");
		System.out.println("Empty Argument Symbol is \"" + Quadruple.EmptyArgument + "\"");
		System.out.println("");

		String format = "%03d: (%s | %s | %s | %s)";
		int line = 0;

		for (Quadruple quadruple : tac) {
			String operator = quadruple.getOperator().toString();
			String argument1 = quadruple.getArgument1();
			String argument2 = quadruple.getArgument2();
			String result = quadruple.getResult();
			System.out.println(String.format(format, line++, operator, argument1, argument2, result));
		}
	}
}
