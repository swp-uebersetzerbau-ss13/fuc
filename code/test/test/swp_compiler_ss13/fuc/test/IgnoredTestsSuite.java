package swp_compiler_ss13.fuc.test;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

interface IgnoredTest { /* category marker */ }

@RunWith(Categories.class)
@Categories.IncludeCategory(IgnoredTest.class)
public class IgnoredTestsSuite extends SuiteClasses {

}
