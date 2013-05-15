package swp_compiler_ss13.fuc.parser;

import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.nodes.IdentifierNode;
import swp_compiler_ss13.common.ast.nodes.StatementNode;
import swp_compiler_ss13.common.ast.nodes.unary.DeclarationNode;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.common.types.primitive.DoubleType;
import swp_compiler_ss13.common.types.primitive.LongType;
import swp_compiler_ss13.common.types.primitive.StringType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.ast.AssignmentNodeImpl;
import swp_compiler_ss13.fuc.ast.DeclarationNodeImpl;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry.ParseTableEntryType;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableGeneratorImpl;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Reduce;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable.StateOutOfBoundsException;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable.TokenNotFoundException;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTableGenerator;


public class ParserImpl implements Parser {
   
   private static final Object NO_VALUE = new Object();
   
   private Lexer lexer;
   private Stack<Integer> parserStack = new Stack<Integer>();
   private ParseTable table;
   private ReportLog reportLog;
   private AST ast = new ASTImpl();
   // private Stack<Token> tokenStack;
   // private List<DeclarationNode> decls = new LinkedList<DeclarationNode>();
   // private List<StatementNode> stmts = new LinkedList<StatementNode>();
   // private Stack<StatementNode> stmtStack = new Stack<StatementNode>();
   private Stack<Object> valueStack = new Stack<>();
   
   @Override
   public void setLexer(Lexer lexer) {
      this.lexer = lexer;
   }
   
   @Override
   public void setReportLog(ReportLog reportLog) {
      this.reportLog = reportLog;
   }
   
   @Override
   public AST getParsedAST() {
      // TODO
      ParseTableGenerator generator = new ParseTableGeneratorImpl();
      table = generator.getTable();
      return parse();
   }
   
   
   private AST parse() {
      
      // add initial state
      parserStack.add(0);
      
      int s = 0;
      TokenType tokenType = null;
      ParseTableEntryType entryType;
      Token token = lexer.getNextToken();
      ParseTableEntry entry = null;
      
      while (true) {
         s = parserStack.peek();
         
         tokenType = token.getTokenType();
         
         if (tokenType == TokenType.NOT_A_TOKEN) {
            // TODO Errorhandling
         }
         
         try {
            entry = table.getEntry(s, token);
         } catch (StateOutOfBoundsException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         } catch (TokenNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
         }
         
         entryType = entry.getType();
         
         
         switch (entryType) {
            case SHIFT: {
               parserStack.push(entry.getNewState());
               Token shiftedToken = token;
               token = lexer.getNextToken();
               
               // Push value corresponding to the token here.
               // Need relation Token -> Value here! If !Token.hasValue(), put NoValue on stack.
               Object value = getSymbolShiftValue(shiftedToken);
               valueStack.push(value);
               
               break;
            }
            
            case REDUCE: {
               
               // pop states from stack
               Reduce reduce = (Reduce) entry;
               for (int i = 1; i <= reduce.getCount(); i++) {
                  parserStack.pop();
               }
               // push next state on stack
               parserStack.push(reduce.getNewState());
               
               // reduce(reduce);
               
               // +++++++++++++++++++++++++++++++++++
               // get action for reduced production
               Production prod = reduce.getProduction();
               ReduceAction reduceAction = getReduceAction(prod);
               
               // If there is anything to do on the value stack
               // (There might be no reduce-action for Productions like unary -> factor, e.g.)
               if (reduceAction != null) {
                  // Pop all values reduced by this production
                  int nrOfValuesReduced = prod.getNrOFSymbolsWOEpsilon();
                  LinkedList<Object> valueHandle = new LinkedList<>();
                  for (int i = 0; i < nrOfValuesReduced; i++) {
                     valueHandle.addFirst(valueStack.pop());
                  }
                  
                  // Execute reduceAction and push onto the stack
                  Object newValue = reduceAction.create(arr(valueHandle));
                  valueStack.push(newValue);
                  // TODO Anything to do here for shortcuts?
               }
            }
            
            case ACCEPT: {
               if (tokenType != TokenType.EOF) {
                  // TODO Errorhandling
               } else {
                  return ast;
               }
            }
            
            case ERROR: {
               // TODO Errorhandling
            }
         }
      }
      
   }
   
   private static Object[] arr(List<Object> objs) {
      return objs.toArray(new Object[objs.size()]);
   }
   
