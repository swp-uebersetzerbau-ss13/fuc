package swp_compiler_ss13.fuc.parser;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

public class ReduceTest {	
	private static Reduce test;
	private static Production production;
	private static int rhsSizeWoEpsilon;
	
	
	@BeforeClass
    public static void setUpEarly() {			 
		test = new Reduce(production);	
		rhsSizeWoEpsilon= 1;
		
	}

	@Test
	public final void testGetProduction() {
	assertTrue( production == test.getProduction());
		 
	}

	 

	 
}
