package swp_compiler_ss13.fuc.parser.generator;

import static org.junit.Assert.assertEquals;
import static swp_compiler_ss13.fuc.parser.grammar.Terminal.EOF;
import static swp_compiler_ss13.fuc.parser.grammar.Terminal.Epsilon;

import java.util.Map;

import junit.extensions.PA;

import org.junit.Test;

import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.fuc.parser.grammar.GrammarSpec;
import swp_compiler_ss13.fuc.parser.grammar.NonTerminal;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Symbol;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.LRActionTable;
import swp_compiler_ss13.fuc.parser.parser.tables.LRGotoTable;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
import swp_compiler_ss13.fuc.parser.parser.tables.LRTableKey;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Accept;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

public class LR0GeneratorTest extends GrammarSpec {
	public static final Terminal plus = new Terminal("plus", TokenType.PLUS);
	public static final Terminal mult = new Terminal("mult", TokenType.TIMES);
	public static final Terminal lb = new Terminal("(", TokenType.LEFT_PARAN);
	public static final Terminal rb = new Terminal(")", TokenType.RIGHT_PARAN);
	public static final Terminal id = new Terminal("id", TokenType.ID);
	
	public static final NonTerminal E = new NonTerminal("E");
	public static final NonTerminal T = new NonTerminal("T");
	public static final NonTerminal Epr = new NonTerminal("Epr");
	public static final NonTerminal Tpr = new NonTerminal("Tpr");
	public static final NonTerminal F = new NonTerminal("F");
	
	public static final Production e1 = new Production(1, E, T, Epr);
	public static final Production epr1 = new Production(2, Epr, plus, T, Epr);
	public static final Production epr2 = new Production(3, Epr, Epsilon);
	public static final Production t1 = new Production(4, T, F, Tpr);
	public static final Production tpr1 = new Production(5, Tpr, mult, F, Tpr);
	public static final Production tpr2 = new Production(6, Tpr, Epsilon);
	public static final Production f1 = new Production(7, F, lb, E, rb);
	public static final Production f2 = new Production(8, F, id);
	
	@Test
	public void testLR0Generator() throws Exception {
		LR0Generator gen = new LR0Generator(getGrammar());
		LRParsingTable table = gen.getParsingTable();
		LRActionTable lrActions = table.getActionTable();
		LRGotoTable lrGoto = table.getGotoTable();
		
		// Check ACTION table
		@SuppressWarnings("unchecked")
		Map<LRTableKey, ALRAction> actionTable = (Map<LRTableKey, ALRAction>) PA.getValue(lrActions, "table");
		assertEquals(42, actionTable.size());
		
		assertActionEntry(actionTable, 13, rb, f1);
		assertActionEntry(actionTable, 11, EOF, epr2);
		assertActionEntry(actionTable, 2, rb, epr2);
		
		assertActionEntry(actionTable, 13, mult, f1);
		assertActionEntry(actionTable, 12, plus, tpr2);
		assertActionEntry(actionTable, 8, plus, t1);
		
		assertActionEntry(actionTable, 3, mult, 9);
		assertActionEntry(actionTable, 5, mult, f2);
		assertActionEntry(actionTable, 4, id, 5);
		
		assertActionEntry(actionTable, 5, EOF, f2);
		assertActionEntry(actionTable, 5, plus, f2);
		assertActionEntry(actionTable, 3, EOF, tpr2);
		
		assertActionEntry(actionTable, 15, EOF, tpr1);
		assertActionEntry(actionTable, 6, rb, e1);
		assertActionEntry(actionTable, 7, lb, 4);
		
		assertActionEntry(actionTable, 13, EOF, f1);
		assertActionEntry(actionTable, 8, rb, t1);
		assertActionEntry(actionTable, 9, lb, 4);
		
		assertActionEntry(actionTable, 1, EOF, new Accept());
		assertActionEntry(actionTable, 12, rb, tpr2);
		assertActionEntry(actionTable, 13, plus, f1);
		assertActionEntry(actionTable, 3, plus, tpr2);
		
		assertActionEntry(actionTable, 15, rb, tpr1);
		assertActionEntry(actionTable, 10, rb, 13);
		assertActionEntry(actionTable, 15, plus, tpr1);
		
		assertActionEntry(actionTable, 9, id, 5);
		assertActionEntry(actionTable, 7, id, 5);
		assertActionEntry(actionTable, 4, lb, 4);
		
		assertActionEntry(actionTable, 3, rb, tpr2);
		assertActionEntry(actionTable, 11, plus, 7);
		assertActionEntry(actionTable, 12, mult, 9);
		
		assertActionEntry(actionTable, 12, EOF, tpr2);
		assertActionEntry(actionTable, 8, EOF, t1);
		assertActionEntry(actionTable, 0, lb, 4);
		
		assertActionEntry(actionTable, 6, EOF, e1);
		assertActionEntry(actionTable, 2, EOF, epr2);
		assertActionEntry(actionTable, 14, EOF, epr1);
		
		assertActionEntry(actionTable, 2, plus, 7);
		assertActionEntry(actionTable, 11, rb, epr2);
		assertActionEntry(actionTable, 14, rb, epr1);
		
		assertActionEntry(actionTable, 0, id, 5);
		assertActionEntry(actionTable, 5, rb, f2);
		
		
		// Check GOTO
		@SuppressWarnings("unchecked")
		Map<LRTableKey, LRParserState> gotoTable = (Map<LRTableKey, LRParserState>) PA.getValue(lrGoto, "table");
		assertEquals(13, gotoTable.size());
		 
		assertGotoEntry(gotoTable, 4, T, 2);
		assertGotoEntry(gotoTable, 4, E, 10);
		assertGotoEntry(gotoTable, 4, F, 3);
		assertGotoEntry(gotoTable, 3, Tpr, 8);
		assertGotoEntry(gotoTable, 7, T, 11);
		
		assertGotoEntry(gotoTable, 7, F, 3);
		assertGotoEntry(gotoTable, 9, F, 12);
		assertGotoEntry(gotoTable, 12, Tpr, 15);
		assertGotoEntry(gotoTable, 0, F, 3);
		assertGotoEntry(gotoTable, 0, E, 1);
		
		assertGotoEntry(gotoTable, 11, Epr, 14);
		assertGotoEntry(gotoTable, 2, Epr, 6);
		assertGotoEntry(gotoTable, 0, T, 2);
	}
	
	private static void assertGotoEntry(Map<LRTableKey, LRParserState> gotoTable, int stateId, Symbol symbol, int expectedStateId) {
		LRParserState actual = gotoTable.get(new LRTableKey(new LRParserState(stateId), symbol));
		LRParserState expected = new LRParserState(expectedStateId);
		assertEquals(expected, actual);
	}
	
	private static void assertActionEntry(Map<LRTableKey, ALRAction> actionTable, int stateId, Symbol symbol, Production productionToReduce) {
		assertActionEntry(actionTable, stateId, symbol, new Reduce(productionToReduce));
	}
	
	private static void assertActionEntry(Map<LRTableKey, ALRAction> actionTable, int stateId, Symbol symbol, int expectedShiftTargetStateId) {
		LRParserState expected = new LRParserState(expectedShiftTargetStateId);
		assertActionEntry(actionTable, stateId, symbol, new Shift(expected));
	}
	
	private static void assertActionEntry(Map<LRTableKey, ALRAction> actionTable, int stateId, Symbol symbol, ALRAction expected) {
		ALRAction actual = actionTable.get(new LRTableKey(new LRParserState(stateId), symbol));
		assertEquals(expected, actual);
	}
}
