/**
 * 
 */
package swp_compiler_ss13.fuc.parser.test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.fuc.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.fuc.parser.parseTableGenerator.ItemSet;
import swp_compiler_ss13.fuc.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.fuc.parser.parseTableGenerator.Variable;
import swp_compiler_ss13.fuc.fuc.parser.table.ActionEntry;
import swp_compiler_ss13.fuc.fuc.parser.table.GotoEntry;
import swp_compiler_ss13.fuc.fuc.parser.table.ParseTable;
import swp_compiler_ss13.fuc.fuc.parser.table.actions.Accept;
import swp_compiler_ss13.fuc.fuc.parser.table.actions.Reduce;
import swp_compiler_ss13.fuc.fuc.parser.table.actions.Shift;

/**
 * @author kensan
 *
 */
@Ignore
public class ParserImplTest {

	
	/**
	 * @throws java.lang.Exception
	 */
	Parser parser = new ParserImpl(){

		@Override
		public AST getParsedAST() {
			this.table = new MyParsetable();
			return parse();
		}
		
	};
	
	List<Token> lexer = new LinkedList<Token>();
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		parser.setLexer(new MyLexer());
		parser.setReportLog(new ReportLog() {
			
			@Override
			public void reportError(String text, Integer line, Integer column,
					String message) {
				System.err.println(text + " " + line +":"+ column);
				
			}
		});
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}


	/**
	 * Test method for {@link swp_compiler_ss13.fuc.parser.ParserImpl#getParsedAST()}.
	 */
	@Test
	public void testGetParsedAST() {
		AST ast = parser.getParsedAST();
		
		Assert.assertNotNull(ast);
	}
	
	
	private class MyParsetable implements ParseTable{

		ArrayList<Map<Terminal,ActionEntry>> actionTable = new ArrayList<Map<Terminal,ActionEntry>>();
		ArrayList<Map<Variable,GotoEntry>> gotoTable = new ArrayList<Map<Variable,GotoEntry>>();
 		
		
		public MyParsetable() {
			
			Map<Terminal,ActionEntry> actionEntry = new HashMap<Terminal,ActionEntry>();
			
			actionEntry.put(new Terminal("num"), new Shift(1));
			actionEntry.put(new Terminal("real"),new Shift(1));
			actionEntry.put(new Terminal("eof"), new Accept());
			actionTable.add(0, actionEntry);
			
			actionEntry = new HashMap<Terminal,ActionEntry>();
			List<Symbol> list = new LinkedList<Symbol>();
			list.add(new Terminal("longsymbol"));
			actionEntry.put(new Terminal("id"), new Reduce(new Production(new Variable("type"),list)));
			list = new LinkedList<Symbol>();
			list.add(new Variable("type"));
			list.add(new Terminal("id"));
			list.add(new Terminal(";"));
			actionEntry.put(new Terminal("eof"), new Reduce(new Production(new Variable("decls"), list )));
			
			actionTable.add(1, actionEntry);
			
			actionEntry = new HashMap<Terminal,ActionEntry>();
			actionEntry.put(new Terminal("id"), new Shift(3));
			actionTable.add(2,actionEntry);
			
			actionEntry = new HashMap<Terminal,ActionEntry>();
			actionEntry.put(new Terminal(";"), new Shift(1));
			actionTable.add(3,actionEntry);
			
		}
		

		@Override
		public ActionEntry getActionEntry(int state, Terminal symbol) {
			return actionTable.get(state).get(symbol);
		}

		@Override
		public GotoEntry getGotoEntry(int state, Variable symbol) {
			// TODO Auto-generated method stub
			return gotoTable.get(state).get(symbol.getString());
		}


		@Override
		public void setActionEntry(int state, Terminal terminal,
				ActionEntry action) throws DoubleEntryException {
			// TODO Auto-generated method stub
			
		}


		@Override
		public void setGotoEntry(int state, Variable variable, GotoEntry action)
				throws DoubleEntryException {
			// TODO Auto-generated method stub
			
		}


		@Override
		public Map<Integer, ItemSet> getStateToItemSet() {
			// TODO Auto-generated method stub
			return null;
		}


	}
	
	private class MyLexer implements Lexer{

		Queue<Token> list = new LinkedList<Token>();

		public MyLexer() {
			
			list.offer(new Token() {

				@Override
				public String getValue() {
					// TODO Auto-generated method stub
					return "long";
				}

				@Override
				public TokenType getTokenType() {
					// TODO Auto-generated method stub
					return TokenType.NUM;
				}

				@Override
				public Integer getLine() {
					// TODO Auto-generated method stub
					return 1;
				}

				@Override
				public Integer getColumn() {
					// TODO Auto-generated method stub
					return 1;
				}
			});

			list.offer(new Token() {

				@Override
				public String getValue() {
					// TODO Auto-generated method stub
					return "a";
				}

				@Override
				public TokenType getTokenType() {
					// TODO Auto-generated method stub
					return TokenType.ID;
				}

				@Override
				public Integer getLine() {
					// TODO Auto-generated method stub
					return 1;
				}

				@Override
				public Integer getColumn() {
					// TODO Auto-generated method stub
					return 6;
				}
			});

			list.offer(new Token() {

				@Override
				public String getValue() {
					// TODO Auto-generated method stub
					return ";";
				}

				@Override
				public TokenType getTokenType() {
					// TODO Auto-generated method stub
					return TokenType.SEMICOLON;
				}

				@Override
				public Integer getLine() {
					// TODO Auto-generated method stub
					return 1;
				}

				@Override
				public Integer getColumn() {
					// TODO Auto-generated method stub
					return 7;
				}
			});
			
			list.offer(new Token() {

				@Override
				public String getValue() {
					// TODO Auto-generated method stub
					return "";
				}

				@Override
				public TokenType getTokenType() {
					// TODO Auto-generated method stub
					return TokenType.EOF;
				}

				@Override
				public Integer getLine() {
					// TODO Auto-generated method stub
					return 1;
				}

				@Override
				public Integer getColumn() {
					// TODO Auto-generated method stub
					return 8;
				}
			});
			


		}
		
		@Override
		public void setSourceStream(InputStream stream) {
			// don't needed here
			
		}

		@Override
		public Token getNextToken() {
			// TODO Auto-generated method stub
			return list.poll();
		}
		
	}
}
