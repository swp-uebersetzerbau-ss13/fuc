package swp_compiler_ss13.fuc.parser.parseTableGenerator.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringReader;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Grammar;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableBuilder;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.WrongGrammarFormatException;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable;

public class ParseTableBuilderTest {

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
	public void testGetTable() {
		Grammar grammar = new Grammar();
		StringReader in = new StringReader(testGrammar);	
		try {
			grammar.readFromFile(in);
		}
		catch (WrongGrammarFormatException e) {
			fail(
					"WrongFormatException while parsing Grammar:\n" +
					e.getMessage()
			);
		}
		catch (IOException e) {
			fail(
					"IOException while parsing Grammar:\n" +
					e.getMessage()
			);
		}
		ParseTableBuilder tableBuilder = new ParseTableBuilder();
		ParseTable table = tableBuilder.getTable(grammar);
		//fail("Not yet implemented");
	}
	
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
