package swp_compiler_ss13.fuc.test;

import org.junit.experimental.categories.Categories;
import org.junit.runner.RunWith;

interface IgnoredTest {
	/* category marker */
	/* tests can be ignored with @Category(IgnoredTest.class) */
}

@RunWith(Categories.class)
@Categories.IncludeCategory(IgnoredTest.class)
public class IgnoredTestsSuite extends SuiteClasses {

}
