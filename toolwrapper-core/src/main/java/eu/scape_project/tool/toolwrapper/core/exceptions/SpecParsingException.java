package eu.scape_project.tool.toolwrapper.core.exceptions;

public class SpecParsingException extends Throwable {
	private static final long serialVersionUID = 3680269489361878011L;

	public SpecParsingException() {
		super();
	}

	public SpecParsingException(String message) {
		super(message);
	}

	public SpecParsingException(Throwable cause) {
		super(cause);
	}

	public SpecParsingException(String message, Throwable cause) {
		super(message, cause);
	}
}
