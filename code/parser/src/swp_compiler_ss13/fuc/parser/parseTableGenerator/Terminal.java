package swp_compiler_ss13.fuc.parser.parseTableGenerator;

import swp_compiler_ss13.common.lexer.Token;

public class Terminal extends Symbol {
   
   public static final Terminal EPSILON = new Terminal("");
   
   
	public SymbolType getType() {
		return SymbolType.TERMINAL;
	}
	
	public boolean equalsToken(Token token) {
		return this.getString().equals(token.getValue());
	}
	
	public Terminal(String stringRep) {
		super(stringRep);
	}
	public Terminal(Token token) {
		//doesn't work, getValue returns not stringRep
		super(token.getValue());
		//maybe better solution, but getStringRep must be complete
		//I don't know the names of indizies
		//super(getStringRep(token));
		
		}

	private static String getStringRep(Token token) {
		switch(token.getTokenType()){
		case AND:
			break;
		case ASSIGNOP:
			break;
		case BOOL_SYMBOL: 
			break;
		case BREAK:
			break;
		case COMMENT:
			break;
		case DIVIDE:
			break;
		case DO:
			break;
		case DOUBLE_SYMBOL:
			break;
		case ELSE:
			break;
		case EOF: return "eof";
		case EQUALS:
			break;
		case FALSE:
			break;
		case GREATER:
			break;
		case GREATER_EQUAL:
			break;
		case ID: return "id";
		case IF:
			break;
		case LEFT_BRACE:
			break;
		case LEFT_BRACKET:
			break;
		case LEFT_PARAN:
			break;
		case LESS:
			break;
		case LESS_OR_EQUAL:
			break;
		case LONG_SYMBOL: return "num";
		case MINUS:
			break;
		case NOT:
			break;
		case NOT_A_TOKEN:
			break;
		case NOT_EQUALS:
			break;
		case NUM:
			break;
		case OR:
			break;
		case PLUS:
			break;
		case PRINT:
			break;
		case REAL:
			break;
		case RETURN:
			break;
		case RIGHT_BRACE:
			break;
		case RIGHT_BRACKET:
			break;
		case RIGHT_PARAN:
			break;
		case SEMICOLON:
			break;
		case STRING:
			break;
		case STRING_SYMBOL:
			break;
		case TIMES:
			break;
		case TRUE:
			break;
		case WHILE:
			break;
		default:
			return null;
	}
		return null;
		
	}
}
