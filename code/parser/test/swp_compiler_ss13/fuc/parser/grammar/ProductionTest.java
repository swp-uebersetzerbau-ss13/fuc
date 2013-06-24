package swp_compiler_ss13.fuc.parser.grammar;

import java.util.Arrays;
import java.util.Collections;

import org.junit.Test;
import static org.junit.Assert.*;

import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class ProductionTest {

	/**
	 * A production with empty rhs (N -> _) results in a single ε on the rhs (N -> ε)
	 */
	@Test
	public void testCtor_EmptyRHS() {
		Production prod = new Production(0, NonTerminal.StartLHS, Collections.<Symbol>emptyList());
		assertEquals(0, prod.getID());
		assertEquals(Terminal.Epsilon, prod.getLastTerminal());
		assertEquals(NonTerminal.StartLHS, prod.getLHS());
		assertEquals(Arrays.<Symbol>asList(Terminal.Epsilon), prod.getRHS());
		assertEquals(0, prod.getRHSSize());
		assertEquals(NonTerminal.StartLHS + " -> ε", prod.getStringRep());
		assertEquals(NonTerminal.StartLHS + " → . ε", prod.toString(0));
	}

	/**
	 * A production with (N -> ..ε...) results in an {@link IllegalArgumentException}
	 */
	@Test
	public void testCtor_WrongEpsilon() {
		try {
			@SuppressWarnings("unused")
			Production prod = new Production(42, NonTerminal.StartLHS, NonTerminal.StartLHS, Terminal.Epsilon, Terminal.EOF);
			fail("Expected an IllegalArgumentException with an epsilon not being the only Symbol on a right hand side");
		} catch (IllegalArgumentException err) {
			assertEquals("Epsilon is only allowed as the single symbol of a RHS!", err.getMessage());
		}
	}

	/**
	 * A production with (N -> N EOF)
	 */
	@Test
	public void testCtor_Normal() {
		Production prod = new Production(42, NonTerminal.StartLHS, NonTerminal.StartLHS, Terminal.EOF);
		assertEquals(42, prod.getID());
		assertEquals(Terminal.EOF, prod.getLastTerminal());
		assertEquals(NonTerminal.StartLHS, prod.getLHS());
		assertEquals(Arrays.<Symbol>asList(NonTerminal.StartLHS, Terminal.EOF), prod.getRHS());
		assertEquals(2, prod.getRHSSize());
		assertEquals(NonTerminal.StartLHS + " -> " + NonTerminal.StartLHS + " " + Terminal.EOF, prod.getStringRep());
		assertEquals(NonTerminal.StartLHS + " → . " + NonTerminal.StartLHS + " " + Terminal.EOF, prod.toString(0));
		assertEquals(NonTerminal.StartLHS + " → " + NonTerminal.StartLHS + " " + Terminal.EOF + " .", prod.toString(2));
	}

	/**
	 * Test all possible branches of {@link Production#equals(Object)}
	 */
	@Test
	public void testEqualsBranches() {
		Production prod = new Production(42, NonTerminal.StartLHS, NonTerminal.StartLHS, Terminal.EOF);
		assertFalse(prod.equals(new Object()));
		assertFalse(prod.equals(new Production(42, new NonTerminal("X"), NonTerminal.StartLHS, Terminal.EOF)));
		assertFalse(prod.equals(new Production(42, NonTerminal.StartLHS, NonTerminal.StartLHS, Terminal.EOF, Terminal.EOF)));
	}
}
