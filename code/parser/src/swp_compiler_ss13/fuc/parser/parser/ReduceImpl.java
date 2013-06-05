package swp_compiler_ss13.fuc.parser.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.lexer.NumToken;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.SymbolTable;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.common.types.Type;
import swp_compiler_ss13.common.types.derived.ArrayType;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.ArrayIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.BranchNodeImpl;
import swp_compiler_ss13.fuc.ast.BreakNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.PrintNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;
import swp_compiler_ss13.fuc.ast.StructIdentifierNodeImpl;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.symbolTable.SymbolTableImpl;

public class ReduceImpl {
	
	
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final Object NO_VALUE = new String("NoValue");

	private static final Logger log = Logger.getLogger(ReduceImpl.class);

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	
	/**
	 * Defines a ReduceAction for every rule in the Grammar
	 * @param prod 
	 * @param reportLog
	 * @return
	 */
	protected static ReduceAction getReduceAction(Production prod, final ReportLog reportLog) {
		switch (prod.getStringRep()) {

		case "program -> decls stmts":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Object left = objs[0]; // Should be NO_VALUE or BlockNode
					Object right = objs[1]; // Should be NO_VALUE or BlockNode

					BlockNodeImpl block = joinBlocks(left, right, reportLog);
					
					block.setCoverage(((BlockNode)left).coverage());
					block.setCoverage(((BlockNode)right).coverage());
					
					return block;
				}
			};

		case "block -> { decls stmts }":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Object left = objs[1]; // Should be NO_VALUE or BlockNode
					Object right = objs[2]; // Should be NO_VALUE or BlockNode
					
					BlockNodeImpl block = joinBlocks(left, right, reportLog);
					
					Token leftBranch = (Token)objs[0];
					block.setCoverage(leftBranch);
					
					block.setCoverage(((BlockNode)left).coverage());
					block.setCoverage(((BlockNode)right).coverage());
					
					Token rightBranch = (Token)objs[3];
					block.setCoverage(rightBranch);
										
