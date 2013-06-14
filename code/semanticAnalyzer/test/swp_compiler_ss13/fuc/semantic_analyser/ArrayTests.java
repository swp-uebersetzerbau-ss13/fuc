package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArrayIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
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
	 * # no errors expected<br/>
	 * long [1] a;<br/>
	 * long l;<br/>
	 * <br/>
	 * l = a[0];<br/>
	 * a[0] = l;
	 */
	@Test
	@Ignore
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
		identifier_a1.setIdentifier("b");
		ArrayIdentifierNode arrayIdentifier_a1 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a1.setIdentifierNode(identifier_a1);
		arrayIdentifier_a1.setIndex(0);

		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l1);
		assignment_l.setRightValue(arrayIdentifier_a1);
		identifier_l1.setParentNode(assignment_l);
		arrayIdentifier_a1.setParentNode(assignment_l);

		// a[0] = l;
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		BasicIdentifierNode identifier_a2 = new BasicIdentifierNodeImpl();
		identifier_a2.setIdentifier("b");
		ArrayIdentifierNode arrayIdentifier_a2 = new ArrayIdentifierNodeImpl();
		arrayIdentifier_a2.setIdentifierNode(identifier_a2);
		arrayIdentifier_a2.setIndex(0);

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

		assertFalse(log.hasErrors());
	}
}
