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

    private static LLVMBackend backend;
    private static ArrayList<Quadruple> tac;


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
        tac = new ArrayList<Quadruple>();
    }

    /* Called after every test */
    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void generateTargetCodeTest_DeclareLong() throws IOException {
        tac.add( new Q(
                Quadruple.Operator.DECLARE_LONG,
                Quadruple.EmptyArgument,
                Quadruple.EmptyArgument,
                "longVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n  %longVariable = alloca i64\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_DeclareLong_InitConst() throws IOException {
        tac.add( new Q(
                Quadruple.Operator.DECLARE_LONG,
                "#0",
                Quadruple.EmptyArgument,
                "longVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n"
                + "  %longVariable = alloca i64\n"
                + "  store i64 0, i64* %longVariable\n"
                + "  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_DeclareLong_InitVar() throws IOException {
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
        String generatedCode = generateCodeAsString(tac);
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
        tac.add( new Q(
                Quadruple.Operator.DECLARE_DOUBLE,
                Quadruple.EmptyArgument,
                Quadruple.EmptyArgument,
                "doubleVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n  %doubleVariable = alloca double\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_DeclareBoolean() throws IOException {
        tac.add(new Q(
                Quadruple.Operator.DECLARE_BOOLEAN,
                Quadruple.EmptyArgument,
                Quadruple.EmptyArgument,
                "boolVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n  %boolVariable = alloca i8\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_DeclareString() throws IOException {
        tac.add( new Q(
                Quadruple.Operator.DECLARE_STRING,
                Quadruple.EmptyArgument,
                Quadruple.EmptyArgument,
                "stringVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n  %stringVariable = alloca i8*\n  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_LongToDouble() throws IOException {
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
        String generatedCode = generateCodeAsString(tac);
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
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n"
                + "  %longVariable = alloca i64\n"
                + "  %doubleVariable = alloca double\n"
                + "  %doubleVariable.0 = load double* %doubleVariable\n"
                + "  %longVariable.0 = fptosi double %doubleVariable.0 to i64\n"
                + "  store i64 %longVariable.0, i64* %longVariable\n"
                + "  ret i64 0\n}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignLongConst() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVariable"));
        tac.add( new Q(Quadruple.Operator.ASSIGN_LONG, "#42", Quadruple.EmptyArgument, "longVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %longVariable = alloca i64\n" +
                "  store i64 42, i64* %longVariable\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignDoubleConst() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVariable"));
        tac.add( new Q(Quadruple.Operator.ASSIGN_DOUBLE, "#42.0", Quadruple.EmptyArgument, "doubleVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %doubleVariable = alloca double\n" +
                "  store double 42.0, double* %doubleVariable\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignBooleanConst() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "boolVariable"));
        tac.add( new Q(Quadruple.Operator.ASSIGN_BOOLEAN, "#true", Quadruple.EmptyArgument, "boolVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %boolVariable = alloca i8\n" +
                "  store i8 true, i8* %boolVariable\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignStringConst() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "stringVariable"));
        tac.add( new Q(Quadruple.Operator.ASSIGN_STRING, "#\"zweiundvierzig\"", Quadruple.EmptyArgument, "stringVariable"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %stringVariable = alloca i8*\n" +
                "  %.string_0 = alloca [14 x i8]\n" +
                "  store [14 x i8] c\"zweiundvierzig\", [14 x i8]* %.string_0\n" +
                "  %stringVariable.0 = getelementptr [14 x i8]* %.string_0, i64 0, i64 0\n" +
                "  store i8* %stringVariable.0, i8** %stringVariable\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignLong() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVariableDest"));
        tac.add(new Q(Quadruple.Operator.DECLARE_LONG, "#42", Quadruple.EmptyArgument, "longVariableSource"));
        tac.add(new Q(Quadruple.Operator.ASSIGN_LONG, "longVariableSource", Quadruple.EmptyArgument, "longVariableDest"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %longVariableDest = alloca i64\n" +
                "  %longVariableSource = alloca i64\n" +
                "  store i64 42, i64* %longVariableSource\n" +
                "  %longVariableSource.0 = load i64* %longVariableSource\n" +
                "  store i64 %longVariableSource.0, i64* %longVariableDest\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignDouble() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVariableDest"));
        tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, "#42.0", Quadruple.EmptyArgument, "doubleVariableSource"));
        tac.add(new Q(Quadruple.Operator.ASSIGN_DOUBLE, "doubleVariableSource", Quadruple.EmptyArgument, "doubleVariableDest"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %doubleVariableDest = alloca double\n" +
                "  %doubleVariableSource = alloca double\n" +
                "  store double 42.0, double* %doubleVariableSource\n" +
                "  %doubleVariableSource.0 = load double* %doubleVariableSource\n" +
                "  store double %doubleVariableSource.0, double* %doubleVariableDest\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignBoolean() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_BOOLEAN, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "boolVariableDest"));
        tac.add(new Q(Quadruple.Operator.DECLARE_BOOLEAN, "#true", Quadruple.EmptyArgument, "boolVariableSource"));
        tac.add(new Q(Quadruple.Operator.ASSIGN_BOOLEAN, "boolVariableSource", Quadruple.EmptyArgument, "boolVariableDest"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %boolVariableDest = alloca i8\n" +
                "  %boolVariableSource = alloca i8\n" +
                "  store i8 true, i8* %boolVariableSource\n" +
                "  %boolVariableSource.0 = load i8* %boolVariableSource\n" +
                "  store i8 %boolVariableSource.0, i8* %boolVariableDest\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    @Test
    public void generateTargetCodeTest_AssignString() throws IOException {
        tac.add(new Q(Quadruple.Operator.DECLARE_STRING, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "stringVariableDest"));
        tac.add(new Q(Quadruple.Operator.DECLARE_STRING, "#\"zweiundvierzig\"", Quadruple.EmptyArgument, "stringVariableSource"));
        tac.add(new Q(Quadruple.Operator.ASSIGN_STRING, "stringVariableSource", Quadruple.EmptyArgument, "stringVariableDest"));
        String generatedCode = generateCodeAsString(tac);
        String expectedCode = "define i64 @main() {\n" +
                "  %stringVariableDest = alloca i8*\n" +
                "  %stringVariableSource = alloca i8*\n" +
                "  %.string_0 = alloca [14 x i8]\n" +
                "  store [14 x i8] c\"zweiundvierzig\", [14 x i8]* %.string_0\n" +
                "  %stringVariableSource.0 = getelementptr [14 x i8]* %.string_0, i64 0, i64 0\n" +
                "  store i8* %stringVariableSource.0, i8** %stringVariableSource\n" +
                "  %stringVariableSource.1 = load i8** %stringVariableSource\n" +
                "  store i8* %stringVariableSource.1, i8** %stringVariableDest\n" +
                "  ret i64 0\n" +
                "}\n";
        assertEquals(expectedCode, generatedCode);
    }

    private String generateCodeAsString(ArrayList<Quadruple> tac) throws IOException {
        Map<String, InputStream> result = backend.generateTargetCode(tac);
        InputStream module = result.get(".ll");
        BufferedReader in = new BufferedReader(new InputStreamReader(module));
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
