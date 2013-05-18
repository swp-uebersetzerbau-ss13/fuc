package swp_compiler_ss13.fuc.parser.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.ExpressionNode;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.binary.ArithmeticBinaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.binary.BinaryExpressionNode.BinaryOperator;
import swp_compiler_ss13.common.ast.nodes.leaf.BasicIdentifierNode;
import swp_compiler_ss13.common.ast.nodes.leaf.LiteralNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.ast.nodes.unary.ArithmeticUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.ast.nodes.unary.LogicUnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.ReturnNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode;
import swp_compiler_ss13.common.ast.nodes.unary.UnaryExpressionNode.UnaryOperator;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.common.types.primitive.BooleanType;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticBinaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.ArithmeticUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.BasicIdentifierNodeImpl;
import swp_compiler_ss13.fuc.ast.BlockNodeImpl;
import swp_compiler_ss13.fuc.ast.BreakNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.ast.LiteralNodeImpl;
import swp_compiler_ss13.fuc.ast.LogicUnaryExpressionNodeImpl;
import swp_compiler_ss13.fuc.ast.ReturnNodeImpl;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.TokenEx;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Error;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;


public class LRParser {
   // --------------------------------------------------------------------------
   // --- variables and constants ----------------------------------------------
   // --------------------------------------------------------------------------
   private static final Object NO_VALUE = new Object();
   
   private final Logger log = Logger.getLogger(getClass());
   
   // --------------------------------------------------------------------------
   // --- constructors ---------------------------------------------------------
   // --------------------------------------------------------------------------
   public LRParser() {
      
   }
   
   // --------------------------------------------------------------------------
   // --- methods --------------------------------------------------------------
   // --------------------------------------------------------------------------
   public AST parse(LexerWrapper lexer, ReportLog reportLog, LRParsingTable table) {
      Stack<LRParserState> parserStack = new Stack<>();
      
      AST ast = new ASTImpl();
      Stack<Object> valueStack = new Stack<>();
      
      // Initialization
      ALRAction action = null;
      TokenEx token = lexer.getNextToken();
      parserStack.add(table.getStartState());
      
      while (true) {
         LRParserState s = parserStack.peek();
         
         TokenType tokenType = token.getTokenType();
         if (tokenType == TokenType.NOT_A_TOKEN) {
            reportLog.reportError(token.getValue(), token.getLine(), token.getColumn(), "Undefined Symbol found!");
            return null;
         }
         
         action = table.getActionTable().get(s, token.getTerminal());
         if (action == null) {
            log.error("Error in Parsetable occured!");
            return null;
         }
         
         
         switch (action.getType()) {
            case SHIFT: {
               // Shift state
               Shift shift = (Shift) action;
               parserStack.push(shift.getNewState());
               
               // Push old token on stack
               valueStack.push(token);
               token = lexer.getNextToken();
               break;
            }
            
            case REDUCE: {
               // pop states from stack
               Reduce reduce = (Reduce) action;
               for (int i = 1; i <= reduce.getPopCount(); i++) {
                  parserStack.pop();
               }
               
               // +++++++++++++++++++++++++++++++++++
               // get action for reduced production
               Production prod = reduce.getProduction();
               ReduceAction reduceAction = getReduceAction(prod);
               
               // check where to go-to... and push next state on stack
               LRParserState newState = table.getGotoTable().get(s, prod.getLHS());
               parserStack.push(newState);
               
               
               // If there is anything to do on the value stack
               // (There might be no reduce-action for Productions like unary -> factor, e.g.)
               if (reduceAction != null) {
                  // Pop all values reduced by this production
                  int nrOfValuesReduced = prod.getRHSSizeWoEpsilon();
                  LinkedList<Object> valueHandle = new LinkedList<>();
                  for (int i = 0; i < nrOfValuesReduced; i++) {
                     valueHandle.addFirst(valueStack.pop());
                  }
                  
                  // Execute reduceAction and push onto the stack
                  Object newValue = reduceAction.create(arr(valueHandle));
                  if (newValue == null) {
                     log.error("Error occurred! newValue is null");
                     return null;
                  }
                  valueStack.push(newValue);
               }
            }
            
            case ACCEPT: {
               if (tokenType != TokenType.EOF) {
                  reportLog.reportError(token.getValue(), token.getLine(), token.getColumn(), "");
               } else {
                  BlockNode programBlock = (BlockNode) valueStack.pop();
                  ast.setRootNode(programBlock);
                  programBlock.setParentNode(ast.getRootNode());
                  return ast;
               }
            }
            
            case ERROR: {
               Error error = (Error) action;
               reportLog.reportError(token.getValue(), token.getLine(), token.getColumn(), "An error occurred: "
                     + error.getMsg());
               return ast; // TODO Correct?
            }
         }
      }
   }
   
