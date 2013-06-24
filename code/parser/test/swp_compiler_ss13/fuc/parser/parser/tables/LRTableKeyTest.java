package swp_compiler_ss13.fuc.parser.parser.tables;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;

public class LRTableKeyTest {
	
	private final LRParserState curState = new LRParserState(1);
	private final LRParserState nextState = new LRParserState(2);
	private final NonTerminal n = new NonTerminal("n");
	private final NonTerminal n2 = new NonTerminal("n2");
	
	@Test
	public void testCtor() {
		LRTableKey key = new LRTableKey(curState, n);
		LRTableKey key2 = new LRTableKey(nextState, n);
		LRTableKey key3 = new LRTableKey(curState, n2);
		
		assertEquals(curState, key.getState());
		assertEquals(n, key.getSymbol());
		assertTrue(key.equals(key));
		assertFalse(key.equals(new Object()));
		assertFalse(key.equals(null));
		
		assertFalse(key.equals(key2));
		assertFalse(key.equals(key3));
		
		try {
			new LRTableKey(null, n);
			fail("Expected a NPE!");
		} catch (NullPointerException err) {
			// Success
		}
		
		try {
			new LRTableKey(curState, null);
			fail("Expected a NPE!");
		} catch (NullPointerException err) {
			// Success
		}
		
		try {
			new LRTableKey(null, null);
			fail("Expected a NPE!");
		} catch (NullPointerException err) {
			// Success
		}
	}
}
