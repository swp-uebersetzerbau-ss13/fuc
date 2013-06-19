package swp_compiler_ss13.fuc.parser.parser.tables.actions;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;


public class ShiftTest {
	private static  LRParserState testNewState;	
	private static Shift test;
	 
	
	@BeforeClass
    public static void setUpEarly() {
		testNewState = new LRParserState(1);
		test = new Shift(testNewState);
		
	}

	@Test
	public final void testGetNewState() {		
		assertTrue(testNewState==test.getNewState());
		
	}

}
