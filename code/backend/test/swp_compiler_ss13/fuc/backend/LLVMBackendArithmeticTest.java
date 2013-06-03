package swp_compiler_ss13.fuc.backend;

import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.IOException;

/**
 * Tests for LLVMBackend: Arithmetic
 */
public class LLVMBackendArithmeticTest extends LLVMBackendTest {

	@Test
	public void generateTargetCodeTest_Return_Const() throws IOException, BackendException {
		tac.add(new Q(
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
		String mainFunctionCode = ""
			+ "  %res = alloca i64\n"
			+ "  store i64 1, i64* %res\n"
			+ "  %res.0 = load i64* %res\n"
			+ "  ret i64 %res.0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test (expected = BackendException.class)
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
		String mainFunctionCode = ""
				+ "  %init = alloca i64\n"
				+ "  %longVariable = alloca i64\n"
				+ "  %init.0 = load i64* %init\n"
				+ "  store i64 %init.0, i64* %longVariable\n"
				+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	/* ARITHMETIC */


	/* Add */

	@Test
	public void generateTargetCodeTest_AddLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "" +
				"  %result = alloca i64\n" +
				"  %result.0 = add i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}


	@Test
	public void generateTargetCodeTest_AddLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = add i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
        expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_AddDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = ""
				+ "  %result = alloca double\n"
				+ "  %result.0 = fadd double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_AddDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.ADD_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fadd double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	/* Sub */

	@Test
	public void generateTargetCodeTest_SubLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "" +
				"  %result = alloca i64\n" +
				"  %result.0 = sub i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_SubLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = sub i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_SubDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = ""
				+ "  %result = alloca double\n"
				+ "  %result.0 = fsub double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_SubDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.SUB_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fsub double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	/* Multiplication */

	@Test
	public void generateTargetCodeTest_MulLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "" +
				"  %result = alloca i64\n" +
				"  %result.0 = mul i64 23, 42\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_MulLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = mul i64 %longVar1.0, %longVar2.0\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_MulDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = ""
				+ "  %result = alloca double\n"
				+ "  %result.0 = fmul double 23.0, 42.0\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_MulDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.MUL_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = fmul double %doubleVar1.0, %doubleVar2.0\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	/* Division */

	@Test
	public void generateTargetCodeTest_DivLong_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "" +
				"  %result = alloca i64\n" +
				"  %result.0 = invoke i64 (i64, i64)* @div_long(i64 23, i64 42) to label %result.0.ok unwind label %UncaughtException\n" +
				"  result.0.ok:\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DivLong_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "longVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_LONG, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "" +
				"  %longVar1 = alloca i64\n" +
				"  %longVar2 = alloca i64\n" +
				"  %result = alloca i64\n" +
				"  %longVar1.0 = load i64* %longVar1\n" +
				"  %longVar2.0 = load i64* %longVar2\n" +
				"  %result.0 = invoke i64 (i64, i64)* @div_long(i64 %longVar1.0, i64 %longVar2.0) to label %result.0.ok unwind label %UncaughtException\n" +
				"  result.0.ok:\n" +
				"  store i64 %result.0, i64* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DivDouble_Const() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = ""
				+ "  %result = alloca double\n"
				+ "  %result.0 = invoke double (double, double)* @div_double(double 23.0, double 42.0) to label %result.0.ok unwind label %UncaughtException\n"
				+ "  result.0.ok:\n"
				+ "  store double %result.0, double* %result\n"
				+ "  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_DivDouble_Var() throws IOException, BackendException {
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar1"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "doubleVar2"));
		tac.add(new Q(Quadruple.Operator.DECLARE_DOUBLE, Quadruple.EmptyArgument, Quadruple.EmptyArgument, "result"));
		tac.add(new Q(Quadruple.Operator.DIV_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "" +
				"  %doubleVar1 = alloca double\n" +
				"  %doubleVar2 = alloca double\n" +
				"  %result = alloca double\n" +
				"  %doubleVar1.0 = load double* %doubleVar1\n" +
				"  %doubleVar2.0 = load double* %doubleVar2\n" +
				"  %result.0 = invoke double (double, double)* @div_double(double %doubleVar1.0, double %doubleVar2.0) to label %result.0.ok unwind label %UncaughtException\n" +
				"  result.0.ok:\n" +
				"  store double %result.0, double* %result\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}
}
