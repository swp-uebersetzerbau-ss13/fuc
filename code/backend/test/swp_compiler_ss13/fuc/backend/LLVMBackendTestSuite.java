package swp_compiler_ss13.fuc.backend;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Test Suite for LLVM Backend
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
		LLVMBackendDeclareTest.class,
		LLVMBackendAssignTest.class,
		LLVMBackendArithmeticTest.class,
		LLVMBackendCompareTest.class,
		LLVMBackendControlAndIOTest.class,
		LLVMBackendArrayTest.class,
		ModuleTest.class,
		BackendRuntimeTest.class
})
public class LLVMBackendTestSuite {
}
