package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.binary.LogicBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class LogicExpressionTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;

	public LogicExpressionTests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyser = new SemanticAnalyser(this.log);
	}

	@After
	public void tearDown() {
		analyser = null;
		log = null;
	}

	/**
	 * # error: usage of long within an logic expression<br/>
	 * long l;<br/>
	 * bool b;<br/>
	 * <br/>
	 * b = b || l;
	 */
	@Test
	public void testLogicExpressionError() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());

		// long b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());

		// b = b || l;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_b1 = new BasicIdentifierNodeImpl();
		identifier_b1.setIdentifier("b");
		BasicIdentifierNode identifier_b2 = new BasicIdentifierNodeImpl();
		identifier_b2.setIdentifier("b");

		LogicBinaryExpressionNode or = new LogicBinaryExpressionNodeImpl();
		or.setOperator(BinaryOperator.LOGICAL_OR);
		or.setLeftValue(identifier_b2);
		or.setRightValue(identifier_l);
		identifier_b2.setParentNode(or);
		identifier_l.setParentNode(or);

		AssignmentNode assignment_b = new AssignmentNodeImpl();
		assignment_b.setLeftValue(identifier_b1);
		assignment_b.setRightValue(or);
		identifier_b1.setParentNode(assignment_b);
		or.setParentNode(assignment_b);

		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("b", new BooleanType());

		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_b);
		blockNode.addStatement(assignment_b);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		assignment_b.setParentNode(blockNode);

		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);

		// TODO better error check
		assertEquals(log.getErrors().size(), 1);
	}
}
