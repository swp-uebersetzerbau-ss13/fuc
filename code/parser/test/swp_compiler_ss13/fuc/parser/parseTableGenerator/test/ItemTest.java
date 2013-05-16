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
		assertTrue( A.compareTo(B) == 0);  assertTrue( A.equals(B) );
		assertTrue( B.compareTo(C) < 0);	assertTrue( ! B.equals(C));
		assertTrue( C.compareTo(D) < 0);
		assertTrue( D.compareTo(E) > 0);
		assertTrue( A_.compareTo(A) < 0);	assertTrue( ! A_.equals(A));
	}

	@Test
	public void testGetSymbolAfterDot() {
		fail("Not yet implemented");
	}

	@Test
	public void testEqualsObject() {
		fail("Not yet implemented");
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
