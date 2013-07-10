package swp_compiler_ss13.fuc.semantic_analyser;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({ ArithmeticExpressionTests.class, AssignmentTests.class,
		BranchTests.class, LogicExpressionTests.class, LoopTests.class,
		OtherTests.class, M1Tests.class, M2Tests.class,
		RelationExpressionTests.class, ArrayTests.class, StructTests.class,
		M3Tests.class })
public class SemanticAnalyserTestSuite {

}
