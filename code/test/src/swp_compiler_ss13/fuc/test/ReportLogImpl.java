package swp_compiler_ss13.fuc.test;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.report.ReportType;

import java.util.ArrayList;
import java.util.List;

public class ReportLogImpl implements ReportLog {
	List<ReportType> errorList = new ArrayList<>();
	List<ReportType> warningList = new ArrayList<>();


	@Override
	public void reportWarning(ReportType type, List<Token> tokens,
							  String message) {
		warningList.add(type);
	}

	@Override
	public void reportError(ReportType type, List<Token> tokens, String message) {
		errorList.add(type);
	}

	public boolean hasErrors(){
		return ! errorList.isEmpty();
	}

	public boolean hasWarnings(){
		return ! warningList.isEmpty();
	}

	public boolean hasEntries(){
		return ! errorList.isEmpty();
	}

	public List<ReportType> getErrors() {
		return errorList;
	}

	public List<ReportType> getWarnings() {
		return warningList;
	}

	public List<ReportType> getEntries(){
		List<ReportType> reportTypes = new ArrayList<>(warningList);
		reportTypes.addAll(errorList);
		return reportTypes;
	}
}
