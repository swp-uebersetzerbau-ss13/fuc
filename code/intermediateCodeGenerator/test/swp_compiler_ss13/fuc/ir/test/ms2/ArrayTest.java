package swp_compiler_ss13.fuc.ir.test.ms2;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGenerator;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ArrayTest {

	private List<Quadruple> generateQuadruples(AST ast) throws IntermediateCodeGeneratorException {
		IntermediateCodeGenerator gen = new IntermediateCodeGeneratorImpl();
		return gen.generateIntermediateCode(ast);
	}

	private String textifyQuadruples(List<Quadruple> quadruples) {
		StringBuilder textQuadruples = new StringBuilder();
		for (Quadruple q : quadruples) {
			textQuadruples.append(String.format("(%s|%s|%s|%s)\n", q.getOperator(), q.getArgument1(), q.getArgument2(),
					q.getResult()));
		}
		return textQuadruples.toString();
	}

	private String textifyQuadruples(AST ast) throws IntermediateCodeGeneratorException {
		return this.textifyQuadruples(this.generateQuadruples(ast));
	}

	@Test
	public void itCanDeclareOneDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("array", new ArrayType(new LongType(), 10));

		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanDeclareMultiDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("array", new ArrayType(new ArrayType(new DoubleType(), 3), 10));

		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#3|!|!)" + "\n";
		expected += "(DECLARE_DOUBLE|!|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAccessOneDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		f.addReturn(f.newArrayIdentifier(3, f.newBasicIdentifier("array")));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_LONG|array|#3|" + tmp0 + ")" + "\n";
		expected += "(RETURN|" + tmp0 + "|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAccessMultiDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("array", new ArrayType(new ArrayType(new LongType(), 5), 10));
		f.addReturn(f.newArrayIdentifier(6, f.newArrayIdentifier(3, f.newBasicIdentifier("array"))));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String tmp1 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#5|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_ARRAY|#5|!|" + tmp0 + ")" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(ARRAY_GET_REFERENCE|array|#6|" + tmp0 + ")" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp1 + ")" + "\n";
		expected += "(ARRAY_GET_LONG|" + tmp0 + "|#3|" + tmp1 + ")" + "\n";
		expected += "(RETURN|" + tmp1 + "|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignOneDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		f.addAssignment(f.newArrayIdentifier(5, f.newBasicIdentifier("array")), f.newLiteral("3", new LongType()));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_LONG|array|#5|" + tmp0 + ")" + "\n";
		expected += "(ASSIGN_LONG|#3|!|" + tmp0 + ")" + "\n";

		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

}
