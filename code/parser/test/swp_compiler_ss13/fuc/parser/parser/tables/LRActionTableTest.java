package swp_compiler_ss13.fuc.parser.parser.tables;

import static org.junit.Assert.*;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parser.tables.actions.Accept;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Error;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction;

public class LRActionTableTest {
	
	private final LRParserState curState = new LRParserState(1);
	private final LRParserState nextState = new LRParserState(2);
	private final Terminal curTerminal = new Terminal("n");
	
	@Test
	public void testLRActionTable() {
		LRActionTable table = new LRActionTable();
		
		// get: Error action
		ALRAction action = table.get(curState, curTerminal);
		assertTrue(action instanceof Error);
		
		// getWithNull: null
		ALRAction actionNull = table.getWithNull(curState, curTerminal);
		assertNull(actionNull);
		
		// set Shift action: ok
		Shift shift = new Shift(nextState);
		try {
			table.set(shift, curState, curTerminal);
		} catch (DoubleEntryException err) {
			fail("Don't expected a DoubleEntryException here!");
		}
		
		// set same Shift action: exception
		try {
			table.set(shift, curState, curTerminal);
			fail("Expected a DoubleEntryException here!");
		} catch (DoubleEntryException err) {
			// Success
		}
		
		// set Reduce action: exception
		Reduce reduce = new Reduce(new Production(0, NonTerminal.StartLHS, Terminal.EOF));
		try {
			table.set(reduce, curState, curTerminal);
			fail("Expected a DoubleEntryException here!");
		} catch (DoubleEntryException err) {
			// Success
		}
		
		// get: same Shift action we set before
		ALRAction action1 = table.get(curState, curTerminal);
		assertTrue(action1 instanceof Shift);
		Shift oldShift = (Shift) action1;
		assertEquals(shift, oldShift);
		
		// getWithNull: same Shift action we set before
		ALRAction action2 = table.getWithNull(curState, curTerminal);
		assertTrue(action2 instanceof Shift);
		Shift oldShift2 = (Shift) action2;
		assertEquals(shift, oldShift2);
		
		// setHard: overwrite Shift with Reduce
		table.setHard(reduce, curState, curTerminal);
		
		// get: same Reduce action we set before
		ALRAction action3 = table.get(curState, curTerminal);
		assertTrue(action3 instanceof Reduce);
		Reduce oldReduce1 = (Reduce) action3;
		assertEquals(reduce, oldReduce1);
		
		// getWithNull: same Reduce action we set before
		ALRAction action4 = table.getWithNull(curState, curTerminal);
		assertTrue(action4 instanceof Reduce);
		Reduce oldReduce2 = (Reduce) action4;
		assertEquals(reduce, oldReduce2);
		
		// setHard: overwrite Shift with Reduce
		Accept accept = new Accept();
		table.setHard(accept, curState, curTerminal);
		
		// get: same Accept action we set before
		ALRAction action5 = table.get(curState, curTerminal);
		assertTrue(action5 instanceof Accept);
		Accept oldAccept1 = (Accept) action5;
		assertEquals(accept, oldAccept1);
		
		// getWithNull: same Reduce action we set before
		ALRAction action6 = table.getWithNull(curState, curTerminal);
		assertTrue(action6 instanceof Accept);
		Accept oldAccept2 = (Accept) action6;
		assertEquals(accept, oldAccept2);
	}
}
