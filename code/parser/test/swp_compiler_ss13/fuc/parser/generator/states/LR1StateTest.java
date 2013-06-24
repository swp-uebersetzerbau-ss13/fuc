package swp_compiler_ss13.fuc.parser.generator.states;

import static org.junit.Assert.*;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.terminals.EfficientTerminalSet;
import swp_compiler_ss13.fuc.parser.generator.terminals.ITerminalSet;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;

public class LR1StateTest {
	public static final Production prod = new Production(0, NonTerminal.StartLHS, Terminal.EOF);
	public static final LR0Item kernel = new LR0Item(prod, 0);
	public static final Grammar grammar = new ProjectGrammar.Complete().getGrammar();
	public static final ITerminalSet lookaheads = new EfficientTerminalSet(grammar.getTerminals());
	public static final LR1Item lr1Item = new LR1Item(kernel, lookaheads);
	public static final LR1State state = new LR1State(lr1Item);
	
	@Test
	public void testLR1State() {
		assertEquals(lr1Item, state.getFirstItem());
		assertEquals(kernel, state.getKernel().getFirstItem());
		assertFalse(state.equals(new Object()));
		assertFalse(state.equals(null));
		assertTrue(state.equals(state));
		assertEquals(1, state.getItemsCount());
	}
}
