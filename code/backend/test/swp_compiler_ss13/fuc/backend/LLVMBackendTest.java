package swp_compiler_ss13.fuc.backend;

import org.junit.*;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;

/**
 * Tests for LLVMBackend
 */
public class LLVMBackendTest {

    static LLVMBackend backend;


    /* Called before all the tests are started*/
    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
        backend = new LLVMBackend();
    }

    /* Called after all the tests have ended */
    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    /* Called before every test */
    @Before
    public void setUp() throws Exception {
    }

    /* Called after every test */
    @After
    public void tearDown() throws Exception {
    }

	@Test
    public void generateTargetCodeTest_DeclareLong() throws IOException {
        Quadruple declaration = new Q(
	        Quadruple.Operator.DECLARE_LONG,
	        Quadruple.EmptyArgument,
	        Quadruple.EmptyArgument,
	        "longVariable");
        InputStream codeStream = generateCode(declaration);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n  %longVariable = alloca i64\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_DeclareLong_InitConst() throws IOException {
        Quadruple declaration = new Q(
	        Quadruple.Operator.DECLARE_LONG,
	        "#0",
	        Quadruple.EmptyArgument,
	        "longVariable");
        InputStream codeStream = generateCode(declaration);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n"
	        + "  %longVariable = alloca i64\n"
	        + "  store i64 0, i64* %longVariable\n"
	        + "  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_DeclareLong_InitVar() throws IOException {
        ArrayList<Quadruple> tac = new ArrayList<Quadruple>();
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        "init",
			        Quadruple.EmptyArgument,
			        "longVariable"));
        InputStream codeStream = generateCode(tac);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n"
	        + "  %init = alloca i64\n"
	        + "  %longVariable = alloca i64\n"
	        + "  %init.0 = load i64* %init\n"
	        + "  store i64 %init.0, i64* %longVariable\n"
	        + "  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_DeclareDouble() throws IOException {
        Quadruple declaration = new Q(
	        Quadruple.Operator.DECLARE_DOUBLE,
	        Quadruple.EmptyArgument,
	        Quadruple.EmptyArgument,
	        "doubleVariable");
        InputStream codeStream = generateCode(declaration);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n  %doubleVariable = alloca double\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_DeclareBoolean() throws IOException {
        Quadruple declaration = new Q(
	        Quadruple.Operator.DECLARE_BOOLEAN,
	        Quadruple.EmptyArgument,
	        Quadruple.EmptyArgument,
	        "boolVariable");
        InputStream codeStream = generateCode(declaration);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n  %boolVariable = alloca i8\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_DeclareString() throws IOException {
        Quadruple declaration = new Q(
	        Quadruple.Operator.DECLARE_STRING,
	        Quadruple.EmptyArgument,
	        Quadruple.EmptyArgument,
	        "stringVariable");
        InputStream codeStream = generateCode(declaration);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n  %stringVariable = alloca i8*\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_LongToDouble() throws IOException {
		ArrayList<Quadruple> tac = new ArrayList<Quadruple>();
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new Q(
			        Quadruple.Operator.LONG_TO_DOUBLE,
			        "longVariable",
			        Quadruple.EmptyArgument,
				        "doubleVariable"));
        InputStream codeStream = generateCode(tac);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n"
	        + "  %longVariable = alloca i64\n"
	        + "  %doubleVariable = alloca double\n"
	        + "  %longVariable.0 = load i64* %longVariable\n"
	        + "  %doubleVariable.0 = sitofp i64 %longVariable.0 to double\n"
	        + "  store double %doubleVariable.0, double* %doubleVariable\n"
	        + "  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

	@Test
    public void generateTargetCodeTest_DoubleToLong() throws IOException {
		ArrayList<Quadruple> tac = new ArrayList<Quadruple>();
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new Q(
			        Quadruple.Operator.DOUBLE_TO_LONG,
			        "doubleVariable",
			        Quadruple.EmptyArgument,
				        "longVariable"));
        InputStream codeStream = generateCode(tac);
        String generatedCode = buildString(codeStream);
        String expectedCode = "define i64 @main() {\n"
	        + "  %longVariable = alloca i64\n"
	        + "  %doubleVariable = alloca double\n"
	        + "  %doubleVariable.0 = load double* %doubleVariable\n"
	        + "  %longVariable.0 = fptosi double %doubleVariable.0 to i64\n"
	        + "  store i64 %longVariable.0, i64* %longVariable\n"
	        + "  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }


    private InputStream generateCode(Quadruple quadruple) {
        ArrayList<Quadruple> tacList = new ArrayList<Quadruple>();
        tacList.add(quadruple);
        Map<String, InputStream> result = backend.generateTargetCode(tacList);
        InputStream module = result.get(".ll");
        return module;
    }

	private InputStream generateCode(ArrayList<Quadruple> tac) {
        Map<String, InputStream> result = backend.generateTargetCode(tac);
        InputStream module = result.get(".ll");
        return module;
    }

    private String buildString(InputStream inputStream) throws IOException {
        BufferedReader in = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder builder = new StringBuilder();
        for (String line = in.readLine(); line != null; line = in.readLine())
            builder.append(line + "\n");
        return builder.toString();
    }

	/**
	 * A bare-bones implementation of the
	 * <code>Quadruple</code> interface used to
	 * generate the left hand side for several
	 * of the above test cases.
	 */
	private static class Q implements Quadruple
	{
		private Operator operator;
		private String argument1;
		private String argument2;
		private String result;

		public Q(Operator o, String a1, String a2, String r)
		{
			operator = o;
			argument1 = a1;
			argument2 = a2;
			result = r;
		}

		public String toString() { return "(" + String.valueOf(operator) + "|" + argument1  + "|" + argument2 + "|" + result + ")"; }

		public Operator getOperator() { return operator; }
		public String getArgument1() { return argument1; }
		public String getArgument2() { return argument2; }
		public String getResult() { return result; }
	}
}
