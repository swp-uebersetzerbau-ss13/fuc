package swp_compiler_ss13.fuc.symbolTable.test;

import junit.framework.Assert;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class SymboltableTest {

	
	private SymbolTable symbolTable;
	private SymbolTable parentTable;

	@Before
	public void setUp() throws Exception {
		symbolTable = new SymbolTableImpl();
		parentTable = new SymbolTableImpl(symbolTable);
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test for parent symboltable
	 */
	@Test
	public void testGetParentSymbolTable() {
		Assert.assertNull(symbolTable.getParentSymbolTable());
		Assert.assertEquals(parentTable.getParentSymbolTable(), symbolTable);
	}


	@Test
	public void testLookupType() {
		DoubleType type = new DoubleType();
		symbolTable.insert("arg1",type);
		BooleanType type2 = new BooleanType();
		symbolTable.insert("arg2", type2);
		LongType type3 = new LongType();
		symbolTable.insert("arg3", type3);
		Assert.assertTrue(symbolTable.isDeclared("arg1"));
		Assert.assertTrue(symbolTable.isDeclared("arg2"));
		Assert.assertTrue(symbolTable.isDeclared("arg3"));
		Assert.assertEquals(symbolTable.lookupType("arg1"), type);
		Assert.assertEquals(symbolTable.lookupType("arg2"), type2);
		Assert.assertEquals(symbolTable.lookupType("arg3"), type3);
		Assert.assertNotSame(symbolTable.lookupType("arg1"), type2);
		Assert.assertNull(symbolTable.lookupType("arg"));
	}

	@Test
	public void testInsert() {
		symbolTable.insert("arg1",new DoubleType());
		symbolTable.insert("arg2", new BooleanType());
		symbolTable.insert("arg3", new LongType());
		Assert.assertTrue(symbolTable.isDeclared("arg1"));
		Assert.assertTrue(symbolTable.isDeclared("arg2"));
		Assert.assertTrue(symbolTable.isDeclared("arg3"));
		Assert.assertFalse(symbolTable.isDeclared("arg"));
	}
	
	
	@Test
	public void testRemove() {
		symbolTable.insert("arg1",new LongType());
		symbolTable.insert("arg2", new BooleanType());
		Assert.assertTrue(symbolTable.isDeclared("arg1"));
		Assert.assertTrue(symbolTable.isDeclared("arg2"));
		Assert.assertTrue(symbolTable.remove("arg1"));
		Assert.assertTrue(symbolTable.remove("arg2"));
		Assert.assertFalse(symbolTable.remove("arg"));
		Assert.assertFalse(symbolTable.isDeclared("arg1"));
		Assert.assertFalse(symbolTable.isDeclared("arg2"));
	}

	/**@Test
	public void testSetLivelinessInformation() {
		fail("Not yet implemented");
	}

	@Test
	public void testGetLivelinessInformation() {
		fail("Not yet implemented");
	}**/

	@Test
	public void testGetNextFreeTemporary() {
		String temporary2 = symbolTable.getNextFreeTemporary();
		String temporary = symbolTable.getNextFreeTemporary();
		Assert.assertNotNull(temporary2);
		Assert.assertNotSame(temporary2, temporary);
		Assert.assertFalse(temporary2.equals(temporary));
	}

}
