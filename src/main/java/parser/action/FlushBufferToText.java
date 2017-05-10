package parser.action;

import parser.ParserData;
import parser.TextMessage;

public class FlushBufferToText implements Action {

	@Override
	public void doAction(String character, ParserData parserData) {
		String messageWithEndBackticks = parserData.getBuffer().toString();
		String messageWithoutBackticks = messageWithEndBackticks.substring(0, messageWithEndBackticks.length() - 3);

		if (messageWithoutBackticks.length() > 0) {
			TextMessage message = new TextMessage(false, messageWithoutBackticks);
			parserData.getMessages().add(message);
		}

		// leave last 3 backticks because we're only sure we're done with text. We don't know if we're going to get any
		// code or backtick characters that might end the code snippet.
		parserData.setBuffer(new StringBuffer("```"));
	}

}
