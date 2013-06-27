package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.RelationExpressionNodeImpl;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;
import swp_compiler_ss13.fuc.parser.errorHandling.ParserReportLogImpl;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.parser.ReduceAction;
import swp_compiler_ss13.fuc.parser.parser.ReduceImpl;

public class ReduceImplTest {

	
	static ReportLog reportLog;
	static List<Token> coverage = new LinkedList<Token>();
	
	@BeforeClass
	public static void setUp() throws Exception {
		reportLog = new ParserReportLogImpl();
	}

	@After
	public void tearDown() throws Exception {
		coverage.clear();
	}

	@Test
	public void testBasicTypeReduceLong(){
		Object obj = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl = getDecl(obj, "llong");
		
		//looks for right return type
		assertTrue(decl instanceof DeclarationNode);
		//looks for the right type
		DeclarationNode declaration = (DeclarationNode)decl;
		assertEquals(declaration.getIdentifier(),"llong");
		assertTrue(declaration.getType() instanceof LongType);
		//looks for right Token in coverage
		checkCoverage(declaration.coverage());
		
	}
	
	@Test
	public void testBasicTypeReduceBool(){
		Object obj = getBasicDecl("bool", TokenType.BOOL_SYMBOL);
		
		Object decl = getDecl(obj, "var");
		
		//looks for right return type
		assertTrue(decl instanceof DeclarationNode);
		//looks for the right type
		DeclarationNode declaration = (DeclarationNode)obj;
		assertEquals(declaration.getIdentifier(),"var");
		assertTrue(declaration.getType() instanceof BooleanType);
		//looks for right Token in coverage
		checkCoverage(declaration.coverage());
	}
	
	@Test
	public void testBasicTypeReduceString(){
		Object obj = getBasicDecl("string", TokenType.STRING_SYMBOL);
		
		Object decl = getDecl(obj, "var");
		//looks for right return type
		assertTrue(decl instanceof DeclarationNode);
		//looks for the right type
		DeclarationNode declaration = (DeclarationNode)decl;
		assertEquals(declaration.getIdentifier(),"var");

		assertTrue(declaration.getType() instanceof StringType);
		//looks for right Token in coverage
		checkCoverage(declaration.coverage());
	}
	
	@Test
	public void testBasicTypeReduceDouble(){
		Object obj = getBasicDecl("double", TokenType.DOUBLE_SYMBOL);
		
		Object decl = getDecl(obj, "var");
		//looks for right return type
		assertTrue(decl instanceof DeclarationNode);
		//looks for the right type
		DeclarationNode declaration = (DeclarationNode)decl;
		assertEquals(declaration.getIdentifier(),"var");
		assertTrue(declaration.getType() instanceof DoubleType);
		//looks for right Token in coverage
		checkCoverage(declaration.coverage());
	}

	
	@Test
	public void testArrayTypeReduce(){
		
		Object type = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object obj = getArrayType(type, "20");
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		DeclarationNode declaration = (DeclarationNode)obj;
		assertTrue(declaration.getType() instanceof ArrayType);
		assertTrue(((ArrayType)declaration.getType()).getLength()==20);
		assertTrue(((ArrayType)declaration.getType()).getInnerType() instanceof LongType);
		//looks for right Token in coverage
		checkCoverage(declaration.coverage());

	}
	
	
	@Test
	public void testArrayDeclReduce(){
		Object type = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object arrayType = getArrayType(type, "10");
		
		Object obj = getDecl(arrayType, "arr");
		
				
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		DeclarationNode declaration = (DeclarationNode)obj;
		assertTrue(declaration.getType() instanceof ArrayType);
		assertTrue(((ArrayType)declaration.getType()).getLength()==10);
		assertTrue(((ArrayType)declaration.getType()).getInnerType() instanceof LongType);
		//looks for right Token in coverage
		checkCoverage(declaration.coverage());

	
	}
	
	@Test
	public void testNumLiteralCreation(){
		
		Object obj = getNumLiteral("30");
		
		//looks for right return type
		assertTrue(obj instanceof LiteralNode);
		LiteralNode literal = (LiteralNode)obj;
		assertTrue(literal.getLiteralType() instanceof LongType);
		assertTrue(literal.getLiteral()=="30");
		
		//looks for right Token in coverage
		checkCoverage(literal.coverage());
	}
	
