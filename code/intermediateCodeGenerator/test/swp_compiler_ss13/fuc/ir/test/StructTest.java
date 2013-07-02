package swp_compiler_ss13.fuc.ir.test;

import static org.junit.Assert.assertEquals;

import java.util.List;

import junit.extensions.PA;

import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.backend.Quadruple;
import swp_compiler_ss13.common.ir.IntermediateCodeGeneratorException;
import swp_compiler_ss13.common.types.derived.Member;
import swp_compiler_ss13.common.types.derived.StructType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTFactory;
import swp_compiler_ss13.fuc.ir.IntermediateCodeGeneratorImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class StructTest {

	private AST ast_print_simple_struct;
	private AST ast_print_complex_struct;
	private IntermediateCodeGeneratorImpl irgen;
	private AST ast_assign_test_1;
	private AST ast_assign_test_2;

	@Before
	public void setUp() throws Exception {
		// reset symbol table variable counter
		PA.setValue(SymbolTableImpl.class, "ext", 0);

		StructType simpleStruct = new StructType("simple", new Member[] {
				new Member("name", new StringType(255L)),
				new Member("value", new LongType()) });

		StructType complexStruct = new StructType("complex", new Member[] {
				new Member("a", new StringType(255L)),
				new Member("b", simpleStruct) });
		ASTFactory f;

		f = new ASTFactory();
		f.addDeclaration("s", simpleStruct);
		f.addPrint(f.newStructIdentifier("name", f.newBasicIdentifier("s")));
		f.addPrint(f.newStructIdentifier("value", f.newBasicIdentifier("s")));
		this.ast_print_simple_struct = f.getAST();

		f = new ASTFactory();
		f.addDeclaration("s", complexStruct);
		f.addPrint(f.newStructIdentifier("a", f.newBasicIdentifier("s")));
		f.addPrint(f.newStructIdentifier("value", f.newStructIdentifier("b", f.newBasicIdentifier("s"))));
		this.ast_print_complex_struct = f.getAST();

		f = new ASTFactory();
		f.addDeclaration("s", complexStruct);
		f.addDeclaration("l", new LongType());
		f.addAssignment(f.newBasicIdentifier("l"),
				f.newStructIdentifier("value", f.newStructIdentifier("b", f.newBasicIdentifier("s"))));
		this.ast_assign_test_1 = f.getAST();

		f = new ASTFactory();
		f.addDeclaration("s", complexStruct);
		f.addDeclaration("l", new LongType());
		f.addAssignment(f.newStructIdentifier("value", f.newStructIdentifier("b", f.newBasicIdentifier("s"))),
				f.newBasicIdentifier("l"));
		this.ast_assign_test_2 = f.getAST();

		this.irgen = new IntermediateCodeGeneratorImpl();
	}

	private String getString(List<Quadruple> quadruples) {
		StringBuilder builder = new StringBuilder();
		for (Quadruple q : quadruples) {
			builder.append(q).append("\n");
		}
		return builder.toString();
	}

	@Test
	public void printSimpleStruct() throws IntermediateCodeGeneratorException {
		List<Quadruple> q = this.irgen.generateIntermediateCode(this.ast_print_simple_struct);
		String actual = this.getString(q);
		String expected = "Quadruple: (DECLARE_STRUCT | #2 | ! | s)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | name)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | value)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | tmp0)\n" +
				"Quadruple: (STRUCT_GET_STRING | s | name | tmp0)\n" +
				"Quadruple: (PRINT_STRING | tmp0 | ! | !)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | tmp1)\n" +
				"Quadruple: (STRUCT_GET_LONG | s | value | tmp1)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | tmp2)\n" +
				"Quadruple: (LONG_TO_STRING | tmp1 | ! | tmp2)\n" +
				"Quadruple: (PRINT_STRING | tmp2 | ! | !)\n";
		assertEquals(expected, actual);
	}

	@Test
	public void printComplexStruct() throws IntermediateCodeGeneratorException {
		List<Quadruple> q = this.irgen.generateIntermediateCode(this.ast_print_complex_struct);
		String actual = this.getString(q);
		String expected = "Quadruple: (DECLARE_STRUCT | #2 | ! | s)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | a)\n" +
				"Quadruple: (DECLARE_STRUCT | #2 | ! | b)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | name)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | value)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | tmp0)\n" +
				"Quadruple: (STRUCT_GET_STRING | s | a | tmp0)\n" +
				"Quadruple: (PRINT_STRING | tmp0 | ! | !)\n" +
				"Quadruple: (DECLARE_REFERENCE | ! | ! | tmp1)\n" +
				"Quadruple: (STRUCT_GET_REFERENCE | s | b | tmp1)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | tmp2)\n" +
				"Quadruple: (STRUCT_GET_LONG | tmp1 | value | tmp2)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | tmp3)\n" +
				"Quadruple: (LONG_TO_STRING | tmp2 | ! | tmp3)\n" +
				"Quadruple: (PRINT_STRING | tmp3 | ! | !)\n";
		assertEquals(expected, actual);
	}

	@Test
	public void assignTest1() throws IntermediateCodeGeneratorException {
		List<Quadruple> q = this.irgen.generateIntermediateCode(this.ast_assign_test_1);
		String actual = this.getString(q);
		String expected = "Quadruple: (DECLARE_STRUCT | #2 | ! | s)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | a)\n" +
				"Quadruple: (DECLARE_STRUCT | #2 | ! | b)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | name)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | value)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | l)\n" +
				"Quadruple: (DECLARE_REFERENCE | ! | ! | tmp0)\n" +
				"Quadruple: (STRUCT_GET_REFERENCE | s | b | tmp0)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | tmp1)\n" +
				"Quadruple: (STRUCT_GET_LONG | tmp0 | value | tmp1)\n" +
				"Quadruple: (ASSIGN_LONG | tmp1 | ! | l)\n";
		assertEquals(expected, actual);
	}

	@Test
	public void assignTest2() throws IntermediateCodeGeneratorException {
		List<Quadruple> q = this.irgen.generateIntermediateCode(this.ast_assign_test_2);
		String actual = this.getString(q);
		String expected = "Quadruple: (DECLARE_STRUCT | #2 | ! | s)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | a)\n" +
				"Quadruple: (DECLARE_STRUCT | #2 | ! | b)\n" +
				"Quadruple: (DECLARE_STRING | ! | ! | name)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | value)\n" +
				"Quadruple: (DECLARE_LONG | ! | ! | l)\n" +
				"Quadruple: (DECLARE_REFERENCE | ! | ! | tmp0)\n" +
				"Quadruple: (STRUCT_GET_REFERENCE | s | b | tmp0)\n" +
				"Quadruple: (STRUCT_SET_LONG | tmp0 | value | l)\n";
		assertEquals(expected, actual);
	}

}
