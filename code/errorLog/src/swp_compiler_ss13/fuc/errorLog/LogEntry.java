package swp_compiler_ss13.fuc.errorLog;

import java.util.List;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.report.ReportType;

public class LogEntry {
	public enum Type {
		WARNNING,
		ERROR
	}
	
	private Type logType;
	private ReportType reportType;
	private List<Token> tokens;
	private String message;

	public LogEntry(Type logType, ReportType reportType, List<Token> tokens, String message) {
		this.logType = logType;
		this.reportType = reportType;
		this.tokens = tokens;
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public List<Token> getTokens() {
		return tokens;
	}

	public Type getLogType() {
		return logType;
	}

	public ReportType getReportType() {
		return reportType;
	}
}
