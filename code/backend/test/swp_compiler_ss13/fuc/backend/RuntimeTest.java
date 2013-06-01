package swp_compiler_ss13.fuc.backend;

import static org.junit.Assert.assertEquals;
import static swp_compiler_ss13.fuc.backend.TACExecutor.*;

import java.io.*;
import java.util.ArrayList;
import java.util.Map;

import junit.extensions.PA;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.junit.*;

import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

/**
 * Runtime tests for the LLVMBackend
 */
public class RuntimeTest {

	private static LLVMBackend backend;
	private static ArrayList<Quadruple> tac;
	private static PrintWriter out;
	private ByteArrayOutputStream os;
	private static Logger logger;


	@BeforeClass
	public static void setUpBeforeClass() throws Exception {

		logger = Logger.getLogger(RuntimeTest.class);
		
		/* only run tests if lli (dynamic compiler from LLVM) is found */
		Assume.assumeTrue(checkForLLIInstallation());
		
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

	@Test
	public void addLongPrintReturnTest() throws InterruptedException, BackendException, IOException {

		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, "#2", Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, "#2", Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_LONG, "longVar1", "longVar2", "result"));
		tac.add(new Q(Quadruple.Operator.PRINT_LONG, "result", Quadruple.EmptyArgument, Quadruple.EmptyArgument));
		tac.add(new Q(Quadruple.Operator.RETURN, "result", Quadruple.EmptyArgument, Quadruple.EmptyArgument));

		ExecutionResult result = TACExecutor.runIR(generateCode(tac));

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
		tac.add(new Q(Quadruple.Operator.PRINT_BOOLEAN, "b3", Quadruple.EmptyArgument, Quadruple.EmptyArgument));

		ExecutionResult res = TACExecutor.runIR(generateCode(tac));
		assertEquals("false\n", res.output);
	}

	@Test
	public void notBooleanTest() throws InterruptedException, BackendException, IOException {

		out.println("DECLARE_BOOLEAN|!|!|b");
		out.println("NOT_BOOLEAN|#FALSE|!|b");
		out.println("PRINT_BOOLEAN|b|!|!|");

		assertEquals(0, runTAC().exitCode);
	}

	@Test
	public void orBooleanTest() throws InterruptedException, BackendException, IOException {

		out.println("DECLARE_BOOLEAN|!|!|b");
		out.println("DECLARE_BOOLEAN|!|!|rhs");
		out.println("OR_BOOLEAN|#FALSE|#TRUE|b");

		assertEquals(0, runTAC().exitCode);
	}

	@Test
	public void printLong() throws InterruptedException, BackendException, IOException {

		out.println("DECLARE_LONG|#1|!|l");
		out.println("PRINT_LONG|l|!|!|");

		assertEquals(0, runTAC().exitCode);
	}

	@Test
	public void printDouble() throws InterruptedException, BackendException, IOException {

		out.println("DECLARE_DOUBLE|#1.0|!|d");
		out.println("PRINT_DOUBLE|d|!|!");

		assertEquals(0, runTAC().exitCode);
	}

	@Test
	public void printString() throws InterruptedException, BackendException, IOException {

		out.println("DECLARE_STRING|#\"bla\"|!|s");
		out.println("PRINT_STRING|s|!|!");

		assertEquals(0, runTAC().exitCode);

	}



	/*
	 * Check if lli is correctly installed.
	 */
	private static boolean checkForLLIInstallation() {

		Level level = Logger.getRootLogger().getLevel();

		Logger.getRootLogger().setLevel(Level.FATAL);
		boolean hasLLI;
		try {
			PA.invokeMethod(TACExecutor.class, "tryToStartLLI()");
			hasLLI = true;
		} catch (Exception e) {
			hasLLI = false;
		}

		Logger.getRootLogger().setLevel(level);

		if (!hasLLI) {
			logger.warn("Runtime tests are ignored, because of missing LLVM lli installation.");
			String infoMsg = "If you have LLVM installed you might need to check your $PATH: " +
					"Intellij IDEA: Run -> Edit Configurations -> Environment variables; " +
					"Eclipse: Run Configurations -> Environment; " +
					"Shell: Check $PATH";
			logger.info(infoMsg);
		}
		return hasLLI;
	}

	private ExecutionResult runTAC() throws IOException, InterruptedException, BackendException {
		out.close();
		ByteArrayInputStream is = new ByteArrayInputStream(os.toByteArray());
		return TACExecutor.runTAC(is);
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
