package swp_compiler_ss13.fuc.backend;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

@RunWith(Suite.class)
@Suite.SuiteClasses({
		DeclareTest.class,
		AssignTest.class,
		ArithmeticTest.class,
		LLVMBackendArrayTest.class,
		CompareTest.class,
		ControlAndIOTest.class,
		ModuleTest.class,
		RuntimeTest.class
})
class SuiteClasses {
}

@RunWith(Categories.class)
@Categories.ExcludeCategory(RuntimeTests.class)
public class TestSuite extends SuiteClasses {
}

