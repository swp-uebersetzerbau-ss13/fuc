package swp_compiler_ss13.fuc.parser.parser;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.AssignmentNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.ternary.BranchNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.PrintNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
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
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.BranchNodeImpl;
import swp_compiler_ss13.fuc.ast.BreakNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.PrintNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;
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

					return joinBlocks(left, right, reportLog);
				}
			};

		case "block -> { decls stmts }":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Object left = objs[1]; // Should be NO_VALUE or BlockNode
					Object right = objs[2]; // Should be NO_VALUE or BlockNode
					
					return joinBlocks(left, right, reportLog);
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
					//TODO: is false to be set??
					
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
			// TODO m2
			break;

		case "loc -> id":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					BasicIdentifierNode identifierNode = new BasicIdentifierNodeImpl();
					Token token = (Token) objs[0];
					if (token.getTokenType() == TokenType.ID) {
						identifierNode.setIdentifier(token.getValue());
					} else {
						log.error("Wrong TokenType in ReduceAction \"loc -> id\"");
						return null;
					}
					return identifierNode;
				}
			};
		case "loc -> loc.id":
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
			return null;
		case "bool -> bool || join":
		case "bool -> join":
		case "join -> join && equality":
			// TODO M2
			break;

		case "join -> equality":
			break; // Nothing to do here

		case "equality -> equality == rel":
		case "equality -> equality != rel":
			// TODO M2
			break;
		case "equality -> rel":
			return null; // Nothing to do here
		case "rel -> expr < expr":
		case "rel -> expr > expr":
		case "rel -> expr >= expr":
		case "rel -> expr <= expr":
			// TODO M2
			break;
		case "rel -> expr":
			break; // Nothing to do here
		case "expr -> expr + term":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					ArithmeticBinaryExpressionNode binop = binop(objs[0], objs[1], objs[2], BinaryOperator.ADDITION);
					
					return binop;
				}
			};
		case "expr -> expr - term":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return binop(objs[0], objs[1], objs[2], BinaryOperator.SUBSTRACTION);
				}
			};
		case "expr -> term":
			break; // Nothing to do here
		case "term -> term * unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return binop(objs[0], objs[1], objs[2],
							BinaryOperator.MULTIPLICATION);
				}
			};
		case "term -> term / unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					return binop(objs[0], objs[1], objs[2], BinaryOperator.DIVISION);
				}
			};
		case "term -> unary":
			break; // Nothing to do here
		case "unary -> ! unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					// Token token = (Token) objs[0]; // not
					UnaryExpressionNode unary = (UnaryExpressionNode) objs[1];

					LogicUnaryExpressionNode logicalUnary = new LogicUnaryExpressionNodeImpl();
					logicalUnary.setOperator(UnaryOperator.LOGICAL_NEGATE);
					logicalUnary.setRightValue(unary);
					unary.setParentNode(logicalUnary);

					return logicalUnary;
				}
			};
		case "unary -> - unary":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					Token token = (Token) objs[0]; // minus
					UnaryExpressionNode unary = (UnaryExpressionNode) objs[1];

					ArithmeticUnaryExpressionNodeImpl arithUnary = new ArithmeticUnaryExpressionNodeImpl();
					arithUnary.setOperator(UnaryOperator.MINUS);
					arithUnary.setRightValue(unary);
					unary.setParentNode(arithUnary);
					
					arithUnary.setCoverage(token);
					arithUnary.setCoverage(unary.coverage());
					
					return arithUnary;
				}
			};
		case "factor -> ( assign )":
			return new ReduceAction() {
				@Override
				public Object create(Object... objs) throws ParserException  {
					// Drop left parathesis
					ExpressionNode assign = (ExpressionNode) objs[1];
					// Drop right parathesis
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

	private static Object joinBlocks(Object left, Object right, ReportLog reportLog) throws ParserException{
		BlockNode newBlock = new BlockNodeImpl();
		newBlock.setSymbolTable(new SymbolTableImpl());
		
		BlockNodeImpl newBlockImpl = (BlockNodeImpl)newBlock;
		// Handle left
		if (!left.equals(NO_VALUE)) {
			BlockNode declsBlock = (BlockNode) left;
			for (DeclarationNode decl : declsBlock.getDeclarationList()) {
				insertDecl(newBlock, decl, reportLog);
				decl.setParentNode(newBlock);
				newBlockImpl.setCoverage(decl.coverage());
			}
		}

		// Handle right
		if (!right.equals(NO_VALUE)) {
			BlockNode stmtsBlock = (BlockNode) right;
			for (StatementNode stmt : stmtsBlock.getStatementList()) {
				newBlock.addStatement(stmt);
				stmt.setParentNode(newBlock);
				newBlockImpl.setCoverage(stmt.coverage());
			}
		}
		
		
		
		
		
		return newBlock;
	}

	/**
	 * Creates a binary operation as ArithmeticBinaryExpressionNode.
	 * Add the coverage token to the node.
	 * @param leftExpr
	 * @param opSign
	 * @param rightExpr
	 * @param op
	 * @return
	 */
	private static ArithmeticBinaryExpressionNode binop(Object leftExpr, Object opSign,
			Object rightExpr, final BinaryOperator op) {
		ExpressionNode left = (ExpressionNode) leftExpr;
		ExpressionNode right = (ExpressionNode) rightExpr;

		ArithmeticBinaryExpressionNode binop = new ArithmeticBinaryExpressionNodeImpl();
		binop.setLeftValue(left);
		binop.setRightValue(right);
		binop.setOperator(op);
		left.setParentNode(binop);
		right.setParentNode(binop);

		//set coverage
		ArithmeticBinaryExpressionNodeImpl binopImpl = ((ArithmeticBinaryExpressionNodeImpl)binop);
		binopImpl.setCoverage(left.coverage());
		binopImpl.setCoverage((Token)opSign);
		binopImpl.setCoverage(right.coverage());
		
		return binop;
	}
	
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
