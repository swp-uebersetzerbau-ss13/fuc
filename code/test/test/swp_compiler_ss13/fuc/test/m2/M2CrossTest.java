package swp_compiler_ss13.fuc.test.m2;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.fuc.backend.LLVMBackend;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.semantic_analyser.SemanticAnalyser;
import swp_compiler_ss13.fuc.test.ExampleProgs;
import swp_compiler_ss13.fuc.test.TestBase;

import java.io.IOException;
import java.util.ServiceLoader;

/**
 * <p>
 * Crosstests for the M2. Only one component (the lexer) is switched. The
 * javabite lexer jarfile has to be placed in <code>fuc/code/dist/</code>.
 * </p>
 * <p>
 * The runtime tests check for results (return values and output) of the execution
 * of the translated examples. The tests require a LLVM installation for
 * executing the LLVM IR. All tests are ignored if no <code>lli</code> is found.
 * </p>
 * <p>
 * All example progs can be found in {@link ExampleProgs}.
 * </p>
 *
 * @author Jens V. Fischer
 */
public class M2CrossTest extends TestBase {

	private static Logger logger = Logger.getLogger(M2CrossTest.class);
	private static ServiceLoader<Lexer> lexerService;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		Logger.getRootLogger().setLevel(Level.ERROR);

		/* only run tests if lli (dynamic compiler from LLVM) is found */
		Assume.assumeTrue(checkForLLIInstallation());
	}

	@Before
	public void setUp() throws Exception {
		lexerService = ServiceLoader.load(Lexer.class);
		// lexer:
		for (Lexer l : lexerService) {
			if (l.getClass().toString().equals("class swp_compiler_ss13.javabite.lexer.LexerJb")) {
				lexer = l;
				logger.info("Javabite lexer found. Performing cross tests");
			}
		}
		if (lexer == null) {
			logger.error("No javabite lexer found. Cannot perform cross tests");
			Assume.assumeTrue("No javabite lexer found. Cannot perform cross tests", false);
		}

		parser = new ParserImpl();
		analyser = new SemanticAnalyser();
		irgen = new IntermediateCodeGeneratorImpl();
		backend = new LLVMBackend();
		errlog = new ReportLogImpl();
	}

	/* M1 progs */
	
	@Test
	public void testSimpleAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.simpleAddProg());
	}

	@Test
	public void testAddProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.addProg());
	}

	@Test
	public void testSimpleMulProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.simpleMulProg());
	}

	@Test
	public void testParenthesesProg() throws IOException, InterruptedException, BackendException, IntermediateCodeGeneratorException {
		testProg(ExampleProgs.parenthesesProg());
	}

	/* M1 progs producing errors */
	
	@Test
	public void testDoubleDeclaration() throws Exception {
		testProgHasError(ExampleProgs.doubleDeclaration());
	}

	@Test
	public void testInvalidIds() throws Exception {
		testProgHasError(ExampleProgs.invalidIds());
	}

	@Test
	public void testMultipleMinusENotation() throws Exception {
		testProgHasError(ExampleProgs.multipleMinusENotation());
	}

	@Test
	public void testMultiplePlusesInExp() throws Exception {
		testProgHasError(ExampleProgs.multiplePlusesInExp());
	}

	@Test
	public void testUndefReturn() throws Exception {
		testProgHasError(ExampleProgs.undefReturn());
	}

	/* M2 progs */

	@Test
	public void testAssignmentProg() throws Exception {
		testProg(ExampleProgs.assignmentProg());
	}

	@Test
	@Ignore("Not yet implemented")
	public void testCondProg() throws Exception {
		testProg(ExampleProgs.condProg());
	}

	@Test
	@Ignore("Not yet implemented")
	public void testPrintProg() throws Exception {
		testProg(ExampleProgs.printProg());
	}

}