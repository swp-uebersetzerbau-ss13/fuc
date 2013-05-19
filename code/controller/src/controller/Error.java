package controller;

public class Error {

	private String _text;
	private Integer _line;
	private Integer _column;
	private String _message;

	public Error(String text, Integer line, Integer column, String message) {
		_text = text;
		_line = line;
		_column = column;
		_message = message;
	}

	public String getText() {
		return _text;
	}

	public Integer getLine() {
		return _line;
	}

	public Integer getColumn() {
		return _column;
	}

	public String getMessage() {
		return _message;
	}
}
