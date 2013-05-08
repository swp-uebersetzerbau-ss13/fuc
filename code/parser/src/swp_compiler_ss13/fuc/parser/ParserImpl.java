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
import swp_compiler_ss13.fuc.parser.parseTableGenerator.Reduce;

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
		TokenType tokenType = null;
		ParseTableEntryType entryType;
		Token token = lexer.getNextToken();
		ParseTableEntry entry = null;

		while(true){
			s = parserStack.peek();
			tokenType = token.getTokenType();

			if(tokenType == TokenType.NOT_A_TOKEN){
				//TODO Errorhandling
			}

			entry = table.getEntry(s, tokenType);

			entryType = entry.getType();

			switch(entryType){
			case SHIFT: {
				parserStack.push(entry.getNewState());
				break;
			}

			case REDUCE: {

				//pop states from stack
				for(int i = 1; i <= ((Reduce)entry).getCount(); i++){
					parserStack.pop();
				}

				//push next state on stack
				parserStack.push(entry.getNewState());

			}

			case ACCEPT: {
				if(tokenType != TokenType.EOF){
					//TODO Errorhandling
				}else{
					//TODO return AST
					return null;
				}	
			}

			case ERROR:{
				//TODO Errorhandling
			}
			}
		}

	}

}
