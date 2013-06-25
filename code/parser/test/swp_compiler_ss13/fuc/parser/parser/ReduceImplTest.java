package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
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
		coverage.clear();
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testBasicTypeReduceLong(){
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object longToken = new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1);
		Object obj = action.create(longToken);
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof LongType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), longToken);
		
	}
	
	@Test
	public void testBasicTypeReduceBool(){
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object boolToken = new TokenImpl("bool", TokenType.BOOL_SYMBOL, 1, 1);
		Object obj = action.create(boolToken);
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof BooleanType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), boolToken);
	}
	
	@Test
	public void testBasicTypeReduceReal(){
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object realToken = new TokenImpl("real", TokenType.DOUBLE_SYMBOL, 1, 1);
		Object obj = action.create(realToken);
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof DoubleType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), realToken);
	}
	
	
	@Test
	public void testDeclReduce(){
		
		ReduceAction action1 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object longToken = new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1);
		DeclarationNode type = (DeclarationNode) action1.create(longToken);
		
		ReduceAction action2 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.decl1, reportLog);
		Object semicolon = new TokenImpl(";",TokenType.SEMICOLON,1,7);
		Object identifier = new TokenImpl("l",TokenType.ID,1,6);
		Object obj = action2.create(type, identifier, semicolon);
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof LongType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), longToken);
		assertEquals(((DeclarationNode)obj).coverage().get(1), identifier);
		assertEquals(((DeclarationNode)obj).coverage().get(2), semicolon);
		//looks for right identifier
		assertEquals(((DeclarationNode)obj).getIdentifier(), ((Token)identifier).getValue());
	
	}
	
	@Test
	public void testDeclReduce2(){
		
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object boolToken = new TokenImpl("bool", TokenType.BOOL_SYMBOL, 1, 1);
		Object bool = action.create(boolToken);
		
		ReduceAction action2 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.decl1, reportLog);
		Object semicolon = new TokenImpl(";",TokenType.SEMICOLON,1,7);
		Object identifier = new TokenImpl("b",TokenType.ID,1,6);
		Object obj = action2.create(bool, identifier, semicolon);
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof BooleanType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), boolToken);
		assertEquals(((DeclarationNode)obj).coverage().get(1), identifier);
		assertEquals(((DeclarationNode)obj).coverage().get(2), semicolon);
		//looks for right identifier
		assertEquals(((DeclarationNode)obj).getIdentifier(), ((Token)identifier).getValue());
	
	}
	
	
	
	@Test
	public void testArrayTypeReduce(){
		ReduceAction action1 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object longToken = new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1);
		DeclarationNode type = (DeclarationNode) action1.create(longToken);
		
		ReduceAction action2 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type1, reportLog);
		Object lfb = new TokenImpl("[",TokenType.ID,1,6);
		Object num = new NumTokenImpl("5",TokenType.NUM,1,7);
		Object rb = new TokenImpl("]",TokenType.ID,1,8);

		
		Object obj = action2.create(type, lfb, num,rb);
		
		
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof ArrayType);
		assertTrue(((ArrayType)((DeclarationNode)obj).getType()).getLength()==5);
		assertTrue(((ArrayType)((DeclarationNode)obj).getType()).getInnerType() instanceof LongType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), longToken);
		assertEquals(((DeclarationNode)obj).coverage().get(1), lfb);
		assertEquals(((DeclarationNode)obj).coverage().get(2), num);
		assertEquals(((DeclarationNode)obj).coverage().get(3), rb);

	}
	
	
	@Test
	public void testArrayDeclReduce(){
		ReduceAction action1 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Object longToken = new TokenImpl("long", TokenType.LONG_SYMBOL, 1, 1);
		DeclarationNode type = (DeclarationNode) action1.create(longToken);
		
		ReduceAction action2 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type1, reportLog);
		Object lfb = new TokenImpl("[",TokenType.ID,1,6);
		Object num = new NumTokenImpl("5",TokenType.NUM,1,7);
		Object rb = new TokenImpl("]",TokenType.ID,1,8);
		
		Object arrayType = action2.create(type, lfb, num,rb);
		
		ReduceAction action3 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.decl1, reportLog);
		Object semicolon = new TokenImpl(";",TokenType.SEMICOLON,1,11);
		Object identifier = new TokenImpl("l",TokenType.ID,1,9);
		
		Object obj = action3.create(arrayType, identifier, semicolon);
		
				
		//looks for right return type
		assertTrue(obj instanceof DeclarationNode);
		//looks for the right type
		assertTrue(((DeclarationNode)obj).getType() instanceof ArrayType);
		assertTrue(((ArrayType)((DeclarationNode)obj).getType()).getLength()==5);
		assertTrue(((ArrayType)((DeclarationNode)obj).getType()).getInnerType() instanceof LongType);
		//looks for right Token in coverage
		assertEquals(((DeclarationNode)obj).coverage().get(0), longToken);
		assertEquals(((DeclarationNode)obj).coverage().get(1), lfb);
		assertEquals(((DeclarationNode)obj).coverage().get(2), num);
		assertEquals(((DeclarationNode)obj).coverage().get(3), rb);
	
	}
	
	@Test
	public void testLiteralCreation(){
		Object literal = new TokenImpl("20",TokenType.NUM,1,6);
		ReduceAction action1 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor3, reportLog);
		
		Object obj = action1.create(literal);
		
		//looks for right return type
		assertTrue(obj instanceof LiteralNode);
		assertTrue(((LiteralNode)obj).getLiteralType() instanceof LongType);
		assertTrue(((LiteralNode)obj).getLiteral()=="20");
		
		//looks for right Token in coverage
		assertEquals(((LiteralNode)obj).coverage().get(0), literal);

	}

	@Test
	public void testIdentifierCreation(){
		Token identifier = new TokenImpl("l",TokenType.ID,1,6);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.loc2, reportLog);

		Object obj = action.create(identifier);

		//looks for right return type
		assertTrue(obj instanceof BasicIdentifierNode);
		assertTrue(((BasicIdentifierNode)obj).getIdentifier()=="l");
		
		//looks for right Token in coverage
		assertEquals(((BasicIdentifierNode)obj).coverage().get(0), identifier);

	}

	@Test
	public void testAssign(){
		Object identifierToken = new TokenImpl("l",TokenType.ID,1,6);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.loc2, reportLog);

		Object identifier = getIdentifier("l", 6, 7);
		
		Object literal = getLiteral("200", 5, 10);

		Object obj = getAssign(identifier, literal);
		
		assertTrue(obj instanceof AssignmentNode);
		assertEquals(((AssignmentNode)obj).getLeftValue(),identifier);
		assertEquals(((AssignmentNode)obj).getRightValue(),literal);
		assertEquals(((AssignmentNode)obj).getLeftValue().getParentNode(),obj);
		assertEquals(((AssignmentNode)obj).getRightValue().getParentNode(),obj);

		
	}
	
	@Test
	public void testLogicalBinaryOp(){
		Object identifier1 = getIdentifier("l", 1, 6);
		
		Object identifier2 = getIdentifier("r", 2, 2);
		
		Object cond = getEqualsOp(identifier1, identifier2);
		
		assertTrue(cond instanceof RelationExpressionNodeImpl);
		assertEquals(((RelationExpressionNodeImpl)cond).getLeftValue(),identifier1);
		assertEquals(((RelationExpressionNodeImpl)cond).getRightValue(),identifier2);
		
	}
	
	
	@Test
	public void branchWoElseTest(){
		
		Object identifier1 = getIdentifier("l", 1, 6);
		
		Object identifier2 = getIdentifier("r", 2, 2);
		
		Object cond = getEqualsOp(identifier1, identifier2);
		
		Object literal = getLiteral("20", 2, 10);
		
		Object assignment1 = getAssign(identifier1, literal);
		
		Object obj = getBranchWithoutElse(cond, assignment1);
		
		assertTrue(obj instanceof BranchNode);
		assertEquals(((BranchNode)obj).getStatementNodeOnTrue(),assignment1);
		assertEquals(((BranchNode)obj).getCondition(),cond);
		assertEquals(((BranchNode)obj).getStatementNodeOnTrue().getParentNode(),obj);
		assertEquals(((BranchNode)obj).getStatementNodeOnFalse(), null);
		assertEquals(((BranchNode)obj).getCondition().getParentNode(),obj);

	}
	
	


	@Test
	public void testBranchWElse(){

		Object identifier1 = getIdentifier("l", 1, 6);
		
		Object identifier2 = getIdentifier("r", 2, 2);
		
		Object cond = getEqualsOp(identifier1, identifier2);
		
		Object literal = getLiteral("20", 2, 10);
		
		Object assignment1 = getAssign(identifier1, literal);
		
		Object assignment2 = getAssign(identifier2, literal);
		
		Object obj = getBranchWithElse(cond, assignment1, assignment2);
		
		assertTrue(obj instanceof BranchNode);
		assertEquals(((BranchNode)obj).getStatementNodeOnTrue(),assignment1);
		assertEquals(((BranchNode)obj).getStatementNodeOnFalse(),assignment2);
		assertEquals(((BranchNode)obj).getCondition(),cond);
		assertEquals(((BranchNode)obj).getStatementNodeOnTrue().getParentNode(),obj);
		assertEquals(((BranchNode)obj).getStatementNodeOnFalse().getParentNode(),obj);
		assertEquals(((BranchNode)obj).getCondition().getParentNode(),obj);

	}
	
	/**
	 * Gets name of Identifier and line and row and returns an identifierNode
	 * @param name
	 * @param line
	 * @param row
	 * @return
	 */
	private Object getIdentifier(String name, int line, int row){
		Token identifierToken = new TokenImpl(name,TokenType.ID,line,row);
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
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.equality1, reportLog);
		
		return action.create(identifier1, equalop, identifier2);
	}
	
	/**
	 * Gets a value and returns a literal
	 * @param value
	 * @param line
	 * @param row
	 * @return
	 */
	private Object getLiteral(String value, int line, int row){
		Object literalToken = new TokenImpl(value,TokenType.NUM,line,row);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.factor3, reportLog);
		
		return action.create(literalToken);
	}
	
	private Object getAssign(Object identifier, Object literal){
		Object assign = new TokenImpl("=",TokenType.ASSIGNOP,1,7);
		
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.assign1, reportLog);
		
		return action.create(identifier,assign,literal);
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
		Token lb = new TokenImpl("(", TokenType.LEFT_PARAN, 3, 10);
		Token rb = new TokenImpl(")", TokenType.RIGHT_PARAN, 3, 10);
		Token elseToken = new TokenImpl("else", TokenType.ELSE,5,1);
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
		Token lb = new TokenImpl("(", TokenType.LEFT_PARAN, 3, 10);
		Token rb = new TokenImpl(")", TokenType.RIGHT_PARAN, 3, 10);
		ReduceAction action6 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt2, reportLog);
		
		return action6.create(ifToken, lb, cond, rb, statement);
	}

}
