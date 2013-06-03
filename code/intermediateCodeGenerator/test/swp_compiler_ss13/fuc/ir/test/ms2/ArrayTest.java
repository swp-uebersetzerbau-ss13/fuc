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
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));

		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanDeclareMultiDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[3][10];
		f.addDeclaration("array", new ArrayType(new ArrayType(new DoubleType(), 3), 10));

		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#3|!|!)" + "\n";
		expected += "(DECLARE_DOUBLE|!|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanReturnOneDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// return array[3];
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
	public void itCanReturnMultiDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[5][10];
		f.addDeclaration("array", new ArrayType(new ArrayType(new LongType(), 5), 10));
		// return array[3][6];
		f.addReturn(f.newArrayIdentifier(6, f.newArrayIdentifier(3, f.newBasicIdentifier("array"))));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String tmp1 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#5|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_REFERENCE|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_REFERENCE|array|#3|" + tmp0 + ")" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp1 + ")" + "\n";
		expected += "(ARRAY_GET_LONG|" + tmp0 + "|#6|" + tmp1 + ")" + "\n";
		expected += "(RETURN|" + tmp1 + "|!|!)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignConstantToOneDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// array[5] = 3;
		f.addAssignment(f.newArrayIdentifier(5, f.newBasicIdentifier("array")), f.newLiteral("3", new LongType()));

		Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(ARRAY_SET_LONG|array|#5|#3)" + "\n";

		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignConstantToMultiDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[5][10];
		f.addDeclaration("array", new ArrayType(new ArrayType(new LongType(), 5), 10));
		// array[3][6] = 9;
		f.addAssignment(f.newArrayIdentifier(6, f.newArrayIdentifier(3, f.newBasicIdentifier("array"))),
				f.newLiteral("9", new LongType()));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#5|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_REFERENCE|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_REFERENCE|array|#3|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_SET_LONG|" + tmp0 + "|#6|#9)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignVariableToOneDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// Long variable;
		f.addDeclaration("variable", new LongType());
		// array[5] = variable;
		f.addAssignment(f.newArrayIdentifier(5, f.newBasicIdentifier("array")), f.newBasicIdentifier("variable"));

		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|variable)" + "\n";
		expected += "(ARRAY_SET_LONG|array|#5|variable)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignVariableToMultiDimensionalArrays() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[5][10];
		f.addDeclaration("array", new ArrayType(new ArrayType(new LongType(), 5), 10));
		// Long variable;
		f.addDeclaration("variable", new LongType());
		// array[3][6] = variable;
		f.addAssignment(f.newArrayIdentifier(6, f.newArrayIdentifier(3, f.newBasicIdentifier("array"))),
				f.newBasicIdentifier("variable"));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#5|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|variable)" + "\n";
		expected += "(DECLARE_REFERENCE|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_REFERENCE|array|#3|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_SET_LONG|" + tmp0 + "|#6|variable)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignOneDimensionalArrayToVariable() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// Long variable;
		f.addDeclaration("variable", new LongType());
		// variable = array[7];
		f.addAssignment(f.newBasicIdentifier("variable"), f.newArrayIdentifier(7, f.newBasicIdentifier("array")));

		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|variable)" + "\n";
		expected += "(ARRAY_GET_LONG|array|#7|variable)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignMultiDimensionalArrayToVariable() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[5][10];
		f.addDeclaration("array", new ArrayType(new ArrayType(new LongType(), 5), 10));
		// Long variable;
		f.addDeclaration("variable", new LongType());
		// array[3][6] = variable;
		f.addAssignment(f.newArrayIdentifier(6, f.newArrayIdentifier(3, f.newBasicIdentifier("array"))),
				f.newBasicIdentifier("variable"));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_ARRAY|#5|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|variable)" + "\n";
		expected += "(DECLARE_REFERENCE|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_REFERENCE|array|#3|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_SET_LONG|" + tmp0 + "|#6|variable)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignArrayToArray() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// Long arrei[6];
		f.addDeclaration("arrei", new ArrayType(new LongType(), 6));
		// array[5] = arrei[3];
		f.addAssignment(f.newArrayIdentifier(5, f.newBasicIdentifier("array")),
				f.newArrayIdentifier(3, f.newBasicIdentifier("arrei")));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_ARRAY|#6|!|arrei)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_LONG|arrei|#3|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_SET_LONG|array|#5|" + tmp0 + ")" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignArrayToVariableWithCast() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// Double variable;
		f.addDeclaration("variable", new DoubleType());
		// variable = array[7];
		f.addAssignment(f.newBasicIdentifier("variable"), f.newArrayIdentifier(7, f.newBasicIdentifier("array")));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_DOUBLE|!|!|variable)" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_LONG|array|#7|" + tmp0 + ")" + "\n";
		expected += "(LONG_TO_DOUBLE|" + tmp0 + "|!|variable)" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignVariableToArrayWithCast() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// Double variable;
		f.addDeclaration("variable", new DoubleType());
		// array[7] = variable;
		f.addAssignment(f.newArrayIdentifier(7, f.newBasicIdentifier("array")), f.newBasicIdentifier("variable"));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_DOUBLE|!|!|variable)" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp0 + ")" + "\n";
		expected += "(DOUBLE_TO_LONG|variable|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_SET_LONG|array|#7|" + tmp0 + ")" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}

	@Test
	public void itCanAssignArrayToArrayWithCast() throws IntermediateCodeGeneratorException {
		ASTFactory f = new ASTFactory();
		// Long array[10];
		f.addDeclaration("array", new ArrayType(new LongType(), 10));
		// Double arrei[6];
		f.addDeclaration("arrei", new ArrayType(new DoubleType(), 6));
		// array[5] = arrei[3];
		f.addAssignment(f.newArrayIdentifier(5, f.newBasicIdentifier("array")),
				f.newArrayIdentifier(3, f.newBasicIdentifier("arrei")));

		long tmp_n = Long.parseLong(new SymbolTableImpl().getNextFreeTemporary().replaceFirst("\\D*", ""));
		String tmp0 = "tmp" + ++tmp_n;
		String tmp1 = "tmp" + ++tmp_n;
		String expected = "";
		expected += "(DECLARE_ARRAY|#10|!|array)" + "\n";
		expected += "(DECLARE_LONG|!|!|!)" + "\n";
		expected += "(DECLARE_ARRAY|#6|!|arrei)" + "\n";
		expected += "(DECLARE_DOUBLE|!|!|!)" + "\n";
		expected += "(DECLARE_DOUBLE|!|!|" + tmp0 + ")" + "\n";
		expected += "(ARRAY_GET_DOUBLE|arrei|#3|" + tmp0 + ")" + "\n";
		expected += "(DECLARE_LONG|!|!|" + tmp1 + ")" + "\n";
		expected += "(DOUBLE_TO_LONG|" + tmp0 + "|!|" + tmp1 + ")" + "\n";
		expected += "(ARRAY_SET_LONG|array|#5|" + tmp1 + ")" + "\n";
		assertEquals(expected, this.textifyQuadruples(f.getAST()));
	}
}
