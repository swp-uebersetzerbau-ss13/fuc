package swp_compiler_ss13.fuc.backend;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;


interface RuntimeTests{ /* category marker */ }

@RunWith(Categories.class)
@Categories.IncludeCategory(RuntimeTests.class)
public class RuntimeTestSuite extends SuiteClasses {
}
