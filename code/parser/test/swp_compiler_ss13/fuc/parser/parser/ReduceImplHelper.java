package swp_compiler_ss13.fuc.parser.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.LinkedList;
import java.util.List;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.fuc.lexer.token.NumTokenImpl;
import swp_compiler_ss13.fuc.lexer.token.TokenImpl;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;

public class ReduceImplHelper {

	protected static ReportLog reportLog;
	protected static List<Token> coverage = new LinkedList<Token>();

	public ReduceImplHelper() {
		super();
	}

	/**
	 * returns a complete statement with semicolon at the end
	 * @param statement
	 * @return
	 */
	protected Object getStatement(Object statement) {
		Token sem = new TokenImpl(";", TokenType.SEMICOLON, -1, -1);
		addAfter(sem, ((ASTNode)statement).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt1, reportLog);
		return action.create(statement,sem);
	}

	/**
	 * returns a representation of an inner block construct, to use in branches or loops
	 * @param var1
	 * @param var2
	 * @return
	 */
	protected Object getInnerBlock(String var1, String var2) {
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
	protected Object getDeclarationBlock(Object declaration1, Object declaration2) {
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.decls1, reportLog);
		return action.create(declaration1,declaration2);
	}

	/**
	 * reduces statementblock and declarationblock to one block
	 * @param decl
	 * @param statement
	 * @return
	 */
	protected Object getDeclBlockStateBlockUnion(Object decl, Object statement) {
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.program1, reportLog);
		return action.create(decl,statement);
	}

	/**
	 * reduces statementblock and declarationblock to one block covered in braces
	 * @param decl
	 * @param statement
	 * @return
	 */
	protected Object getDeclBlockStateBlockInBrace(Object decl, Object stmt) {
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
	protected Object getStatementBlock(Object statement1, Object statement2) {
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmts1, reportLog);
		return action.create(statement1, statement2);
	}

	/**
	 * Gets name of Identifier and line and row and returns an identifierNode
	 * @param name
	 * @return
	 */
	protected Object getIdentifier(String name) {
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
	protected Object getEqualsOp(Object identifier1, Object identifier2) {
		Token equalop = new TokenImpl("==", TokenType.EQUALS, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.equality1, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}

	/**
	 * Gets two identifier and reduce to an equal operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	protected Object getNotEqualOp(Object identifier1, Object identifier2) {
		Token equalop = new TokenImpl("==", TokenType.NOT_EQUALS, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.equality2, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}

	/**
	 * Gets two identifier and reduce to an greater than operation
	 * @param identifier1
	 * @param identifier2
	 * @return
	 */
	protected Object getGreaterOp(Object identifier1, Object identifier2) {
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
	protected Object getLesserOp(Object identifier1, Object identifier2) {
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
	protected Object getGreaterEqualOp(Object identifier1, Object identifier2) {
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
	protected Object getLessEqualOp(Object identifier1, Object identifier2) {
		Token equalop = new TokenImpl("<=", TokenType.LESS_OR_EQUAL, 2, 3);
		addAfter(equalop, ((ASTNode)identifier1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.rel2, reportLog);
		return action.create(identifier1, equalop, identifier2);
	}

	/**
	 * Gets two expressions and reduce to an addition operation
	 * @param expr1
	 * @param expr2
	 * @return
	 */
	protected Object getAdditionOp(Object expr1, Object expr2) {
		Token binop = new TokenImpl("+", TokenType.PLUS, 2, 3);
		addAfter(binop, ((ASTNode)expr1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.expr1, reportLog);
		return action.create(expr1, binop, expr2);
	}

	/**
	 * Gets two expressions and reduce to a subtraction operation
	 * @param expr1
	 * @param expr2
	 * @return
	 */
	protected Object getSubtractionOp(Object expr1, Object expr2) {
		Token binop = new TokenImpl("-", TokenType.MINUS, 2, 3);
		addAfter(binop, ((ASTNode)expr1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.expr2, reportLog);
		return action.create(expr1, binop, expr2);
	}
	
	/**
	 * Gets two expressions and reduce to a multiplication operation
	 * @param expr1
	 * @param expr2
	 * @return
	 */
	protected Object getMultiplicationOp(Object expr1, Object expr2) {
		Token binop = new TokenImpl("*", TokenType.TIMES, 2, 3);
		addAfter(binop, ((ASTNode)expr1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.term1, reportLog);
		return action.create(expr1, binop, expr2);
	}
	
	/**
	 * Gets two expressions and reduce to a division operation
	 * @param expr1
	 * @param expr2
	 * @return
	 */
	protected Object getDivisionOp(Object expr1, Object expr2) {
		Token binop = new TokenImpl("/", TokenType.DIVIDE, 2, 3);
		addAfter(binop, ((ASTNode)expr1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.term2, reportLog);
		return action.create(expr1, binop, expr2);
	}
	
	/**
	 * Gets an expression and reduce to a unary not operation
	 * @param expr1
	 * @return
	 */
	protected Object getNotOp(Object expr1) {
		Token unop = new TokenImpl("!", TokenType.NOT, -1, -1);
		addBefore(unop, ((ASTNode)expr1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.unary1, reportLog);
		return action.create(unop,expr1);
	}
	
	/**
	 * Gets an expression and reduce to a unary - operation
	 * @param expr1
	 * @return
	 */
	protected Object getMinusOp(Object expr1) {
		Token unop = new TokenImpl("-", TokenType.MINUS, -1, -1);
		addBefore(unop, ((ASTNode)expr1).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.unary2, reportLog);
		return action.create(unop,expr1);
	}
	
	/**
	 * Gets a value and returns a num literal
	 * @param value
	 * @return
	 */
	protected Object getNumLiteral(String value) {
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
	protected Object getStringLiteral(String value) {
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
	protected Object getRealLiteral(String value) {
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
	protected Object getBoolLiteral(String value, TokenType ttype) {
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
	protected Object getAssign(Object identifier, Object literal) {
		Token assign = new TokenImpl("=",TokenType.ASSIGNOP,1,7);
		addAfter(assign, ((ASTNode)identifier).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.assign1, reportLog);
		
		return action.create(identifier,assign,literal);
	}

	/**
	 * creates an array reduction with given type and length
	 * @return
	 */
	protected Object getArrayType(Object type, String length) {
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
	protected Object getBranchWithElse(Object cond, Object statement1, Object statement2) {
		
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
	protected Object getBranchWithoutElse(Object cond, Object statement) {
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
	protected Object getDecl(Object type, String id) {
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
	protected Object getBasicDecl(String type, TokenType ttype) {
		ReduceAction action1 = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type2, reportLog);
		Token longToken = new TokenImpl(type, ttype, 1, 1);
		coverage.add(longToken);
		return action1.create(longToken);
	}
	
	/**
	 * Creates a do while reduction end returns a dowhilenode
	 * @param cond
	 * @param statement
	 * @return
	 */
	protected Object getDoWhile(Object cond, Object statement) {
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
	protected Object getWhile(Object cond, Object statement) {
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
	 * creates a return statement
	 * @param identifier
	 * @return
	 */
	protected Object getReturn(Object identifier) {
		
		Token ret = new TokenImpl("return", TokenType.RETURN, -1, -1);
		Token sem = new TokenImpl(";", TokenType.SEMICOLON, -1, -1);
		
		if(identifier == null){
			coverage.add(ret);
			coverage.add(sem);
			ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt6, reportLog);

			return action.create(ret,sem);
			
		}else{
			
			addBefore(ret, ((ASTNode)identifier).coverage());
			addAfter(sem, ((ASTNode)identifier).coverage());
			ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt10, reportLog);
			
			return action.create(ret,identifier,sem);
		}
	}
	
	/**
	 * creates a print statement
	 * @param identifier
	 * @return
	 */
	protected Object getPrint(Object identifier) {
		
		Token print = new TokenImpl("print", TokenType.PRINT, -1, -1);
		Token sem = new TokenImpl(";", TokenType.SEMICOLON, -1, -1);
			
		addBefore(print, ((ASTNode)identifier).coverage());
		addAfter(sem, ((ASTNode)identifier).coverage());
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt7, reportLog);
			
		return action.create(print,identifier,sem);
		
	}
	
	/**
	 * creates a break statement
	 * @return
	 */
	protected Object getBreak() {
		
		Token breakToken = new TokenImpl("break", TokenType.BREAK, -1, -1);
		Token sem = new TokenImpl(";", TokenType.SEMICOLON, -1, -1);
		

		coverage.add(breakToken);
		coverage.add(sem);
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.stmt5, reportLog);
		
		return action.create(breakToken,sem);
	}
	
	/**
	 * creates a record statement
	 * @return
	 */
	protected Object getRecord(Object decls) {
		
		Token record = new TokenImpl("record", TokenType.RECORD_SYMBOL, -1, -1);
		Token lb = new TokenImpl("{", TokenType.LEFT_BRACE, -1, -1);
		Token rb = new TokenImpl("}", TokenType.RIGHT_BRACE, -1, -1);
		
		addBefore(record,((ASTNode)decls).coverage());
		addBefore(lb,((ASTNode)decls).coverage());
		
		addAfter(rb,((ASTNode)decls).coverage());
		
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.type3, reportLog);
		
		
		return action.create(record,lb,decls,rb);
	}
	
	/**
	 * returns an access on an arrayNode
	 * @param identifier
	 * @param assignment
	 * @return
	 */
	protected Object getArrayAccess(Object identifier, Object assignment) {
		
		Token lb = new TokenImpl("[", TokenType.LEFT_BRACKET, -1, -1);
		Token rb = new TokenImpl("]", TokenType.RIGHT_BRACKET, -1, -1);
		
		addBefore(lb,((ASTNode)assignment).coverage());
		
		addAfter(rb,((ASTNode)assignment).coverage());
		
		ReduceAction action = ReduceImpl.getReduceAction(ProjectGrammar.Complete.loc1, reportLog);
		
		return action.create(identifier,lb,assignment,rb);
	}
	
	
	/**
	 * adds token after specific coverage
	 * @param ins
	 * @param cov
	 */
	private void addAfter(Token ins, List<Token> cov) {
		Token last = cov.get(cov.size()-1);
		addAfter(ins, last);
	}

	private void addAfter(Token ins, Token cov) {
		for(Token token : coverage){
			if(cov == token){
				int index = coverage.indexOf(token);
				coverage.add(index+1, ins);
				break;
			}
		}
	}

	private void addBefore(Token ins, List<Token> cov) {
		Token first = cov.get(0);
		addBefore(ins, first);
	}

	private void addBefore(Token ins, Token cov) {
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
	protected void checkCoverage(List<Token> cov) {
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
	protected void checkSymbolTable(SymbolTable symbolTable, Object ...decls) {
		for(int i = 0; i<decls.length;i++){
			assertTrue(symbolTable.isDeclared(((DeclarationNode)decls[i]).getIdentifier()));
		}
	}
	

}