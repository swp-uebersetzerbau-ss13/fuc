package swp_compiler_ss13.fuc.parser.parseTableGenerator.test;

import static org.junit.Assert.*;

//import java.io.FileInputStream;
//import java.io.FileReader;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.fuc.parser.parseTableGenerator.Grammar;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.WrongGrammarFormatException;

public class GrammarTest {

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
	public void testReadFromFile() {
		//fail("Not yet implemented");
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
		// manually create a production:
		List<Symbol> rightSide = new ArrayList<Symbol>();
		rightSide.add(new Variable("Expr"));
		rightSide.add(new Terminal("+"));
		rightSide.add(new Variable("Term"));
		Production prod0 = new Production(new Variable("Expr"), rightSide);
		rightSide.set(1, new Terminal("-"));
		Production prod1 = new Production(new Variable("Expr"), rightSide);
		file.close();
		assertTrue(
				"Production \"Expr -> Expr + Term\" should have been added",
				grammar.getProductions().get(0).getLeft().compareTo(prod0.getLeft()) == 0
		);
		assertTrue(
				"Production \"Expr -> Expr - Term\" should have been added",
				grammar.getProductions().get(1).getLeft().compareTo(prod1.getLeft()) == 0
		);
		// check if FIRST-Sets have been calculated correctly:
		Set<Terminal> FIRSTshouldbe = new HashSet<Terminal>();
		FIRSTshouldbe.add(new Terminal("num"));
		FIRSTshouldbe.add(new Terminal("real"));
		assertTrue(
				"FIRST ( first production ) should equal { num, real }",
				grammar.getProductions().get(0).getFIRST().equals(FIRSTshouldbe)
		);
		// check if FOLLOW-Sets have been calculated correctly:
		Variable Expr = grammar.getVariable("Expr");
		Variable Term = grammar.getVariable("Term");
		Variable Fac = grammar.getVariable("Fac");
		Set<Terminal> ExprExpectedFOLLOW = new HashSet<Terminal>();
			ExprExpectedFOLLOW.add(new Terminal("$"));
			ExprExpectedFOLLOW.add(new Terminal("+"));
			ExprExpectedFOLLOW.add(new Terminal("-"));
		Set<Terminal> TermExpectedFOLLOW = new HashSet<Terminal>(ExprExpectedFOLLOW);
			TermExpectedFOLLOW.add(new Terminal("*"));
			TermExpectedFOLLOW.add(new Terminal("/"));
		Set<Terminal> FacExpectedFOLLOW = new HashSet<Terminal>(TermExpectedFOLLOW);
		assertTrue(
				"FOLLOW ( Expr ) should be equal to { $, +, - }",
				Expr.getFOLLOW().equals(ExprExpectedFOLLOW)
		);
		assertTrue(
				"FOLLOW ( Term ) should be equal to { $, +, -, *, / }",
				Term.getFOLLOW().equals(TermExpectedFOLLOW)
		);
		assertTrue(
				"FOLLOW ( Fac ) should be equal to { $, +, -, *, / }",
				Fac.getFOLLOW().equals(FacExpectedFOLLOW)
		);
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