					return block;
				}
			};

		case "decls -> decls decl":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Object left = objs[0]; // Should be NO_VALUE, BlockNode or DeclarationNode
					Object right = objs[1]; // Should be DeclarationNode or
											// BlockNode

					LinkedList<DeclarationNode> declList = new LinkedList<>();
					// Handle left
					if (!left.equals(NO_VALUE)) {
						if (left instanceof BlockNode) {
							BlockNode leftBlock = (BlockNode) left;
							declList.addAll(leftBlock.getDeclarationList());
						} else if (!(left instanceof DeclarationNode)) {
							log.error("Error in decls -> decls decl: Left must be a DeclarationNode!");
						} else {
							declList.add((DeclarationNode) left);
						}
					}

					// Handle right
					if (right instanceof BlockNode) {
						BlockNode tmpBlock = (BlockNode) right;
						declList.addAll(tmpBlock.getDeclarationList());
					} else {
						if (!(right instanceof DeclarationNode)) {
							log.error("Error in decls -> decls decl: Right must be a DeclarationNode!");
						} else {
							declList.add((DeclarationNode) right);
						}
					}

					// Create new BlockNode
					BlockNode block = new BlockNodeImpl();
					block.setSymbolTable(new SymbolTableImpl());
					for (DeclarationNode decl : declList) {
						insertDecl(block, decl, reportLog);
					}
					return block;
				}
			};

		case "decls -> ε":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return NO_VALUE; // Symbolizes epsilon
				}
			};

		case "decl -> type id ;":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					
					if(!(objs[0] instanceof DeclarationNode)){
						if(objs[0] instanceof ASTNode){
							reportLog.reportError(ReportType.UNDEFINED, ((ASTNode)objs[0]).coverage(), "there is no Declarationnode found!");
							throw new ParserException("Declarationnode expected");
						}
						if(objs[0] instanceof Token){
							List<Token> list = new ArrayList<Token>();
							list.add((Token)objs[0]);
							reportLog.reportError(ReportType.UNDEFINED, list, "there is no Declarationnode found!");
							throw new ParserException("Declarationnode expected");

						}
					}
					
					DeclarationNode decl = (DeclarationNode) objs[0];
					Token idToken = (Token) objs[1];
					Token semicolon = (Token) objs[2];
					
					if(decl.getType() instanceof ReduceStringType){
						List<Token> coverage = decl.coverage();
						decl = new DeclarationNodeImpl();
						decl.setType(new StringType((long)idToken.getValue().length()));
						((DeclarationNodeImpl)decl).setCoverage(coverage);
					}

					// Set ID
					decl.setIdentifier(idToken.getValue());
					
					//Set left token
					((DeclarationNodeImpl)decl).setCoverage(idToken, semicolon);
					return decl;
				}
			};

		case "stmts -> stmts stmt":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Object left = objs[0]; // Should be NO_VALUE or BlockNode or other
											// StatementNode
					Object right = objs[1]; // Should be StatementNode or
											// BlockNode

					LinkedList<StatementNode> stmtList = new LinkedList<>();
					// Handle left
					if (!left.equals(NO_VALUE)) {
						// there are just 0ne statement
						if (left instanceof BlockNode) {
							stmtList.addAll(((BlockNode) left).getStatementList());
						} else if (!(left instanceof StatementNode)) {
							log.error("Error in decls -> decls decl: Left must be a DeclarationNode!");
						} else {
							stmtList.add((StatementNode) left);
						}
					}

					// Handle right
					if (right instanceof BlockNode) {
						BlockNode oldBlock = (BlockNode) right;
						stmtList.addAll(oldBlock.getStatementList());
					} else {
						if (!(right instanceof StatementNode)) {
							log.error("Error in decls -> decls decl: Right must be a DeclarationNode!");
						} else {
							stmtList.add((StatementNode) right);
						}
					}

					// Create new BlockNode
					BlockNode block = new BlockNodeImpl();
					for (StatementNode stmt : stmtList) {
						block.addStatement(stmt);
						stmt.setParentNode(block);
					}
					return block;
				}
			};

		case "stmts -> ε":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return NO_VALUE; // Symbolizes epsilon
				}
			};

		case "stmt -> assign ;":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					AssignmentNode assign = (AssignmentNode) objs[0];
					Token semicolon = (Token) objs[1];
					((AssignmentNodeImpl)assign).setCoverage(semicolon);
					return  assign;
				}
			};
		case "stmt -> block":
			break;

		case "stmt -> if ( assign ) stmt":
			return new ReduceAction() {

				@Override
				public Object create(Object... objs) throws ParserException {
					
					BranchNodeImpl node = new BranchNodeImpl();
					
					Token ifToken = (Token)objs[0];
					Token leftBranch = (Token)objs[1];
					node.setCoverage(ifToken,leftBranch);
					
					
					if(objs[2] instanceof AssignmentNode){
						AssignmentNode condition = (AssignmentNode)objs[2];
						node.setCondition(condition);
						node.setCoverage(condition.coverage());
					}else{
						writeReportError(reportLog, objs[2], "Assignment");
					}
					
					Token rightBranch = (Token)objs[3];
					node.setCoverage(rightBranch);
					
					if(objs[4] instanceof BlockNode){
						node.setStatementNodeOnTrue((BlockNode)objs[4]);
					}else{
						if(objs[4] instanceof StatementNode){
							StatementNode block = (StatementNode)objs[4];
							node.setStatementNodeOnTrue(block);
							node.setCoverage(block.coverage());
						}else{
							writeReportError(reportLog, objs[4], "Block or Statement");
						}
					}
					
					return node;
				}
				
			};
		case "stmt -> if ( assign ) stmt else stmt":
			return new ReduceAction() {

				@Override
				public Object create(Object... objs) throws ParserException {
					
					BranchNodeImpl node = new BranchNodeImpl();
					
					Token ifToken = (Token)objs[0];
					Token leftBranch = (Token)objs[1];
					node.setCoverage(ifToken,leftBranch);
					
					if(objs[2] instanceof AssignmentNode){
						AssignmentNode condition = (AssignmentNode)objs[2];
						node.setCondition(condition);
						node.setCoverage(condition.coverage());
					}else{
						writeReportError(reportLog, objs[2], "Assignment");
					}
					
					Token rightBranch = (Token)objs[3];
					node.setCoverage(rightBranch);
					
					if(objs[4] instanceof BlockNode){
						BlockNode block = (BlockNode)objs[4];
						node.setStatementNodeOnFalse(block);
						node.setCoverage(block.coverage());
					}else{
						if(objs[4] instanceof StatementNode){
							StatementNode block = (StatementNode)objs[4];
							node.setStatementNodeOnFalse(block);
							node.setCoverage(block.coverage());
						}else{
							writeReportError(reportLog, objs[4], "Statement or BlockNode");
						}
					}
					
					Token elseToken = (Token)objs[5];
					node.setCoverage(elseToken);
							
					if(objs[6] instanceof BlockNode){
						BlockNode block = (BlockNode)objs[6];
						node.setStatementNodeOnFalse(block);
						node.setCoverage(block.coverage());
					}else{
						if(objs[6] instanceof StatementNode){
							StatementNode block = (StatementNode)objs[6];
							node.setStatementNodeOnFalse(block);
							node.setCoverage(block.coverage());						
						}else{
							writeReportError(reportLog, objs[6], "Block or Statement");
						}
					}
					return node;
				}

			};
			
		case "stmt -> while ( assign ) stmt":
		case "stmt -> do stmt while ( assign )":
			// TODO M3
			break;

		case "stmt -> break ;":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					BreakNodeImpl breakImpl = new BreakNodeImpl();
					breakImpl.setCoverage((Token)objs[0],(Token)objs[1]);
					return new BreakNodeImpl();
				}

			};
		case "stmt -> return ;":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					ReturnNodeImpl returnImpl = new ReturnNodeImpl();
					returnImpl.setCoverage((Token)objs[0],(Token)objs[1]);
					return returnImpl;
				}
			};
		case "stmt -> return loc ;":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					ReturnNode returnNode = new ReturnNodeImpl();
					IdentifierNode identifier = (IdentifierNode) objs[1];
					returnNode.setRightValue(identifier);
					identifier.setParentNode(returnNode);
					return returnNode;
				}
			};
			
		case "stmt -> print loc ;":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					PrintNode printNode = new PrintNodeImpl();
					if(objs[0] instanceof IdentifierNode){
						printNode.setRightValue((IdentifierNode)objs[0]);
					}else{
						writeReportError(reportLog, objs[0], "Identifier");
					}
					
					return printNode;
				}
			};
			
		case "loc -> loc [ assign ]":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					ArrayIdentifierNodeImpl arrayIdentifier= new ArrayIdentifierNodeImpl();
					
					if(!(objs[0] instanceof IdentifierNode)){
						writeReportError(reportLog, objs[0], "Identifier");
					}
					
					IdentifierNode node = (IdentifierNode)objs[0];
					arrayIdentifier.setIdentifierNode(node);
					arrayIdentifier.setCoverage(node.coverage());
					node.setParentNode(arrayIdentifier);
					
					if(!(objs[1] instanceof Token)){
						writeReportError(reportLog, objs[1], "Token [");
					}
					
					Token leftSquareBracket = (Token) objs[1];
					arrayIdentifier.setCoverage(leftSquareBracket);

					if(!(objs[2] instanceof LiteralNode)){
						writeReportError(reportLog, objs[2], "Literal");
					}					
					
					LiteralNode literal = (LiteralNode)objs[2];
					if(!(literal.getLiteralType() instanceof LongType)){
						writeReportError(reportLog, objs[2], "Number");
					}
					
					int index = Integer.parseInt(literal.getLiteral());
					arrayIdentifier.setIndex(index);
					arrayIdentifier.setCoverage(literal.coverage());
					
					if(!(objs[3] instanceof Token)){
						writeReportError(reportLog, objs[1], "Token [");
					}
					
					Token rightSquareBracket = (Token) objs[3];
					arrayIdentifier.setCoverage(rightSquareBracket);
					return arrayIdentifier;
				}
			};

		case "loc -> id":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					BasicIdentifierNodeImpl identifierNode = new BasicIdentifierNodeImpl();
					
					if(!(objs[0] instanceof Token)){
						writeReportError(reportLog, objs[0], "Token id");
					}
					
					Token token = (Token) objs[0];
					identifierNode.setIdentifier(token.getValue());
					identifierNode.setCoverage(token);
					return identifierNode;
				}
			};
		case "loc -> loc.id":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {

					StructIdentifierNodeImpl identifierNode = new StructIdentifierNodeImpl();
					
					if(!(objs[0] instanceof IdentifierNode)){
						writeReportError(reportLog, objs[0], "Identifier");
					}
					
					IdentifierNode node = (IdentifierNode)objs[0];
					identifierNode.setIdentifierNode(node);
					identifierNode.setCoverage(node.coverage());
					node.setParentNode(identifierNode);
					
					if(!(objs[1] instanceof Token)){
						writeReportError(reportLog, objs[1], "Token .");
					}
					
					Token dot = (Token) objs[1];
					identifierNode.setCoverage(dot);

					if(!(objs[2] instanceof Token)){
						writeReportError(reportLog, objs[2], "Token id");
					}
					
					Token token = (Token) objs[2];
					identifierNode.setFieldName(token.getValue());
					identifierNode.setCoverage(token);
					
					return identifierNode;
				}
			};
		case "assign -> loc = assign":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					AssignmentNodeImpl assignNode = new AssignmentNodeImpl();
					IdentifierNode identifier = (IdentifierNode) objs[0];
					Token equalSign = (Token) objs[1];
					ExpressionNode node = (ExpressionNode) objs[2];
					assignNode.setLeftValue(identifier);
					assignNode.getLeftValue().setParentNode(assignNode);
					assignNode.setRightValue(node); 
					assignNode.getRightValue().setParentNode(assignNode);
					
					AssignmentNodeImpl assignImpl = ((AssignmentNodeImpl)assignNode);
					
					assignImpl.setCoverage(identifier.coverage());
					assignImpl.setCoverage(equalSign);
					assignImpl.setCoverage(node.coverage());
					return assignNode;
				}
			};
		case "assign -> bool":
			break;
		case "bool -> bool || join":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.LOGICAL_OR, "||", objs);
				}
			};
		case "bool -> join":
		case "join -> join && equality":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.LOGICAL_AND, "&&", objs);
				}
			};
		case "join -> equality":
			break; // Nothing to do here

		case "equality -> equality == rel":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.EQUAL, "==", objs);
				}
			};
		case "equality -> equality != rel":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.INEQUAL, "!=", objs);
				}
			};
		case "equality -> rel":
			return null; // Nothing to do here
		case "rel -> expr < expr":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.LESSTHAN, "<", objs);
				}
			};
		case "rel -> expr > expr":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.GREATERTHAN, "<", objs);
				}
			};
		case "rel -> expr >= expr":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.GREATERTHANEQUAL, ">=", objs);
				}
			};
		case "rel -> expr <= expr":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createLogicalBinaryExpr(reportLog, BinaryOperator.LESSTHANEQUAL, "<=", objs);
				}
			};
		case "rel -> expr":
			break; // Nothing to do here
		case "expr -> expr + term":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createBinaryExpr(reportLog, BinaryOperator.ADDITION, "+", objs);
				}
			};
		case "expr -> expr - term":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createBinaryExpr(reportLog, BinaryOperator.SUBSTRACTION, "-", objs);
				}
			};
		case "expr -> term":
			break; // Nothing to do here
		case "term -> term * unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createBinaryExpr(reportLog, BinaryOperator.MULTIPLICATION, "*", objs);
				}
			};
		case "term -> term / unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return createBinaryExpr(reportLog, BinaryOperator.DIVISION, "/", objs);
				}
			};
		case "term -> unary":
			break; // Nothing to do here
		case "unary -> ! unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					
					LogicUnaryExpressionNodeImpl unary = new LogicUnaryExpressionNodeImpl();
					
					if(!(objs[0] instanceof Token)){
						writeReportError(reportLog, objs[0], "Token !");
					}
					
					Token token = (Token) objs[0];
					
					if(!(objs[1] instanceof ExpressionNode)){
						writeReportError(reportLog, objs[1], "Expression");
					}
					
					ExpressionNode expr = (ExpressionNode)objs[1];

					unary.setOperator(UnaryOperator.LOGICAL_NEGATE);
					unary.setRightValue(expr);
					expr.setParentNode(unary);
					unary.setCoverage(token);
					unary.setCoverage(expr.coverage());
					
					return unary;
				}
			};
		case "unary -> - unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					
					if(!(objs[0] instanceof Token)){
						writeReportError(reportLog, objs[0], "Token !");
					}
					
					Token token = (Token) objs[0];
					
					if(!(objs[1] instanceof ExpressionNode)){
						writeReportError(reportLog, objs[1], "Expression");
					}
					
					ExpressionNode expr = (ExpressionNode)objs[1];

					ArithmeticUnaryExpressionNodeImpl arithUnary = new ArithmeticUnaryExpressionNodeImpl();
					arithUnary.setOperator(UnaryOperator.MINUS);
					arithUnary.setRightValue(expr);
					expr.setParentNode(arithUnary);
					
					arithUnary.setCoverage(token);
					arithUnary.setCoverage(expr.coverage());
					
					return arithUnary;
				}
			};
		case "factor -> ( assign )":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					
					if(!(objs[0] instanceof Token)){
						writeReportError(reportLog, objs[0], "Token (");
					}
					
					if(!(objs[1] instanceof AssignmentNode)){
						writeReportError(reportLog, objs[1], "Assignment");
					}
					
					if(!(objs[2] instanceof Token)){
						writeReportError(reportLog, objs[2], "Token )");
					}

					AssignmentNodeImpl assign = (AssignmentNodeImpl) objs[1];
					assign.setCoverageAtFront((Token)objs[0]);
					assign.setCoverage((Token)objs[2]);
					
					return  assign;
				}
			};
		case "unary -> factor":
		case "factor -> loc":
			break;
		case "factor -> num":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					LiteralNode literal = new LiteralNodeImpl();
					Token token = (Token) objs[0];
					literal.setLiteral(token.getValue());
					literal.setLiteralType(new LongType());
					return literal;
				}
			};
		case "factor -> real":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					LiteralNode literal = new LiteralNodeImpl();
					Token token = (Token) objs[0];
					literal.setLiteral(token.getValue());
					literal.setLiteralType(new DoubleType());
					return literal;
				}
			};
		case "factor -> true":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					LiteralNode literal = new LiteralNodeImpl();
					Token token = (Token) objs[0];
					literal.setLiteral(token.getValue());
					literal.setLiteralType(new BooleanType());
					return literal;
				}
			};
		case "factor -> false":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					LiteralNode literal = new LiteralNodeImpl();
					Token token = (Token) objs[0];
					literal.setLiteral(token.getValue());
					literal.setLiteralType(new BooleanType());
					return literal;
				}
			};
		case "factor -> string":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					LiteralNode literal = new LiteralNodeImpl();
					Token token = (Token) objs[0];
					literal.setLiteral(token.getValue());
					literal.setLiteralType(new StringType((long) token
							.getValue().length()));
					return literal;
				}

			};
		case "type -> type [ num ]":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					//typeToken DeclarationNode is wasted after reduce, not needed any more
					DeclarationNode typeToken = (DeclarationNode) objs[0];
					Token leftBrace = (Token) objs[1];
					
					if(!(objs[2] instanceof NumToken)){
						writeReportError(reportLog,objs[2],"Number");
					}
					NumToken size = (NumToken) objs[2];
					Token rightBrace = (Token) objs[3];
					
					//create Array declaration
					Type type = new ArrayType(typeToken.getType(), size.getLongValue().intValue());
					DeclarationNodeImpl declImpl = new DeclarationNodeImpl();
					declImpl.setType(type);
					
					//set coverage
					declImpl.setCoverage(typeToken.coverage());
					declImpl.setCoverage(leftBrace,size,rightBrace);
					return declImpl;
				}

			};
		case "type -> bool":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Token token = (Token)objs[0];
					DeclarationNodeImpl decl = new DeclarationNodeImpl();
					decl.setType(new BooleanType());
					decl.setCoverage(token);
					
					return decl;
				}

			};
		case "type -> string":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Token token = (Token)objs[0];
					DeclarationNodeImpl decl = new DeclarationNodeImpl();
					
					//decl is thrown away afterwards, nobody has to know ReduceStringType
					decl.setType(new ReduceStringType(Type.Kind.STRING));
					
					decl.setCoverage(token);
					
					return decl;
				}

			};
		case "type -> num":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Token token = (Token)objs[0];
					DeclarationNodeImpl decl = new DeclarationNodeImpl();
					decl.setType(new LongType());
					decl.setCoverage(token);
					
					return decl;
				}

			};
		case "type -> real":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Token token = (Token)objs[0];
					DeclarationNodeImpl decl = new DeclarationNodeImpl();
					decl.setType(new DoubleType());
					decl.setCoverage(token);
					
					return decl;
				}

			};
		case "type -> record { decls }":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					//TODO: type reduce
					return null;
				}

			};
		default:
			return null; // Means "no ReduceAction to perform"
		}
		return null;
	}


	/**
	 * Inserts the {@link DeclarationNode} into the block and its {@link SymbolTable} safely.
	 * 
	 * @param block
	 * @param decl
	 */
	private static void insertDecl(BlockNode block, DeclarationNode decl, final ReportLog reportLog) throws ParserException {
		SymbolTable symbolTable = block.getSymbolTable();
		// TODO M2: Shadowing allowed???
		if (symbolTable.isDeclaredInCurrentScope(decl.getIdentifier())) {
			reportLog.reportError(ReportType.DOUBLE_DECLARATION, decl.coverage(), "The variable '" + 
			decl.getIdentifier() + "' of type '" + decl.getType() + "' has been declared twice!");
			throw new DoubleIdentifierException("double id exception");
		}
		block.addDeclaration(decl);
		block.getSymbolTable().insert(decl.getIdentifier(), decl.getType());
		decl.setParentNode(block);
	}

	private static BlockNodeImpl joinBlocks(Object left, Object right, ReportLog reportLog) throws ParserException{
		BlockNodeImpl newBlock = new BlockNodeImpl();
		newBlock.setSymbolTable(new SymbolTableImpl());
		
		// Handle left
		if (!left.equals(NO_VALUE)) {
			BlockNode declsBlock = (BlockNode) left;
			for (DeclarationNode decl : declsBlock.getDeclarationList()) {
				insertDecl(newBlock, decl, reportLog);
				decl.setParentNode(newBlock);
			}
		}

		// Handle right
		if (!right.equals(NO_VALUE)) {
			BlockNode stmtsBlock = (BlockNode) right;
			for (StatementNode stmt : stmtsBlock.getStatementList()) {
				newBlock.addStatement(stmt);
				stmt.setParentNode(newBlock);
			}
		}
		
		return newBlock;
	}

