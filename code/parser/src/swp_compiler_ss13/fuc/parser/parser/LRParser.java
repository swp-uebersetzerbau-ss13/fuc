package swp_compiler_ss13.fuc.parser.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Stack;

import org.apache.log4j.Logger;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.ast.ASTNode;
import swp_compiler_ss13.common.ast.nodes.marynary.BlockNode;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.report.ReportType;
import swp_compiler_ss13.fuc.ast.ASTImpl;
import swp_compiler_ss13.fuc.parser.grammar.Production;
import swp_compiler_ss13.fuc.parser.grammar.Terminal;
import swp_compiler_ss13.fuc.parser.grammar.TokenEx;
import swp_compiler_ss13.fuc.parser.parser.ReduceAction.ReduceException;
import swp_compiler_ss13.fuc.parser.parser.states.LRParserState;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.ALRAction;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Error;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Reduce;
import swp_compiler_ss13.fuc.parser.parser.tables.actions.Shift;

public class LRParser {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	public static final Long STRING_LENGTH = 255L;

	private final Logger log = Logger.getLogger(LRParser.class);
	
	private ReduceImpl reduceImpl;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@SuppressWarnings("incomplete-switch")
	public AST parse(LexerWrapper lexer, ReportLog reportLog,
			LRParsingTable table) throws ParserException{
		reduceImpl = new ReduceImpl();
		reduceImpl.setReportLog(reportLog);
		
		Stack<LRParserState> parserStack = new Stack<>();

		AST ast = new ASTImpl();
		Stack<Object> valueStack = new Stack<>();

		// Initialization
		ALRAction action = null;
		TokenEx token = lexer.getNextToken();
		parserStack.add(table.getStartState());

		WHILE: while (true) {
			LRParserState state = parserStack.peek();
			log.debug("current token: " + token);

			TokenType tokenType = token.getTokenType();
			switch (tokenType) {
			case NOT_A_TOKEN:
				List<Token> list = new ArrayList<Token>();
				list.add(token);
				reportLog.reportError(ReportType.UNRECOGNIZED_TOKEN, list,
						"Found undefined token '" + token.getValue() + "'!");
				throw new ParserException("Found undefined token");

			case COMMENT:
				// Skip it silently
				token = lexer.getNextToken();
				continue WHILE;
			}

			Terminal terminal = token.getTerminal();
			if (terminal == null)
				throw new ParserException("No Terminal associated with token: " + token);
			action = table.getActionTable().get(state, terminal);
			if (action == null) {
				log.error("Error in Parsetable occured!");
				throw new ParserException("An Error in Parsetable occured");
			}

			switch (action.getType()) {
			case SHIFT: {
				// Shift state
				Shift shift = (Shift) action;
				log.debug(shift.toString());
				parserStack.push(shift.getNewState());

				// Push old token on stack
				valueStack.push(token);
				token = lexer.getNextToken();
			}
			break;

			case REDUCE: {
				// Print current value stack
				log.debug(printStack(valueStack));
				
				// pop reduced states from stack
				Reduce reduce = (Reduce) action;
				for (int i = 1; i <= reduce.getPopCount(); i++) {
					parserStack.pop();
				}

				// +++++++++++++++++++++++++++++++++++
				// get action for reduced production
				Production prod = reduce.getProduction();
				log.debug(reduce.toString());
				
				ReduceAction reduceAction = reduceImpl.getReduceAction(prod);

				// If there is anything to do on the value stack
				// (There might be no reduce-action for Productions like unary
				// -> factor, e.g.)
				if (reduceAction != null) {
					// Pop all values reduced by this production
					int nrOfValuesReduced = reduce.getPopCount();
					LinkedList<Object> valueHandle = new LinkedList<>();
					for (int i = 0; i < nrOfValuesReduced; i++) {
						valueHandle.addFirst(valueStack.pop());
					}
					
					// (Safely) execute reduceAction and push onto the stack
					Object newValue = null;
					try {
						newValue = reduceAction.create(arr(valueHandle));
					} catch (ReduceException err) {
						writeReportError(reportLog, reduce, err);
						throw new ParserException("An error occured during " + reduce + ": ", err);
					} catch (ParserException err) {
						throw err;	// Re-Throw
					} catch (Exception err) {
						throw new ParserException("An error occured during " + reduce + ": ", err);
					}
										
					if (newValue == null) {
						log.error("Error occurred! newValue is null");
						throw new ParserException("Error occurred! newValue is null");
					}
					valueStack.push(newValue);
				}

				// check where to go-to... and push next state on stack
				LRParserState newState = table.getGotoTable().get(parserStack.peek(),
						prod.getLHS());
				if (newState.isErrorState()) {
					List<Token> list = new ArrayList<Token>();
					list.add(token);
					reportLog.reportError(ReportType.UNDEFINED, list, "");
					throw new ParserException("Error state occurred");
				}
				parserStack.push(newState);
			}
			break;

			case ACCEPT: {
				if (tokenType != TokenType.EOF) {
					List<Token> list = new ArrayList<Token>();
					list.add(token);
					reportLog.reportError(ReportType.UNRECOGNIZED_TOKEN, list,"");
					throw new ParserException("End of File expected!");
				} else {
					BlockNode programBlock = (BlockNode) valueStack.pop();
					ast.setRootNode(programBlock);
					return ast;
				}
			}

			case ERROR: {
				// TODO Inser error recovery here
				Error error = (Error) action;
				List<Token> list = new ArrayList<Token>();
				list.add(token);
				reportLog.reportError(ReportType.UNDEFINED,list,
						"An error occurred: " + error.getMsg());
						throw new ParserException("Got Error State from Actiontable");
			}
			}
		}
	}
	

	
	/**
	 * Gets ReportLog, the Object, thats made some trouble and the message whats expected instead
	 * 
	 * @param reportLog
	 * @param reduce
	 * @param err
	 */
	private void writeReportError(ReportLog reportLog, Reduce reduce, ReduceException err) {
		Object obj = err.getObj();
		Class<?> clazz = err.getClazz();
		String objStr = obj == null ? "<null>" : obj.getClass().getSimpleName();
		
		List<Token> tokens = null;
		if (obj instanceof ASTNode){
			ASTNode node = (ASTNode) obj;
			tokens = node.coverage();
		} else if (obj instanceof Token){
			tokens = Arrays.asList((Token) obj);
		}
		
		reportLog.reportError(ReportType.UNDEFINED, tokens, "Expected an instance of " +
				clazz.getSimpleName() + " during " + reduce + ", but got " + objStr + " instead!");
	}

	private static Object[] arr(List<Object> objs) {
		return objs.toArray(new Object[objs.size()]);
	}

	private static String printStack(Stack<Object> stack) {
		StringBuilder b = new StringBuilder("Stack:");
		for (Object obj : stack) {
			b.append(" ").append(obj.toString());
		}
		return b.toString();
	}
}
