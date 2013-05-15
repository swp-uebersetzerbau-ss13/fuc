/**
 * 
 */
package swp_compiler_ss13.fuc.parser.test;

import static org.junit.Assert.fail;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.fuc.parser.ParserImpl;
import swp_compiler_ss13.fuc.parser.table.ActionEntry;
import swp_compiler_ss13.fuc.parser.table.ParseTable;

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
			this.table = new ParseTable() {

				@Override
				public ActionEntry getEntry(int state, Token symbol)
						throws StateOutOfBoundsException, TokenNotFoundException {
					return null;
				}
				
			};
			return null;
		}
		
	};
	
	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		
	}

	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
	}

	/**
	 * Test method for {@link swp_compiler_ss13.fuc.parser.ParserImpl#setLexer(swp_compiler_ss13.common.lexer.Lexer)}.
	 */
	@Test
	public void testSetLexer() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link swp_compiler_ss13.fuc.parser.ParserImpl#setReportLog(swp_compiler_ss13.common.parser.ReportLog)}.
	 */
	@Test
	public void testSetReportLog() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link swp_compiler_ss13.fuc.parser.ParserImpl#getParsedAST()}.
	 */
	@Test
	public void testGetParsedAST() {
		fail("Not yet implemented");
	}

}
