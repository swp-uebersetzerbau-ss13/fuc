package swp_compiler_ss13.fuc.parser.errorHandling;

import java.util.List;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.report.ReportType;

public class ParserReportLogImpl implements ReportLog{

	@Override
	public void reportWarning(ReportType type, List<Token> tokens,
			String message) {
		System.out.println(message);
		
	}

	@Override
	public void reportError(ReportType type, List<Token> tokens, String message) {
		System.out.println(message);
		
	}

}
