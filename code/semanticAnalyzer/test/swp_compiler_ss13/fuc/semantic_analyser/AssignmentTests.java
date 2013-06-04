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
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class AssignmentTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;
	
	public AssignmentTests() {
		
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
	 * # error: assignment of boolean to long-identifier<br/>
	 * long l;<br/>
	 * l = true;<br/>
	 */
	@Test
	public void testSimpleTypeError() {
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());
		
		// l = true;
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		LiteralNode literal_true = new LiteralNodeImpl();
		literal_true.setLiteral("true");
		literal_true.setLiteralType(new BooleanType());
		
		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(literal_true);
		identifier_l.setParentNode(assignment_l);
		literal_true.setParentNode(assignment_l);
		
		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		
		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);
		
		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		
		analyser.analyse(ast);
		
		// TODO better error check
		System.out.println(log);
		assertEquals(log.getErrors().size(), 1);
	}

	/**
	 * # error: assignment of wrong type inside a new block<br/>
	 * bool b;<br/>
	 * <br/>
	 * if (true){<br/>
	 *     b = 1;<br/>
	 * }
	 */
	@Test
	public void testInnerBlockTypeError() {
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("b", new BooleanType());
		
		// bool b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_b.setType(new BooleanType());
		
		// b = 1;
		BasicIdentifierNode identifier_b = new BasicIdentifierNodeImpl();
		identifier_b.setIdentifier("b");
		LiteralNode literal_1 = new LiteralNodeImpl();
		literal_1.setLiteral("1");
		literal_1.setLiteralType(new LongType());
		
		AssignmentNode assignment_b = new AssignmentNodeImpl();
		assignment_b.setLeftValue(identifier_b);
		assignment_b.setRightValue(literal_1);
		identifier_b.setParentNode(assignment_b);
		literal_1.setParentNode(assignment_b);
		
		// if (true) {...}
		LiteralNode literal_true = new LiteralNodeImpl();
		literal_true.setLiteral("true");
		literal_true.setLiteralType(new BooleanType());
		
		SymbolTable branchBlockTable = new SymbolTableImpl(symbolTable);
		BlockNode branchBlock = new BlockNodeImpl();
		branchBlock.addStatement(assignment_b);
		branchBlock.setSymbolTable(branchBlockTable);
		assignment_b.setParentNode(branchBlock);
		
		BranchNode branch = new BranchNodeImpl();
		branch.setCondition(literal_true);
		branch.setStatementNodeOnTrue(branchBlock);
		literal_true.setParentNode(branch);
		branchBlock.setParentNode(branch);
		
		// main block
		BlockNode mainBlock = new BlockNodeImpl();
		mainBlock.addDeclaration(declaration_b);
		mainBlock.addStatement(branch);
		mainBlock.setSymbolTable(symbolTable);
		declaration_b.setParentNode(mainBlock);
		branch.setParentNode(mainBlock);
		
		// analyse AST
		AST ast = new ASTImpl();
		ast.setRootNode(mainBlock);
		
		analyser.analyse(ast);
		
		// TODO better error check
		System.out.println(log);
		assertEquals(log.getErrors().size(), 1);
	}
	
}
