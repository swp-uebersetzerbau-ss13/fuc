package swp_compiler_ss13.fuc.backend;

import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.IOException;

import static swp_compiler_ss13.common.backend.Quadruple.EmptyArgument;

/**
 * Tests for LLVMBackend: Relops (comparisons)
 */
public class CompareTest extends TestBase {

	@org.junit.Test
	public void generateTargetCodeTest_CompareEqualLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_E, "#23", "#42", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  icmp eq i64 23, 42\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareGreaterLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_G, "#23", "#42", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  icmp sgt i1 23, 42\n" +
				"  store i64 %res.0, i64* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareLessLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_L, "#23", "#42", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  icmp slt i64 23, 42\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareGreaterEqualLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_GE, "#23", "#42", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  icmp sge i64 23, 42\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareLessEqualLong_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_LE, "#23", "#42", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  icmp sle i64 23, 42\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

//	@Test
//	@Ignore
//	public void generateTargetCodeTest_CompareEqualLong_Var() throws IOException, BackendException {
//		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "l1"));
//		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "l2"));
//		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_LONG, EmptyArgument, EmptyArgument, "res"));
//		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_LONG_E, "longVar1", "longVar2", "result"));
//		String mainFunctionCode = "" +
//				"  %longVar1 = alloca i64\n" +
//				"  %longVar2 = alloca i64\n" +
//				"  %result = alloca i64\n" +
//				"  %longVar1.0 = load i64* %longVar1\n" +
//				"  %longVar2.0 = load i64* %longVar2\n" +
//				"  %result.0 = add i64 %longVar1.0, %longVar2.0\n" +
//				"  store i64 %result.0, i64* %result\n" +
//				"  ret i64 0\n";
//		expectMain(mainFunctionCode, generateCodeAsString(tac));
//	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareEqualDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_DOUBLE_E, "#23.0", "#42.0", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  fcmp oeq double 23.0, 42.0\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareGreaterDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_DOUBLE_G, "#23.0", "#42.0", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  fcmp ogt double 23.0, 42.0\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareLessDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_DOUBLE_L, "#23.0", "#42.0", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  fcmp olt double 23.0, 42.0\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareGreaterEqualDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_DOUBLE_GE, "#23.0", "#42.0", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  fcmp oge double 23.0, 42.0\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@org.junit.Test
	public void generateTargetCodeTest_CompareLessEqualDouble_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Quadruple.Operator.DECLARE_BOOLEAN, EmptyArgument, EmptyArgument, "res"));
		tac.add(new QuadrupleImpl(Quadruple.Operator.COMPARE_DOUBLE_LE, "#23.0", "#42.0", "res"));
		String mainFunctionCode = "" +
				"  %res = alloca i1\n" +
				"  %res.0 =  fcmp ole double 23.0, 42.0\n" +
				"  store i1 %res.0, i1* %res\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

}
