package swp_compiler_ss13.fuc.ir.test;

import static org.junit.Assert.fail;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;

public class ComplexAssignmentTest {

	private AST ast_array_to_array_one_dim;
	private IntermediateCodeGeneratorImpl irgen;

	@Before
	public void setUp() throws Exception {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("a", new ArrayType(new LongType(), 1));
		f.addDeclaration("b", new ArrayType(new LongType(), 1));
		f.addAssignment(f.newBasicIdentifier("a"), f.newBasicIdentifier("b"));
		this.ast_array_to_array_one_dim = f.getAST();
		this.irgen = new IntermediateCodeGeneratorImpl();
	}

	@Test
	public void test() throws IntermediateCodeGeneratorException {
		try {
			List<Quadruple> q = this.irgen.generateIntermediateCode(this.ast_array_to_array_one_dim);
			System.out.println(q);
			fail("Exception expected");
		} catch (IntermediateCodeGeneratorException e) {
		}
	}

}
