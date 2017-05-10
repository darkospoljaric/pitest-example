package parser;

public class TextMessage {

	private boolean code;
	private String message;

	public TextMessage(boolean code, String message) {
		super();
		this.code = code;
		this.message = message;
	}

	public boolean isCode() {
		return code;
	}

	public void setCode(boolean code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
