package swp_compiler_ss13.fuc.parser.parser;

public class ParserException extends RuntimeException {
	// --------------------------------------------------------------------------
	// --- variables and constants ----------------------------------------------
	// --------------------------------------------------------------------------
	private static final long serialVersionUID = 3566092106272262484L;
	
	private String reportLogMessage;

	private String reportLogText;
	// --------------------------------------------------------------------------
	// --- constructors ---------------------------------------------------------
	// --------------------------------------------------------------------------
	public ParserException(String msg) {
		super(msg);
	}
	
	public ParserException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
	
	public void addReportLogMessage(String msg){
		this.reportLogMessage = msg;
	}
	
	public String getReportLogMessage(){
		return reportLogMessage;
	}
	
	
	public void addReportLogText(String text){
		this.reportLogText = text;
	}
	
	public String getReportLogText(){
		return reportLogText;
	}
	
	// --------------------------------------------------------------------------
	// --- methods --------------------------------------------------------------
	// --------------------------------------------------------------------------

	// --------------------------------------------------------------------------
	// --- getter/setter --------------------------------------------------------
	// --------------------------------------------------------------------------
}
