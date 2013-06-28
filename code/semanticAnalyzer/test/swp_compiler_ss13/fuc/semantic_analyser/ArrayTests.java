package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
<<<<<<< HEAD
import swp_compiler_ss13.fuc.errorLog.LogEntry;
=======
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
>>>>>>> origin/master
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ArrayTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public ArrayTests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyser = new SemanticAnalyser();
		analyser.setReportLog(log);
	}

	@After
	public void tearDown() {
		analyser = null;
		log = null;
	}
	
	/**
	 * # error: assignment of bool-array-field to long<br/>
	 * bool [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = a[0];
	 */
	@Test
	public void testAssignmentFromArrayTypeError() {
		// bool [1] a;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new ArrayType(new BooleanType(), 1));

		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// l = a[0];
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_a1 = new BasicIdentifierNodeImpl();
		identifier_a1.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a1 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a1.setIdentifierNode(identifier_a1);
		arrayIdentifier_a1.setIndex(0);
		identifier_a1.setParentNode(arrayIdentifier_a1);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(arrayIdentifier_a1);
		identifier_l.setParentNode(assignment_l);
		arrayIdentifier_a1.setParentNode(assignment_l);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new ArrayType(new BooleanType(), 1));
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}
	
	/**
	 * # error: assignment of long to bool-array-field<br/>
	 * bool [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * a[0] = l;
	 */
	@Test
	public void testAssignmentToArrayTypeError() {
		// bool [1] a;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new ArrayType(new BooleanType(), 1));

		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// a[0] = l;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_a2 = new BasicIdentifierNodeImpl();
		identifier_a2.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a2 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a2.setIdentifierNode(identifier_a2);
		arrayIdentifier_a2.setIndex(0);
		identifier_a2.setParentNode(arrayIdentifier_a2);

		AssignmentNode assignment_a = new AssignmentNodeImpl();
		assignment_a.setLeftValue(arrayIdentifier_a2);
		assignment_a.setRightValue(identifier_l);
		identifier_l.setParentNode(assignment_a);
		arrayIdentifier_a2.setParentNode(assignment_a);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new ArrayType(new BooleanType(), 1));
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_a);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
		assignment_a.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		List<LogEntry> errors = log.getErrors();
		assertEquals(errors.size(), 1);
		assertEquals(errors.get(0).getReportType(), ReportType.TYPE_MISMATCH);
	}

	/**
	 * # no errors expected<br/>
	 * long [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = a[0];<br/>
	 * a[0] = l;
	 */
	@Test
	public void testArrayAssignments() {
		// long [1] a;
		DeclarationNode declaration_a = new DeclarationNodeImpl();
		declaration_a.setIdentifier("a");
		declaration_a.setType(new ArrayType(new LongType(), 1));

		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// l = a[0];
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");
		BasicIdentifierNode identifier_a1 = new BasicIdentifierNodeImpl();
		identifier_a1.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a1 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a1.setIdentifierNode(identifier_a1);
<<<<<<< HEAD
		arrayIdentifier_a1.setIndex(0);
		identifier_a1.setParentNode(arrayIdentifier_a1);
=======
		
		LiteralNode value = new LiteralNodeImpl();
		value.setLiteral("0");
		value.setLiteralType(new LongType());
		arrayIdentifier_a1.setIndexNode(value);
>>>>>>> origin/master

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l1);
		assignment_l.setRightValue(arrayIdentifier_a1);
		identifier_l1.setParentNode(assignment_l);
		arrayIdentifier_a1.setParentNode(assignment_l);

		// a[0] = l;
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		BasicIdentifierNode identifier_a2 = new BasicIdentifierNodeImpl();
		identifier_a2.setIdentifier("a");
		ArrayIdentifierNode arrayIdentifier_a2 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a2.setIdentifierNode(identifier_a2);
<<<<<<< HEAD
		arrayIdentifier_a2.setIndex(0);
		identifier_a2.setParentNode(arrayIdentifier_a2);
=======
		
		LiteralNode value2 = new LiteralNodeImpl();
		value2.setLiteral("0");
		value2.setLiteralType(new LongType());
		arrayIdentifier_a2.setIndexNode(value2);
>>>>>>> origin/master

		AssignmentNode assignment_a = new AssignmentNodeImpl();
		assignment_a.setLeftValue(arrayIdentifier_a2);
		assignment_a.setRightValue(identifier_l2);
		identifier_l2.setParentNode(assignment_a);
		arrayIdentifier_a2.setParentNode(assignment_a);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("a", new ArrayType(new LongType(), 1));
		symbolTable.insert("l", new LongType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_a);
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_l);
		blockNode.addStatement(assignment_a);
		blockNode.setSymbolTable(symbolTable);
		declaration_a.setParentNode(blockNode);
		declaration_l.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);
		assignment_a.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		System.out.println(log);
		assertFalse(log.hasErrors());
	}
	
//	/**
//	 * # error: array out-of-bounds access<br/>
//	 * long [1] a;<br/>
//	 * long l;<br/>
//	 * <br/>
//	 * l = a[2];
//	 */
//	@Test
//	public void testOutOfBoundsError() {
//		// long [1] a;
//		DeclarationNode declaration_a = new DeclarationNodeImpl();
//		declaration_a.setIdentifier("a");
//		declaration_a.setType(new ArrayType(new LongType(), 1));
//
//		// long l;
//		DeclarationNode declaration_l = new DeclarationNodeImpl();
//		declaration_l.setIdentifier("l");
//		declaration_l.setType(new LongType());
//
//		// l = a[2];
//		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
//		identifier_l.setIdentifier("l");
//		BasicIdentifierNode identifier_a = new BasicIdentifierNodeImpl();
//		identifier_a.setIdentifier("a");
//		ArrayIdentifierNode arrayIdentifier_a = new ArrayIdentifierNodeImpl();
//		arrayIdentifier_a.setIdentifierNode(identifier_a);
//		arrayIdentifier_a.setIndex(2);
//		identifier_a.setParentNode(arrayIdentifier_a);
//
//		AssignmentNode assignment_l = new AssignmentNodeImpl();
//		assignment_l.setLeftValue(identifier_l);
//		assignment_l.setRightValue(arrayIdentifier_a);
//		identifier_l.setParentNode(assignment_l);
//		arrayIdentifier_a.setParentNode(assignment_l);
//
//		// main block
//		SymbolTable symbolTable = new SymbolTableImpl();
//		symbolTable.insert("a", new ArrayType(new LongType(), 1));
//		symbolTable.insert("l", new LongType());
//
//		BlockNode blockNode = new BlockNodeImpl();
//		blockNode.addDeclaration(declaration_a);
//		blockNode.addDeclaration(declaration_l);
//		blockNode.addStatement(assignment_l);
//		blockNode.setSymbolTable(symbolTable);
//		declaration_a.setParentNode(blockNode);
//		declaration_l.setParentNode(blockNode);
//		assignment_l.setParentNode(blockNode);
//
//		// analyse AST
//		AST ast = new ASTImpl();
//		ast.setRootNode(blockNode);
//		analyser.analyse(ast);
//
//		List<LogEntry> errors = log.getErrors();
//		assertEquals(errors.size(), 1);
//		// TODO correct reportType
//		//assertEquals(errors.get(0).getReportType(), ReportType.UNDEFINED);
//	}
}
