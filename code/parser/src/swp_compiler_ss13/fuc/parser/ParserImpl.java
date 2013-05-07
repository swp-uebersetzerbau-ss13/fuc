package swp_compiler_ss13.fuc.parser;

import java.util.Stack;

import swp_compiler_ss13.common.ast.AST;
import swp_compiler_ss13.common.lexer.Lexer;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.lexer.TokenType;
import swp_compiler_ss13.common.parser.Parser;
import swp_compiler_ss13.common.parser.ReportLog;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTable;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableEntry.ParseTableEntryType;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.ParseTableGenerator;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Symbol;
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Terminal;

public class ParserImpl implements Parser {
	
	private Lexer lexer;
	private Stack<Integer> parserStack = new Stack<Integer>();
	private ParseTable table;
	private ReportLog reportLog;
	
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
		ParseTableGenerator generator = null;
		table = generator.getTable();
		return parse();
	}
	
	
	private AST parse(){ 
		
		//add initial state
		parserStack.add(0);
		
		int s = 0;
		Token token = lexer.getNextToken();
		Symbol symbol = null;
		ParseTableEntry entry = null;
		
		while(true){
			s = parserStack.peek();
			
			entry = table.getEntry(s, symbol);
			
			if(entry.getType() == ParseTableEntryType.SHIFT){
				
			}else{
				if(entry.getType() == ParseTableEntryType.REDUCE){
					
				}else{
					if(lexer.getNextToken().getTokenType() != TokenType.EOF){
						
					}else{
						break;
					}
				}
			}
			
		}
		
		return null;
	}

}
