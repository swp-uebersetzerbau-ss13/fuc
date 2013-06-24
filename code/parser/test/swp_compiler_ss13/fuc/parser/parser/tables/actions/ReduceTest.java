package swp_compiler_ss13.fuc.parser.parser.tables.actions;

import static org.junit.Assert.assertTrue;

import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.grammar.Production;

public class ReduceTest {	
	private static Reduce test;
	private static Production production;
//	private static int rhsSizeWoEpsilon;
	
	
	@BeforeClass
    public static void setUpEarly() {			 
		test = new Reduce(production);	
//		rhsSizeWoEpsilon= 1;
		
	}

	@Test
	public final void testGetProduction() {
	assertTrue( production == test.getProduction());
		 
	}

	 

	 
}
