package swp_compiler_ss13.fuc.parser.generator;

import swp_compiler_ss13.fuc.parser.generator.automaton.Dfa;
import swp_compiler_ss13.fuc.parser.generator.items.LR1Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR1State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;

public class LR1Generator extends ALRGenerator<LR1Item, LR1State>{

	public LR1Generator(Grammar grammar) throws RuntimeException {
		super(grammar);
		// TODO Auto-generated constructor stub
	}
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------

	@Override
	public Dfa<LR1Item, LR1State> createDFA() {
		// TODO Auto-generated method stub
		return null;
	}
   
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   
   
   // --------------------------------------------------------------------------
   // --- getter/setter --------------------------------------------------------
   // --------------------------------------------------------------------------
}
