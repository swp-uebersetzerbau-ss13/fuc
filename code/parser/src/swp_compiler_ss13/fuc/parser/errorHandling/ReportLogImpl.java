package swp_compiler_ss13.fuc.parser.errorHandling;

import java.util.LinkedList;
import java.util.List;
import swp_compiler_ss13.common.parser.ReportLog;

public class ReportLogImpl implements ReportLog {

	private List<Error> _errors;

	public ReportLogImpl() {
		_errors = new LinkedList<>();
	}
	
	@Override
	public void reportError(String text, Integer line, Integer column, String message) {
		_errors.add(new Error(text, line, column, message));
	}
	
	public boolean hasErrors() {
		return !_errors.isEmpty();
	}
	
	public List<Error> getErrors() {
		return _errors;
	}
}
