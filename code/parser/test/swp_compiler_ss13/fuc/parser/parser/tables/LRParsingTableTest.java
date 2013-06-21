package swp_compiler_ss13.fuc.parser.parser.tables;

import static org.junit.Assert.assertEquals;

import java.util.LinkedHashMap;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.generator.states.AState;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.generator.states.LR1StateTest;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;

public class LRParsingTableTest {
	
	private final LRParserState curState = new LRParserState(1);
	
	@Test
	public void testLRParsingTable() {
		LinkedHashMap<LRParserState, AState<?>> states = new LinkedHashMap<>();
		
		// Construct LRState
		LR1State state = LR1StateTest.state;
		states.put(curState, state);
		
		LRParsingTable table = new LRParsingTable(states);
		assertEquals(states.size(), table.getStatesCount());
		assertEquals(curState, table.getStartState());
		assertEquals(state, table.getGenState(curState));
		assertEquals(state, table.getGenStateEnries().next().getValue());
		assertEquals(curState, table.getGenStates().next());
	}
}
