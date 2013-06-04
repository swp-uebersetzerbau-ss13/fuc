package swp_compiler_ss13.fuc.backend;

import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static swp_compiler_ss13.common.backend.Quadruple.EmptyArgument;
import static swp_compiler_ss13.common.backend.Quadruple.Operator;

/**
 * Tests for LLVMBackend: Controll flow and IO
 */
public class LLVMBackendControlAndIOTest extends LLVMBackendTest {

	@Test
	public void generateTargetCodeTest_Return_Const() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(
			        Operator.RETURN,
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
			        Operator.DECLARE_LONG,
			        "#1",
			        Quadruple.EmptyArgument,
			        "res"));
		tac.add(new QuadrupleImpl(
			        Operator.RETURN,
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

	@Test
	public void generateTargetCodeTest_BranchTrue() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#TRUE", EmptyArgument, "cond"));
		tac.add(new QuadrupleImpl(Operator.BRANCH, "true", "false", "cond"));
		tac.add(new QuadrupleImpl(Operator.LABEL, "true", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.PRINT_STRING, "#\"true branch\"", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.BRANCH, "end", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.LABEL, "false", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.PRINT_STRING, "#\"false branch\"", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.BRANCH, "end", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.LABEL, "end", EmptyArgument, EmptyArgument));
		String mainFunctionCode = "" +
				"  %cond = alloca i1\n" +
				"  store i1 1, i1* %cond\n" +
				"  %cond.0 = load i1* %cond\n" +
				"  br i1 %cond.0, label %true, label %false\n" +
				"  true:\n" +
				"  %.string_0 = alloca [12 x i8]\n" +
				"  store [12 x i8] [i8 116, i8 114, i8 117, i8 101, i8 32, i8 98, i8 114, i8 97, i8 110, i8 99, i8 104, i8 0], [12 x i8]* %.string_0\n" +
				"  %.tmp.0 = getelementptr [12 x i8]* %.string_0, i64 0, i64 0\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.0)\n" +
				"  br label %end\n" +
				"  false:\n" +
				"  %.string_1 = alloca [13 x i8]\n" +
				"  store [13 x i8] [i8 102, i8 97, i8 108, i8 115, i8 101, i8 32, i8 98, i8 114, i8 97, i8 110, i8 99, i8 104, i8 0], [13 x i8]* %.string_1\n" +
				"  %.tmp.1 = getelementptr [13 x i8]* %.string_1, i64 0, i64 0\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.1)\n" +
				"  br label %end\n" +
				"  end:\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_BranchFalse() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "cond"));
		tac.add(new QuadrupleImpl(Operator.BRANCH, "true", "false", "cond"));
		tac.add(new QuadrupleImpl(Operator.LABEL, "true", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.PRINT_STRING, "#\"true branch\"", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.BRANCH, "end", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.LABEL, "false", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.PRINT_STRING, "#\"false branch\"", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.BRANCH, "end", EmptyArgument, EmptyArgument));
		tac.add(new QuadrupleImpl(Operator.LABEL, "end", EmptyArgument, EmptyArgument));
		String mainFunctionCode = "" +
				"  %cond = alloca i1\n" +
				"  store i1 0, i1* %cond\n" +
				"  %cond.0 = load i1* %cond\n" +
				"  br i1 %cond.0, label %true, label %false\n" +
				"  true:\n" +
				"  %.string_0 = alloca [12 x i8]\n" +
				"  store [12 x i8] [i8 116, i8 114, i8 117, i8 101, i8 32, i8 98, i8 114, i8 97, i8 110, i8 99, i8 104, i8 0], [12 x i8]* %.string_0\n" +
				"  %.tmp.0 = getelementptr [12 x i8]* %.string_0, i64 0, i64 0\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.0)\n" +
				"  br label %end\n" +
				"  false:\n" +
				"  %.string_1 = alloca [13 x i8]\n" +
				"  store [13 x i8] [i8 102, i8 97, i8 108, i8 115, i8 101, i8 32, i8 98, i8 114, i8 97, i8 110, i8 99, i8 104, i8 0], [13 x i8]* %.string_1\n" +
				"  %.tmp.1 = getelementptr [13 x i8]* %.string_1, i64 0, i64 0\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.1)\n" +
				"  br label %end\n" +
				"  end:\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}


	@Test
	public void generateTargetCodeTest_PrintLong() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_LONG, "#2", EmptyArgument, "l1"));
		tac.add(new QuadrupleImpl(Operator.PRINT_LONG, "l1", EmptyArgument, EmptyArgument));
		String mainFunctionCode = "" +
				"  %l1 = alloca i64\n" +
				"  store i64 2, i64* %l1\n" +
				"  %l1.0 = load i64* %l1\n" +
				"  %.tmp.0 = call i8* (i64)* @ltoa(i64 %l1.0)\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.0)\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_PrintDouble() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_DOUBLE, "#2.0", EmptyArgument, "d1"));
		tac.add(new QuadrupleImpl(Operator.PRINT_DOUBLE, "d1", EmptyArgument, EmptyArgument));
		String mainFunctionCode = "" +
				"  %d1 = alloca double\n" +
				"  store double 2.0, double* %d1\n" +
				"  %d1.0 = load double* %d1\n" +
				"  %.tmp.0 = call i8* (double)* @dtoa(double %d1.0)\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.0)\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_PrintBoolean() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_BOOLEAN, "#FALSE", EmptyArgument, "b1"));
		tac.add(new QuadrupleImpl(Operator.PRINT_BOOLEAN, "b1", EmptyArgument, EmptyArgument));
		String mainFunctionCode = "" +
				"  %b1 = alloca i1\n" +
				"  store i1 0, i1* %b1\n" +
				"  %b1.0 = load i1* %b1\n" +
				"  %.tmp.0 = call i8* (i1)* @btoa(i1 %b1.0)\n" +
				"  call i32 (i8*, ...)* @printf(i8* %.tmp.0)\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

	@Test
	public void generateTargetCodeTest_PrintString() throws IOException, BackendException {
		tac.add(new QuadrupleImpl(Operator.DECLARE_STRING, "#\"bla\"", EmptyArgument, "s1"));
		tac.add(new QuadrupleImpl(Operator.PRINT_STRING, "s1", EmptyArgument, EmptyArgument));
		String mainFunctionCode = "" +
				"  %s1 = alloca i8*\n" +
				"  %.string_0 = alloca [4 x i8]\n" +
				"  store [4 x i8] [i8 98, i8 108, i8 97, i8 0], [4 x i8]* %.string_0\n" +
				"  %s1.0 = getelementptr [4 x i8]* %.string_0, i64 0, i64 0\n" +
				"  store i8* %s1.0, i8** %s1\n" +
				"  %s1.1 = load i8** %s1\n" +
				"  call i32 (i8*, ...)* @printf(i8* %s1.1)\n" +
				"  ret i64 0\n";
		expectMain(mainFunctionCode, generateCodeAsString(tac));
	}

}
