package swp_compiler_ss13.fuc.parser.generator;

import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR0State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;

public class ALRGeneratorTest {
	// Generate parsing table
	Grammar grammar = new ProjectGrammar.Complete().getGrammar();
	ALRGenerator<LR0Item, LR0State> generator = new LR0Generator(grammar);
	LRParsingTable table = generator.getParsingTable();
	

	@Test
	public final void testCreateDFA() {
	assertNotNull(generator.createDFA().toString());
		 
	}

	@Test
	public final void testGetGrammarInfo() {
		assertNotNull(generator.getGrammarInfo().toString());
	}

	@Test
	public final void testGetParsingTable() {
		assertNotNull(generator.getParsingTable().toString());
	}

}
