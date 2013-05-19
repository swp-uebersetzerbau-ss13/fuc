package swp_compiler_ss13.fuc.parser;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.fuc.parser.generator.ALRGenerator;
import swp_compiler_ss13.fuc.parser.generator.LR0Generator;
import swp_compiler_ss13.fuc.parser.generator.items.LR0Item;
import swp_compiler_ss13.fuc.parser.generator.states.LR0State;
import swp_compiler_ss13.fuc.parser.grammar.Grammar;
import swp_compiler_ss13.fuc.parser.grammar.ProjectGrammar;
import swp_compiler_ss13.fuc.parser.parser.DoubleIdentifierException;
import swp_compiler_ss13.fuc.parser.parser.LRParser;
import swp_compiler_ss13.fuc.parser.parser.LexerWrapper;
import swp_compiler_ss13.fuc.parser.parser.ParserException;
import swp_compiler_ss13.fuc.parser.parser.tables.LRParsingTable;
import swp_compiler_ss13.fuc.semantic_analyser.SemanticAnalyser;

public class ParserImpl implements Parser {
	// --------------------------------------------------------------------------
	// --- variables and constants
	// ----------------------------------------------
	// --------------------------------------------------------------------------
	private Lexer lexer = null;
	private ReportLog reportLog = null;

	// --------------------------------------------------------------------------
	// --- constructors
	// ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ParserImpl() {

	}

	// --------------------------------------------------------------------------
	// --- methods
	// --------------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public AST getParsedAST() {
		// Generate parsing table
		Grammar grammar = new ProjectGrammar.M1().getGrammar();
		ALRGenerator<LR0Item, LR0State> generator = new LR0Generator(grammar);
		LRParsingTable table = generator.getParsingTable();

		// Run LR-parser with table
		LRParser lrParser = new LRParser();
		LexerWrapper lexWrapper = new LexerWrapper(this.lexer, grammar);
		AST ast = null;
		try{
			ast = lrParser.parse(lexWrapper, this.reportLog, table);
		}catch(DoubleIdentifierException e){
			return null;
		}catch(ParserException e){
			return null;
		}
		
			// Call semantic analysis
			// TODO Fix dependency cycle caused by ReportLogImpl + Error!!!
			SemanticAnalyser analyzer = new SemanticAnalyser(this.reportLog);
			ast = analyzer.analyse(ast);
		
		

		return ast;
	}

	// --------------------------------------------------------------------------
	// --- getter/setter
	// --------------------------------------------------------
	// --------------------------------------------------------------------------
	@Override
	public void setLexer(Lexer lexer) {
		this.lexer = lexer;
	}

	@Override
	public void setReportLog(ReportLog reportLog) {
		this.reportLog = reportLog;
	}
}
