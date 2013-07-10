package swp_compiler_ss13.fuc.test;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;



@RunWith(Suite.class)
@Suite.SuiteClasses({
		M1RuntimeTest.class,
		M2RuntimeTest.class,
		M3RuntimeTest.class,
		AdditionalRuntimeTest.class})
class SuiteClasses {
}

@RunWith(Categories.class)
@Categories.ExcludeCategory(IgnoredTest.class)
public class TestSuite extends SuiteClasses {
}
