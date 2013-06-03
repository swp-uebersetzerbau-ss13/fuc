package swp_compiler_ss13.fuc.backend;

import org.junit.Test;
import swp_compiler_ss13.common.backend.BackendException;
import swp_compiler_ss13.common.backend.Quadruple;

import java.io.IOException;

/**
 * Tests for LLVMBackend: Controll flow and IO
 */
public class LLVMBackendControlAndIOTest extends LLVMBackendTest {

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
}
