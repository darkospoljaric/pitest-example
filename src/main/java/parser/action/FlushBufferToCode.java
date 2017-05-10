package parser.action;

import java.util.List;

import parser.ParserData;
import parser.TextMessage;

/**
 * Mimics Rocket.Chat server behavior, which is not always logical.
 * 
 * @author dspoljaric
 */
public class FlushBufferToCode implements Action {

	@Override
	public void doAction(String character, ParserData parserData) {
		String messageWithBackTicks = parserData.getBuffer().toString();

		if (messageWithBackTicks.length() == 6) {
			if (parserData.getMessages().isEmpty()) {
				addEmptyCodeMessage(parserData);
			} else {
				appendBufferToLastMessage(parserData.getMessages(), messageWithBackTicks);
			}
		} else {
			addNewCodeMessage(parserData.getMessages(), messageWithBackTicks);
		}
		
		parserData.setBuffer(new StringBuffer());
	}

	private void addNewCodeMessage(List<TextMessage> messages, String messageWithBackTicks) {
		String messageWithoutBackticks = stripBackTicks(messageWithBackTicks);
		TextMessage message = new TextMessage(true, messageWithoutBackticks);
		messages.add(message);
	}

	private void appendBufferToLastMessage(List<TextMessage> messages, String messageWithBackTicks) {
		TextMessage lastMessage = messages.get(messages.size() - 1);
		lastMessage.setMessage(lastMessage.getMessage() + messageWithBackTicks);
	}

	private void addEmptyCodeMessage(ParserData parserData) {
		TextMessage textMessage = new TextMessage(true, "");
		parserData.getMessages().add(textMessage);
	}

	private String stripBackTicks(String messageWithBackTicks) {
		String startingBacktickRemoved = messageWithBackTicks.substring(3, messageWithBackTicks.length());
		return startingBacktickRemoved.substring(0, startingBacktickRemoved.length() - 3);
	}

}
