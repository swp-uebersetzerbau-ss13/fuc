package swp_compiler_ss13.fuc.symbolTable.test;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.text.StyledEditorKit.BoldAction;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.Type.Kind;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.PrimitiveType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class SymboltableTest {

	
	private List<SymbolTable> listOfSymbolTable; 

	@Before
	public void setUp() throws Exception {
		listOfSymbolTable = new ArrayList<SymbolTable>();
		//set roottable
		listOfSymbolTable.add(new SymbolTableImpl());
		SymbolTable tmp = listOfSymbolTable.get(0);
		tmp.insert("id0", new BooleanType());
		tmp.setIdentifierAlias("id0", "identifier0");
		
		for(int i = 1; i < 10; i++){
			//set children
			tmp = new SymbolTableImpl(listOfSymbolTable.get(i-1));
			listOfSymbolTable.add(tmp);
			if(i<3){
				tmp.insert("id"+i, new StringType(5L));
			}else{
				if(i<6){
					tmp.insert("id"+i, new LongType());
					tmp.setIdentifierAlias("id"+i, "identifier"+i);
				}else{
					tmp.insert("id"+i, new DoubleType());
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
		Assert.assertNull(listOfSymbolTable.get(0).getParentSymbolTable());
		Assert.assertEquals(listOfSymbolTable.get(5).getParentSymbolTable(),listOfSymbolTable.get(4));
		Assert.assertNotEquals(listOfSymbolTable.get(5).getParentSymbolTable(),listOfSymbolTable.get(3));
	}



	@Test
	public void testIsDeclared(){
		Assert.assertTrue(listOfSymbolTable.get(4).isDeclared("id4"));
		Assert.assertTrue(listOfSymbolTable.get(6).isDeclared("id4"));
		Assert.assertTrue(listOfSymbolTable.get(9).isDeclared("id0"));
		Assert.assertFalse(listOfSymbolTable.get(4).isDeclared("id7"));
		Assert.assertFalse(listOfSymbolTable.get(1).isDeclared("id4"));
	}

	@Test
	public void testInsert() {
		Assert.assertTrue(listOfSymbolTable.get(6).insert("id8", new BooleanType()));
		Assert.assertFalse(listOfSymbolTable.get(1).insert("id1", new DoubleType()));
		Assert.assertTrue(listOfSymbolTable.get(9).insert("id12", new BooleanType()));
		Assert.assertTrue(listOfSymbolTable.get(6).isDeclared("id8"));
		Assert.assertTrue(listOfSymbolTable.get(1).isDeclared("id1"));
		Assert.assertTrue(listOfSymbolTable.get(9).isDeclared("id12"));
	}
	
	
	@Test
	public void testRemove() {
		Assert.assertTrue(listOfSymbolTable.get(3).remove("id3"));
		Assert.assertTrue(listOfSymbolTable.get(1).remove("id1"));
		Assert.assertFalse(listOfSymbolTable.get(9).remove("id12"));
		Assert.assertFalse(listOfSymbolTable.get(1).isDeclared("id1"));
		Assert.assertFalse(listOfSymbolTable.get(3).isDeclared("id3"));
	}

	@Test
	public void testSetLivelinessInformation() {
		
	}

	@Test
	public void testGetLivelinessInformation() {

	}

	@Test
	public void testGetNextFreeTemporary() {
		//test for given name
		listOfSymbolTable.get(2).insert("tmp1", new BooleanType());
		listOfSymbolTable.get(0).insert("tmp4", new BooleanType());
		
		List<String> list = new LinkedList<String>();
		
		//test for correct naming
		for(int i = 0; i<6; i++){
			String tmp = listOfSymbolTable.get(2).getNextFreeTemporary();
			Assert.assertFalse(list.contains(tmp));
			list.add(tmp);
		}
		Assert.assertFalse(list.contains("tmp1"));
		Assert.assertFalse(list.contains("tmp4"));
	}
	
	@Test
	public void testIsDeclaredInCurrentScope() {
		
		Assert.assertTrue(listOfSymbolTable.get(4).isDeclaredInCurrentScope("id4"));
		Assert.assertTrue(listOfSymbolTable.get(6).isDeclaredInCurrentScope("id6"));
		Assert.assertFalse(listOfSymbolTable.get(9).isDeclaredInCurrentScope("id0"));
		Assert.assertFalse(listOfSymbolTable.get(4).isDeclaredInCurrentScope("id7"));
		Assert.assertFalse(listOfSymbolTable.get(1).isDeclaredInCurrentScope("id4"));
		
	}
	
	
	@Test
	public void testLookupType() {
		
		Assert.assertTrue(listOfSymbolTable.get(4).lookupType("id0") instanceof BooleanType);
		Assert.assertTrue(listOfSymbolTable.get(0).lookupType("id0") instanceof BooleanType);
		Assert.assertFalse(listOfSymbolTable.get(4).lookupType("id0") instanceof DoubleType);
		Assert.assertTrue(listOfSymbolTable.get(9).lookupType("id6") instanceof DoubleType);
		Assert.assertNull(listOfSymbolTable.get(7).lookupType("id8"));
		
	}
	
	@Test
	public void testLookupTypeInCurrentScope() {
		
		Assert.assertFalse(listOfSymbolTable.get(4).lookupTypeInCurrentScope("id4") instanceof BooleanType);
		Assert.assertTrue(listOfSymbolTable.get(0).lookupTypeInCurrentScope("id0") instanceof BooleanType);
		Assert.assertTrue(listOfSymbolTable.get(4).lookupTypeInCurrentScope("id4") instanceof LongType);
		Assert.assertTrue(listOfSymbolTable.get(9).lookupTypeInCurrentScope("id9") instanceof DoubleType);
		Assert.assertNull(listOfSymbolTable.get(9).lookupTypeInCurrentScope("id8"));
		
	}
	
	@Test
	public void testPutTemporary() {
		
		listOfSymbolTable.get(9).putTemporary("id72", new BooleanType());
		listOfSymbolTable.get(0).putTemporary("id7", new LongType());
		listOfSymbolTable.get(3).putTemporary("id82", new DoubleType());
		
		Assert.assertTrue(listOfSymbolTable.get(0).isDeclaredInCurrentScope("id72"));
		Assert.assertTrue(listOfSymbolTable.get(0).isDeclaredInCurrentScope("id82"));
		Assert.assertTrue(listOfSymbolTable.get(0).isDeclaredInCurrentScope("id7"));
		Assert.assertFalse(listOfSymbolTable.get(0).isDeclaredInCurrentScope("id8"));
		Assert.assertFalse(listOfSymbolTable.get(3).isDeclaredInCurrentScope("id82"));
		Assert.assertFalse(listOfSymbolTable.get(9).isDeclaredInCurrentScope("id72"));
		
	}
	
	@Test
	public void TestGetIdentifierAlias(){
		listOfSymbolTable.get(9).setIdentifierAlias("id9", "identifier9");
		listOfSymbolTable.get(9).setIdentifierAlias("id7", "identifier7");
		
		Assert.assertEquals("identifier9", listOfSymbolTable.get(9).getIdentifierAlias("id9"));
		Assert.assertNotEquals("identifier7", listOfSymbolTable.get(9).getIdentifierAlias("id7"));
		Assert.assertNull(listOfSymbolTable.get(7).getIdentifierAlias("id7"));
	}
	
	@Test
	public void TestGetDeclaringSymbolTable() {
		
		Assert.assertEquals(listOfSymbolTable.get(0), listOfSymbolTable.get(7).getDeclaringSymbolTable("id0"));
		Assert.assertEquals(listOfSymbolTable.get(6), listOfSymbolTable.get(9).getDeclaringSymbolTable("id6"));
		Assert.assertEquals(listOfSymbolTable.get(6), listOfSymbolTable.get(6).getDeclaringSymbolTable("id6"));
		
		Assert.assertNotEquals(listOfSymbolTable.get(0), listOfSymbolTable.get(5).getDeclaringSymbolTable("id2"));
		Assert.assertNotEquals(listOfSymbolTable.get(6), listOfSymbolTable.get(5).getDeclaringSymbolTable("id9"));
		
	}

}