	@Test
	public void testStringLiteralCreation(){
		
		Object obj = getStringLiteral("lala");
		
		//looks for right return type
		assertTrue(obj instanceof LiteralNode);
		LiteralNode literal = (LiteralNode)obj;
		assertTrue(literal.getLiteralType() instanceof StringType);
		assertTrue(literal.getLiteral()=="lala");
		assertTrue(literal.getLiteralType().getWidth()==4L);
		
		//looks for right Token in coverage
		checkCoverage(literal.coverage());
	}
	
	@Test
	public void testRealLiteralCreation(){
		
		Object obj = getRealLiteral("30");
		
		//looks for right return type
		assertTrue(obj instanceof LiteralNode);
		LiteralNode literal = (LiteralNode)obj;
		assertTrue(literal.getLiteralType() instanceof DoubleType);
		assertTrue(literal.getLiteral()=="30");
		
		//looks for right Token in coverage
		checkCoverage(literal.coverage());
	}
	
	@Test
	public void testTrueLiteralCreation(){
		
		Object obj = getBoolLiteral("true",TokenType.TRUE);
		
		//looks for right return type
		assertTrue(obj instanceof LiteralNode);
		LiteralNode literal = (LiteralNode)obj;
		assertTrue(literal.getLiteralType() instanceof BooleanType);
		assertTrue(literal.getLiteral()=="true");
		
		//looks for right Token in coverage
		checkCoverage(literal.coverage());
	}
	
	@Test
	public void testFalseLiteralCreation(){
		
		Object obj = getBoolLiteral("false",TokenType.FALSE);
		
		//looks for right return type
		assertTrue(obj instanceof LiteralNode);
		LiteralNode literal = (LiteralNode)obj;
		assertTrue(literal.getLiteralType() instanceof BooleanType);
		assertTrue(literal.getLiteral()=="false");
		
		//looks for right Token in coverage
		checkCoverage(literal.coverage());
	}
	
	

	@Test
	public void testIdentifierCreation(){

		Object obj = getIdentifier("l");

		//looks for right return type
		assertTrue(obj instanceof BasicIdentifierNode);
		BasicIdentifierNode basic = (BasicIdentifierNode)obj;
		assertTrue(basic.getIdentifier()=="l");
		
		//looks for right Token in coverage
		checkCoverage(basic.coverage());

	}

	@Test
	public void testAssign(){

		Object identifier = getIdentifier("l");
		
		Object literal = getNumLiteral("200");

		Object obj = getAssign(identifier, literal);
		
		assertTrue(obj instanceof AssignmentNode);
		AssignmentNode assignment = (AssignmentNode)obj;
		assertEquals(assignment.getLeftValue(),identifier);
		assertEquals(assignment.getRightValue(),literal);
		assertEquals(assignment.getLeftValue().getParentNode(),obj);
		assertEquals(assignment.getRightValue().getParentNode(),obj);

		checkCoverage(assignment.coverage());

	}
	
	@Test
	public void testEqualOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getEqualsOp(identifier1, identifier2);
		
		assertTrue(cond instanceof RelationExpressionNodeImpl);
		RelationExpressionNodeImpl relationExpression = (RelationExpressionNodeImpl)cond;
		assertEquals(relationExpression.getLeftValue(),identifier1);
		assertEquals(relationExpression.getRightValue(),identifier2);
		
