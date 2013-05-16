package swp_compiler_ss13.fuc.parser.parseTableGenerator.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Grammar;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Item;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ItemSet;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.WrongGrammarFormatException;

public class ItemSetTest {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetReducableItem() {
		ItemSet itemSet = new ItemSet();
		List<Symbol> right = new ArrayList<Symbol>();
		right.add(new Variable("B"));
		right.add(new Terminal("("));
		right.add(new Variable("C"));
		right.add(new Terminal(")"));
		Item i1 = new Item(new Variable("A"),right,0);
		itemSet.add(i1);
		assertTrue( "itemSet.getReducableItem() should return null", itemSet.getReducableItem() == null );
		Item i2 = new Item(new Variable("A"),right,1);
		itemSet.add(i2);
		assertTrue( "itemSet.getReducableItem() should return null", itemSet.getReducableItem() == null );
		Item i3 = new Item(new Variable("A"),right,4);
		itemSet.add(i3);
		assertTrue( "itemSet.getReducableItem() should return " + i3.getString(), itemSet.getReducableItem() == i3 );
	}
	
	@Test
	public void testAdd() {
		ItemSet itemSet = new ItemSet();
		Item item0 = new Item(new Production(new Variable("A"), new Variable("B")),0);
		itemSet.add( item0 );
		assertTrue("set contains one element", itemSet.size() == 1);
		assertFalse( "itemSet.add should be false, because an equal element is already present", itemSet.add( new Item(new Production(new Variable("A"), new Variable("B")),0) ));
		assertTrue("set still contains one element", itemSet.size() == 1);
		
		Item item1 = new Item(new Production(new Variable("A"), new Variable("B")),1);
		itemSet.add( item1 );
		assertTrue("set contains two elements", itemSet.size() == 2);
		
		// even this reference is not yet existent in the set, it should not be added, because it equals item1!:
		assertTrue( "Set.equals works: ", item1.equals( new Item(new Production(new Variable("A"), new Variable("B")),1) ));
		assertFalse( "itemSet.add should be false, because an equal element is already present", itemSet.add( new Item(new Production(new Variable("A"), new Variable("B")),1)));
		assertTrue("set still contains two elements", itemSet.size() == 2);
	}

	@Test
	public void testCLOSURE() {
		StringReader file = new StringReader(testGrammar);
		Grammar grammar = new Grammar();
		try {
			grammar.readFromFile(file);
		}
		catch(WrongGrammarFormatException e)
		{
			file.close();
			fail("WrongGrammarFormatException while parsing the grammar, namely:\n"
					+ e.getMessage()
			);
		}
		catch(IOException e)
		{
			file.close();
			fail("IOException while parsing the grammar!");
		}
		ItemSet itemSet = new ItemSet();
		List<Symbol> right = new ArrayList<Symbol>();
		right.add(new Variable("Term"));
		right.add(new Terminal("*"));
		right.add(new Variable("Fac"));
		Item i1 = new Item(new Variable("Term"),right,0);
		itemSet.add(i1);
		itemSet.CLOSURE(grammar);
		//fail("CLOSURE not yet working!");
		assertTrue( "right number of Items in CLOSURE", itemSet.size() == 5);
		
		
		ItemSet CLOSUREShouldBe = new ItemSet();
		// Term -> . Term * Fac
		List<Symbol> rightShouldBe0 = new ArrayList<Symbol>();
		rightShouldBe0.add(new Variable("Term"));
		rightShouldBe0.add(new Terminal("*"));
		rightShouldBe0.add(new Variable("Fac"));
		CLOSUREShouldBe.add( new Item( new Variable("Term"), rightShouldBe0, 0));
		// Term -> . Term / Fac
		List<Symbol> rightShouldBe1 = new ArrayList<Symbol>();
		rightShouldBe1.add(new Variable("Term"));
		rightShouldBe1.add(new Terminal("/"));
		rightShouldBe1.add(new Variable("Fac"));
		CLOSUREShouldBe.add( new Item( new Variable("Term"), rightShouldBe1, 0));
		// Term -> . Fac
		List<Symbol> rightShouldBe2 = new ArrayList<Symbol>();
		rightShouldBe2.add(new Variable("Fac"));
		CLOSUREShouldBe.add( new Item( new Variable("Term"), rightShouldBe2, 0));
		// Fac -> . num
		List<Symbol> rightShouldBe3 = new ArrayList<Symbol>();
		rightShouldBe3.add(new Terminal("num"));
		CLOSUREShouldBe.add( new Item( new Variable("Fac"), rightShouldBe3, 0));
		// Fac -> . real
		List<Symbol> rightShouldBe4 = new ArrayList<Symbol>();
		rightShouldBe4.add(new Terminal("real"));
		CLOSUREShouldBe.add( new Item( new Variable("Fac"), rightShouldBe4, 0));
		assertTrue( "itemSet.getReducableItem() should return ", itemSet.equals(CLOSUREShouldBe));
	}
	/*
	 * Expr -> Expr + Term | Expr - Term | Term
	 * Term -> Term * Fac | Term / Fac | Fac
	 * Fac -> num | real
	 * 
	 * FIRST:
	 * FIRST( Expr ) = FIRST( Term ) = FIRST( Fac ) = { num, real }
	 * 
	 * FOLLOW:
	 * FOLLOW( Expr ) = { $, +, - }
	 * FOLLOW( Term ) = FOLLOW( Fac ) = { $, +, -, *, / }
	 *//*
	 * Expr -> Expr + Term | Expr - Term | Term
	 * Term -> Term * Fac | Term / Fac | Fac
	 * Fac -> num | real
	 * 
	 * FIRST:
	 * FIRST( Expr ) = FIRST( Term ) = FIRST( Fac ) = { num, real }
	 * 
	 * FOLLOW:
	 * FOLLOW( Expr ) = { $, +, - }
	 * FOLLOW( Term ) = FOLLOW( Fac ) = { $, +, -, *, / }
	 */
	private static String testGrammar = 
			"symbols:\n" +
			"num,real,+,-,*,/\n" +
			"variables:\n" +
			"Expr,Term,Fac\n" +
			"productions:\n" +
			"Expr:\n" +
			"Expr + Term\n" +
			"Expr - Term\n" +
			"Term\n" +
			"Term:\n" +
			"Term * Fac\n" +
			"Term / Fac\n" +
			"Fac\n" +
			"Fac:\n" +
			"num\n" +
			"real\n";
}