//	/**
//	 * Creates a binary operation as ArithmeticBinaryExpressionNode.
//	 * Add the coverage token to the node.
//	 * @param leftExpr
//	 * @param opSign
//	 * @param rightExpr
//	 * @param op
//	 * @return
//	 */
//	private static ArithmeticBinaryExpressionNode binop(Object leftExpr, Object opSign,
//			Object rightExpr, final BinaryOperator op) {
//		ExpressionNode left = (ExpressionNode) leftExpr;
//		ExpressionNode right = (ExpressionNode) rightExpr;
//
//		ArithmeticBinaryExpressionNode binop = new ArithmeticBinaryExpressionNodeImpl();
//		binop.setLeftValue(left);
//		binop.setRightValue(right);
//		binop.setOperator(op);
//		left.setParentNode(binop);
//		right.setParentNode(binop);
//
//		//set coverage
//		ArithmeticBinaryExpressionNodeImpl binopImpl = ((ArithmeticBinaryExpressionNodeImpl)binop);
//		binopImpl.setCoverage(left.coverage());
//		binopImpl.setCoverage((Token)opSign);
//		binopImpl.setCoverage(right.coverage());
//		
//		return binop;
//	}
	
	private static void writeReportError(final ReportLog reportLog,
			Object obj,String msg) {
		if(obj instanceof ASTNode){
			reportLog.reportError(ReportType.UNDEFINED, ((ASTNode)obj).coverage(), "there is no " + msg + " found!");
			throw new ParserException(msg +" expected");
		}
		if(obj instanceof Token){
			List<Token> list = new ArrayList<Token>();
			list.add((Token)obj);
			reportLog.reportError(ReportType.UNDEFINED, list, "there is no " + msg + " found!");
			throw new ParserException(msg +" expected");

		}
	}
	
	/**
	 * @param reportLog
	 * @param objs
	 * @return
	 */
	private static Object createLogicalBinaryExpr(final ReportLog reportLog,  
			final BinaryOperator op, String opStr,
			Object... objs) {
		
		LogicBinaryExpressionNodeImpl binExpr = new LogicBinaryExpressionNodeImpl();
		
		if(!(objs[0] instanceof ExpressionNode)){
			writeReportError(reportLog, objs[0], "Expression");
		}
		
		ExpressionNode left = (ExpressionNode)objs[0];
		binExpr.setLeftValue(left);
		binExpr.setCoverage(left.coverage());
		left.setParentNode(binExpr);
		
		if(!(objs[1] instanceof Token)){
			writeReportError(reportLog, objs[1], "Token " + opStr);
		}

		binExpr.setCoverage((Token)objs[1]);					
		binExpr.setOperator(op);
		
		if(!(objs[2] instanceof ExpressionNode)){
			writeReportError(reportLog, objs[2], "Expression");
		}
		
		ExpressionNode right = (ExpressionNode)objs[2];
		binExpr.setRightValue(right);
		binExpr.setCoverage(right.coverage());
		right.setParentNode(binExpr);
		
		return binExpr;
	}
	
	/**
	 * @param reportLog
	 * @param objs
	 * @return
	 */
	private static Object createBinaryExpr(final ReportLog reportLog,  
			final BinaryOperator op, String opStr,
			Object... objs) {
		
		ArithmeticBinaryExpressionNodeImpl binExpr = new ArithmeticBinaryExpressionNodeImpl();
		
		if(!(objs[0] instanceof ExpressionNode)){
			writeReportError(reportLog, objs[0], "Expression");
		}
		
		ExpressionNode left = (ExpressionNode)objs[0];
		binExpr.setLeftValue(left);
		binExpr.setCoverage(left.coverage());
		left.setParentNode(binExpr);
		
		if(!(objs[1] instanceof Token)){
			writeReportError(reportLog, objs[1], "Token " + opStr);
		}

		binExpr.setCoverage((Token)objs[1]);					
		binExpr.setOperator(op);
		
		if(!(objs[2] instanceof ExpressionNode)){
			writeReportError(reportLog, objs[2], "Expression");
		}
		
		ExpressionNode right = (ExpressionNode)objs[2];
		binExpr.setRightValue(right);
		binExpr.setCoverage(right.coverage());
		right.setParentNode(binExpr);
		
		return binExpr;
	}

	private static class ReduceStringType extends Type{

		/**
		 * its not possible to create a StringType without the length, so
		 * we need a dummy class to do it right
		 */
		protected ReduceStringType(Kind kind) {
			super(kind);
		}

		@Override
		public String getTypeName() {
			return "String";
		}

		@Override
		public String toString() {
			return getTypeName();
		}
	}
}
