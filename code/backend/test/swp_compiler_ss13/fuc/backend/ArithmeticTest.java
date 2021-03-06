package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.BackendException;
import java.io.IOException;

import static swp_compiler_ss13.common.backend.Quadruple.*;

/**
 * Tests for LLVMBackend: Arithmetic
 */
public class ArithmeticTest extends TestBase {


	/* Add */

	@org.junit.Test
	public void generateTargetCodeTest_AddLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.ADD_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "  %result = alloca i64\n  store i64 0, i64* %result\n  %result.0 = add i64 23, 42\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}


	@org.junit.Test
	public void generateTargetCodeTest_AddLong_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.ADD_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "  %longVar1 = alloca i64\n  store i64 0, i64* %longVar1\n  %longVar2 = alloca i64\n  store i64 0, i64* %longVar2\n  %result = alloca i64\n  store i64 0, i64* %result\n  %longVar1.0 = load i64* %longVar1\n  %longVar2.0 = load i64* %longVar2\n  %result.0 = add i64 %longVar1.0, %longVar2.0\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
        expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_AddDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.ADD_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = "  %result = alloca double\n  store double 0.0, double* %result\n  %result.0 = fadd double 23.0, 42.0\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_AddDouble_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.ADD_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "  %doubleVar1 = alloca double\n  store double 0.0, double* %doubleVar1\n  %doubleVar2 = alloca double\n  store double 0.0, double* %doubleVar2\n  %result = alloca double\n  store double 0.0, double* %result\n  %doubleVar1.0 = load double* %doubleVar1\n  %doubleVar2.0 = load double* %doubleVar2\n  %result.0 = fadd double %doubleVar1.0, %doubleVar2.0\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	/* Sub */

	@org.junit.Test
	public void generateTargetCodeTest_SubLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.SUB_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "  %result = alloca i64\n  store i64 0, i64* %result\n  %result.0 = sub i64 23, 42\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_SubLong_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.SUB_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "  %longVar1 = alloca i64\n  store i64 0, i64* %longVar1\n  %longVar2 = alloca i64\n  store i64 0, i64* %longVar2\n  %result = alloca i64\n  store i64 0, i64* %result\n  %longVar1.0 = load i64* %longVar1\n  %longVar2.0 = load i64* %longVar2\n  %result.0 = sub i64 %longVar1.0, %longVar2.0\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_SubDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.SUB_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = "  %result = alloca double\n  store double 0.0, double* %result\n  %result.0 = fsub double 23.0, 42.0\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_SubDouble_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.SUB_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "  %doubleVar1 = alloca double\n  store double 0.0, double* %doubleVar1\n  %doubleVar2 = alloca double\n  store double 0.0, double* %doubleVar2\n  %result = alloca double\n  store double 0.0, double* %result\n  %doubleVar1.0 = load double* %doubleVar1\n  %doubleVar2.0 = load double* %doubleVar2\n  %result.0 = fsub double %doubleVar1.0, %doubleVar2.0\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	/* Multiplication */

	@org.junit.Test
	public void generateTargetCodeTest_MulLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.MUL_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "  %result = alloca i64\n  store i64 0, i64* %result\n  %result.0 = mul i64 23, 42\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_MulLong_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.MUL_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "  %longVar1 = alloca i64\n  store i64 0, i64* %longVar1\n  %longVar2 = alloca i64\n  store i64 0, i64* %longVar2\n  %result = alloca i64\n  store i64 0, i64* %result\n  %longVar1.0 = load i64* %longVar1\n  %longVar2.0 = load i64* %longVar2\n  %result.0 = mul i64 %longVar1.0, %longVar2.0\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_MulDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.MUL_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = "  %result = alloca double\n  store double 0.0, double* %result\n  %result.0 = fmul double 23.0, 42.0\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_MulDouble_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.MUL_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "  %doubleVar1 = alloca double\n  store double 0.0, double* %doubleVar1\n  %doubleVar2 = alloca double\n  store double 0.0, double* %doubleVar2\n  %result = alloca double\n  store double 0.0, double* %result\n  %doubleVar1.0 = load double* %doubleVar1\n  %doubleVar2.0 = load double* %doubleVar2\n  %result.0 = fmul double %doubleVar1.0, %doubleVar2.0\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	/* Division */

