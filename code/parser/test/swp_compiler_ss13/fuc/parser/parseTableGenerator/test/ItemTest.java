package swp_compiler_ss13.fuc.parser.parseTableGenerator.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Item;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;

public class ItemTest {

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
	public void testCompareTo() {
		String[] AStr = { "B" , "C", "D" };
		String[] BStr = { "B" , "C", "D" };
		String[] CStr = { "B" , "C", "D", "E" };
		String[] DStr = { "B" , "C", "X", "E" };
		String[] EStr = { "B" , "C", "A", "E" };
		Item A = new Item(prodFromStrings("A", AStr),0);
		Item A_ = new Item(prodFromStrings("A", AStr),1);
		Item B = new Item(prodFromStrings("A", BStr),0);
		Item C = new Item(prodFromStrings("A", CStr),0);
		Item D = new Item(prodFromStrings("A", DStr),0);
		Item E = new Item(prodFromStrings("A", EStr),0);
		assertTrue( "A.compareTo(B) == 0", A.compareTo(B) == 0);  assertTrue( "A.equals(B)", A.equals(B) );
		assertTrue( "B.compareTo(C) < 0", B.compareTo(C) < 0);	assertTrue( "! B.equals(C)", ! B.equals(C));
		assertTrue( "C.compareTo(D) < 0", C.compareTo(D) < 0);
		assertTrue( "D.compareTo(E) > 0", D.compareTo(E) > 0);
		assertTrue( "A.compareTo(A_) < 0", A.compareTo(A_) < 0);	assertTrue( "! A_.equals(A)", ! A_.equals(A));
	}

	@Test
	public void testGetSymbolAfterDot() {
		// A -> . B ( C )
		Item A = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 0);
		assertTrue( "\"B\" is after the dot!", A.getSymbolAfterDot().equals(new Variable("B")));
		// A -> B . ( C )
		Item B = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 1);
		assertTrue( "\"(\" is after the dot!", B.getSymbolAfterDot().equals(new Terminal("(")));
		// A -> B ( C ) .
		Item C = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 4);
		assertTrue( "nothing follows the dot!", C.getSymbolAfterDot() == null);
	}

	@Test
	public void testEqualsObject() {
		// A -> . B ( C )
		Item A = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 0);
		assertTrue( "\"B\" is after the dot!", A.getSymbolAfterDot().equals(new Variable("B")));
		// A -> . B ( C )
		Item B = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 0);
		assertTrue( "Item A should be equal Item B", A.equals(B));
		assertTrue( "A.compareTo(B) == 0", A.compareTo(B) == 0);
		// A -> B ( . C )
		Item C = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 2);
		assertFalse( "A.equals(C) == false", A.equals(C) );
		assertTrue( "A.compareTo(C) < 0", A.compareTo(C) < 0);
	}
	
	@Test
	public void testGetStringItem() {
		Item A = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 0);
		assertTrue(
			"testing string serialisation of \"A -> . B ( C )\"",
			A.getStringItem().equals(
				"A -> . B ( C )"
			)
		);
		Item B = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 2);
		assertTrue(
			"testing string serialisation of \"A -> B ( . C )\"",
			B.getStringItem().equals(
				"A -> B ( . C )"
			)
		);
		Item C = new Item( new Production(new Variable("A"), new Variable("B"), new Terminal("("), new Variable("C"), new Terminal(")")), 4);
		assertTrue(
			"testing string serialisation of \"A -> B ( C ) .\"",
			C.getStringItem().equals(
				"A -> B ( C ) ."
			)
		);
	}
	private Production prodFromStrings(String leftStr,String rightStr[])
	{
		Variable left = new Variable(leftStr);
		List<Symbol> right = new ArrayList<Symbol>();
		for(int i=0; i<rightStr.length; i++)
			right.add(new Variable(rightStr[i]));
		return new Production(left,right);
	}

}
