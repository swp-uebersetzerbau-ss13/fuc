package swp_compiler_ss13.fuc.symbolTable.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class SymboltableTest {

	private List<SymbolTable> listOfSymbolTable;

	@Before
	public void setUp() throws Exception {
		this.listOfSymbolTable = new ArrayList<SymbolTable>();
		// set roottable
		this.listOfSymbolTable.add(new SymbolTableImpl());
		SymbolTable tmp = this.listOfSymbolTable.get(0);
		tmp.insert("id0", new BooleanType());
		tmp.setIdentifierAlias("id0", "identifier0");

		for (int i = 1; i < 10; i++) {
			// set children
			tmp = new SymbolTableImpl(this.listOfSymbolTable.get(i - 1));
			this.listOfSymbolTable.add(tmp);
			if (i < 3) {
				tmp.insert("id" + i, new StringType(5L));
			} else {
				if (i < 6) {
					tmp.insert("id" + i, new LongType());
					tmp.setIdentifierAlias("id" + i, "identifier" + i);
				} else {
					tmp.insert("id" + i, new DoubleType());
				}
			}

		}

	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test for parent symboltable
	 */
	@Test
	public void testGetParentSymbolTable() {
		Assert.assertNull(this.listOfSymbolTable.get(0).getParentSymbolTable());
		Assert.assertEquals(this.listOfSymbolTable.get(5).getParentSymbolTable(), this.listOfSymbolTable.get(4));
		Assert.assertNotEquals(this.listOfSymbolTable.get(5).getParentSymbolTable(), this.listOfSymbolTable.get(3));
	}

	@Test
	public void testIsDeclared() {
		Assert.assertTrue(this.listOfSymbolTable.get(4).isDeclared("id4"));
		Assert.assertTrue(this.listOfSymbolTable.get(6).isDeclared("id4"));
		Assert.assertTrue(this.listOfSymbolTable.get(9).isDeclared("id0"));
		Assert.assertFalse(this.listOfSymbolTable.get(4).isDeclared("id7"));
		Assert.assertFalse(this.listOfSymbolTable.get(1).isDeclared("id4"));
	}

	@Test
	public void testInsert() {
		Assert.assertTrue(this.listOfSymbolTable.get(6).insert("id8", new BooleanType()));
		Assert.assertFalse(this.listOfSymbolTable.get(1).insert("id1", new DoubleType()));
		Assert.assertTrue(this.listOfSymbolTable.get(9).insert("id12", new BooleanType()));
		Assert.assertTrue(this.listOfSymbolTable.get(6).isDeclared("id8"));
		Assert.assertTrue(this.listOfSymbolTable.get(1).isDeclared("id1"));
		Assert.assertTrue(this.listOfSymbolTable.get(9).isDeclared("id12"));
	}

	@Test
	public void testRemove() {
		Assert.assertTrue(this.listOfSymbolTable.get(3).remove("id3"));
		Assert.assertTrue(this.listOfSymbolTable.get(1).remove("id1"));
		Assert.assertFalse(this.listOfSymbolTable.get(9).remove("id12"));
		Assert.assertFalse(this.listOfSymbolTable.get(1).isDeclared("id1"));
		Assert.assertFalse(this.listOfSymbolTable.get(3).isDeclared("id3"));
	}

	@Test
	public void testSetLivelinessInformation() {

	}

	@Test
	public void testGetLivelinessInformation() {

	}

	@Test
	public void testGetNextFreeTemporary() {
		// test for given name
		this.listOfSymbolTable.get(2).insert("tmp1", new BooleanType());
		this.listOfSymbolTable.get(0).insert("tmp4", new BooleanType());

		List<String> list = new LinkedList<String>();

		// test for correct naming
		for (int i = 0; i < 6; i++) {
			String tmp = this.listOfSymbolTable.get(2).getNextFreeTemporary();
			Assert.assertFalse(list.contains(tmp));
			list.add(tmp);
		}
		Assert.assertFalse(list.contains("tmp1"));
		Assert.assertFalse(list.contains("tmp4"));
	}

	@Test
	public void testIsDeclaredInCurrentScope() {

		Assert.assertTrue(this.listOfSymbolTable.get(4).isDeclaredInCurrentScope("id4"));
		Assert.assertTrue(this.listOfSymbolTable.get(6).isDeclaredInCurrentScope("id6"));
		Assert.assertFalse(this.listOfSymbolTable.get(9).isDeclaredInCurrentScope("id0"));
		Assert.assertFalse(this.listOfSymbolTable.get(4).isDeclaredInCurrentScope("id7"));
		Assert.assertFalse(this.listOfSymbolTable.get(1).isDeclaredInCurrentScope("id4"));

	}

	@Test
	public void testLookupType() {

		Assert.assertTrue(this.listOfSymbolTable.get(4).lookupType("id0") instanceof BooleanType);
		Assert.assertTrue(this.listOfSymbolTable.get(0).lookupType("id0") instanceof BooleanType);
		Assert.assertFalse(this.listOfSymbolTable.get(4).lookupType("id0") instanceof DoubleType);
		Assert.assertTrue(this.listOfSymbolTable.get(9).lookupType("id6") instanceof DoubleType);
		Assert.assertNull(this.listOfSymbolTable.get(7).lookupType("id8"));

	}

	@Test
	public void testLookupTypeInCurrentScope() {

		Assert.assertFalse(this.listOfSymbolTable.get(4).lookupTypeInCurrentScope("id4") instanceof BooleanType);
		Assert.assertTrue(this.listOfSymbolTable.get(0).lookupTypeInCurrentScope("id0") instanceof BooleanType);
		Assert.assertTrue(this.listOfSymbolTable.get(4).lookupTypeInCurrentScope("id4") instanceof LongType);
		Assert.assertTrue(this.listOfSymbolTable.get(9).lookupTypeInCurrentScope("id9") instanceof DoubleType);
		Assert.assertNull(this.listOfSymbolTable.get(9).lookupTypeInCurrentScope("id8"));

	}

	@Test
	public void testPutTemporary() {

		this.listOfSymbolTable.get(9).putTemporary("id72", new BooleanType());
		this.listOfSymbolTable.get(0).putTemporary("id7", new LongType());
		this.listOfSymbolTable.get(3).putTemporary("id82", new DoubleType());

		Assert.assertTrue(this.listOfSymbolTable.get(0).isDeclaredInCurrentScope("id72"));
		Assert.assertTrue(this.listOfSymbolTable.get(0).isDeclaredInCurrentScope("id82"));
		Assert.assertTrue(this.listOfSymbolTable.get(0).isDeclaredInCurrentScope("id7"));
		Assert.assertFalse(this.listOfSymbolTable.get(0).isDeclaredInCurrentScope("id8"));
		Assert.assertFalse(this.listOfSymbolTable.get(3).isDeclaredInCurrentScope("id82"));
		Assert.assertFalse(this.listOfSymbolTable.get(9).isDeclaredInCurrentScope("id72"));

	}

	@Test
	public void TestGetIdentifierAlias() {
		this.listOfSymbolTable.get(9).setIdentifierAlias("id9", "identifier9");
		this.listOfSymbolTable.get(9).setIdentifierAlias("id7", "identifier7");

		Assert.assertEquals("identifier9", this.listOfSymbolTable.get(9).getIdentifierAlias("id9"));
		Assert.assertNotEquals("identifier7", this.listOfSymbolTable.get(9).getIdentifierAlias("id7"));
		Assert.assertEquals("id7", this.listOfSymbolTable.get(7).getIdentifierAlias("id7"));
	}

	@Test
	public void TestGetDeclaringSymbolTable() {

		Assert.assertEquals(this.listOfSymbolTable.get(0), this.listOfSymbolTable.get(7).getDeclaringSymbolTable("id0"));
		Assert.assertEquals(this.listOfSymbolTable.get(6), this.listOfSymbolTable.get(9).getDeclaringSymbolTable("id6"));
		Assert.assertEquals(this.listOfSymbolTable.get(6), this.listOfSymbolTable.get(6).getDeclaringSymbolTable("id6"));

		Assert.assertNotEquals(this.listOfSymbolTable.get(0),
				this.listOfSymbolTable.get(5).getDeclaringSymbolTable("id2"));
		Assert.assertNotEquals(this.listOfSymbolTable.get(6),
				this.listOfSymbolTable.get(5).getDeclaringSymbolTable("id9"));

	}

}
