package swp_compiler_ss13.fuc.parser.parseTableGenerator.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.*;
//import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;
@Ignore
public class ProductionTest {

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
		Production A = prodFromStrings("A", AStr);
		Production B = prodFromStrings("A", BStr);
		Production C = prodFromStrings("A", CStr);
		Production D = prodFromStrings("A", DStr);
		Production E = prodFromStrings("A", EStr);
		assertTrue( A.compareTo(B) == 0);
		assertTrue( B.compareTo(C) < 0);
		assertTrue( C.compareTo(D) < 0);
		assertTrue( D.compareTo(E) > 0);
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
