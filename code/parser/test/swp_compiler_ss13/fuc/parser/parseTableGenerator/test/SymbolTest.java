package swp_compiler_ss13.fuc.parser.parseTableGenerator.test;

import static org.junit.Assert.*;

import java.util.HashSet;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;

public class SymbolTest {

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
		Symbol[] sym = {
				new Terminal("a"), // 0
				new Terminal("a"), // 1
				new Terminal("c"), // 2
				new Variable("a"), // 3
				new Variable("a"), // 4
				new Variable("c"), // 5
			};
		/*assertTrue( " ", sym[0].compareTo(sym[1]) == 0);
		assertTrue( sym[0].equals(sym[1]));
		assertTrue( Math.signum(sym[0].compareTo(sym[2])) == -1);
		
		assertTrue( Math.signum(sym[0].compareTo(sym[3])) == -1);
		assertTrue( sym[3].compareTo(sym[4]) == 0);
		assertTrue( Math.signum(sym[3].compareTo(sym[5])) == -1);*/
		
		HashSet<Terminal> set = new HashSet<Terminal>();
		Terminal term = new Terminal("haha");
		set.add(term);
		assertFalse( "the set should not be empty!", set.isEmpty());
		assertTrue( "the set should contain the terminal now!", set.contains(new Terminal("haha")));
		//assertTrue( "the set should contain the terminal now!", set.contains(new Terminal("haha")));
	}

}
