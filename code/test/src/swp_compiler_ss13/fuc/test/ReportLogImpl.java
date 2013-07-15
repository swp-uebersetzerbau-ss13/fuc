package swp_compiler_ss13.fuc.test;

import swp_compiler_ss13.common.lexer.Token;
import swp_compiler_ss13.common.report.ReportLog;
import swp_compiler_ss13.common.report.ReportType;

import java.util.ArrayList;
import java.util.List;

/**
 * Implementation of the ReportLog interface for use in testing.
 */
public class ReportLogImpl implements ReportLog, Cloneable {
	List<ReportType> errorList = new ArrayList<>();
	List<ReportType> warningList = new ArrayList<>();


	/**
	 * Report a warning.
	 *
	 * @param type distinguishes the warning in machine readable format
	 * @param tokens is a list of tokens containing the warning cause and
	 *        identify the position in code
	 * @param message describes the warning in human readable manner
	 */
	@Override
	public void reportWarning(ReportType type, List<Token> tokens,
							  String message) {
		warningList.add(type);
	}

	/**
	 *  Report an error.
	 *
	 * @param type distinguishes the error in machine readable format
	 * @param tokens is a list of tokens containing the error cause and
	 *        identify the position in code
	 * @param message describes the error in human readable manner
	 */
	@Override
	public void reportError(ReportType type, List<Token> tokens, String message) {
		errorList.add(type);
	}

	/**
	 * Test if the ReprotLog contains errors.
	 *
	 * @return true if the ReportLog contains errors.
	 */
	public boolean hasErrors(){
		return ! errorList.isEmpty();
	}

	/**
	 * Test if the ReprotLog contains warnings.
	 *
	 * @return true if the ReportLog contains warning.
	 */
	public boolean hasWarnings(){
		return ! warningList.isEmpty();
	}

	/**
	 * Test if the ReprotLog contains warnings or errors.
	 *
	 * @return true if the ReportLog contains warning or erros.
	 */
	public boolean hasEntries(){
		return ! errorList.isEmpty();
	}

	/**
	 * Get the list of errors.
	 * @return the list of errors
	 */
	public List<ReportType> getErrors() {
		return errorList;
	}

	/**
	 * Get the list of warnings.
	 * @return the list of warnings
	 */
	public List<ReportType> getWarnings() {
		return warningList;
	}

	/**
	 * Get a list of errors and warnings.
	 * @return list of errors and warnings
	 */
	public List<ReportType> getEntries(){
		List<ReportType> reportTypes = new ArrayList<>(warningList);
		reportTypes.addAll(errorList);
		return reportTypes;
	}

	/**
	 * Clone the ReportLog.
	 *
	 * @return a clone of the ReportLog
	 * @throws CloneNotSupportedException
	 */
	@Override
	protected ReportLogImpl clone() {
		List<ReportType> errorList = new ArrayList<>();
		List<ReportType> warningList = new ArrayList<>();
		for ( ReportType reportType : this.errorList)
			errorList.add(reportType);
		for (ReportType reportType : this.warningList)
			warningList.add(reportType);
		ReportLogImpl reportLog = new ReportLogImpl();
		reportLog.errorList = errorList;
		reportLog.warningList = warningList;

		return reportLog;
	}
}
