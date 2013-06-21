package swp_compiler_ss13.fuc.parser.parser.tables;

import static org.junit.Assert.*;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRErrorState;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;

public class LRGotoTableTest {
	
	private final LRParserState curState = new LRParserState(1);
	private final LRParserState nextState = new LRParserState(2);
	private final LRParserState nextState2 = new LRParserState(3);
	private final NonTerminal n = new NonTerminal("n");
	
	@Test
	public void testGotoTable() {
		LRGotoTable table = new LRGotoTable();
		
		// Produce LRErrorState
		LRParserState newState0 = table.get(curState, n);
		assertEquals(LRErrorState.ERROR_ID, newState0.getId());
		assertTrue(newState0.isErrorState());
		
		// Set nextState
		try {
			table.set(nextState, curState, n);
		} catch (DoubleEntryException err) {
			fail("Should not throw an error here!");
		}
		LRParserState newState1 = table.get(curState, n);
		assertEquals(nextState.getId(), newState1.getId());
		assertFalse(newState1.isErrorState());
		
		// Set nextState
		try {
			table.set(nextState, curState, n);
			fail("Expected DoubleEntryException!");
		} catch (DoubleEntryException err) {
			// Success
		}
		
		// Try setting nextState2, with same key
		try {
			table.set(nextState2, curState, n);
			fail("Expected DoubleEntryException!");
		} catch (DoubleEntryException err) {
			// Success
		}
	}
}
