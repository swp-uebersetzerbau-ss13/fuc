package swp_compiler_ss13.fuc.gui.text.token;

import java.util.HashMap;
import java.util.Map;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.gui.text.ColorWrapper;
import swp_compiler_ss13.fuc.gui.text.StringColourPair;
import swp_compiler_ss13.fuc.gui.text.StringColourPair.DefaultColorWrapper;
import swp_compiler_ss13.fuc.gui.text.Text_Controller;
import swp_compiler_ss13.fuc.gui.text.Text_Model;

/**
 * @author "Eduard Wolf"
 *
 */
public class TextGUITokenVisualizationModel extends Text_Model {

	private final Map<TokenType, ColorWrapper> tokenColor;

	public TextGUITokenVisualizationModel(Text_Controller controller) {
		super(controller, ModelType.TOKEN);
		tokenColor = new HashMap<>();
		ColorWrapper wrapper;
		for (TokenType type : TokenType.values()) {
			switch (type) {
			case AND:
			case OR:
			case NOT:
			case NOT_EQUALS:
			case EQUALS:
			case GREATER:
			case GREATER_EQUAL:
			case LESS:
			case LESS_OR_EQUAL:
				wrapper = DefaultColorWrapper.BLUE;
				break;
			case ASSIGNOP:
				wrapper = DefaultColorWrapper.GREEN;
				break;
			case BOOL_SYMBOL:
			case DOUBLE_SYMBOL:
			case RECORD_SYMBOL:
			case LONG_SYMBOL:
				wrapper = DefaultColorWrapper.ORANGE;
				break;
			case BREAK:
			case WHILE:
			case DO:
			case IF:
			case ELSE:
			case RETURN:
			case PRINT:
				wrapper = DefaultColorWrapper.YELLOW;
				break;
			case NUM:
			case REAL:
			case STRING:
			case TRUE:
			case FALSE:
				wrapper = DefaultColorWrapper.RED;
				break;
			case COMMENT:
			case EOF:
			case NOT_A_TOKEN:
				wrapper = DefaultColorWrapper.GRAY;
				break;
			case DIVIDE:
			case TIMES:
			case PLUS:
			case MINUS:
				wrapper = DefaultColorWrapper.CYAN;
				break;
			case ID:
			case DOT:
				wrapper = DefaultColorWrapper.ORANGE;
				break;
			case LEFT_BRACE:
			case LEFT_BRACKET:
			case RIGHT_BRACE:
			case RIGHT_BRACKET:
			case LEFT_PARAN:
			case RIGHT_PARAN:
			case SEMICOLON:
			default:
				wrapper = DefaultColorWrapper.BLACK;
				break;
			}
			tokenColor.put(type, wrapper);
		}
	}

	@Override
	protected StringColourPair tokenToViewInformation(Token token) {
		if (token == null) {
			return new StringColourPair();
		}
		String text = "";
		if (token.getTokenType() == TokenType.NUM || token.getTokenType() == TokenType.REAL
				|| token.getTokenType() == TokenType.ID) {
			text += "<" + token.getTokenType() + ", " + token.getValue() + ">\n";
		} else {
			text += "<" + token.getValue() + ">\n";
		}
		return new StringColourPair().setText(text).setColor(tokenColor.get(token.getTokenType()));
	}

}
