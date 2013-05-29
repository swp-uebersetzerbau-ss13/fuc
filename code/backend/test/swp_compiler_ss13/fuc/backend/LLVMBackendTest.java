package swp_compiler_ss13.fuc.backend;

import org.junit.*;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

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
	@Ignore
	public void generateTargetCodeTest_DeclareLong() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String expectedCode = "define i64 @main() {\n  %longVariable = alloca i64\n  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));

	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareLong_InitConst() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        "#0",
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %longVariable = alloca i64\n"
			+ "  store i64 0, i64* %longVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));

	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareLong_InitVar() throws IOException, BackendException {
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
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca i64\n"
			+ "  %longVariable = alloca i64\n"
			+ "  %init.0 = load i64* %init\n"
			+ "  store i64 %init.0, i64* %longVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareDouble() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String expectedCode = "define i64 @main() {\n  %doubleVariable = alloca double\n  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareDouble_InitConst() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        "#0.0",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  store double 0.0, double* %doubleVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareDouble_InitVar() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        "init",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca double\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %init.0 = load double* %init\n"
			+ "  store double %init.0, double* %doubleVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareBoolean() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "boolVariable"));
		String expectedCode = "define i64 @main() {\n  %boolVariable = alloca i8\n  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareBoolean_InitConst_False() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        "#FALSE",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %booleanVariable = alloca i8\n"
			+ "  store i8 0, i8* %booleanVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareBoolean_InitConst_True() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        "#TRUE",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %booleanVariable = alloca i8\n"
			+ "  store i8 1, i8* %booleanVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareBoolean_InitVar() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        "init",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca i8\n"
			+ "  %booleanVariable = alloca i8\n"
			+ "  %init.0 = load i8* %init\n"
			+ "  store i8 %init.0, i8* %booleanVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareString() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String expectedCode = "define i64 @main() {\n  %stringVariable = alloca i8*\n  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareString_InitConst() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        "#\"Foo\"",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %stringVariable = alloca i8*\n"
			+ "  %.string_0 = alloca [3 x i8]\n"
			+ "  store [3 x i8] c\"Foo\", [3 x i8]* %.string_0\n"
			+ "  %stringVariable.0 = getelementptr [3 x i8]* %.string_0, i64 0, i64 0\n"
			+ "  store i8* %stringVariable.0, i8** %stringVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DeclareString_InitVar() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        "init",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca i8*\n"
			+ "  %stringVariable = alloca i8*\n"
			+ "  %init.0 = load i8** %init\n"
			+ "  store i8* %init.0, i8** %stringVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	// Conversion

	@Test
	@Ignore
	public void generateTargetCodeTest_LongToDouble() throws IOException, BackendException {
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
		String expectedCode = "define i64 @main() {\n"
			+ "  %longVariable = alloca i64\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %longVariable.0 = load i64* %longVariable\n"
			+ "  %doubleVariable.0 = sitofp i64 %longVariable.0 to double\n"
			+ "  store double %doubleVariable.0, double* %doubleVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DoubleToLong() throws IOException, BackendException {
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
		String expectedCode = "define i64 @main() {\n"
			+ "  %longVariable = alloca i64\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %doubleVariable.0 = load double* %doubleVariable\n"
			+ "  %longVariable.0 = fptosi double %doubleVariable.0 to i64\n"
			+ "  store i64 %longVariable.0, i64* %longVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}


	// Assign

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignLong_Const() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_LONG,
			        "#0",
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %longVariable = alloca i64\n"
			+ "  store i64 0, i64* %longVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignLong_Var() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_LONG,
			        "init",
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca i64\n"
			+ "  %longVariable = alloca i64\n"
			+ "  %init.0 = load i64* %init\n"
			+ "  store i64 %init.0, i64* %longVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignDouble_Const() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_DOUBLE,
			        "#0.0",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  store double 0.0, double* %doubleVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignDouble_Var() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_DOUBLE,
			        "init",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca double\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %init.0 = load double* %init\n"
			+ "  store double %init.0, double* %doubleVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignBoolean_Const_False() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_BOOLEAN,
			        "#FALSE",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %booleanVariable = alloca i8\n"
			+ "  store i8 0, i8* %booleanVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignBoolean_Const_True() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_BOOLEAN,
			        "#TRUE",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %booleanVariable = alloca i8\n"
			+ "  store i8 1, i8* %booleanVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignBoolean_Var() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_BOOLEAN,
			        "init",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca i8\n"
			+ "  %booleanVariable = alloca i8\n"
			+ "  %init.0 = load i8* %init\n"
			+ "  store i8 %init.0, i8* %booleanVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignString_Const() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_STRING,
			        "#\"Foo\"",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %stringVariable = alloca i8*\n"
			+ "  %.string_0 = alloca [3 x i8]\n"
			+ "  store [3 x i8] c\"Foo\", [3 x i8]* %.string_0\n"
			+ "  %stringVariable.0 = getelementptr [3 x i8]* %.string_0, i64 0, i64 0\n"
			+ "  store i8* %stringVariable.0, i8** %stringVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AssignString_Var() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		tac.add(new Q(
			        Quadruple.Operator.ASSIGN_STRING,
			        "init",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String expectedCode = "define i64 @main() {\n"
			+ "  %init = alloca i8*\n"
			+ "  %stringVariable = alloca i8*\n"
			+ "  %init.0 = load i8** %init\n"
			+ "  store i8* %init.0, i8** %stringVariable\n"
			+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	// Control flow

	@Test
	@Ignore
	public void generateTargetCodeTest_Return_Const() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.RETURN,
			        "#1",
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument));
		String expectedCode = "define i64 @main() {\n"
			+ "  ret i64 1\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_Return_Var() throws IOException, BackendException {
		tac.add(new Q(
			        Quadruple.Operator.DECLARE_LONG,
			        "#1",
			        Quadruple.EmptyArgument,
			        "res"));
		tac.add(new Q(
			        Quadruple.Operator.RETURN,
			        "res",
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument));
		String expectedCode = "define i64 @main() {\n"
			+ "  %res = alloca i64\n"
			+ "  store i64 1, i64* %res\n"
			+ "  %res.0 = load i64* %res\n"
			+ "  ret i64 %res.0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test (expected = BackendException.class)
	@Ignore
	public void generateTargetCodeTest_AssignWithoutDeclaration() throws IOException, BackendException {
		tac.add(new Q(
				Quadruple.Operator.DECLARE_LONG,
				Quadruple.EmptyArgument,
				Quadruple.EmptyArgument,
				"longVariable"));
		tac.add(new Q(
				Quadruple.Operator.ASSIGN_LONG,
				"init",
				Quadruple.EmptyArgument,
				"longVariable"));
		String expectedCode = "define i64 @main() {\n"
				+ "  %init = alloca i64\n"
				+ "  %longVariable = alloca i64\n"
				+ "  %init.0 = load i64* %init\n"
				+ "  store i64 %init.0, i64* %longVariable\n"
				+ "  ret i64 0\n}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}


	// ARITHMETIC
	
	// Add


	@Test
	@Ignore
	public void generateTargetCodeTest_AddLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_LONG, "#23", "#42", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %result = alloca i64\n" + 
				"  %result.0 = add i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" + 
				"  ret i64 0\n" + 
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AddLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_LONG, "longVar1", "longVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = add i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
        assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AddDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_DOUBLE, "#23.0", "#42.0", "result"));
		String expectedCode = "define i64 @main() {\n"
				+ "  %result = alloca double\n"
				+ "  %result.0 = fadd double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n"
				+ "}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_AddDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fadd double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	// Sub

	@Test
	@Ignore
	public void generateTargetCodeTest_SubLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_LONG, "#23", "#42", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %result = alloca i64\n" +
				"  %result.0 = sub i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_SubLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_LONG, "longVar1", "longVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = sub i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_SubDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_DOUBLE, "#23.0", "#42.0", "result"));
		String expectedCode = "define i64 @main() {\n"
				+ "  %result = alloca double\n"
				+ "  %result.0 = fsub double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n"
				+ "}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_SubDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fsub double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	// Multiplication

	@Test
	@Ignore
	public void generateTargetCodeTest_MulLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_LONG, "#23", "#42", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %result = alloca i64\n" +
				"  %result.0 = mul i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_MulLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_LONG, "longVar1", "longVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = mul i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_MulDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_DOUBLE, "#23.0", "#42.0", "result"));
		String expectedCode = "define i64 @main() {\n"
				+ "  %result = alloca double\n"
				+ "  %result.0 = fmul double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n"
				+ "}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_MulDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fmul double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	// Division

	@Test
	@Ignore
	public void generateTargetCodeTest_DivLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_LONG, "#23", "#42", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %result = alloca i64\n" +
				"  %result.0 = sdiv i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DivLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_LONG, "longVar1", "longVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = sdiv i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DivDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_DOUBLE, "#23.0", "#42.0", "result"));
		String expectedCode = "define i64 @main() {\n"
				+ "  %result = alloca double\n"
				+ "  %result.0 = fdiv double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n"
				+ "}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	@Test
	@Ignore
	public void generateTargetCodeTest_DivDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String expectedCode = "define i64 @main() {\n" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fdiv double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n" +
				"}\n";
		assertEquals(expectedCode, generateCodeAsString(tac));
	}

	// Util


	private String generateCodeAsString(ArrayList<Quadruple> tac) throws IOException, BackendException {
		String baseFileName = "baseFileName";
		Map<String, InputStream> result = backend.generateTargetCode(baseFileName, tac);
		InputStream module = result.get("baseFileName.ll");
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
