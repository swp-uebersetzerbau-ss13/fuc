package symbolTable.testcase;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.parser.SymbolTable;
import symbolTable.impl.SymbolTableImpl;

public class SymboltableTest {

	
	private SymbolTable symbolTable;

	@Before
	public void setUp() throws Exception {
		symbolTable = new SymbolTableImpl();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testGetParentSymbolTable() {
		fail("Not yet implemented");
	}

	@Test
	public void testIsDeclared() {
		fail("Not yet implemented");
	}

	@Test
	public void testLookupType() {
		fail("Not yet implemented");
	}

	@Test
	public void testInsert() {
		fail("Not yet implemented");
	}

	@Test
	public void testRemove() {
		fail("Not yet implemented");
	}

	@Test
	public void testSetLivelinessInformation() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLivelinessInformation() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetNextFreeTemporary() {
		fail("Not yet implemented");
	}

}
