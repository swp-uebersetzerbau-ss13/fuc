package lexer;

import lexer.input.InputTest;
import lexer.keywords.KeywordTest;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

/**
 * @author Thomas Benndorf, Tay Phuong Ho
 * @since 28.04.2013
 * 
 */
@RunWith(Suite.class)
@SuiteClasses({ InputTest.class, KeywordTest.class })
public class LexerTestSuite {
	public static final String fileLocation1 = "~/common/examples/m1/add.prog";
	public static final String fileLocation2 = "~/common/examples/m1/error_double_decl.prog";
	public static final String fileLocation3 = "~/common/examples/m1/error_invalid_ids.prog";
	public static final String fileLocation4 = "~/common/examples/m1/error_multiple_minus_e_notation.prog";
	public static final String fileLocation5 = "~/common/examples/m1/error_multiple_pluses_in_exp.prog";
	public static final String fileLocation6 = "~/common/examples/m1/error_undef_return.prog";
	public static final String fileLocation7 = "~/common/examples/m1/paratheses.prog";
	public static final String fileLocation8 = "~/common/examples/m1/simple_add.prog";
	public static final String fileLocation9 = "~/common/examples/m1/simple_mul.prog";

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
