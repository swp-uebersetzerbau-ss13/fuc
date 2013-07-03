package swp_compiler_ss13.fuc.backend;

import static org.junit.Assert.assertEquals;
import static swp_compiler_ss13.common.backend.Quadruple.EmptyArgument;
import static swp_compiler_ss13.fuc.backend.Executor.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import junit.extensions.PA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;

import org.junit.experimental.categories.Category;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

/**
 * Runtime tests for the LLVMBackend
 */
@Category(RuntimeTests.class)
public class RuntimeTest {

	private static LLVMBackend backend;
	private static ArrayList<Quadruple> tac;
		private static PrintWriter out;
	private ByteArrayOutputStream os;
	private static Logger logger;

	private static boolean hasLLI;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
		logger = Logger.getLogger(RuntimeTest.class);

		/* only run tests if lli (dynamic compiler from LLVM) is found */
		hasLLI = checkForLLIInstallation();
		if (!hasLLI){
			logger.warn("Runtime tests are ignored, because of missing LLVM lli installation.");
			String infoMsg = "If you have LLVM installed you might need to check your $PATH: " +
					"Intellij IDEA: Run -> Edit Configurations -> Environment variables; " +
					"Eclipse: Run Configurations -> Environment; " +
					"Shell: Check $PATH";
			logger.info(infoMsg);
		}
		Assume.assumeTrue("Missing LLVM installation", hasLLI);

		backend = new LLVMBackend();
	}

	/* Called before every test */
	@Before
	public void setUp() throws Exception {

		tac = new ArrayList<Quadruple>();
		os = new ByteArrayOutputStream();
		out = new PrintWriter(os);
	}

	/* Called after every test */
	@After
	public void tearDown() throws Exception {
	}

	@AfterClass
	public static void tearDownBeforeClass() {

	}

	@Test
	public void addLongPrintReturnTest() throws InterruptedException, BackendException, IOException {

		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, "#2", Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, "#2", Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_LONG, "longVar1", "longVar2", "result"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.LONG_TO_STRING, "result", Quadruple.EmptyArgument, "s"));
		tac.add(new Q(Quadruple.Operator.PRINT_STRING, "s", Quadruple.EmptyArgument, Quadruple.EmptyArgument));
		tac.add(new Q(Quadruple.Operator.RETURN, "result", Quadruple.EmptyArgument, Quadruple.EmptyArgument));

		ExecutionResult result = Executor.runIR(generateCode(tac));

		assertEquals(4, result.exitCode);
		assertEquals("4\n", result.output);
	}

	@Test
	public void booleanArithmeticTest() throws IOException, BackendException, InterruptedException {
		tac.add(new Q(Quadruple.Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "b1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "b2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "b3"));
		tac.add(new Q(Quadruple.Operator.ASSIGN_BOOLEAN, "#FALSE", Quadruple.EmptyArgument, "b1"));
		tac.add(new Q(Quadruple.Operator.ASSIGN_BOOLEAN, "#TRUE", Quadruple.EmptyArgument, "b2"));
		tac.add(new Q(Quadruple.Operator.AND_BOOLEAN, "b1", "b2", "b3"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.BOOLEAN_TO_STRING, "b3", Quadruple.EmptyArgument, "s"));
		tac.add(new Q(Quadruple.Operator.PRINT_STRING, "s", Quadruple.EmptyArgument, Quadruple.EmptyArgument));

		ExecutionResult res = Executor.runIR(generateCode(tac));
		assertEquals("false\n", res.output);
	}

	@Test
	public void divisionThroughZero() throws IOException, BackendException, InterruptedException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, "#0", Quadruple.EmptyArgument, "l"));
		tac.add(new Q(Quadruple.Operator.DIV_LONG, "#23", "l", "result"));

		ExecutionResult res = Executor.runIR(generateCode(tac));
		assertEquals("Program terminated by uncaught exception of type 'DivisionByZeroException'\n", res.output);
	}

	@Test
	public void printTest() throws Exception {
		tac.add(new Q(Quadruple.Operator.PRINT_STRING, "#\"\n\"", Quadruple.EmptyArgument, Quadruple.EmptyArgument));
		tac.add(new Q(Quadruple.Operator.PRINT_STRING, "#\"bla\"", Quadruple.EmptyArgument, Quadruple.EmptyArgument));

		ExecutionResult res = Executor.runIR(generateCode(tac));
		assertEquals("\nbla\n", res.output);

	}

	@Test
	public void compareTest() throws Exception {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res1"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res2"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_LE, "#23", "#42", "res1"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.BOOLEAN_TO_STRING, "res1", Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.PRINT_STRING, "s", Quadruple.EmptyArgument, Quadruple.EmptyArgument));
		tac.add(new Q(Quadruple.Operator.PRINT_STRING, "#\"\n\"", Quadruple.EmptyArgument, Quadruple.EmptyArgument));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_DOUBLE_G, "#23.0", "#42.0", "res2"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.BOOLEAN_TO_STRING, "res2", Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.PRINT_STRING, "s", Quadruple.EmptyArgument, Quadruple.EmptyArgument));

		ExecutionResult res = Executor.runIR(generateCode(tac));
		assertEquals("true\nfalse\n", res.output);
	}

  @Test
  public void arrayTest() throws Exception {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_ARRAY, "#5", EmptyArgument, "array"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, null));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_SET_LONG, "array", "#0", "#1"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_SET_LONG, "array", "#1", "#2"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_SET_LONG, "array", "#2", "#3"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_SET_LONG, "array", "#3", "#4"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_SET_LONG, "array", "#4", "#5"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "acc"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "tmp"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_GET_LONG, "array", "#0", "acc"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_GET_LONG, "array", "#1", "tmp"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ADD_LONG, "acc", "tmp", "acc"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_GET_LONG, "array", "#2", "tmp"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ADD_LONG, "acc", "tmp", "acc"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_GET_LONG, "array", "#3", "tmp"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ADD_LONG, "acc", "tmp", "acc"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ARRAY_GET_LONG, "array", "#4", "tmp"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.ADD_LONG, "acc", "tmp", "acc"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.LONG_TO_STRING, "acc", Quadruple.EmptyArgument, "s"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.PRINT_STRING, "s", Quadruple.EmptyArgument, Quadruple.EmptyArgument));
		ExecutionResult res = Executor.runIR(generateCode(tac));
		assertEquals("15\n", res.output);
  }

