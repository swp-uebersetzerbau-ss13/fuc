package swp_compiler_ss13.fuc.parser.grammar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.grammar.Symbol.SymbolType;

public class SymbolTest {
	@Test
	public void testSymbolWithTerminal() {
		Terminal t = new Terminal("t");
		assertEquals("t", t.getId());
		assertEquals(SymbolType.TERMINAL, t.getType());
		assertEquals(true, t.isTerminal());
		assertEquals(false, t.isNonTerminal());
		assertTrue(t.equals(t));
		assertFalse(t.equals(null));
		assertFalse(t.equals(new Object()));
		assertFalse(t.equals(new Terminal("tt")));
		assertTrue(t.equals(new Terminal("t")));
		assertFalse(t.equals(new NonTerminal("t")));
		assertEquals(t.getId(), t.toString());
	}

	@Test
	public void testSymbolWithNonTerminal() {
		NonTerminal n = new NonTerminal("t");
		assertEquals("t", n.getId());
		assertEquals(SymbolType.NONTERMINAL, n.getType());
		assertEquals(false, n.isTerminal());
		assertEquals(true, n.isNonTerminal());
		assertTrue(n.equals(n));
		assertFalse(n.equals(null));
		assertFalse(n.equals(new Object()));
		assertFalse(n.equals(new NonTerminal("tt")));
		assertTrue(n.equals(new NonTerminal("t")));
		assertFalse(n.equals(new Terminal("t")));
		assertEquals(n.getId(), n.toString());
	}

	@Test
	public void testSymbolCtor() {
		try {
			new Symbol(null, "s") {
			};
			fail("NPE expected!");
		} catch (NullPointerException err) {
			// Success
		}
		
		try {
			new Symbol(SymbolType.TERMINAL, null) {
			};
			fail("NPE expected!");
		} catch (NullPointerException err) {
			// Success
		}
		
		try {
			new Symbol(null, null) {
			};
			fail("NPE expected!");
		} catch (NullPointerException err) {
			// Success
		}
	}
}
