package swp_compiler_ss13.fuc.test.m1;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import swp_compiler_ss13.common.test.ExampleProgs;
import swp_compiler_ss13.fuc.test.Compiler;
import swp_compiler_ss13.fuc.test.TestBase;

/**
 * <p>
 * Test for the M1 examples producing an expected error.
 * </p>
 * <p>
 * All example progs can be found in {@link ExampleProgs}.
 * </p>
 * 
 * @author Jens V. Fischer
 */
public class M1ErrorTest extends TestBase {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		Logger.getRootLogger().setLevel(Level.INFO);
	}

	@Before
	public void setUp() throws Exception {
		compiler = new Compiler();
	}


	@Test
	public void testDoubleDeclaration() throws Exception {
		testProgCompilation(ExampleProgs.doubleDeclaration());
	}

	@Test
	public void testInvalidIds() throws Exception {
		testProgCompilation(ExampleProgs.invalidIds());
	}

	@Test
	public void testMultipleMinusENotation() throws Exception {
		testProgCompilation(ExampleProgs.multipleMinusENotation());
	}

	@Test
	public void testMultiplePlusesInExp() throws Exception {
		testProgCompilation(ExampleProgs.multiplePlusesInExp());
	}

	@Test
	public void testUndefReturn() throws Exception {
		testProgCompilation(ExampleProgs.undefReturnProg());
	}

}
