package swp_compiler_ss13.fuc.parser.parser.states;

import static org.junit.Assert.*;

import org.junit.Test;

public class LRParserStateTest {
	@Test
	public void testParserState() {
		LRParserState state = new LRParserState(0);
		assertEquals(0, state.getId());
		
		assertTrue(state.equals(state));		
		assertTrue(state.equals(new LRParserState(0)));
		assertFalse(state.equals(new Object()));
		assertFalse(state.equals(null));
	}
	@Test
	public void testErrorState() {
		final String MSG = "Hello, World!";
		LRErrorState state = new LRErrorState(MSG);
		assertEquals(LRErrorState.ERROR_ID, state.getId());
		
		assertTrue(state.equals(state));		
		assertTrue(state.equals(new LRErrorState("Another message")));
		assertFalse(state.equals(new Object()));
		assertFalse(state.equals(null));
		assertEquals(MSG, state.toString());
	}
}