		checkCoverage(relationExpression.coverage());
	}
	
	@Test
	public void testGreaterThenOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getGreaterOp(identifier1, identifier2);
		
		assertTrue(cond instanceof RelationExpressionNodeImpl);
		RelationExpressionNodeImpl relationExpression = (RelationExpressionNodeImpl)cond;
		assertEquals(relationExpression.getLeftValue(),identifier1);
		assertEquals(relationExpression.getRightValue(),identifier2);
		
		checkCoverage(relationExpression.coverage());
	}
	
	@Test
	public void testLesserThenOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getLesserOp(identifier1, identifier2);
		
		assertTrue(cond instanceof RelationExpressionNodeImpl);
		RelationExpressionNodeImpl relationExpression = (RelationExpressionNodeImpl)cond;
		assertEquals(relationExpression.getLeftValue(),identifier1);
		assertEquals(relationExpression.getRightValue(),identifier2);
		
		checkCoverage(relationExpression.coverage());
	}
	
	@Test
	public void testGreaterEqualThenOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getGreaterEqualOp(identifier1, identifier2);
		
		assertTrue(cond instanceof RelationExpressionNodeImpl);
		RelationExpressionNodeImpl relationExpression = (RelationExpressionNodeImpl)cond;
		assertEquals(relationExpression.getLeftValue(),identifier1);
		assertEquals(relationExpression.getRightValue(),identifier2);
		
		checkCoverage(relationExpression.coverage());
	}
	
	@Test
	public void testLessEqualThenOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getLessEqualOp(identifier1, identifier2);
		
		assertTrue(cond instanceof RelationExpressionNodeImpl);
		RelationExpressionNodeImpl relationExpression = (RelationExpressionNodeImpl)cond;
		assertEquals(relationExpression.getLeftValue(),identifier1);
		assertEquals(relationExpression.getRightValue(),identifier2);
		
		checkCoverage(relationExpression.coverage());
	}
	
	
	

	@Test
	public void branchWoElseTest(){
		
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getEqualsOp(identifier1, identifier2);
		
		Object identifier3 = getIdentifier("l");
		
		Object literal = getNumLiteral("20");
		
		Object assignment1 = getAssign(identifier3, literal);
		
		Object obj = getBranchWithoutElse(cond, assignment1);
		
		assertTrue(obj instanceof BranchNode);
		BranchNode branchNode = (BranchNode)obj;
		assertEquals(branchNode.getStatementNodeOnTrue(),assignment1);
		assertEquals(branchNode.getCondition(),cond);
		assertEquals(branchNode.getStatementNodeOnTrue().getParentNode(),obj);
		assertEquals(branchNode.getStatementNodeOnFalse(), null);
		assertEquals(branchNode.getCondition().getParentNode(),obj);

		checkCoverage(branchNode.coverage());
	}
	
	


	@Test
	public void testBranchWElse(){

		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");

		Object cond = getEqualsOp(identifier1, identifier2);
		
        Object identifier3 = getIdentifier("p");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("q");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object obj = getBranchWithElse(cond, statement1, statement2);
		
		assertTrue(obj instanceof BranchNode);
		
		BranchNode branchNode = (BranchNode)obj;
		assertEquals(branchNode.getStatementNodeOnTrue(),assignment1);
		assertEquals(branchNode.getStatementNodeOnFalse(),assignment2);
		assertEquals(branchNode.getCondition(),cond);
		assertEquals(branchNode.getStatementNodeOnTrue().getParentNode(),obj);
		assertEquals(branchNode.getStatementNodeOnFalse().getParentNode(),obj);
		assertEquals(branchNode.getCondition().getParentNode(),obj);
		checkCoverage(branchNode.coverage());

	}
	
	@Test
	public void testBranchWElseAndBlockOnTrue(){

		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");

		Object cond = getEqualsOp(identifier1, identifier2);
		   
        Object statement1 = getInnerBlock("var1","var2");

        Object identifier4 = getIdentifier("q");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object obj = getBranchWithElse(cond, statement1, statement2);
		
		assertTrue(obj instanceof BranchNode);
		
		BranchNode branchNode = (BranchNode)obj;
		assertEquals(branchNode.getStatementNodeOnTrue(),statement1);
		assertEquals(branchNode.getStatementNodeOnFalse(),statement2);
		assertEquals(branchNode.getCondition(),cond);
		assertEquals(branchNode.getStatementNodeOnTrue().getParentNode(),obj);
		assertEquals(branchNode.getStatementNodeOnFalse().getParentNode(),obj);
		assertEquals(branchNode.getCondition().getParentNode(),obj);
		checkCoverage(branchNode.coverage());
	}
	
	@Test
	public void testBranchWElseAndBlocks(){

		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");

		Object cond = getGreaterEqualOp(identifier1, identifier2);
		   
        Object statement1 = getInnerBlock("var1","var2");

        Object statement2 = getInnerBlock("var3","var4");
		
		Object obj = getBranchWithElse(cond, statement1, statement2);
		
		assertTrue(obj instanceof BranchNode);
		
		BranchNode branchNode = (BranchNode)obj;
		assertEquals(branchNode.getStatementNodeOnTrue(),statement1);
		assertEquals(branchNode.getStatementNodeOnFalse(),statement2);
		assertEquals(branchNode.getCondition(),cond);
		assertEquals(branchNode.getStatementNodeOnTrue().getParentNode(),obj);
		assertEquals(branchNode.getStatementNodeOnFalse().getParentNode(),obj);
		assertEquals(branchNode.getCondition().getParentNode(),obj);
		checkCoverage(branchNode.coverage());
	}
	
	
	@Test
	public void testStatementBlockCreation(){
		
		Object identifier3 = getIdentifier("p");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("q");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object block = getStatementBlock(statement1, statement2);
		
		assertTrue(block instanceof BlockNode);
		
		BlockNode blockNode = (BlockNode)block;
		
		List<StatementNode> list = blockNode.getStatementList();
		
		assertTrue(list.contains(statement2));
		assertTrue(list.contains(statement1));

		checkCoverage(blockNode.coverage());
		checkParents(blockNode,statement1,statement2);

	}
	
	@Test
	public void testBlockofBlockCreation(){
		
		Object identifier3 = getIdentifier("p");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("q");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object block = getStatementBlock(statement1, statement2);
		
		Object identifier2 = getIdentifier("l");
		
		Object literal3 = getNumLiteral("20");
		
        Object assignment3 = getAssign(identifier2, literal3);
        
        Object statement3 = getStatement(assignment3);
        
        Object newBlock = getStatementBlock(block, statement3);
		
		assertTrue(newBlock instanceof BlockNode);
		
		BlockNode node = (BlockNode)newBlock;
		
		List<StatementNode> list = node.getStatementList();
		
		assertTrue(list.contains(statement2));
		assertTrue(list.contains(statement1));
		assertTrue(list.contains(statement3));
		
		checkCoverage(node.coverage());
		checkParents(node,statement1,statement2,statement3);

		
	}
	
	@Test
	public void testStatementBlockUnion(){
		Object identifier3 = getIdentifier("p");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("q");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object block = getStatementBlock(statement1, statement2);
		
		Object identifier2 = getIdentifier("l");
		
		Object literal3 = getNumLiteral("20");
		
        Object assignment3 = getAssign(identifier2, literal3);
        
        Object statement3 = getStatement(assignment3);
        
        Object identifier1 = getIdentifier("a");
		
		Object literal4 = getNumLiteral("100");
		
        Object assignment4 = getAssign(identifier1, literal4);
        
        Object statement4 = getStatement(assignment4);
        
        Object block2 = getStatementBlock(statement3,statement4);
		
        Object newBlock = getStatementBlock(block, block2);
        
		assertTrue(newBlock instanceof BlockNode);
		
		BlockNode node = (BlockNode)newBlock;
		
		List<StatementNode> list = node.getStatementList();
		
		assertTrue(list.contains(statement1));
		assertTrue(list.contains(statement2));
		assertTrue(list.contains(statement3));
		assertTrue(list.contains(statement4));
		
		checkCoverage(node.coverage());
		checkParents(node,statement1,statement2,statement3,statement4);
		
	}
	
	@Test
	public void testDeclarationBlock(){
		Object type1 = getBasicDecl("double", TokenType.DOUBLE_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("string", TokenType.STRING_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object block = getDeclarationBlock(decl1, decl2);
		
		assertTrue(block instanceof BlockNode);
		
		BlockNode node = (BlockNode)block;
		
		List<DeclarationNode> list = node.getDeclarationList();
		
		assertTrue(list.contains(decl1));
		assertTrue(list.contains(decl2));
		
		checkCoverage(node.coverage());
		checkSymbolTable(node.getSymbolTable(),decl1,decl2);
		checkParents(node,decl1,decl2);

		
	}
	
	

	@Test
	public void testDeclarationBlockStatement(){
		Object type1 = getBasicDecl("double", TokenType.DOUBLE_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("string", TokenType.STRING_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object declBlock = getDeclarationBlock(decl1, decl2);
		
		Object type3 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl3 = getDecl(type3, "var3");
		
		Object block = getDeclarationBlock(declBlock, decl3);
		
		assertTrue(block instanceof BlockNode);
		
		BlockNode node = (BlockNode)block;
		
		List<DeclarationNode> list = node.getDeclarationList();
		
		assertTrue(list.contains(decl1));
		assertTrue(list.contains(decl2));
		assertTrue(list.contains(decl3));
		
		checkCoverage(node.coverage());
		checkSymbolTable(node.getSymbolTable(),decl1,decl2,decl3);	
		checkParents(node,decl1,decl2,decl3);
	}
	
	
	@Test
	public void testDeclarationBlockUnion(){
		Object type1 = getBasicDecl("double", TokenType.DOUBLE_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("string", TokenType.STRING_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object declBlock1 = getDeclarationBlock(decl1, decl2);
		
		Object type3 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl3 = getDecl(type3, "var3");
		
		Object type4 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl4 = getDecl(type4, "var4");
		
		Object declBlock2 = getDeclarationBlock(decl3,decl4);
		
		Object block = getDeclarationBlock(declBlock1, declBlock2);
		
		assertTrue(block instanceof BlockNode);
		
		BlockNode node = (BlockNode)block;
		
		List<DeclarationNode> list = node.getDeclarationList();
		
		assertTrue(list.contains(decl1));
		assertTrue(list.contains(decl2));
		assertTrue(list.contains(decl3));
		assertTrue(list.contains(decl4));
		
		checkCoverage(node.coverage());
		checkSymbolTable(node.getSymbolTable(),decl1,decl2,decl3,decl4);
		checkParents(node,decl1,decl2,decl3,decl4);
	}
	
	private void checkParents(BlockNode node, Object ...objs) {

		for(int i = 0; i<objs.length; i++){
			assertEquals(node,((ASTNode)objs[i]).getParentNode());
		}
		
	}

	@Test
	public void testDeclStatmentBlockUnion(){
		Object type1 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object declBlock = getDeclarationBlock(decl1, decl2);
		
		Object identifier3 = getIdentifier("var1");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("var2");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object stmtBlock = getStatementBlock(statement1, statement2);
		
		Object block = getDeclBlockStateBlockUnion(declBlock, stmtBlock);

		assertTrue(block instanceof BlockNode);
		
		BlockNode node = (BlockNode)block;
		
		List<DeclarationNode> list1 = node.getDeclarationList();
		
		assertTrue(list1.contains(decl1));
		assertTrue(list1.contains(decl2));
		
		List<StatementNode> list2 = node.getStatementList();
		
		assertTrue(list2.contains(statement1));
		assertTrue(list2.contains(statement2));
		
		checkCoverage(node.coverage());
		checkSymbolTable(node.getSymbolTable(),decl1,decl2);
		checkParents(node,decl1,decl2,statement1,statement2);
	}
	
	@Test
	public void testDeclStatmentBlockInBrace(){
		Object type1 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object declBlock = getDeclarationBlock(decl1, decl2);
		
		Object identifier3 = getIdentifier("var1");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("var2");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object stmtBlock = getStatementBlock(statement1, statement2);
		
		Object block = getDeclBlockStateBlockInBrace(declBlock, stmtBlock);

		assertTrue(block instanceof BlockNode);
		
		BlockNode node = (BlockNode)block;
		
		List<DeclarationNode> list1 = node.getDeclarationList();
		
		assertTrue(list1.contains(decl1));
		assertTrue(list1.contains(decl2));
		
		List<StatementNode> list2 = node.getStatementList();
		
		assertTrue(list2.contains(statement1));
		assertTrue(list2.contains(statement2));
		
		checkCoverage(node.coverage());
		checkSymbolTable(node.getSymbolTable(),decl1,decl2);
		checkParents(node,decl1,decl2,statement1,statement2);
	}
	
	@Test
	public void testDoWhileWithBlock(){

		Object identifier3 = getIdentifier("var1");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);
		
        Object identifier1 = getIdentifier("l");
        
        Object identifier2 = getIdentifier("r");
        
        Object cond = getEqualsOp(identifier1, identifier2);
        
		Object obj = getDoWhile(cond, statement1);
		
		assertTrue(obj instanceof DoWhileNode);
		
		DoWhileNode dowhile = (DoWhileNode)obj;
		assertEquals(dowhile.getLoopBody(),statement1);
		assertEquals(dowhile.getCondition(),cond);
		assertEquals(dowhile.getLoopBody().getParentNode(),obj);
		assertEquals(dowhile.getCondition().getParentNode(),obj);
		checkCoverage(dowhile.coverage());
	}
	
	@Test
	public void testWhile(){

		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getEqualsOp(identifier1, identifier2);

		Object identifier3 = getIdentifier("var1");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);
		
		Object obj = getWhile(cond, statement1);
		
		assertTrue(obj instanceof WhileNode);
		
		WhileNode whileNode = (WhileNode)obj;
		assertEquals(whileNode.getLoopBody(),statement1);
		assertEquals(whileNode.getCondition(),cond);
		assertEquals(whileNode.getLoopBody().getParentNode(),obj);
		assertEquals(whileNode.getCondition().getParentNode(),obj);
		checkCoverage(whileNode.coverage());
	}
	
	@Test
	public void testDoWhileWithStatement(){

        Object statement1 = getInnerBlock("var1","var2");
		
        Object identifier1 = getIdentifier("l");
        
        Object identifier2 = getIdentifier("r");
        
        Object cond = getEqualsOp(identifier1, identifier2);
        
		Object obj = getDoWhile(cond, statement1);
		
		assertTrue(obj instanceof DoWhileNode);
		
		DoWhileNode dowhile = (DoWhileNode)obj;
		assertEquals(dowhile.getLoopBody(),statement1);
		assertEquals(dowhile.getCondition(),cond);
		assertEquals(dowhile.getLoopBody().getParentNode(),obj);
		assertEquals(dowhile.getCondition().getParentNode(),obj);
		checkCoverage(dowhile.coverage());
	}
	
	
	/**
	 * returns a complete statement with semicolon at the end
	 * @param statement
	 * @return
	 */
	private Object getStatement(Object statement){
		Token sem = new TokenImpl(";", TokenType.SEMICOLON, -1, -1);
		addAfter(sem, ((ASTNode)statement).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt1, reportLog);
		return action.create(statement,sem);
	}
	
	
	
	private Object getInnerBlock(String var1, String var2){
Object type1 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl1 = getDecl(type1, var1);
		
		Object type2 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl2 = getDecl(type2, var2);
		
		Object declBlock = getDeclarationBlock(decl1, decl2);
		
		Object identifier3 = getIdentifier(var1);
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier(var2);
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object stmtBlock = getStatementBlock(statement1, statement2);
		
		return getDeclBlockStateBlockInBrace(declBlock, stmtBlock);
	}
	
	
	
	/**
	 * 
	 * @param declaration1
	 * @param declaration2
	 * @return
	 */
	private Object getDeclarationBlock(Object declaration1, Object declaration2){
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.decls1, reportLog);
		return action.create(declaration1,declaration2);
	}
	
	/**
	 * reduces statementblock and declarationblock to one block
	 * @param decl
	 * @param statement
	 * @return
	 */
	private Object getDeclBlockStateBlockUnion(Object decl,Object statement){
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.program1, reportLog);
		return action.create(decl,statement);
	}
	
	/**
	 * reduces statementblock and declarationblock to one block covered in braces
	 * @param decl
	 * @param statement
	 * @return
	 */
	private Object getDeclBlockStateBlockInBrace(Object decl,Object stmt){
		Token lb = new TokenImpl("{", TokenType.LEFT_BRACE, -1, -1);
		addBefore(lb, ((ASTNode)decl).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.block1, reportLog);
		Token rb = new TokenImpl("}", TokenType.RIGHT_BRACE, -1, -1);
		addAfter(rb, ((ASTNode)stmt).coverage());
		return action.create(lb,decl,stmt,rb);
	}
	
	
	
	/**
	 * 
	 * @param statement1
	 * @param statement2
	 * @return
	 */
	private Object getStatementBlock(Object statement1, Object statement2){
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmts1, reportLog);
		return action.create(statement1, statement2);
	}
	
	/**
	 * Gets name of Identifier and line and row and returns an identifierNode
	 * @param name
	 * @return
	 */
	private Object getIdentifier(String name){
		Token identifierToken = new TokenImpl(name,TokenType.ID,-1,-1);
		coverage.add(identifierToken);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.loc2, reportLog);
		return action.create(identifierToken);
	}
	
	
	/**
	 * Gets two identifier and reduce to an equal operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	private Object getEqualsOp(Object identifier1, Object identifier2){
		Token equalop = new TokenImpl("==", TokenType.EQUALS, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.equality1, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}
	
	/**
	 * Gets two identifier and reduce to an greater than operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	private Object getGreaterOp(Object identifier1, Object identifier2){
		Token equalop = new TokenImpl(">", TokenType.GREATER, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.rel4, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}
	
	/**
	 * Gets two identifier and reduce to an lesser than operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	private Object getLesserOp(Object identifier1, Object identifier2){
		Token equalop = new TokenImpl("<", TokenType.LESS, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.rel1, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}
	
	/**
	 * Gets two identifier and reduce to an greater equal than operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	private Object getGreaterEqualOp(Object identifier1, Object identifier2){
		Token equalop = new TokenImpl(">=", TokenType.GREATER_EQUAL, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.rel3, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}
	
	/**
	 * Gets two identifier and reduce to an greater than operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	private Object getLessEqualOp(Object identifier1, Object identifier2){
		Token equalop = new TokenImpl("<=", TokenType.LESS_OR_EQUAL, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.rel2, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}
	
	/**
	 * Gets a value and returns a num literal
	 * @param value
	 * @return
	 */
	private Object getNumLiteral(String value){
		Token literalToken = new TokenImpl(value,TokenType.NUM,-1,-1);
		coverage.add(literalToken);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor3, reportLog);
		
		return action.create(literalToken);
	}
	
	/**
	 * Gets a value and returns a string literal
	 * @param value
	 * @return
	 */
	private Object getStringLiteral(String value){
		Token literalToken = new TokenImpl(value,TokenType.STRING,-1,-1);
		coverage.add(literalToken);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor7, reportLog);
		
		return action.create(literalToken);
	}
	
	/**
	 * Gets a value and returns a real literal
	 * @param value
	 * @return
	 */
	private Object getRealLiteral(String value){
		Token literalToken = new TokenImpl(value,TokenType.REAL,-1,-1);
		coverage.add(literalToken);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor4, reportLog);
		
		return action.create(literalToken);
	}
	
	/**
	 * Gets a value and returns a bool literal
	 * @param value
	 * @return
	 */
	private Object getBoolLiteral(String value, TokenType ttype){
		Token literalToken = new TokenImpl(value,ttype,-1,-1);
		coverage.add(literalToken);
		ReduceAction action = null;
		if(ttype == TokenType.TRUE){
			action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor5, reportLog);
		}else{
			action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor6, reportLog);
		}
		return action.create(literalToken);
	}
	
	
	/**
	 * returns an assignment
	 * @param identifier
	 * @param literal
	 * @return
	 */
	private Object getAssign(Object identifier, Object literal){
		Token assign = new TokenImpl("=",TokenType.ASSIGNOP,1,7);
		addAfter(assign, ((ASTNode)identifier).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.assign1, reportLog);
		
		return action.create(identifier,assign,literal);
	}
	
	/**
	 * creates an array reduction with given type and length
	 * @return
	 */
	private Object getArrayType(Object type, String length){
		ReduceAction action2 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type1, reportLog);
		Token rb = new TokenImpl("]",TokenType.ID,1,8);
		addAfter(rb, ((ASTNode)type).coverage());
		Token num = new NumTokenImpl(length,TokenType.NUM,1,7);
		addAfter(num, ((ASTNode)type).coverage());
		Token lfb = new TokenImpl("[",TokenType.ID,1,6);
		addAfter(lfb, ((ASTNode)type).coverage());
		
		return action2.create(type, lfb, num,rb);
	}
	
	/**
	 * Gets condition, statement1 for true and statement2 for false branch, returns a 
	 * Branchnode with else condition
	 * @param cond
	 * @param assignment1
	 * @param assignment2
	 * @return
	 */
	private Object getBranchWithElse(Object cond, Object statement1, Object statement2){
		
		Token ifToken = new TokenImpl("if", TokenType.IF, 3, 9);
		addBefore(ifToken, ((ASTNode)cond).coverage());
		
		Token lb = new TokenImpl("(", TokenType.LEFT_PARAN, 3, 10);
		addBefore(lb, ((ASTNode)cond).coverage());
		
		Token rb = new TokenImpl(")", TokenType.RIGHT_PARAN, 3, 10);
		addAfter(rb, ((ASTNode)cond).coverage());
		
		Token elseToken = new TokenImpl("else", TokenType.ELSE,5,1);
		addAfter(elseToken, ((ASTNode)statement1).coverage());
		
		ReduceAction action6 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt3, reportLog);
		
		return action6.create(ifToken, lb, cond, rb, statement1,elseToken,statement2);
	}
	
	/**
	 * Gets condition, statement and returns a branch without else
	 * @param cond
	 * @param assignment
	 * @return
	 */
	private Object getBranchWithoutElse(Object cond, Object statement) {
		Token ifToken = new TokenImpl("if", TokenType.IF, 3, 9);
		addBefore(ifToken, ((ASTNode)cond).coverage());
		
		Token lb = new TokenImpl("(", TokenType.LEFT_PARAN, 3, 10);
		addBefore(lb, ((ASTNode)cond).coverage());
		
		Token rb = new TokenImpl(")", TokenType.RIGHT_PARAN, 3, 10);
		addBefore(rb, ((ASTNode)statement).coverage());
		
		ReduceAction action6 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt2, reportLog);
		
		return action6.create(ifToken, lb, cond, rb, statement);
	}
	
	/**
	 * creates an declaration reduction for given arrayType and identifier
	 * @return
	 */
	private Object getDecl(Object type, String id){
		ReduceAction action3 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.decl1, reportLog);
		Token semicolon = new TokenImpl(";",TokenType.SEMICOLON,1,11);
		addAfter(semicolon, ((ASTNode)type).coverage());
		
		Token identifier = new TokenImpl(id, TokenType.ID, -1, -1);
		addAfter(identifier, ((ASTNode)type).coverage());
		
		return action3.create(type, identifier, semicolon);
	}
	/**
	 * Creates a declaration for base types
	 * @param type
	 * @param ttype
	 * @return
	 */
	private Object getBasicDecl(String type, TokenType ttype){
		ReduceAction action1 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Token longToken = new TokenImpl(type, ttype, 1, 1);
		coverage.add(longToken);
		return action1.create(longToken);
	}

	private Object getDoWhile(Object cond, Object statement){
		Token doToken = new TokenImpl("do", TokenType.DO, 1, 9);
		addBefore(doToken, ((ASTNode)statement).coverage());
		
		Token whileToken = new TokenImpl("while", TokenType.WHILE, 3, 9);
		addBefore(whileToken, ((ASTNode)cond).coverage());
		
		Token lb = new TokenImpl("(", TokenType.LEFT_PARAN, 3, 10);
		addBefore(lb, ((ASTNode)cond).coverage());
		
		Token sem = new TokenImpl(";", TokenType.SEMICOLON, 12, 3);
		addAfter(sem, ((ASTNode)cond).coverage());
		
		Token rb = new TokenImpl(")", TokenType.RIGHT_PARAN, 3, 10);
		addAfter(rb, ((ASTNode)cond).coverage());
		
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt9, reportLog);
		
		return action.create(doToken, statement, whileToken, lb, cond, rb, sem);
	}
	
	/**
	 * creates a while reduce for a condition and a statement
	 * @param cond
	 * @param statement
	 * @return
	 */
	private Object getWhile(Object cond, Object statement){
		Token whileToken = new TokenImpl("while", TokenType.WHILE, 3, 9);
		addBefore(whileToken, ((ASTNode)cond).coverage());
		
		Token lb = new TokenImpl("(", TokenType.LEFT_PARAN, 3, 10);
		addBefore(lb, ((ASTNode)cond).coverage());
		
		Token rb = new TokenImpl(")", TokenType.RIGHT_PARAN, 3, 10);
		addBefore(rb, ((ASTNode)statement).coverage());
		
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt4, reportLog);
		
		return action.create(whileToken, lb, cond, rb, statement);
	}
	
	
	/**
	 * adds token after specific coverage
	 * @param ins
	 * @param cov
	 */
	private void addAfter(Token ins,List<Token> cov){
		Token last = cov.get(cov.size()-1);
		addAfter(ins, last);
	}
	
	private void addAfter(Token ins, Token cov){
		for(Token token : coverage){
			if(cov == token){
				int index = coverage.indexOf(token);
				coverage.add(index+1, ins);
				break;
			}
		}
	}
	
	private void addBefore(Token ins,List<Token> cov){
		Token first = cov.get(0);
		addBefore(ins, first);
	}
	
	private void addBefore(Token ins, Token cov){
		for(Token token : coverage){
			if(cov == token){
				int index = coverage.indexOf(cov);
				coverage.add(index, ins);
				break;
			}
		}
	}

	/**
	 * checks, if coverage is set right
	 * @param cov
	 */
	private void checkCoverage(List<Token> cov) {
		for(int i = 0; i<cov.size();i++){
			System.out.println(cov.get(i).getValue()+ " : " + coverage.get(i).getValue());
			assertEquals(cov.get(i), coverage.get(i));
		}
	}
	
	/**
	 * checks, if all variables are declared in symboltable
	 * @param symbolTable
	 * @param decls
	 */
	private void checkSymbolTable(SymbolTable symbolTable, Object ...decls) {
		for(int i = 0; i<decls.length;i++){
			assertTrue(symbolTable.isDeclared(((DeclarationNode)decls[i]).getIdentifier()));
		}
	}

}