//	@Test
//	@Ignore
//	public void notBooleanTest() throws InterruptedException, BackendException, IOException {
//
//		out.println("DECLARE_BOOLEAN|!|!|b");
//		out.println("NOT_BOOLEAN|#FALSE|!|b");
//		out.println("PRINT_BOOLEAN|b|!|!|");
//
//		assertEquals(0, runTAC().exitCode);
//	}
//
//	@Test
//	@Ignore
//	public void orBooleanTest() throws InterruptedException, BackendException, IOException {
//
//		out.println("DECLARE_BOOLEAN|!|!|b");
//		out.println("DECLARE_BOOLEAN|!|!|rhs");
//		out.println("OR_BOOLEAN|#FALSE|#TRUE|b");
//
//		assertEquals(0, runTAC().exitCode);
//	}
//
//	@Test
//	@Ignore
//	public void printLong() throws InterruptedException, BackendException, IOException {
//
//		out.println("DECLARE_LONG|#1|!|l");
//		out.println("PRINT_LONG|l|!|!|");
//
//		assertEquals(0, runTAC().exitCode);
//	}
//
//	@Test
//	@Ignore
//	public void printDouble() throws InterruptedException, BackendException, IOException {
//
//		out.println("DECLARE_DOUBLE|#1.0|!|d");
//		out.println("PRINT_DOUBLE|d|!|!");
//
//		assertEquals(0, runTAC().exitCode);
//	}
//
//	@Test
//	@Ignore
//	public void printString() throws InterruptedException, BackendException, IOException {
//
//		out.println("DECLARE_STRING|#\"bla\"|!|s");
//		out.println("PRINT_STRING|s|!|!");
//
//		assertEquals(0, runTAC().exitCode);
//	}



	/*
	 * Check if lli is correctly installed.
	 */
	private static boolean checkForLLIInstallation() {

		Level level = Logger.getRootLogger().getLevel();

		Logger.getRootLogger().setLevel(Level.FATAL);
		boolean hasLLI;
		try {
			PA.invokeMethod(Executor.class, "tryToStartLLI()");
			hasLLI = true;
		} catch (Exception e) {
			hasLLI = false;
		}

		Logger.getRootLogger().setLevel(level);

		return hasLLI;
	}

	private ExecutionResult runTAC() throws IOException, InterruptedException, BackendException {
		out.close();
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		return Executor.runTAC(is);
	}

	private InputStream generateCode(ArrayList<Quadruple> tac) throws IOException, BackendException {
		String baseFileName = "baseFileName";
		Map<String, InputStream> result = backend.generateTargetCode(baseFileName, tac);
		return 	result.get("baseFileName.ll");
	}

	/**
	 * A bare-bones implementation of the <code>Quadruple</code> interface used
	 * to generate the left hand side for several of the above test cases.
	 */
	private static class Q implements Quadruple {
		private Operator operator;
		private String argument1;
		private String argument2;
		private String result;

		public Q(Operator o, String a1, String a2, String r) {
			operator = o;
			argument1 = a1;
			argument2 = a2;
			result = r;
		}

		public String toString() {
			return "(" + String.valueOf(operator) + "|" + argument1 + "|" + argument2 + "|" + result + ")";
		}

		public Operator getOperator() {
			return operator;
		}

		public String getArgument1() {
			return argument1;
		}

		public String getArgument2() {
			return argument2;
		}

		public String getResult() {
			return result;
		}
	}
}
