package swp_compiler_ss13.fuc.parser.generator.automaton;

import static org.junit.Assert.*;

import java.util.List;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.generator.states.LR1StateTest;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class DfaTest {
	
	private final Terminal t = new Terminal("t");
	
	@Test
	public void testDfa() {
		Dfa<LR1Item, LR1State> dfa = new Dfa<>(LR1StateTest.state);
		LR0Item srcItem = LR1StateTest.lr1Item.getLR0Kernel();
		DfaEdge<LR1State> edge = new DfaEdge<>(dfa.getStartState(), t, dfa.getStartState(), srcItem);
		dfa.getEdges().add(edge);
		
		List<DfaEdge<LR1State>> edgesFrom = dfa.getEdgesFrom(dfa.getStartState());
		assertEquals(1, edgesFrom.size());
		assertEdge(edge, edgesFrom.get(0));
		
		List<DfaEdge<LR1State>> edgesTo = dfa.getEdgesTo(dfa.getStartState());
		assertEquals(1, edgesTo.size());
		assertEdge(edge, edgesTo.get(0));
		
		assertFalse(edge.equals(null));
		assertFalse(edge.equals(new Object()));
		assertTrue(edge.equals(edge));
		
		assertFalse(new DfaEdge<>(null, t, dfa.getStartState(), srcItem).equals(new DfaEdge<>(dfa.getStartState(), t, dfa.getStartState(), srcItem)));
	}
	
	private static void assertEdge(DfaEdge<LR1State> expected, DfaEdge<LR1State> actual) {
		assertEquals(expected.getDst(), actual.getDst());
		assertEquals(expected.getSrc(), actual.getSrc());
		assertEquals(expected.getSrcItem(), actual.getSrcItem());
		assertEquals(expected.getSymbol(), actual.getSymbol());
		assertEquals(expected.isDestAccepting(), actual.isDestAccepting());
	}
}
