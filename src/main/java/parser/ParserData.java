package parser;

import java.util.ArrayList;
import java.util.List;

public class ParserData {
	
	private List<TextMessage> messages = new ArrayList<TextMessage>();
	private StringBuffer buffer = new StringBuffer();

	public List<TextMessage> getMessages() {
		return messages;
	}

	public void setMessages(List<TextMessage> messages) {
		this.messages = messages;
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public void setBuffer(StringBuffer buffer) {
		this.buffer = buffer;
	}


}
