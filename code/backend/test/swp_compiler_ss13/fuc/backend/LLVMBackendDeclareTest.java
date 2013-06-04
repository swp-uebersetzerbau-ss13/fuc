package swp_compiler_ss13.fuc.backend;

import org.junit.*;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Tests for LLVMBackend: Declarations
 */
public class LLVMBackendDeclareTest extends LLVMBackendTest {

	@Test
	public void generateTargetCodeTest_DeclareLong() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String mainFunctionCode = "  %longVariable = alloca i64\n  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));

	}

	@Test
	public void generateTargetCodeTest_DeclareLong_InitConst() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        "#0",
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String mainFunctionCode = ""
			+ "  %longVariable = alloca i64\n"
			+ "  store i64 0, i64* %longVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));

	}

	@Test
	public void generateTargetCodeTest_DeclareLong_InitVar() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        "init",
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String mainFunctionCode = ""
			+ "  %init = alloca i64\n"
			+ "  %longVariable = alloca i64\n"
			+ "  %init.0 = load i64* %init\n"
			+ "  store i64 %init.0, i64* %longVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareDouble() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String mainFunctionCode = "  %doubleVariable = alloca double\n  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareDouble_InitConst() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        "#0.0",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String mainFunctionCode = ""
			+ "  %doubleVariable = alloca double\n"
			+ "  store double 0.0, double* %doubleVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareDouble_InitVar() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        "init",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String mainFunctionCode = ""
			+ "  %init = alloca double\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %init.0 = load double* %init\n"
			+ "  store double %init.0, double* %doubleVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareBoolean() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "boolVariable"));
		String mainFunctionCode = "  %boolVariable = alloca i1\n  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareBoolean_InitConst_False() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        "#FALSE",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String mainFunctionCode = ""
			+ "  %booleanVariable = alloca i1\n"
			+ "  store i1 0, i1* %booleanVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareBoolean_InitConst_True() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        "#TRUE",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String mainFunctionCode = ""
			+ "  %booleanVariable = alloca i1\n"
			+ "  store i1 1, i1* %booleanVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareBoolean_InitVar() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        "init",
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		String mainFunctionCode = ""
			+ "  %init = alloca i1\n"
			+ "  %booleanVariable = alloca i1\n"
			+ "  %init.0 = load i1* %init\n"
			+ "  store i1 %init.0, i1* %booleanVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareString() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String mainFunctionCode = "  %stringVariable = alloca i8*\n  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareString_InitConst() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        "#\"Foo\"",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String mainFunctionCode = ""
			+ "  %stringVariable = alloca i8*\n"
			+ "  %.string_0 = alloca [4 x i8]\n"
			+ "  store [4 x i8] c\"Foo\\00\", [4 x i8]* %.string_0\n"
			+ "  %stringVariable.0 = getelementptr [4 x i8]* %.string_0, i64 0, i64 0\n"
			+ "  store i8* %stringVariable.0, i8** %stringVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DeclareString_InitVar() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        "init",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String mainFunctionCode = ""
			+ "  %init = alloca i8*\n"
			+ "  %stringVariable = alloca i8*\n"
			+ "  %init.0 = load i8** %init\n"
			+ "  store i8* %init.0, i8** %stringVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	/* Conversion */

	@Test
	public void generateTargetCodeTest_LongToDouble() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.LONG_TO_DOUBLE,
			        "longVariable",
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		String mainFunctionCode = ""
			+ "  %longVariable = alloca i64\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %longVariable.0 = load i64* %longVariable\n"
			+ "  %doubleVariable.0 = sitofp i64 %longVariable.0 to double\n"
			+ "  store double %doubleVariable.0, double* %doubleVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DoubleToLong() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DOUBLE_TO_LONG,
			        "doubleVariable",
			        Quadruple.EmptyArgument,
			        "longVariable"));
		String mainFunctionCode = ""
			+ "  %longVariable = alloca i64\n"
			+ "  %doubleVariable = alloca double\n"
			+ "  %doubleVariable.0 = load double* %doubleVariable\n"
			+ "  %longVariable.0 = fptosi double %doubleVariable.0 to i64\n"
			+ "  store i64 %longVariable.0, i64* %longVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

}