	@org.junit.Test
	public void generateTargetCodeTest_DivLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.DIV_LONG, "#23", "#42", "result"));
		String mainFunctionCode = "  %result = alloca i64\n  store i64 0, i64* %result\n  %result.0 = invoke i64 (i64, i64)* @div_long(i64 23, i64 42) to label %result.0.ok unwind label %UncaughtException\n  result.0.ok:\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_DivLong_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "longVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.DIV_LONG, "longVar1", "longVar2", "result"));
		String mainFunctionCode = "  %longVar1 = alloca i64\n  store i64 0, i64* %longVar1\n  %longVar2 = alloca i64\n  store i64 0, i64* %longVar2\n  %result = alloca i64\n  store i64 0, i64* %result\n  %longVar1.0 = load i64* %longVar1\n  %longVar2.0 = load i64* %longVar2\n  %result.0 = invoke i64 (i64, i64)* @div_long(i64 %longVar1.0, i64 %longVar2.0) to label %result.0.ok unwind label %UncaughtException\n  result.0.ok:\n  store i64 %result.0, i64* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test(expected = BackendException.class)
	public void generateTargetCodeTest_DivThroughZero_Long() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.DIV_LONG, "#23", "#0", "result"));
		generateCodeAsString(tac);
	}

	@org.junit.Test
	public void generateTargetCodeTest_DivDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.DIV_DOUBLE, "#23.0", "#42.0", "result"));
		String mainFunctionCode = "  %result = alloca double\n  store double 0.0, double* %result\n  %result.0 = invoke double (double, double)* @div_double(double 23.0, double 42.0) to label %result.0.ok unwind label %UncaughtException\n  result.0.ok:\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_DivDouble_Var() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "doubleVar2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.DIV_DOUBLE, "doubleVar1", "doubleVar2", "result"));
		String mainFunctionCode = "  %doubleVar1 = alloca double\n  store double 0.0, double* %doubleVar1\n  %doubleVar2 = alloca double\n  store double 0.0, double* %doubleVar2\n  %result = alloca double\n  store double 0.0, double* %result\n  %doubleVar1.0 = load double* %doubleVar1\n  %doubleVar2.0 = load double* %doubleVar2\n  %result.0 = invoke double (double, double)* @div_double(double %doubleVar1.0, double %doubleVar2.0) to label %result.0.ok unwind label %UncaughtException\n  result.0.ok:\n  store double %result.0, double* %result\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test(expected = BackendException.class)
	public void generateTargetCodeTest_DivThroughZero_DOUBLE() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, EmptyArgument, EmptyArgument, "result"));
		tac.add(new QuadrupleImpl(Operator.DIV_DOUBLE, "#23.0", "#0.0", "result"));
		generateCodeAsString(tac);
	}

	/* Boolean Arithmetic */

	@org.junit.Test
	public void generateTargetCodeTest_NotBoolean_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Operator.NOT_BOOLEAN, "#FALSE", EmptyArgument, "res"));
			String mainFunctionCode = "  %res = alloca i1\n  store i1 0, i1* %res\n  %res.0 = sub i1 1, 0\n  store i1 %res.0, i1* %res\n  ret i64 0\n";
			expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_NotBoolean_Var() throws Exception {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "b"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Operator.NOT_BOOLEAN, "b", EmptyArgument, "res"));
		String mainFunctionCode = "  %b = alloca i1\n  store i1 0, i1* %b\n  %res = alloca i1\n  store i1 0, i1* %res\n  %b.0 = load i1* %b\n  %res.0 = sub i1 1, %b.0\n  store i1 %res.0, i1* %res\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_OrBoolean_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Operator.OR_BOOLEAN, "#FALSE", "#TRUE", "res"));
		String mainFunctionCode = "  %res = alloca i1\n  store i1 0, i1* %res\n  %res.0 = or i1 0, 1\n  store i1 %res.0, i1* %res\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_OrBoolean_Var() throws Exception {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "b1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "b2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Operator.OR_BOOLEAN, "b1", "b2", "res"));
		String mainFunctionCode = "  %b1 = alloca i1\n  store i1 0, i1* %b1\n  %b2 = alloca i1\n  store i1 0, i1* %b2\n  %res = alloca i1\n  store i1 0, i1* %res\n  %b1.0 = load i1* %b1\n  %b2.0 = load i1* %b2\n  %res.0 = or i1 %b1.0, %b2.0\n  store i1 %res.0, i1* %res\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_AndBoolean_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Operator.AND_BOOLEAN, "#FALSE", "#TRUE", "res"));
		String mainFunctionCode = "  %res = alloca i1\n  store i1 0, i1* %res\n  %res.0 = and i1 0, 1\n  store i1 %res.0, i1* %res\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_AndBoolean_Var() throws Exception {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "b1"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "b2"));
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Operator.AND_BOOLEAN, "b1", "b2", "res"));
		String mainFunctionCode = "  %b1 = alloca i1\n  store i1 0, i1* %b1\n  %b2 = alloca i1\n  store i1 0, i1* %b2\n  %res = alloca i1\n  store i1 0, i1* %res\n  %b1.0 = load i1* %b1\n  %b2.0 = load i1* %b2\n  %res.0 = and i1 %b1.0, %b2.0\n  store i1 %res.0, i1* %res\n  ret i64 0\n";
		expectMain("", mainFunctionCode, generateCodeAsString(tac));
	}
}


