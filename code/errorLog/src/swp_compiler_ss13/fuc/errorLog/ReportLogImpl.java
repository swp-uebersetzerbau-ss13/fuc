package swp_compiler_ss13.fuc.errorLog;

import java.util.LinkedList;
import java.util.List;
import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.report.ReportType;

import swp_compiler_ss13.common.report.ReportLog;

public class ReportLogImpl implements ReportLog {
	List<LogEntry> entries;

	public ReportLogImpl() {
		entries = new LinkedList<>();
	}

	@Override
	public void reportError(ReportType type, List<Token> tokens, String message) {
		entries.add(new LogEntry(LogEntry.Type.ERROR, type, tokens, message));
	}

	@Override
	public void reportWarning(ReportType type, List<Token> tokens, String message) {
		entries.add(new LogEntry(LogEntry.Type.WARNNING, type, tokens, message));
	}
	
	public boolean hasErrors() {
		for (LogEntry e : entries) {
			if (e.getLogType() == LogEntry.Type.ERROR) {
				return true;
			}
		}
		
		return false;
	}
	
	public boolean hasWarnings() {
		for (LogEntry e : entries) {
			if (e.getLogType() == LogEntry.Type.WARNNING) {
				return true;
			}
		}
		
		return false;
	}
	
	public List<LogEntry> getErrors() {
		List<LogEntry> l = new LinkedList<>();
		
		for (LogEntry e : entries) {
			if (e.getLogType() == LogEntry.Type.ERROR) {
				l.add(e);
			}
		}
		
		return l;
	}
	
	public List<LogEntry> getEntries() {		
		return entries;
	}

	@Override
	public String toString() {
		StringBuilder s = new StringBuilder();
		
		for (LogEntry e : getEntries()) {
			s.append(e);
			s.append("\n");
		}
		
		return s.toString();
	}
	
	public void clear() {
		entries.clear();
	}
}
