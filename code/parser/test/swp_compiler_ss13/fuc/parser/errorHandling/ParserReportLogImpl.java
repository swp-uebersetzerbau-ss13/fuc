package swp_compiler_ss13.fuc.parser.errorHandling;

import swp_compiler_ss13.common.parser.ReportLog;

public class ParserReportLogImpl implements ReportLog{

	@Override
	public void reportError(String text, Integer line, Integer column,
			String message) {
		System.err.println(text + " : l:" + line + " c:" + " " + message);
	}

}
