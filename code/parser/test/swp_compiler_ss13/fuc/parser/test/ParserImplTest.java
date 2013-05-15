/**
 * 
 */
package swp_compiler_ss13.fuc.parser.test;

import static org.junit.Assert.fail;

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
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Accept;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Production;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Reduce;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Shift;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Variable;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.interfaces.ParseTable;

/**
 * @author kensan
 *
 */
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

		ArrayList<Map<TokenType,ParseTableEntry>> table = new ArrayList<Map<TokenType,ParseTableEntry>>();
		
		
		public MyParsetable() {
			
			Map<TokenType,ParseTableEntry> entry = new HashMap<TokenType,ParseTableEntry>();
			
			entry.put(TokenType.NUM, new Shift(1));
			entry.put(TokenType.REAL,new Shift(1));
			entry.put(TokenType.EOF, new Accept());
			table.add(0, entry);
			
			entry = new HashMap<TokenType,ParseTableEntry>();
			List<Symbol> list = new LinkedList<Symbol>();
			list.add(new Terminal("num"));
			entry.put(TokenType.ID, new Reduce(1,2,new Production(new Variable("type"),list)));
			list = new LinkedList<Symbol>();
			list.add(new Variable("type"));
			list.add(new Terminal("id"));
			list.add(new Terminal(";"));
			entry.put(TokenType.EOF, new Reduce(3,0, new Production(new Variable("decls"), list )));
			
			table.add(1, entry);
			
			entry = new HashMap<TokenType,ParseTableEntry>();
			entry.put(TokenType.ID, new Shift(3));
			table.add(2,entry);
			
			entry = new HashMap<TokenType,ParseTableEntry>();
			entry.put(TokenType.SEMICOLON, new Shift(1));
			table.add(3,entry);
			
		}
		
		@Override
		public ParseTableEntry getEntry(int state, Token symbol){
			
			return table.get(state).get(symbol.getTokenType());
			
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
