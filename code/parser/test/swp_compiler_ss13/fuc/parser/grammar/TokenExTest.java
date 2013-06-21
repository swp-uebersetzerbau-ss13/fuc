package swp_compiler_ss13.fuc.parser.grammar;

import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.grammar.TokenEx;

public class TokenExTest {
	static Token token;
	static Terminal terminal;
	static TokenEx test1;
	static Grammar grammar;
	static TokenType t;
	static String testText = "test";
	
	@BeforeClass
    public static void setUpEarly() {
		 
		terminal = new Terminal(testText,t);		
		test1 = new TokenEx(token, terminal);		 
	 
	}

	@Test
	public final void testGetToken() {
		System.out.println(test1.getToken());
		assertNull(test1.getToken());
	}

	@Test
	public final void testGetTerminal() {
		//assertNull(test1.getTerminal());
		System.out.println(test1.getTerminal()); 
		 
	}

	@Test
	public final void testGetValue() {
		//System.out.println(test1.getValue());
		//assertNull(test1.getValue().toString() );
	}

	@Test
	public final void testGetTokenType() {
		//assertNull(test1.getTokenType());
		
		//System.out.println(test1.getTokenType());
		 
	}

	@Test
	public final void testGetLine() {
		//assertNull(test1.getLine());
		//System.out.println(test1.getLine());
	}

	@Test
	public final void testGetColumn() {
		//assertNull(test1.getColumn());
		//System.out.println(test1.getColumn());
	}

}
