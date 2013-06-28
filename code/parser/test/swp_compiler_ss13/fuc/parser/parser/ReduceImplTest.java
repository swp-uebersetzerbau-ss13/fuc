package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.DoWhileNode;
import swp_compiler_ss13.common.ast.nodes.binary.WhileNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BreakNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.RelationExpressionNodeImpl;
import swp_compiler_ss13.fuc.errorLog.ReportLogImpl;
import swp_compiler_ss13.fuc.parser.parser.ReduceAction.ReduceException;

public class ReduceImplTest extends ReduceImplHelper {
	
	@Before
	public void setUp() throws Exception {
		ReportLogImpl reportLog = new ReportLogImpl();
		ReduceImpl reduceImpl = new ReduceImpl();
		reduceImpl.setReportLog(reportLog);
		ReduceImplHelper.init(this, reportLog, reduceImpl);
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
	
	@Test (expected = ReduceException.class)
	public void testBasicTypeFailDouble(){
		Object obj = getIdentifier("blub");
		getDecl(obj, "var");
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
	
	@Test (expected = ParserException.class)
	public void testWrongTypeReduce(){

		getBasicDecl("double", TokenType.PLUS);
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
	public void testAdditionOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object expr = getAdditionOp(identifier1, identifier2);
		
		assertTrue(expr instanceof BinaryExpressionNode);
		BinaryExpressionNode binaryExpression = (BinaryExpressionNode)expr;
		assertEquals(binaryExpression.getLeftValue(),identifier1);
		assertEquals(binaryExpression.getRightValue(),identifier2);
		
		checkCoverage(binaryExpression.coverage());
	}
	
	@Test
	public void testSubtractionOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object expr = getSubtractionOp(identifier1, identifier2);
		
		assertTrue(expr instanceof BinaryExpressionNode);
		BinaryExpressionNode binaryExpression = (BinaryExpressionNode)expr;
		assertEquals(binaryExpression.getLeftValue(),identifier1);
		assertEquals(binaryExpression.getRightValue(),identifier2);
		
		checkCoverage(binaryExpression.coverage());
	}
	
	@Test
	public void testMultiplicationOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object expr = getMultiplicationOp(identifier1, identifier2);
		
		assertTrue(expr instanceof BinaryExpressionNode);
		BinaryExpressionNode binaryExpression = (BinaryExpressionNode)expr;
		assertEquals(binaryExpression.getLeftValue(),identifier1);
		assertEquals(binaryExpression.getRightValue(),identifier2);
		
		checkCoverage(binaryExpression.coverage());
	}
	
	@Test
	public void testDivisionOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object expr = getDivisionOp(identifier1, identifier2);
		
		assertTrue(expr instanceof BinaryExpressionNode);
		BinaryExpressionNode binaryExpression = (BinaryExpressionNode)expr;
		assertEquals(binaryExpression.getLeftValue(),identifier1);
		assertEquals(binaryExpression.getRightValue(),identifier2);
		
