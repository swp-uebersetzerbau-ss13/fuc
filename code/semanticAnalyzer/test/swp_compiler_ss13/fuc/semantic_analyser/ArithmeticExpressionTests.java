package swp_compiler_ss13.fuc.semantic_analyser;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ArithmeticExpressionTests {

	private SemanticAnalyser analyser;
	private ReportLogImpl log;
	
	public ArithmeticExpressionTests() {
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
	 * # error: usage of boolean within an arithmetic expression<br/>
	 * long l;<br/>
	 * bool b;<br/>
	 * <br/>
	 * l = 1 + 2 - b;
	 */
	@Test
	public void testArithmeticExpressionTypeError(){
		// long l;
		DeclarationNode declaration_l = new DeclarationNodeImpl();
		declaration_l.setIdentifier("l");
		declaration_l.setType(new LongType());
		
		//long b;
		DeclarationNode declaration_b = new DeclarationNodeImpl();
		declaration_b.setIdentifier("b");
		declaration_l.setType(new BooleanType());
		
		// l = 1 + 2 - b;
		LiteralNode literal_1 = new LiteralNodeImpl();
		literal_1.setLiteral("1");
		literal_1.setLiteralType(new LongType());
		LiteralNode literal_2 = new LiteralNodeImpl();
		literal_2.setLiteral("2");
		literal_2.setLiteralType(new LongType());
		BasicIdentifierNode identifier_l = new BasicIdentifierNodeImpl();
		identifier_l.setIdentifier("l");
		BasicIdentifierNode identifier_b = new BasicIdentifierNodeImpl();
		identifier_b.setIdentifier("b");
		
		ArithmeticBinaryExpressionNode add = new ArithmeticBinaryExpressionNodeImpl();
		add.setOperator(BinaryOperator.ADDITION);
		add.setLeftValue(literal_1);
		add.setRightValue(literal_2);
		literal_1.setParentNode(add);
		literal_2.setParentNode(add);
		
		ArithmeticBinaryExpressionNode sub = new ArithmeticBinaryExpressionNodeImpl();
		sub.setOperator(BinaryOperator.SUBSTRACTION);
		sub.setLeftValue(add);
		sub.setRightValue(identifier_b);
		add.setParentNode(sub);
		identifier_b.setParentNode(sub);
		
		AssignmentNode assignment_l = new AssignmentNodeImpl();
		assignment_l.setLeftValue(identifier_l);
		assignment_l.setRightValue(sub);
		identifier_l.setParentNode(assignment_l);
		sub.setParentNode(assignment_l);
		
		// main block
		SymbolTable symbolTable = new SymbolTableImpl();
		symbolTable.insert("l", new LongType());
		symbolTable.insert("b", new BooleanType());
		
		BlockNode blockNode = new BlockNodeImpl();
		blockNode.addDeclaration(declaration_l);
		blockNode.addDeclaration(declaration_b);
		blockNode.addStatement(assignment_l);
		blockNode.setSymbolTable(symbolTable);
		declaration_l.setParentNode(blockNode);
		declaration_b.setParentNode(blockNode);
		assignment_l.setParentNode(blockNode);
		
		// analyse AST 
		AST ast = new ASTImpl();
		ast.setRootNode(blockNode);
		analyser.analyse(ast);
		
		// TODO better error check
		assertEquals(log.getErrors().size(), 1);
	}
}