   private static Object[] arr(List<Object> objs) {
      return objs.toArray(new Object[objs.size()]);
   }
   
   private ReduceAction getReduceAction(Production prod) {
      switch (prod.getStringRep()) {
      
         case "program -> decls stmts":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  Object left = objs[0]; // Should be NO_VALUE or BlockNode
                  Object right = objs[1]; // Should be NO_VALUE or BlockNode
                  
                  return joinBlocks(left, right);
               }
            };
            
         case "block -> { decls stmts }":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  Object left = objs[1]; // Should be NO_VALUE or BlockNode
                  Object right = objs[2]; // Should be NO_VALUE or BlockNode
                  
                  return joinBlocks(left, right);
               }
            };
            
         case "decls -> decls decl":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  Object left = objs[0]; // Should be NO_VALUE or DeclarationNode
                  Object right = objs[1]; // Should be DeclarationNode or BlockNode
                  
                  LinkedList<DeclarationNode> declList = new LinkedList<>();
                  // Handle left
                  if (!left.equals(NO_VALUE)) {
                     // We have exactlly ONE decl
                     if (!(left instanceof DeclarationNode)) {
                        log.error("Error in decls -> decls decl: Left must be a DeclarationNode!");
                     } else {
                        declList.add((DeclarationNode) left);
                     }
                  }
                  
                  // Handle right
                  if (right instanceof BlockNode) {
                     BlockNode oldBlock = (BlockNode) right;
                     declList.addAll(oldBlock.getDeclarationList());
                  } else {
                     if (!(right instanceof DeclarationNode)) {
                        log.error("Error in decls -> decls decl: Right must be a DeclarationNode!");
                     } else {
                        declList.add((DeclarationNode) right);
                     }
                  }
                  
                  // Create new BlockNode
                  BlockNode block = new BlockNodeImpl();
                  for (DeclarationNode decl : declList) {
                     block.addDeclaration(decl);
                     decl.setParentNode(block);
                  }
                  return block;
               }
            };
            
         case "decls -> ε":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  return NO_VALUE; // Symbolizes epsilon
               }
            };
            
         case "decl -> type id ;":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  Token typeToken = (Token) objs[0];
                  Token idToken = (Token) objs[1];
                  
                  DeclarationNode decl = new DeclarationNodeImpl();
                  // Set type
                  switch (typeToken.getTokenType()) {
                     case REAL:
                        decl.setType(new DoubleType());
                        break;
                     case NUM:
                        decl.setType(new LongType());
                        break;
                     case STRING:
                        decl.setType(new StringType((long) typeToken.getValue().length()));
                        break;
                     default:
                        break;
                  }
                  
                  // Set ID
                  decl.setIdentifier(idToken.getValue());
                  return decl;
               }
            };
            
         case "stmts -> stmts stmt":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  Object left = objs[0]; // Should be NO_VALUE or StatementNode
                  Object right = objs[1]; // Should be StatementNode or BlockNode
                  
                  LinkedList<StatementNode> stmtList = new LinkedList<>();
                  // Handle left
                  if (!left.equals(NO_VALUE)) {
                     // there are just 0ne statement
                     if (!(left instanceof StatementNode)) {
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
               public Object create(Object... objs) {
                  return NO_VALUE; // Symbolizes epsilon
               }
            };
            
         case "stmt -> assign":
            break; // Nothing to reduce here
         case "stmt -> block":
            break; // Nothing to reduce here. Block is a Stmt
            
         case "stmt -> if ( assign ) stmt":
         case "stmt -> if ( assign ) stmt else stmt":
         case "stmt -> while ( assign ) stmt":
         case "stmt -> do stmt while ( assign )":
            // TODO M2
            break;
         
         case "stmt -> break ;":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  return new BreakNodeImpl();
               }
               
            };
         case "stmt -> return ;":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  return new ReturnNodeImpl();
               }
            };
         case "stmt -> return loc ;":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  ReturnNode returnNode = new ReturnNodeImpl();
                  IdentifierNode identifier = (IdentifierNode) objs[1];
                  returnNode.setRightValue(identifier);
                  identifier.setParentNode(returnNode);
                  return returnNode;
               }
            };
         case "stmt -> print loc ;":
         case "loc -> loc [ assign ]":
            // TODO m2
            break;
         
         case "loc -> id":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
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
               public Object create(Object... objs) {
                  AssignmentNodeImpl assignNode = new AssignmentNodeImpl();
                  assignNode.setLeftValue((IdentifierNode) objs[0]);
                  assignNode.getLeftValue().setParentNode(assignNode);
                  assignNode.setRightValue((StatementNode) objs[2]); // [1] is the "=" token
                  assignNode.getRightValue().setParentNode(assignNode);
                  return assignNode;
               }
            };
         case "assign -> bool":
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
               public Object create(Object... objs) {
                  return binop(objs[0], objs[2], BinaryOperator.ADDITION);
               }
            };
         case "expr -> expr - term":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  return binop(objs[0], objs[2], BinaryOperator.SUBSTRACTION);
               }
            };
         case "expr -> term":
            break; // Nothing to do here
         case "term -> term * unary":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  return binop(objs[0], objs[2], BinaryOperator.MULTIPLICATION);
               }
            };
         case "term -> term / unary":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  return binop(objs[0], objs[2], BinaryOperator.DIVISION);
               }
            };
         case "term -> unary":
            break; // Nothing to do here
         case "unary -> ! unary":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
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
               public Object create(Object... objs) {
                  // Token token = (Token) objs[0]; // minus
                  UnaryExpressionNode unary = (UnaryExpressionNode) objs[1];
                  
                  ArithmeticUnaryExpressionNode arithUnary = new ArithmeticUnaryExpressionNodeImpl();
                  arithUnary.setOperator(UnaryOperator.MINUS);
                  arithUnary.setRightValue(unary);
                  unary.setParentNode(arithUnary);
                  return arithUnary;
               }
            };
         case "unary -> factor":
         case "factor -> ( assign )":
         case "factor -> loc":
            break;
         case "factor -> num":
            return new ReduceAction() {
               
               @Override
               public Object create(Object... objs) {
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
               public Object create(Object... objs) {
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
               public Object create(Object... objs) {
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
               public Object create(Object... objs) {
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
               public Object create(Object... objs) {
                  LiteralNode literal = new LiteralNodeImpl();
                  Token token = (Token) objs[0];
                  literal.setLiteral(token.getValue());
                  literal.setLiteralType(new StringType((long) token.getValue().length()));
                  return literal;
               }
               
            };
         case "type -> type [ num ]":
         case "type -> bool":
         case "type -> string":
         case "type -> num":
         case "type -> real":
         case "type -> record { decls }":
         default:
            return null; // Means "no ReduceAction to perform"
      }
      return null;
   }
   
   
   private static Object joinBlocks(Object left, Object right) {
      BlockNode newBlock = new BlockNodeImpl();
      // Handle left
      if (!left.equals(NO_VALUE)) {
         BlockNode declsBlock = (BlockNode) left;
         for (DeclarationNode decl : declsBlock.getDeclarationList()) {
            newBlock.addDeclaration(decl);
            decl.setParentNode(newBlock);
         }
      }
      
      // Handle right
      if (!right.equals(NO_VALUE)) {
         BlockNode stmtsBlock = (BlockNode) right;
         for (StatementNode decl : stmtsBlock.getStatementList()) {
            newBlock.addStatement(decl);
            decl.setParentNode(newBlock);
         }
      }
      return newBlock;
   }
   
   
   private static ArithmeticBinaryExpressionNode binop(Object leftExpr, Object rightExpr, final BinaryOperator op) {
      ExpressionNode left = (ExpressionNode) leftExpr;
      ExpressionNode right = (ExpressionNode) rightExpr;
      
      ArithmeticBinaryExpressionNode binop = new ArithmeticBinaryExpressionNodeImpl();
      binop.setLeftValue(left);
      binop.setRightValue(right);
      binop.setOperator(op);
      left.setParentNode(binop);
      right.setParentNode(binop);
      
      return binop;
   }
   
   
   private interface ReduceAction {
      Object create(Object... objs);
   }
}
