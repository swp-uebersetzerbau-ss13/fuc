package swp_compiler_ss13.fuc.parser;

import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.assign;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.factor;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.id;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.loc;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.sem;
import static swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar.type;

import org.junit.Test;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.parser.Parser;

public class ParserImplTest {
	@Test
	public void testParsingComplete() {
	      Parser parser = new ParserImpl();
	      parser.setLexer(new TestLexer(type, id, sem, loc, assign, factor));
	      AST ast = parser.getParsedAST();
	}
}
