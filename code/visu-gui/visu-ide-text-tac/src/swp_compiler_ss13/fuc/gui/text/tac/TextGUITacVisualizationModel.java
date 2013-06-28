package swp_compiler_ss13.fuc.gui.text.tac;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.backend.Quadruple.Operator;
import swp_compiler_ss13.fuc.gui.text.ColorWrapper;
import swp_compiler_ss13.fuc.gui.text.StringColourPair;
import swp_compiler_ss13.fuc.gui.text.StringColourPair.DefaultColorWrapper;
import swp_compiler_ss13.fuc.gui.text.Text_Controller;
import swp_compiler_ss13.fuc.gui.text.Text_Model;

/**
 * @author "Eduard Wolf"
 * 
 */
public class TextGUITacVisualizationModel extends Text_Model {

	private final Map<Operator, ColorWrapper> tacColor;

	public TextGUITacVisualizationModel(Text_Controller controller) {
		super(controller, ModelType.TAC);
		tacColor = new HashMap<>();
		ColorWrapper wrapper;
		for (Operator type : Operator.values()) {
			switch (type) {
			case ADD_DOUBLE:
			case ADD_LONG:
			case SUB_DOUBLE:
			case SUB_LONG:
			case MUL_DOUBLE:
			case MUL_LONG:
			case DIV_DOUBLE:
			case DIV_LONG:
				wrapper = DefaultColorWrapper.CYAN;
				break;
			case AND_BOOLEAN:
			case OR_BOOLEAN:
			case NOT_BOOLEAN:
			case COMPARE_DOUBLE_E:
			case COMPARE_DOUBLE_G:
			case COMPARE_DOUBLE_GE:
			case COMPARE_DOUBLE_L:
			case COMPARE_DOUBLE_LE:
			case COMPARE_LONG_E:
			case COMPARE_LONG_G:
			case COMPARE_LONG_GE:
			case COMPARE_LONG_L:
			case COMPARE_LONG_LE:
				wrapper = DefaultColorWrapper.YELLOW;
				break;
			case ASSIGN_BOOLEAN:
			case ASSIGN_DOUBLE:
			case ASSIGN_LONG:
			case ASSIGN_STRING:
				wrapper = DefaultColorWrapper.GREEN;
				break;
			case ARRAY_GET_BOOLEAN:
			case ARRAY_GET_DOUBLE:
			case ARRAY_GET_LONG:
			case ARRAY_GET_REFERENCE:
			case ARRAY_GET_STRING:
				wrapper = DefaultColorWrapper.ORANGE;
				break;
			case ARRAY_SET_BOOLEAN:
			case ARRAY_SET_DOUBLE:
			case ARRAY_SET_LONG:
			case ARRAY_SET_STRING:
				wrapper = DefaultColorWrapper.ORANGE;
				break;
			case DECLARE_ARRAY:
			case DECLARE_BOOLEAN:
			case DECLARE_DOUBLE:
			case DECLARE_LONG:
			case DECLARE_REFERENCE:
			case DECLARE_STRING:
				wrapper = DefaultColorWrapper.BLUE;
				break;
			case LABEL:
			case BRANCH:
			case RETURN:
				wrapper = DefaultColorWrapper.RED;
				break;

			case DOUBLE_TO_LONG:
			case LONG_TO_DOUBLE:
				wrapper = DefaultColorWrapper.GRAY;
				break;
			case PRINT_STRING:
			default:
				wrapper = DefaultColorWrapper.BLACK;
				break;
			}
			tacColor.put(type, wrapper);
		}
	}

	@Override
	protected StringColourPair tacToViewInformation(Quadruple quadruple) {
		if (quadruple == null) {
			return new StringColourPair();
		}
		String format = "(%s | %s | %s | %s)\n";
		Operator operator = quadruple.getOperator();
		String text = String.format(format, operator.toString(), quadruple.getArgument1(),
				quadruple.getArgument2(), quadruple.getResult());
		return new StringColourPair().setText(text).setColor(tacColor.get(operator));

	}

}
