package swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry;

public interface ParseTable {
	
	public ParseTableEntry getEntry(int state, Token symbol) throws StateOutOfBoundsException, TokenNotFoundException;
	public class StateOutOfBoundsException extends Exception {
		public StateOutOfBoundsException() { super(); };
		public StateOutOfBoundsException(String message) { super(message); };
		private static final long serialVersionUID = 1L;
	}
	
	public class TokenNotFoundException extends Exception {
		public TokenNotFoundException() { super(); };
		public TokenNotFoundException(String message) { super(message); };
		private static final long serialVersionUID = 1L;
	};

}
