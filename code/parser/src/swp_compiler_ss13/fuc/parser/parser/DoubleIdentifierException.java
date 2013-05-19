package swp_compiler_ss13.fuc.parser.parser;

public class DoubleIdentifierException extends ParserException{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public DoubleIdentifierException(String msg) {
		super(msg);
	}

	@Override
	public void addReportLogMessage(String msg) {
				super.addReportLogMessage(msg);
	}

	@Override
	public String getReportLogMessage() {
				return super.getReportLogMessage();
	}

	@Override
	public void addReportLogText(String text) {
				super.addReportLogText(text);
	}

	@Override
	public String getReportLogText() {
				return super.getReportLogText();
	}
	
	

}
