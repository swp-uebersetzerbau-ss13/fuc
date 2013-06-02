package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.BranchNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.parser.errorHandling.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class BranchTests {

	private SemanticAnalyser analyzer;
	private ReportLogImpl log;
	
	public BranchTests() {
	}

	@Before
	public void setUp() {
		log = new ReportLogImpl();
		analyzer = new SemanticAnalyser(this.log);
	}

	@After
	public void tearDown() {
		analyzer = null;
		log = null;
	}
	
	/**
	 * # error: usage of long as branch condition<br/>
	 * long l;<br/>
	 * l = 1;<br/>
	 * <br/>
	 * if (l)<br/>
	 *     l = 0;
	 */
	@Test
	public void testBranchConditionTypeError() {
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());
		
		BasicIdentifierNode identifier_l1 = new BasicIdentifierNodeImpl();
		identifier_l1.setIdentifier("l");
		BasicIdentifierNode identifier_l2 = new BasicIdentifierNodeImpl();
		identifier_l2.setIdentifier("l");
		
		LiteralNode literal_1 = new LiteralNodeImpl();
		literal_1.setLiteral("1");
		literal_1.setLiteralType(new LongType());
		LiteralNode literal_0 = new LiteralNodeImpl();
		literal_0.setLiteral("0");
		literal_0.setLiteralType(new LongType());
		LiteralNode literal_true = new LiteralNodeImpl();
		literal_true.setLiteral("true");
		literal_true.setLiteralType(new BooleanType());
		
		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l2);
		assignment_l.setRightValue(literal_1);
		identifier_l2.setParentNode(assignment_l);
		literal_1.setParentNode(assignment_l);
		
		BranchNode branch = new BranchNodeImpl();
		branch.setCondition(identifier_l1);
		branch.setStatementNodeOnTrue(assignment_l);
		identifier_l1.setParentNode(branch);
		assignment_l.setParentNode(branch);
		
		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(branch);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		branch.setParentNode(blockNode);
		
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		
		analyzer.analyse(ast);
		
		// TODO better error-check
		assertEquals(log.getErrors().size(), 1);
	}


}