		checkCoverage(binaryExpression.coverage());
	}
	
	@Test
	public void testNotOp(){
		Object identifier1 = getIdentifier("l");
		
		Object expr = getNotOp(identifier1);
		
		assertTrue(expr instanceof UnaryExpressionNode);
		UnaryExpressionNode unaryExpr = (UnaryExpressionNode)expr;
		assertEquals(unaryExpr.getRightValue(),identifier1);
		assertEquals(unaryExpr.getOperator(), UnaryOperator.LOGICAL_NEGATE);
		
		checkCoverage(unaryExpr.coverage());
	}
	
	@Test
	public void testMinusOp(){
		Object identifier1 = getIdentifier("l");
		
		Object expr = getMinusOp(identifier1);
		
		assertTrue(expr instanceof UnaryExpressionNode);
		UnaryExpressionNode unaryExpr = (UnaryExpressionNode)expr;
		assertEquals(unaryExpr.getRightValue(),identifier1);
		assertEquals(unaryExpr.getOperator(), UnaryOperator.MINUS);
		
		checkCoverage(unaryExpr.coverage());
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
	public void testNotEqualOp(){
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");
		
		Object cond = getNotEqualOp(identifier1, identifier2);
		
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
		
		Object identifier5 = getIdentifier("var1");
		
        Object literal5 = getNumLiteral("20");
		
        Object assignment5 = getAssign(identifier5, literal5);
        
        Object statement5 = getStatement(assignment5);
		
        Object identifier7 = getIdentifier("l");
        
        Object identifier6 = getIdentifier("r");
        
        Object cond = getEqualsOp(identifier7, identifier6);
        
		Object obj = getDoWhile(cond, statement5);
		
		Object identifier3 = getIdentifier("p");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

		Object block = getStatementBlock(obj, statement1);
		
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
		
		assertTrue(list.contains(obj));
		assertTrue(list.contains(statement1));
		assertTrue(list.contains(statement3));
		assertTrue(list.contains(statement4));
		
		checkCoverage(node.coverage());
		checkParents(node,statement1,obj,statement3,statement4);
		
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
	public void testDeclStatementBlockUnion(){
		Object type1 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("long", TokenType.LONG_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object declBlock = getDeclarationBlock(decl1, decl2);
		
        Object blockWhile = getInnerBlock("blub", "blab");
		
        Object identifier7 = getIdentifier("l");
        
        Object identifier6 = getIdentifier("r");
        
        Object cond = getEqualsOp(identifier7, identifier6);
        
		Object obj = getDoWhile(cond, blockWhile);
		
		Object identifier3 = getIdentifier("var1");
		
        Object literal = getNumLiteral("20");
		
        Object assignment1 = getAssign(identifier3, literal);
        
        Object statement1 = getStatement(assignment1);

        Object identifier4 = getIdentifier("var2");
		
		Object literal2 = getNumLiteral("25");
		
		Object assignment2 = getAssign(identifier4, literal2);
		
		Object statement2 = getStatement(assignment2);
		
		Object stmtBlock = getStatementBlock(obj, statement1);
		
		Object identifier1 = getIdentifier("l");
		
		Object identifier2 = getIdentifier("r");

		Object cond1 = getGreaterEqualOp(identifier1, identifier2);
		   
        Object statement6 = getInnerBlock("var1","var2");

        Object statement7 = getInnerBlock("var3","var4");
		
		Object branch = getBranchWithElse(cond1, statement6, statement7);
		
		stmtBlock = getStatementBlock(stmtBlock, statement2);
		
		stmtBlock = getStatementBlock(stmtBlock, branch);
		
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
		checkParents(node,decl1,decl2,statement1,statement2,branch,obj);
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
	
	@Test
	public void testDoWhileWithFail(){

        Object statement1 = getInnerBlock("var1","var2");
		
        Object identifier1 = getIdentifier("l");
        
		Object obj = getDoWhile(identifier1, statement1);
		
		assertTrue(obj instanceof DoWhileNode);
		
		DoWhileNode dowhile = (DoWhileNode)obj;
		assertEquals(dowhile.getLoopBody(),statement1);
		assertEquals(dowhile.getLoopBody().getParentNode(),obj);
		assertEquals(dowhile.getCondition().getParentNode(),obj);
		checkCoverage(dowhile.coverage());
	}
	
	@Test
	public void testReturn(){
		
		Object ret = getReturn(null);
		
		assertTrue(ret instanceof ReturnNode);
		
		ReturnNode node = (ReturnNode)ret;
		
		assertNull(node.getRightValue());
		
		checkCoverage(node.coverage());
	}
	
	@Test
	public void testReturnWithIdentifier(){
		
		Object identifier = getIdentifier("a");
		
		Object ret = getReturn(identifier);
		
		assertTrue(ret instanceof ReturnNode);
		
		ReturnNode node = (ReturnNode)ret;
		
		assertEquals(node.getRightValue(),identifier);
		
		checkCoverage(node.coverage());
	}
	
	@Test
	public void testBreak(){
		
		Object br = getBreak();
		
		assertTrue(br instanceof BreakNode);
		
		BreakNode node = (BreakNode)br;
		
		checkCoverage(node.coverage());
	}
	
	@Test
	public void testPrint(){
	
		Object identifier = getIdentifier("a");
		
		Object ret = getPrint(identifier);
		
		assertTrue(ret instanceof PrintNode);
		
		PrintNode node = (PrintNode)ret;
		
		assertEquals(node.getRightValue(),identifier);
		
		checkCoverage(node.coverage());
	}
	
	@Test
	public void testRecordDeclaration(){
		Object type1 = getBasicDecl("double", TokenType.DOUBLE_SYMBOL);
		
		Object decl1 = getDecl(type1, "var1");
		
		Object type2 = getBasicDecl("string", TokenType.STRING_SYMBOL);
		
		Object decl2 = getDecl(type2, "var2");
		
		Object block = getDeclarationBlock(decl1, decl2);
		
		Object record = getRecord(block);
		
		Object decl = getDecl(record, "myRecord");
		
		assertTrue(decl instanceof DeclarationNode);
		
		DeclarationNode node = (DeclarationNode)decl;
		
		assertEquals(node.getIdentifier(), "myRecord");
		
		checkCoverage(node.coverage());
	}
	
	@Test
	public void testArrayAccess(){
		
		Object id = getIdentifier("bla");
		
		Object identifier = getIdentifier("arr");
		
		Object number = getNumLiteral("5");
		
		Object array = getArrayAccess(identifier, number);
		
		Object assign = getAssign(id, array);
		
		Object stmt = getStatement(assign);
		
		assertTrue(stmt instanceof StatementNode);
		assertTrue(array instanceof ArrayIdentifierNodeImpl);
		ArrayIdentifierNodeImpl node = (ArrayIdentifierNodeImpl)array;
		assertEquals(node.getIndexNode(),number);
		checkCoverage(((ASTNode)stmt).coverage());
		
	}

}

