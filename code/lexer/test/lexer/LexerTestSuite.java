package lexer;

import lexer.arithmetic.ArithmeticExpressionsTest;
import lexer.declaration.DeclarationTest;
import lexer.input.InputStreamTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Thomas Benndorf
 * @since 28.04.2013
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ InputStreamTest.class, DeclarationTest.class,
		ArithmeticExpressionsTest.class })
public class LexerTestSuite {
	public static final String fileLocation1 = "~/common/examples/m1/add.prog";
	public static final String fileLocation2;
	public static final String fileLocation3;
	public static final String fileLocation4;
	public static final String fileLocation5;
	public static final String fileLocation6;
	public static final String fileLocation7;
	public static final String fileLocation8;
	public static final String fileLocation9;

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

}