   // private void reduce(Reduce reduce) {
   // Production prod = reduce.getProduction();
   //
   // switch (prod.getString()) {
   //
   // case "program -> decls stmts":
   // case "decls -> decls decl":
   // case "decl -> type id ;":
   // createDeclarationNode();
   // break;
   // case "stmts -> stmts stmt":
   // case "stmt -> assign":
   // case "stmt -> block":
   // case "stmt -> if ( assign ) stmt":
   // case "stmt -> if ( assign ) stmt else stmt":
   // case "stmt -> while ( assign ) stmt":
   // case "stmt -> do stmt while ( assign )":
   // case "stmt -> break ;":
   // case "stmt -> return ;":
   // case "stmt -> return loc ;":
   // case "stmt -> print loc ;":
   // case "loc -> loc [ assign ]":
   // case "loc -> id":
   // case "loc -> loc.id":
   // case "assign -> loc = assign":
   // case "assign -> bool":
   // case "bool -> bool || join":
   // case "bool -> join":
   // case "join -> join && equality":
   // case "join -> equality":
   // case "equality -> equality == rel":
   // case "equality -> equality != rel":
   // case "equality -> rel":
   // case "rel -> expr < expr":
   // case "rel -> expr > expr":
   // case "rel -> expr >= expr":
   // case "rel -> expr <= expr":
   // case "rel -> expr":
   // case "expr -> expr + term":
   // case "expr -> expr - term":
   // case "expr -> term":
   // case "term -> term * unary":
   // case "term -> term / unary":
   // case "term -> unary":
   // case "unary -> ! unary":
   // case "unary -> - unary":
   // case "unary -> factor":
   // case "factor -> ( assign )":
   // case "factor -> loc":
   // case "factor -> num":
   // case "factor -> real":
   // case "factor -> true":
   // case "factor -> false":
   // case "factor -> string":
   // case "type -> type [ num ]":
   // case "type -> bool":
   // case "type -> string":
   // case "type -> num":
   // case "type -> real":
   // case "type -> record { decls }":
   // }
   // }
   
   
   private Object getSymbolShiftValue(Token t) {
      switch (t.getTokenType()) {
         case TRUE:
            return null; // TODO WTF, which node to use for TRUE???
            
         case FALSE:
            return null; // TODO WTF, which node to use for TRUE???
            
         default:
            return NO_VALUE;
      }
   }
   
   private ReduceAction getReduceAction(Production prod) {
      switch (prod.getString()) {
      
         case "program -> decls stmts":
         case "decls -> decls decl":
         case "decl -> type id ;":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  DeclarationNode decl = new DeclarationNodeImpl();
                  Token typeToken = (Token) objs[0];
                  
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
                  }
                  
                  // Set ID
                  Token idToken = (Token) objs[1];
                  decl.setIdentifier(idToken.getValue());
                  return decl;
               }
            };
         case "stmts -> stmts stmt":
         case "stmt -> assign":
         case "stmt -> block":
         case "stmt -> if ( assign ) stmt":
         case "stmt -> if ( assign ) stmt else stmt":
         case "stmt -> while ( assign ) stmt":
         case "stmt -> do stmt while ( assign )":
         case "stmt -> break ;":
         case "stmt -> return ;":
         case "stmt -> return loc ;":
         case "stmt -> print loc ;":
         case "loc -> loc [ assign ]":
         case "loc -> id":
         case "loc -> loc.id":
         case "assign -> loc = assign":
            return new ReduceAction() {
               @Override
               public Object create(Object... objs) {
                  AssignmentNodeImpl assignNode = new AssignmentNodeImpl();
                  assignNode.setLeftValue((IdentifierNode) objs[0]);
                  assignNode.setRightValue((StatementNode) objs[1]);
                  return assignNode;
               }
            };
         case "assign -> bool":
         case "bool -> bool || join":
         case "bool -> join":
         case "join -> join && equality":
         case "join -> equality":
         case "equality -> equality == rel":
         case "equality -> equality != rel":
         case "equality -> rel":
         case "rel -> expr < expr":
         case "rel -> expr > expr":
         case "rel -> expr >= expr":
         case "rel -> expr <= expr":
         case "rel -> expr":
         case "expr -> expr + term":
         case "expr -> expr - term":
         case "expr -> term":
         case "term -> term * unary":
         case "term -> term / unary":
         case "term -> unary":
         case "unary -> ! unary":
         case "unary -> - unary":
         case "unary -> factor":
         case "factor -> ( assign )":
         case "factor -> loc":
         case "factor -> num":
         case "factor -> real":
         case "factor -> true":
         case "factor -> false":
         case "factor -> string":
         case "type -> type [ num ]":
         case "type -> bool":
         case "type -> string":
         case "type -> num":
         case "type -> real":
         case "type -> record { decls }":
         default:
            return null;
      }
   }
   
   // /**
   // *
   // */
   // private void createDeclarationNode() {
   // DeclarationNode decl = new DeclarationNodeImpl();
   // Token tmp = null;
   // while (!tokenStack.isEmpty()) {
   // tmp = tokenStack.pop();
   // switch (tmp.getTokenType()) {
   // case ID:
   // decl.setIdentifier(tmp.getValue());
   // break;
   // case REAL:
   // decl.setType(new DoubleType());
   // break;
   // case NUM:
   // decl.setType(new LongType());
   // break;
   // case STRING:
   // decl.setType(new StringType((long) tmp.getValue().length()));
   // break;
   // default:
   // break;
   // }
   // }
   // decls.add(decl);
   // }
   
   
   private interface ReduceAction {
      Object create(Object... objs);
   }
}
