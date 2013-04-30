package swp_compiler_ss13.fuc.semantic_analyser;

import java.util.ArrayList;
import java.util.List;

import swp_compiler_ss13.common.parser.ReportLog;

public class TestReportLog implements ReportLog {

	public final List<ErrorContainer> errors = new ArrayList<>();

	@Override
	public void reportError(String text, Integer line, Integer column, String message) {
		errors.add(new ErrorContainer(text, line, column, message));
	}

	public class ErrorContainer {
		private final String text;
		private final Integer line;
		private final Integer column;
		private final String message;

		public ErrorContainer(String text, Integer line, Integer column, String message) {
			this.text = text;
			this.line = line;
			this.column = column;
			this.message = message;
		}

		public String getText() {
			return text;
		}

		public Integer getLine() {
			return line;
		}

		public Integer getColumn() {
			return column;
		}

		public String getMessage() {
			return message;
		}
	}

}
