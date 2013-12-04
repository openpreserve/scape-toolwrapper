package eu.scape_project.tool.toolwrapper.core.exceptions;

public class ErrorParsingCmdArgsException extends Throwable {
	private static final long serialVersionUID = 3680269489361878011L;

	public ErrorParsingCmdArgsException() {
		super();
	}

	public ErrorParsingCmdArgsException(String message) {
		super(message);
	}

	public ErrorParsingCmdArgsException(Throwable cause) {
		super(cause);
	}

	public ErrorParsingCmdArgsException(String message, Throwable cause) {
		super(message, cause);
	}
}
