package swp_compiler_ss13.fuc.backend;

import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.IOException;

/**
 * Tests for LLVMBackend: Assignments (unindexed copies) and conversions
 */
public class LLVMBackendAssignTest extends LLVMBackendTest {


	@Test
	public void generateTargetCodeTest_AssignLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_LONG,
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
	public void generateTargetCodeTest_AssignLong_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "longVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_LONG,
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
	public void generateTargetCodeTest_AssignDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_DOUBLE,
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
	public void generateTargetCodeTest_AssignDouble_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_DOUBLE,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "doubleVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_DOUBLE,
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
	public void generateTargetCodeTest_AssignBoolean_Const_False() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_BOOLEAN,
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
	public void generateTargetCodeTest_AssignBoolean_Const_True() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_BOOLEAN,
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
	public void generateTargetCodeTest_AssignBoolean_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_BOOLEAN,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "booleanVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_BOOLEAN,
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
	public void generateTargetCodeTest_AssignString_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_STRING,
			        "#\"\\0Foo\"",
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		String mainFunctionCode = ""
			+ "  %stringVariable = alloca i8*\n"
			+ "  %.string_0 = alloca [5 x i8]\n"
			+ "  store [5 x i8] [i8 0, i8 70, i8 111, i8 111, i8 0], [5 x i8]* %.string_0\n"
			+ "  %stringVariable.0 = getelementptr [5 x i8]* %.string_0, i64 0, i64 0\n"
			+ "  store i8* %stringVariable.0, i8** %stringVariable\n"
			+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_AssignString_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "init"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_STRING,
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument,
			        "stringVariable"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.ASSIGN_STRING,
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

	/* Control flow */

	@Test
	public void generateTargetCodeTest_Return_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.RETURN,
			        "#1",
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument));
		String mainFunctionCode = ""
			+ "  ret i64 1\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_Return_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.DECLARE_LONG,
			        "#1",
			        Quadruple.EmptyArgument,
			        "res"));
		tac.add(new QuadrupleImpl(
			        Quadruple.Operator.RETURN,
			        "res",
			        Quadruple.EmptyArgument,
			        Quadruple.EmptyArgument));
		String mainFunctionCode = ""
			+ "  %res = alloca i64\n"
			+ "  store i64 1, i64* %res\n"
			+ "  %res.0 = load i64* %res\n"
			+ "  ret i64 %res.0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test (expected = BackendException.class)
	public void generateTargetCodeTest_AssignWithoutDeclaration() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
				Quadruple.Operator.DECLARE_LONG,
				Quadruple.EmptyArgument,
				Quadruple.EmptyArgument,
				"longVariable"));
		tac.add(new QuadrupleImpl(
				Quadruple.Operator.ASSIGN_LONG,
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

}
